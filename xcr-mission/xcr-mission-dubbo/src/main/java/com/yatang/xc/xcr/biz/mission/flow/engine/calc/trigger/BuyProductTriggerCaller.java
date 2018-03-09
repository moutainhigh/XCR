package com.yatang.xc.xcr.biz.mission.flow.engine.calc.trigger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.yatang.xc.xcr.biz.mission.domain.MissionBuyListPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteInfoPO;
import com.yatang.xc.xcr.biz.mission.dto.RuleCalculateDto;
import com.yatang.xc.xcr.biz.mission.enums.EnumMissionExecuteStatus;
import com.yatang.xc.xcr.biz.mission.flow.MissionExecuteFlowService;
import com.yatang.xc.xcr.biz.mission.flow.engine.bo.BuyProductResult;
import com.yatang.xc.xcr.biz.mission.flow.engine.calc.MissionTriggerCaller;
import com.yatang.xc.xcr.biz.mission.flow.engine.creator.rule.SimpleRuleCreator;
import com.yatang.xc.xcr.biz.mission.service.MissionService;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 购买电商商品确认任务调用的接口<br>
 * 调用指定的接口，只需要传入门店信息，不需要传入其他信息
 * 
 * @author yangqingsong
 *
 */
@Service("buyProductTriggerCaller")
public class BuyProductTriggerCaller extends BaseTriggerCaller {

	protected final Log log = LogFactory.getLog(this.getClass());

	@Value("${mission.trigger.buy.useMock}")
	private boolean useMock;

	@Value("${mission.trigger.buy.url}")
	private String url;

	// @Value("${mission.trigger.buy.specifyProductIds}")
	// private String specifyProductIds;

	@Autowired
	private MissionService missionService;

	@Autowired
	private MissionExecuteFlowService missionExecuteFlowService;


	/**
	 * 执行调用操作
	 */
	@Override
	public RuleCalculateDto call(MissionExecuteInfoPO executePO) {
		log.info("Calculate run:" + this.getClass().getSimpleName());
		SimpleRuleCreator.RuleDefinition ruleDefinition = new SimpleRuleCreator.RuleDefinition();
		try {
			String login = missionExecuteFlowService.getLoginByMerchantId(executePO.getMerchantId());
			if (StringUtils.isEmpty(login)) {
				log.error("can not find ytaccount by MerchantId:" + executePO.getMerchantId());
				return ruleDefinition;
			}

			
			List<MissionBuyListPO> buyList = missionService.queryBuyListBy(login, executePO.getMissonInfoId(), null);
			log.info("buyProductTrigger : buyList :" + JSON.toJSONString(buyList));
			if (buyList != null && !buyList.isEmpty()) {

				if (useMock || EnumMissionExecuteStatus.STATUS_FINISHED.getCode().equals(executePO.getStatus())
						|| EnumMissionExecuteStatus.STATUS_AWARD_AUDIT.getCode().equals(executePO.getStatus())
						|| EnumMissionExecuteStatus.STATUS_END.getCode().equals(executePO.getStatus())) {
					ruleDefinition.setMissionFinished(true);
				} else {
					boolean hasReturnValeu = false;
					MissionBuyListPO buy = null;
					int productCount = 0;
					int usedCount = 0;
					Set<String> orderIdset = new HashSet<String>();
					for (MissionBuyListPO buyItem : buyList) {
						if (String.valueOf(executePO.getId()).equals(buyItem.getExecuteId())) {
							buy = buyItem;
						} else {
							if (!StringUtils.isEmpty(buyItem.getReturnValue())) {
								hasReturnValeu = true;
								if (!orderIdset.contains(buyItem.getOrderId())) {
									orderIdset.add(buyItem.getOrderId());
									productCount += buyItem.getProductCount();
								}
							}
							if (buyItem.getUsedProductCount() != null) {
								usedCount += buyItem.getUsedProductCount();
							}
						}
					}
					log.info("buyProductTrigger : productCount:" + productCount + " usedCount:" + usedCount
							+ " hasReturnValeu" + hasReturnValeu + " buy :" + JSON.toJSONString(buy));
					if (buy != null) {
						// 调用 电商 接口查询 是否购买指定商品
						Map<String, String> params = new HashMap<String, String>();
						params.put("productIds", buy.getProductIds());
						params.put("login", buy.getLogin());
						params.put("orderId", buy.getOrderId());
						log.info("buyProductTrigger : params :" + JSON.toJSONString(params));
						OkHttpClient client = new OkHttpClient();

						MediaType JSONMedia = MediaType.parse("application/json; charset=utf-8");
						RequestBody body = RequestBody.create(JSONMedia, JSON.toJSONString(params));
						Request request = new Request.Builder().url(url).post(body).build();
						okhttp3.Response response1 = client.newCall(request).execute();
						String json = response1.body().string();
						log.info("buyProductTrigger : json :" + json);
						// String json = HttpClientUtils.doPost(url, params);
						if (json.startsWith("<!DOCTYPE")) {
							log.error("buyProductTrigger call faild... url:" + url + " return json not valid json:"
									+ json);
							throw new RuntimeException("cal url:" + url + " return json not valid... json:" + json);
						}
						BuyProductResult result = (BuyProductResult) JSON.toJavaObject(JSON.parseObject(json),
								BuyProductResult.class);
						log.info("buyProductTrigger : result :" + JSON.toJSONString(result));
						if (result != null && result.isResult()) {

							buy.setProductCount(0);
							buy.setReturnValue(json);
							int count = 0;
							if (result.getProductCount() != null && !result.getProductCount().isEmpty()) {
								for (int icount : result.getProductCount().values()) {
									count += icount;
								}

								buy.setProductCount(count);
								if (!hasReturnValeu && count > 0) {
									buy.setUsedProductCount(1);
									log.info("pass 1: executeId" + executePO.getId());
									ruleDefinition.setMissionFinished(true);
								} else {
									log.info("productCount 1:" + productCount + " usedCount:" + usedCount
											+ " orderIdset:" + JSON.toJSONString(orderIdset));
									if (!orderIdset.contains(buy.getOrderId())) {
										productCount += count;
									}
									log.info("productCount 2:" + productCount + " usedCount:" + usedCount
											+ " orderIdset:" + JSON.toJSONString(orderIdset));
									if (usedCount < productCount) {
										ruleDefinition.setMissionFinished(true);
										log.info("pass 2: executeId" + executePO.getId());
										buy.setUsedProductCount(1);
									}
								}
							}
							missionService.updateBuyList(buy);
						}
					}
				}
			}
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
		}

		return ruleDefinition;
	}

}
