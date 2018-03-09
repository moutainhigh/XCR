
package com.yatang.xc.xcr.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yatang.xc.xcr.model.ResultMap;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.vo.SingleUploadResult;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * 这是图片上传的接口
 *
 * @author caotao
 * @date 2017年3月29日(星期三)
 */
@Controller
@RequestMapping("ImgUpload")
public class FileUploadAction {
    private static Logger log = LoggerFactory.getLogger(FileUploadAction.class);
    
    private static String UPLOAD_TIMEOUT="上传图片超时，请稍后重试";
    private static String UPLOAD_FAILED="上传图片失败";
    
    private OkHttpClient client = new OkHttpClient();
    @Value("${fileserver.singleupload.url}")
    private String singleFileUploadUrl;
    @Value("${fileserver.singleupload.imgurl}")
    private String singleFileUploadImgUrl;
    @Value("${SYSTEM_CODE}")
    private String SYSTEM_CODE;
    @Value("${URL_PREFIX}")
    private String URL_PREFIX;
    @Value("${OUTGOODSURL_PREFIX}")
    private String OUTGOODSURL_PREFIX;
    @Value("${INFO_OK}")
    private String INFO_OK;

    /**
     * IOS图片上传的接口
     *
     * @param file
     * @param request
     * @param res
     * @throws IOException
     * @author caotao
     */
    @RequestMapping(value = "UploadIos", method = RequestMethod.POST)
    @ResponseBody
    public void uploadIos(@RequestParam(value = "uploadFile", required = false) MultipartFile file,
                          HttpServletRequest request, HttpServletResponse res) throws IOException {
        log.info("现在执行图片上传的接口");
        SingleUploadResult result = new SingleUploadResult();
        try {
            String fileName = file.getOriginalFilename();
            result = JSON.parseObject(uploadFileBody(file, fileName.toString()), SingleUploadResult.class);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
            result.setResponseCode("304");
            result.setErrMsg(e.getMessage());
        }
        JSONObject json = getFileUploadJson(result);
        res.setCharacterEncoding("UTF-8");
        res.setHeader("contentType", "application/json; charset=utf-8");
        log.info("\n*******响应的数据为："+json);
        res.getWriter().println(json);
    }

    @RequestMapping(value = "UploadAndroid", method = RequestMethod.POST)
    @ResponseBody
    public void uploadAndroid(@org.springframework.web.bind.annotation.RequestBody String msg,
                              HttpServletRequest request, HttpServletResponse res) throws IOException, ServletException {
        log.info("现在执行图片Upload接口");
        SingleUploadResult result = new SingleUploadResult();
        try {
            JSONObject jsonTemp = CommonUtil.methodBeforeImg(msg);
            JSONArray imgStr = jsonTemp.getJSONArray("Pic");
            String fileName = jsonTemp.getString("FileName");
            System.err.println("imgStr:" + imgStr.size());
            byte[] imageByte = new byte[imgStr.size()];
            for (int i = 0; i < imgStr.size(); i++) {
                imageByte[i] = imgStr.getByteValue(i);
            }
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), imageByte);
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("uploadFile", fileName, fileBody).addFormDataPart("systemCode", SYSTEM_CODE)
                    .build();
            Request requestHttp = new Request.Builder().url(singleFileUploadUrl).post(requestBody).build();
            Response response = client.newCall(requestHttp).execute();
            String string = response.body().string();
            log.info(string);
            result = JSON.parseObject(string, SingleUploadResult.class);
            System.out.println("result====" + result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
            result.setResponseCode("304");
            result.setErrMsg(e.getMessage());
        }
        JSONObject json = getFileUploadJson(result);
        res.getWriter().println(json);
    }


    /**
     * V2.5.1 新增安卓文件上传
     *
     * @param file
     * @param res
     * @throws IOException
     * @throws ServletException
     */
    @ResponseBody
    @RequestMapping(value = "UploadFileForAndroid")
    public void uploadFileForAndroid(MultipartFile file, HttpServletResponse res) throws IOException, ServletException {
        log.info("in UploadFileForAndroid ... ");
        log.info("fileName:" + file.getOriginalFilename());

        String strMessage = uploadFileBody(file, String.valueOf(new Date().getTime()));
        if ( ! strMessage.startsWith("{") && ! strMessage.endsWith("}")) {
            log.error(strMessage);
            res.getWriter().println(ResultMap.failll("上传异常请稍后重试").toStringEclipse());
            return;
        }

        SingleUploadResult result = JSON.parseObject(strMessage, SingleUploadResult.class);
        log.info("fileUrl:" + result.getFileOnServerUrl());
        JSONObject json = getFileUploadJson(result);
        res.getWriter().println(json);
    }

    /**
     * 活动照片的上传
     * @param userId
     * @param file
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @RequestMapping(value = "uploadImg", method = RequestMethod.POST)
    @ResponseBody
    public void uploadActivity(String userId, String storeNo, @RequestParam(value = "uploadFile", required = false) MultipartFile file, HttpServletResponse response) throws IOException, ServletException {
        System.err.println("\n**********userId:" + userId + "    storeNo:" + storeNo);
        SingleUploadResult result = new SingleUploadResult();
        try {
            StringBuffer fileName = new StringBuffer();
            if (userId != null && !"".equals(userId)) {
                fileName.append(userId).append("_").append(System.currentTimeMillis());
            } else {
                fileName.append(storeNo).append("_").append(System.currentTimeMillis());
            }
            result = JSON.parseObject(uploadFileBodyHTML5(file, fileName.toString()), SingleUploadResult.class);
            result.setFileOnServerUrl(OUTGOODSURL_PREFIX.concat(result.getFileOnServerUrl()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setResponseCode("100");
            result.setErrMsg(e.getMessage());
        }
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("code", result.getResponseCode());
        jsonObj.put("url", result.getFileOnServerUrl());
        log.info("\n**************上传文件响应的json数据为：" + jsonObj);
        response.getWriter().print(jsonObj);
    }

    /**
     * 组装上传文件的body
     *
     * @param file
     * @param fileName
     * @return
     * @throws IOException
     */
    public String uploadFileBody(MultipartFile file, String fileName) throws IOException {
        InputStream inputStream = file.getInputStream();
        byte[] body = new byte[inputStream.available()];
        inputStream.read(body);
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), body);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uploadFile", fileName.toString(), fileBody).addFormDataPart("systemCode", SYSTEM_CODE)
                .build();
        Request requestHttp = new Request.Builder().url(singleFileUploadUrl).post(requestBody).build();
        Response respResult = client.newCall(requestHttp).execute();
        String string = respResult.body().string();
        log.info("\n**************上传文件返回的数据:" + string);
        return string;
    }

    /**
     * 组装上传文件的body
     *
     * @param file
     * @param fileName
     * @return
     * @throws IOException
     */
    public String uploadFileBodyHTML5(MultipartFile file, String fileName) throws IOException {
        InputStream inputStream = file.getInputStream();
        byte[] body = new byte[inputStream.available()];
        inputStream.read(body);
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), body);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uploadFile", fileName.toString(), fileBody).addFormDataPart("systemCode", SYSTEM_CODE)
                .build();
        Request requestHttp = new Request.Builder().url(singleFileUploadImgUrl).post(requestBody).build();
        Response respResult = client.newCall(requestHttp).execute();
        String string = respResult.body().string();
        log.info("\n**************上传文件返回的数据:" + string);
        return string;
    }

    /**
     * 封装图片上传的返回数据
     *
     * @param result
     * @return
     * @author caotao
     */
    public JSONObject getFileUploadJson(SingleUploadResult result) {
        JSONObject json = new JSONObject();
        JSONObject stateJson = new JSONObject();
        JSONObject subJson = new JSONObject();
        if (result.getResponseCode().equals(INFO_OK)) {
        	stateJson=CommonUtil.pageStatus2("M00", "正常");
            subJson.put("Url", URL_PREFIX + result.getFileOnServerUrl());
            json.put("Status", stateJson);
            json.put("mapdata", subJson);
        } else {
        	if(result.getResponseCode().equals("304")){
        		stateJson=CommonUtil.pageStatus2("M02", UPLOAD_TIMEOUT);
        	}else{
        		stateJson=CommonUtil.pageStatus2("M02", UPLOAD_FAILED);
        	}
            json.put("Status", stateJson);
        }
        return json;
    }
}
