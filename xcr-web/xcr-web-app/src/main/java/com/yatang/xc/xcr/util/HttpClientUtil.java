package com.yatang.xc.xcr.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * @author gaodawei
 *         2017年7月5日(星期三)
 */
public class HttpClientUtil {
    private static Logger log = LoggerFactory.getLogger(HttpClientUtil.class);

    public static JSONObject okHttpPost(String url, String param) {
        return okHttp(url, param, false);
    }

    public static JSONObject okHttpPostJson(String url, String param) {
        return okHttp(url, param, true);
    }

    public static JSONObject okHttp(String url, String param, boolean isJsonType) {
        Date date = new Date();
        log.info("\n***************于" + DateUtils.getLogDataTime(null, date) + "  http方式请求url:" + url + "\n***************request data is:" + param);
        OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(200, TimeUnit.SECONDS)
                .build();
        MediaType JSONMedia = null;
        if (isJsonType) {
            JSONMedia = MediaType.parse("application/json; charset=utf-8");
        } else {
            JSONMedia = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
        }
        RequestBody body = RequestBody.create(JSONMedia, param);
        JSONObject result = null;
        Request request = new Request.Builder().url(url).post(body).build();
        try {
            long httpStart = System.currentTimeMillis();


            okhttp3.Response response1 = client.newCall(request).execute();
            long httpEnd = System.currentTimeMillis();
            String json = response1.body().string();
            log.info("\n***************" + DateUtils.getLogDataTime(null, date) + "  发起http请求对应接口 url:" + url
                    + "\n*************** cost time:" + Double.valueOf(httpEnd - httpStart) / 1000
                    + "s\n*************** response data is:" + json + "\n***************request data is:" + param);
            result = JSONObject.parseObject(json);
        } catch (Exception e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }


    public static String sendPostByJSon(String url, String param) throws Exception {
        final String CONTENT_TYPE_TEXT_JSON = "text/json";
        DefaultHttpClient client = new DefaultHttpClient(
                new PoolingClientConnectionManager());


        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");

        StringEntity se = new StringEntity(param);
        se.setContentType(CONTENT_TYPE_TEXT_JSON);

        httpPost.setEntity(se);

        CloseableHttpResponse response2 = null;

        response2 = client.execute(httpPost);
        HttpEntity entity2 = null;
        entity2 = response2.getEntity();
        String s2 = EntityUtils.toString(entity2, "UTF-8");
        return s2;
    }

    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // �򿪺�URL֮�������
            URLConnection conn = realUrl.openConnection();
            // ����ͨ�õ���������
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("contentType",
                    "application/json");
            // ����POST�������������������
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // ��ȡURLConnection�����Ӧ�������
            out = new PrintWriter(conn.getOutputStream());
            // �����������
            out.print(param);
            // flush������Ļ���
            out.flush();
            // ����BufferedReader����������ȡURL����Ӧ
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
        }
        //ʹ��finally�����ر��������������
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // �򿪺�URL֮�������
            URLConnection connection = realUrl.openConnection();
            // ����ͨ�õ���������
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // ����ʵ�ʵ�����
            connection.connect();
            // ��ȡ������Ӧͷ�ֶ�
            Map<String, List<String>> map = connection.getHeaderFields();
            // �������е���Ӧͷ�ֶ�
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // ���� BufferedReader����������ȡURL����Ӧ
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("����GET��������쳣��" + e);
            e.printStackTrace();
        }
        // ʹ��finally�����ر�������
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }


}
