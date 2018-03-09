package com.yatang.xc.xcr.biz.message.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.busi.common.utils.BeanConvertUtils;
import com.busi.mongo.MongoTools;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import com.yatang.xc.xcr.biz.message.common.RpConstants;
import com.yatang.xc.xcr.biz.message.dto.ScanPaymentMessage;
import com.yatang.xc.xcr.biz.message.dto.ScanPaymentMessageQuery;
import com.yatang.xc.xcr.biz.message.dto.SwiftPassMessage;
import com.yatang.xc.xcr.biz.message.dto.SwiftPassMessageQuery;
import com.yatang.xc.xcr.biz.message.service.ScanPaymentMessageService;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

@Service
@Transactional
public class ScanPaymentMessageServiceImpl implements ScanPaymentMessageService {

    private static final Logger logger = LoggerFactory.getLogger(ScanPaymentMessageServiceImpl.class);

    @Autowired
    private MongoTools mongoTools;

    /**
     * 查询总数
     *
     * @param params
     * @return
     */
    @Override
    public long queryTotalSize(ScanPaymentMessageQuery query) {

        logger.info("查询条件params={}", JSON.toJSONString(query));
        BasicDBObject queryObj = createQueryParams(query);

        // 组装模糊查询条件
        // buildFuzzyQueryCondition(params.getFuzzyFields(), queryObj);

        return mongoTools.getCollection(RpConstants.MONGO_COLL.SCAN_PAYMENT).count(queryObj);
    }

    /**
     * 分页查询
     *
     * @param params
     * @return
     */
    @Override
    public List<ScanPaymentMessage> queryScanPay(ScanPaymentMessageQuery query) {
        // 组装查询条件
        logger.info("查询条件params={}", JSON.toJSONString(query));
        BasicDBObject queryObj = createQueryParams(query);

        // 组装模糊查询条件 ，目前不需要
        // buildFuzzyQueryCondition(params.getFuzzyFields(), queryObj);

        int pageNum = query.getPageNum();
        int pageSize = query.getPageSize();

        MongoCursor<Document> cursor = null;

        // 初始化list指定容量，避免反复list扩容占用内存
        List<ScanPaymentMessage> scanPaymentMessages = new ArrayList<ScanPaymentMessage>(pageSize);
        try {
            // 设置分页条件
            int skip = (pageNum - 1) * pageSize;

            logger.info("查询条件queryObj={},skip={},pageSize={}", JSON.toJSONString(queryObj), skip, pageSize);

            MongoCollection prdColl = this.mongoTools.getCollection(RpConstants.MONGO_COLL.SCAN_PAYMENT);

            cursor = prdColl.find(queryObj).sort(new BasicDBObject("index", 1)).skip(skip).limit(pageSize).iterator();

            while (cursor.hasNext()) {
                scanPaymentMessages.add(JSON.parseObject(cursor.next().toJson(), ScanPaymentMessage.class));
            }

        } catch (Exception e) {
            logger.error("mongodb查询商品数据失败，失败原因：", e);
        } finally {
            cursor.close();
        }
        logger.info("查询结果scanPaymentMessages={}", JSON.toJSONString(scanPaymentMessages));
        return scanPaymentMessages;
    }

    /**
     * 单条保存
     *
     * @param params
     * @return
     */
    @Override
    public boolean saveScanPaymentMessage(ScanPaymentMessage scanPay) {
        // 商品主表
        MongoCollection<Document> sacanPaymentColl = this.mongoTools.getCollection(RpConstants.MONGO_COLL.SCAN_PAYMENT);

        // 查询条件
        BasicDBObject query = new BasicDBObject();

        query.put("_id", scanPay.getOrderId());
        // 如果订单编号不存在，则新增
        if (sacanPaymentColl.count(query) == 0) {
            Document doc = BeanConvertUtils.convert(scanPay, Document.class);
            doc.put("_id", scanPay.getOrderId());
            sacanPaymentColl.insertOne(doc);
            return true;
        }
        return false;

    }

    /**
     * 批量保存
     *
     * @param params
     * @return
     */
    @Override
    public void saveScanPaymentMessage(List<ScanPaymentMessage> ScanPaymentMessages) {
        // 商品主表
        MongoCollection<Document> sacanPaymentColl = this.mongoTools.getCollection(RpConstants.MONGO_COLL.SCAN_PAYMENT);

        // 查询条件
        BasicDBObject query = new BasicDBObject();

        // 遍历保存
        for (ScanPaymentMessage scanPay : ScanPaymentMessages) {
            query.put("_id", scanPay.getOrderId());
            // 如果订单编号不存在，则新增
            if (sacanPaymentColl.count(query) == 0) {
                Document doc = BeanConvertUtils.convert(scanPay, Document.class);
                doc.put("_id", scanPay.getOrderId());
                sacanPaymentColl.insertOne(doc);
            }
        }

    }

    /**
     * 保存单条扫码交易流水
     *
     * @param swiftPassMessage
     * @return
     */
    @Override
    public boolean saveSwiftPassMessage(SwiftPassMessage swiftPassMessage) {
        try {
            // 扫码交易流水表
            MongoCollection<Document> swiftPassMessageColl = this.mongoTools.getCollection(RpConstants.MONGO_COLL.SCAN_TURNOVER);
            BasicDBObject query = new BasicDBObject();
            query.put("_id", swiftPassMessage.getOut_trade_no());
            if (swiftPassMessageColl.count(query) == 0) {
                Document doc = BeanConvertUtils.convert(swiftPassMessage, Document.class);
                doc.put("_id", swiftPassMessage.getOut_trade_no());
                swiftPassMessageColl.insertOne(doc);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新单个流水
     *
     * @param swiftPassMessage
     * @return
     */
    @Override
    public boolean updateSwiftPassMessage(SwiftPassMessage swiftPassMessage) {
        try {
            MongoCollection<Document> swiftPassMessageColl = this.mongoTools.getCollection(RpConstants.MONGO_COLL.SCAN_TURNOVER);
            Bson b = Filters.eq("_id", swiftPassMessage.getOut_trade_no());
            Document doc = getDocument(swiftPassMessage);
            Document docT = new Document("$set", doc);
            swiftPassMessageColl.updateOne(b, docT);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新没有门店的流水
     *
     * @param swiftPassMessage
     * @return
     */
    @Override
    public boolean updateSwiftPassMessageForHaveNoShopCode(SwiftPassMessage swiftPassMessage) {
        try {
            MongoCollection<Document> swiftPassMessageColl = this.mongoTools.getCollection(RpConstants.MONGO_COLL.SCAN_TURNOVER);
            Bson b = Filters.eq("_id", swiftPassMessage.getOut_trade_no());
            Document doc = getDocumentForHaveNoShopCode(swiftPassMessage);
            Document docT = new Document("$set", doc);
            swiftPassMessageColl.updateOne(b, docT);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Document getDocument(SwiftPassMessage swiftPassMessage) {
        Document doc = new Document();
        String createTime = swiftPassMessage.getCreateTime();
        String pushSendTime = swiftPassMessage.getPushSendTime();
        String sendDcMqResult = swiftPassMessage.getSendDcMqResult();
        if (StringUtils.isNotEmpty(createTime)) {
            doc.append("createTime", createTime);
        }
        if (StringUtils.isNotEmpty(pushSendTime)) {
            doc.append("pushSendTime", pushSendTime);
        }
        if (StringUtils.isNotEmpty(sendDcMqResult)) {
            doc.append("sendDcMqResult", sendDcMqResult);
        }
        return doc;
    }

    private Document getDocumentForHaveNoShopCode(SwiftPassMessage swiftPassMessage) {
        Document doc = new Document();
        doc.append("mch_id", swiftPassMessage.getMch_id());
        doc.append("haveShopCode", 1);
        doc.append("sendDcMqResult", "1");  //设置推送为失败，以便重推
        return doc;
    }


    @Override
    public List<SwiftPassMessage> getSwiftPassMessageList(SwiftPassMessageQuery swiftPassMessageQuery) {
        // 组装查询条件
        logger.info("查询条件params={}", JSON.toJSONString(swiftPassMessageQuery));
        BasicDBObject queryObj = createQueryParams(swiftPassMessageQuery);
        int pageNum = swiftPassMessageQuery.getPageNum();
        int pageSize = swiftPassMessageQuery.getPageSize();
        MongoCursor<Document> cursor = null;
        // 初始化list指定容量，避免反复list扩容占用内存
        List<SwiftPassMessage> swiftPassMessages = new ArrayList<>(pageSize);
        try {
            // 设置分页条件
            int skip = (pageNum - 1) * pageSize;
            logger.info("查询条件queryObj={},skip={},pageSize={}", JSON.toJSONString(queryObj), skip, pageSize);
            MongoCollection prdColl = this.mongoTools.getCollection(RpConstants.MONGO_COLL.SCAN_TURNOVER);
            cursor = prdColl.find(queryObj).sort(new BasicDBObject("time_end", -1)).skip(skip).limit(pageSize).iterator();
            while (cursor.hasNext()) {
                swiftPassMessages.add(JSON.parseObject(cursor.next().toJson(), SwiftPassMessage.class));
            }
        } catch (Exception e) {
            logger.error("mongodb查询数据失败，失败原因：", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        logger.info("查询结果swiftPassMessages={}", JSON.toJSONString(swiftPassMessages));
        return swiftPassMessages;
    }

    @Override
    public List<SwiftPassMessage> getListWithFailedToSendMQ(String timeStart, String timeEnd) {
        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("sendDcMqResult", "1");
        queryObj.put("time_end", new BasicDBObject("$gte", timeStart));  // $get(>=)
        queryObj.put("time_end", new BasicDBObject("$lte", timeEnd));    //$lte(<=)
        return querySwiftPassMessageList(queryObj);
    }

    @Override
    public long querySwiftPassMessageTotalSize(SwiftPassMessageQuery swiftPassMessageQuery) {
        logger.info("查询条件params={}", JSON.toJSONString(swiftPassMessageQuery));
        BasicDBObject queryObj = createQueryParams(swiftPassMessageQuery);
        return mongoTools.getCollection(RpConstants.MONGO_COLL.SCAN_TURNOVER).count(queryObj);
    }

    @Override
    public List<SwiftPassMessage> getListWithNoShopCode() {
        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("haveShopCode", 0);
        return querySwiftPassMessageList(queryObj);
    }

    /**
     * 查询流水
     *
     * @param queryObj
     * @return
     */
    private List<SwiftPassMessage> querySwiftPassMessageList(BasicDBObject queryObj) {
        MongoCursor<Document> cursor = null;
        List<SwiftPassMessage> swiftPassMessages = new ArrayList<>();
        try {
            MongoCollection prdColl = this.mongoTools.getCollection(RpConstants.MONGO_COLL.SCAN_TURNOVER);
            cursor = prdColl.find(queryObj).iterator();
            while (cursor.hasNext()) {
                swiftPassMessages.add(JSON.parseObject(cursor.next().toJson(), SwiftPassMessage.class));
            }
        } catch (Exception e) {
            logger.error("mongodb查询数据失败，失败原因：", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        logger.info("查询结果swiftPassMessages={}", JSON.toJSONString(swiftPassMessages));
        return swiftPassMessages;
    }


    /**
     * 组装模糊查询条件
     *
     * @param fuzzyFields
     * @param queryObj
     */
    private void buildFuzzyQueryCondition(Set<String> fuzzyFields, BasicDBObject queryObj) {
        if (null != fuzzyFields && fuzzyFields.size() > 0) {
            for (String fuzzyField : fuzzyFields) {
                if (queryObj.containsField(fuzzyField)) {
                    BasicDBObject fuzzyCondition = new BasicDBObject();
                    fuzzyCondition.put("$regex", escape(queryObj.getString(fuzzyField)));
                    queryObj.put(fuzzyField, fuzzyCondition);
                }
            }
        }
    }

    /**
     * 特殊字符转义
     *
     * @param src
     */
    private String escape(String src) {
        if (StringUtils.isNotBlank(src)) {
            String[] fbsArr = {"\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|"};
            for (String key : fbsArr) {
                if (src.contains(key)) {
                    src = src.replace(key, "\\" + key);
                }
            }
        }
        return src;
    }

    /**
     * 转换查询条件为合适的格式
     *
     * @param query
     * @return
     */
    private BasicDBObject createQueryParams(Object query) {
        BasicDBObject params = new BasicDBObject();
        JSONObject jsonObject = (JSONObject) JSON.toJSON(query);
        for (Entry<String, Object> entry : jsonObject.entrySet()) {
            if (entry.getValue() != null) {
                if ("pageNum".equals(entry.getKey()) || "pageSize".equals(entry.getKey())) {
                    continue;
                }
                params.put(entry.getKey(), entry.getValue());
            }
        }
        return params;

    }
}
