package com.yatang.xc.xcr.web.report;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.Clock;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;

import metrics_influxdb.HttpInfluxdbProtocol;
import metrics_influxdb.InfluxdbProtocol;
import metrics_influxdb.InfluxdbReporter;
import metrics_influxdb.UdpInfluxdbProtocol;
import metrics_influxdb.api.measurements.MetricMeasurementTransformer;
import metrics_influxdb.measurements.HttpInlinerSender;
import metrics_influxdb.measurements.MeasurementReporter;
import metrics_influxdb.measurements.Sender;
import metrics_influxdb.measurements.UdpInlinerSender;
import metrics_influxdb.misc.Miscellaneous;
import metrics_influxdb.v08.Influxdb;
import metrics_influxdb.v08.InfluxdbHttp;
import metrics_influxdb.v08.InfluxdbUdp;

/**
 * InfluxdbReporter 继承 原 InfluxdbReporter 
 * 为了使用 MyMeasurementReporter
 * @author zhaokun
 *
 */
public class MyInfluxdbReporter extends InfluxdbReporter {

	public static MyBuilder myForRegistry(MetricRegistry registry) {
		return new MyBuilder(registry);
	}
	

	 public static class MyBuilder
	 {
	   private final MetricRegistry registry;
	   private Clock clock;
	   private String prefix;
	   private TimeUnit rateUnit;
	   private TimeUnit durationUnit;
	   private MetricFilter filter;
	   private boolean skipIdleMetrics;
	   private ScheduledExecutorService executor;
	   InfluxdbProtocol protocol;
	   Influxdb influxdbDelegate;
	   Map<String, String> tags;
	   MetricMeasurementTransformer transformer = MetricMeasurementTransformer.NOOP;

	   private MyBuilder(MetricRegistry registry) {
	     this.registry = registry;
	     this.clock = Clock.defaultClock();
	     this.prefix = null;
	     this.rateUnit = TimeUnit.SECONDS;
	     this.durationUnit = TimeUnit.MILLISECONDS;
	     this.filter = MetricFilter.ALL;
	     this.protocol = new HttpInfluxdbProtocol();
	     this.tags = new HashMap();
	   }

	   public MyBuilder withClock(Clock clock)
	   {
	     this.clock = clock;
	     return this;
	   }

	   public MyBuilder withScheduler(ScheduledExecutorService executor) {
	     this.executor = executor;
	     return this;
	   }

	   public MyBuilder prefixedWith(String prefix)
	   {
	     this.prefix = prefix;
	     return this;
	   }

	   public MyBuilder convertRatesTo(TimeUnit rateUnit)
	   {
	     this.rateUnit = rateUnit;
	     return this;
	   }

	   public MyBuilder convertDurationsTo(TimeUnit durationUnit)
	   {
	     this.durationUnit = durationUnit;
	     return this;
	   }

	   public MyBuilder filter(MetricFilter filter)
	   {
	     this.filter = filter;
	     return this;
	   }

	   public MyBuilder skipIdleMetrics(boolean skipIdleMetrics)
	   {
	     this.skipIdleMetrics = skipIdleMetrics;
	     return this;
	   }

	   /**
	    * 重写了此逻辑 为了使用MyMeasurementReporter
	    * @return
	    */
	   public ScheduledReporter build()
	   {
	     ScheduledReporter reporter;

	       Sender s = buildSender();
	       reporter = new MyMeasurementReporter(s, this.registry, this.filter, this.rateUnit, this.durationUnit, this.clock, this.tags, this.transformer, this.executor);

	     return reporter;
	   }



	   public MyBuilder protocol(InfluxdbProtocol protocol)
	   {
	     Objects.requireNonNull(protocol, "given InfluxdbProtocol cannot be null");
	     this.protocol = protocol;
	     return this;
	   }

	   public MyBuilder transformer(MetricMeasurementTransformer transformer)
	   {
	     Objects.requireNonNull(transformer, "given MetricMeasurementTransformer cannot be null");
	     this.transformer = transformer;
	     return this;
	   }

	   public MyBuilder tag(String tagKey, String tagValue)
	   {
	     Miscellaneous.requireNotEmptyParameter(tagKey, "tag");
	     Miscellaneous.requireNotEmptyParameter(tagValue, "value");
	     this.tags.put(tagKey, tagValue);
	     return this;
	   }

	   private Influxdb buildInfluxdb() {
	     if (this.protocol instanceof HttpInfluxdbProtocol)
	       try {
	         HttpInfluxdbProtocol p = (HttpInfluxdbProtocol)this.protocol;
	         return new InfluxdbHttp(p.scheme, p.host, p.port, p.database, p.user, p.password, this.durationUnit);
	       } catch (RuntimeException exc) {
	         throw exc;
	       }
	       catch (Exception exc) {
	         throw new RuntimeException(exc.getMessage(), exc);
	       }
	     if (this.protocol instanceof UdpInfluxdbProtocol) {
	       UdpInfluxdbProtocol p = (UdpInfluxdbProtocol)this.protocol;
	       return new InfluxdbUdp(p.host, p.port);
	     }
	     throw new IllegalStateException("unsupported protocol: " + this.protocol);
	   }

	   private Sender buildSender()
	   {
	     if (this.protocol instanceof HttpInfluxdbProtocol) {
	       return new HttpInlinerSender((HttpInfluxdbProtocol)this.protocol);
	     }

	     if (this.protocol instanceof UdpInfluxdbProtocol) {
	       return new UdpInlinerSender((UdpInfluxdbProtocol)this.protocol);
	     }
	     throw new IllegalStateException("unsupported protocol: " + this.protocol);
	   }
	 }


}
