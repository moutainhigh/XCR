package com.yatang.xc.xcr.web.report;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Clock;
import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;
import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;

import metrics_influxdb.api.measurements.MetricMeasurementTransformer;
import metrics_influxdb.measurements.Measure;
import metrics_influxdb.measurements.Sender;

/**
 * MyMeasurementReporter 继承 原 ScheduledReporter 
 * @author zhaokun
 *
 */
public class MyMeasurementReporter extends ScheduledReporter {

	private static Logger					log	= LoggerFactory.getLogger(MyMeasurementReporter.class);

	private final Sender					sender;
	private final Clock						clock;
	private Map<String, String>				baseTags;
	private MetricMeasurementTransformer	transformer;



	public MyMeasurementReporter(Sender sender, MetricRegistry registry, MetricFilter filter, TimeUnit rateUnit,
			TimeUnit durationUnit, Clock clock, Map<String, String> baseTags, MetricMeasurementTransformer transformer,
			ScheduledExecutorService executor) {
		super(registry, "measurement-reporter", filter, rateUnit, durationUnit, executor);
		this.baseTags = baseTags;
		this.sender = sender;
		this.clock = clock;
		this.transformer = transformer;
	}



	public MyMeasurementReporter(Sender sender, MetricRegistry registry, MetricFilter filter, TimeUnit rateUnit,
			TimeUnit durationUnit, Clock clock, Map<String, String> baseTags,
			MetricMeasurementTransformer transformer) {
		super(registry, "measurement-reporter", filter, rateUnit, durationUnit);
		this.baseTags = baseTags;
		this.sender = sender;
		this.clock = clock;
		this.transformer = transformer;
	}



	private Measure fromTimer(String metricName, Timer t, long timestamp) {
		Snapshot snapshot = t.getSnapshot();

		Map tags = new HashMap(this.baseTags);
		tags.putAll(this.transformer.tags(metricName));

		Measure measure = new Measure(this.transformer.measurementName(metricName)).timestamp(timestamp).addTag(tags)
				.addValue("count", snapshot.size()).addValue("min", convertDuration(snapshot.getMin()))
				.addValue("max", convertDuration(snapshot.getMax()))
				.addValue("mean", convertDuration(snapshot.getMean()))
				.addValue("std-dev", convertDuration(snapshot.getStdDev()))
				.addValue("50-percentile", convertDuration(snapshot.getMedian()))
				.addValue("75-percentile", convertDuration(snapshot.get75thPercentile()))
				.addValue("95-percentile", convertDuration(snapshot.get95thPercentile()))
				.addValue("99-percentile", convertDuration(snapshot.get99thPercentile()))
				.addValue("999-percentile", convertDuration(snapshot.get999thPercentile()))
				.addValue("one-minute", convertRate(t.getOneMinuteRate()))
				.addValue("five-minute", convertRate(t.getFiveMinuteRate()))
				.addValue("fifteen-minute", convertRate(t.getFifteenMinuteRate()))
				.addValue("mean-minute", convertRate(t.getMeanRate())).addValue("run-count", t.getCount());

		return measure;
	}



	private Measure fromMeter(String metricName, Meter mt, long timestamp) {
		Map tags = new HashMap(this.baseTags);
		tags.putAll(this.transformer.tags(metricName));

		Measure measure = new Measure(this.transformer.measurementName(metricName)).timestamp(timestamp).addTag(tags)
				.addValue("count", mt.getCount()).addValue("one-minute", convertRate(mt.getOneMinuteRate()))
				.addValue("five-minute", convertRate(mt.getFiveMinuteRate()))
				.addValue("fifteen-minute", convertRate(mt.getFifteenMinuteRate()))
				.addValue("mean-minute", convertRate(mt.getMeanRate()));

		return measure;
	}



	private Measure fromHistogram(String metricName, Histogram h, long timestamp) {
		Snapshot snapshot = h.getSnapshot();

		Map tags = new HashMap(this.baseTags);
		tags.putAll(this.transformer.tags(metricName));

		Measure measure = new Measure(this.transformer.measurementName(metricName)).timestamp(timestamp).addTag(tags)
				.addValue("count", snapshot.size()).addValue("min", snapshot.getMin())
				.addValue("max", snapshot.getMax()).addValue("mean", snapshot.getMean())
				.addValue("std-dev", snapshot.getStdDev()).addValue("50-percentile", snapshot.getMedian())
				.addValue("75-percentile", snapshot.get75thPercentile())
				.addValue("95-percentile", snapshot.get95thPercentile())
				.addValue("99-percentile", snapshot.get99thPercentile())
				.addValue("999-percentile", snapshot.get999thPercentile()).addValue("run-count", h.getCount());

		return measure;
	}



	/**
	 * 重写了此类 定制了 count 的逻辑 每次记录后count清空
	 * @param metricName
	 * @param c
	 * @param timestamp
	 * @return
	 */
	private Measure fromCounter(String metricName, Counter c, long timestamp) {
		Map tags = new HashMap(this.baseTags);
		tags.putAll(this.transformer.tags(metricName));

		long count = c.getCount();
		Measure measure = null;
		if (count > 0) {
			measure = new Measure(this.transformer.measurementName(metricName)).timestamp(timestamp).addTag(tags)
					.addValue("count", count);
			c.dec(count);
		}
		return measure;
	}



	private Measure fromGauge(String metricName, Gauge g, long timestamp) {
		Map tags = new HashMap(this.baseTags);
		tags.putAll(this.transformer.tags(metricName));

		Measure measure = new Measure(this.transformer.measurementName(metricName)).timestamp(timestamp).addTag(tags);

		Object o = g.getValue();

		if (o == null) {
			return null;
		}
		if ((o instanceof Long) || (o instanceof Integer)) {
			long value = ((Number) o).longValue();
			measure.addValue("value", value);
		} else if (o instanceof Double) {
			Double d = (Double) o;
			if ((d.isInfinite()) || (d.isNaN())) {
				return null;
			}
			measure.addValue("value", d.doubleValue());
		} else if (o instanceof Float) {
			Float f = (Float) o;
			if ((f.isInfinite()) || (f.isNaN())) {
				return null;
			}
			measure.addValue("value", f.floatValue());
		} else {
			String value = "" + o;
			measure.addValue("value", value);
		}

		return measure;
	}



	public void report(SortedMap<String, Gauge> gauges, SortedMap<String, Counter> counters,
			SortedMap<String, Histogram> histograms, SortedMap<String, Meter> meters, SortedMap<String, Timer> timers) {
		long timestamp = this.clock.getTime();

		for (Map.Entry entry : gauges.entrySet()) {
			this.sender.send(fromGauge((String) entry.getKey(), (Gauge) entry.getValue(), timestamp));
		}

		for (Map.Entry entry : counters.entrySet()) {
			this.sender.send(fromCounter((String) entry.getKey(), (Counter) entry.getValue(), timestamp));
		}

		for (Map.Entry entry : histograms.entrySet()) {
			this.sender.send(fromHistogram((String) entry.getKey(), (Histogram) entry.getValue(), timestamp));
		}

		for (Map.Entry entry : meters.entrySet()) {
			this.sender.send(fromMeter((String) entry.getKey(), (Meter) entry.getValue(), timestamp));
		}

		for (Map.Entry entry : timers.entrySet()) {
			this.sender.send(fromTimer((String) entry.getKey(), (Timer) entry.getValue(), timestamp));
		}

		this.sender.flush();
	}
}
