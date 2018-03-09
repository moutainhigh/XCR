package com.yatang.xc.xcr.web.action;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yatang.xc.xcr.vo.SingleUploadResult;

/**
 * Created by dunmengjun on 2017/3/6.
 */

@Controller
@RequestMapping(value = "xcr")
public class FileUploadAction {

    private final Log log = LogFactory.getLog(this.getClass());

    private OkHttpClient client = new OkHttpClient();

    @Value("${fileserver.singleupload.url}")
    private String singleFileUploadUrl;

    @Value("${fileserver.multiupload.url}")
    private String multiFileUploadUrl;

    @Value("${fileserver.url}")
    private String fileserverUrl;

    /**
     * 原有文件上传(返回“fileOnServerUrl+图片服务器地址”)
     *
     * @param files
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/fileupload/single.htm", method = RequestMethod.POST)
    @ResponseBody
    public String singleFileUpload(@RequestParam(value = "file", required = false) MultipartFile[] files,
                                   HttpServletRequest request, HttpServletResponse response, Model model) {
        String imgServerUrl = getFileServerUrl(files);
        String resultUrl = "fileOnServerUrl:" + imgServerUrl;
        log.info("文件服务器路径：" + resultUrl);
        return resultUrl;
    }

    /**
     * 默认文件上传(只返回服务器文件地址)
     *
     * @param files
     * @return
     */
    @RequestMapping(value = "/fileupload/default.htm", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject singleFileUpload(@RequestParam(value = "file", required = false) MultipartFile[] files) {
        String imgServerUrl = getFileServerUrl(files);
        JSONObject json = new JSONObject();
        json.put("data", imgServerUrl);
        return json;
    }

    private String getFileServerUrl(MultipartFile[] files) {
        long time = new Date().getTime();
        SingleUploadResult result = new SingleUploadResult();
        String resultUrl = "";
        if (files == null || files.length == 0) {
            return "";
        }
        MultipartFile file = null;
        if (files.length == 1) {
            file = files[0];
        }
        if (file == null) {
            return "";
        }
        String fileRelName = file.getOriginalFilename();
        String suffix = fileRelName.substring(fileRelName.lastIndexOf("."));
        if (StringUtils.isNotEmpty(suffix)) {
            if (!suffix.equals(".jpg") && !suffix.equals(".png") && !suffix.equals(".bmp") && !suffix.equals(".gif")) {
                return "error";
            }
        }
        try {
            String systemCode = "xcr_manage";
            String fileName = null;
            fileName = file.getName() + time;
            byte[] fileByte = file.getBytes();
            log.info("fileName:" + fileName);
            log.info("");
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), fileByte);
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("uploadFile", fileName, fileBody).addFormDataPart("systemCode", systemCode)
                    .build();
            log.info("requestBody:" + requestBody);
            Request requestHttp = new Request.Builder().url(singleFileUploadUrl).post(requestBody).build();
            Response response2 = client.newCall(requestHttp).execute();
            String string = response2.body().string();
            log.info("response2.body().string() -> " + string);
            result = JSON.parseObject(string, SingleUploadResult.class);
            log.info("result:" + result);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode("304");
            result.setErrMsg(e.getMessage());
            return "error";
        }
        JSONObject dataJson = new JSONObject();
        dataJson.put("fileOnServerUrl", fileserverUrl + result.getFileOnServerUrl());
        resultUrl = fileserverUrl + "/" + result.getFileOnServerUrl();
        log.info("文件服务器路径：" + resultUrl);
        return resultUrl;

    }


    public String fileUpload(MultipartFile[] files) {
        long time = new Date().getTime();
        SingleUploadResult result = new SingleUploadResult();
        String resultUrl = "";
        if (files == null || files.length == 0) {
            return "";
        }
        MultipartFile file = null;
        if (files.length == 1) {
            file = files[0];
        }
        if (file == null) {
            return "";
        }

        try {
            String systemCode = "xcr_manage";
            String fileName = null;
            fileName = file.getName() + time;
            byte[] fileByte = file.getBytes();
            log.info("fileName:" + fileName);
            RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileByte);
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("uploadFile", fileName, fileBody).addFormDataPart("systemCode", systemCode)
                    .build();
            log.info("requestBody:" + requestBody);
            Request requestHttp = new Request.Builder().url(singleFileUploadUrl).post(requestBody).build();
            Response response2 = client.newCall(requestHttp).execute();
            String string = response2.body().string();
            log.info("response2.body().string() -> " + string);
            result = JSON.parseObject(string, SingleUploadResult.class);
            log.info("result:" + result);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode("304");
            result.setErrMsg(e.getMessage());
            return "error";
        }
        JSONObject dataJson = new JSONObject();
        dataJson.put("fileOnServerUrl", fileserverUrl + result.getFileOnServerUrl());
        resultUrl = fileserverUrl + "/" + result.getFileOnServerUrl();
        log.info("文件服务器路径：" + resultUrl);
        return resultUrl;


    }

}
