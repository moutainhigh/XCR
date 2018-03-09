package test.com.yatang.xc.xcr.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/** 
* @function 
* @author     作者 : gaodawei
* @Email      邮箱 : 695390275@qq.com
* @date       创建时间：2017年12月2日 上午1:09:54 
* @version 1.0 
 */
public class ThroughUrlDownloadImgUtil {
	
	/** 
     * 从网络Url中下载文件 
     * @param urlStr 
     * @param fileName 
     * @param savePath 
     * @throws IOException 
     */  
    public static void  downLoadFromUrl(String urlStr,String fileName,String savePath) throws IOException{  
        URL url = new URL(urlStr);    
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();    
        conn.setConnectTimeout(3*1000);  
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");  
        InputStream inputStream = conn.getInputStream();    
        byte[] getData = readInputStream(inputStream);      
        File saveDir = new File(savePath);  
        if(!saveDir.exists()){  
            saveDir.mkdir();  
        }  
        File file = new File(saveDir+File.separator+fileName);      
        FileOutputStream fos = new FileOutputStream(file);       
        fos.write(getData);   
        if(fos!=null){  
            fos.close();    
        }  
        if(inputStream!=null){  
            inputStream.close();  
        }  
        System.out.println("info:"+url+" download success");   
    }  
    /** 
     * 从输入流中获取字节数组 
     * @param inputStream 
     * @return 
     * @throws IOException 
     */  
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {    
        byte[] buffer = new byte[1024];    
        int len = 0;    
        ByteArrayOutputStream bos = new ByteArrayOutputStream();    
        while((len = inputStream.read(buffer)) != -1) {    
            bos.write(buffer, 0, len);    
        }    
        bos.close();    
        return bos.toByteArray();    
    } 
    
    /**
     * 根据指定的字符串截取文件中的字符串
     * @param separtorChar 分隔符
     * @param filePath   需要处理的文件路径
     * @throws IOException
     */
    public static String[] splitFile(String separtorChar,String filePath) throws IOException {
    	// 指定的文件
        File file = new File(filePath);
        // 读取文件
        FileInputStream fis = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String line;
        String[] strArr = null;
        while ((line = br.readLine()) != null) {
           strArr=line.split(";");
        }
		return strArr;
    }
    
    
    public static void main(String[] args) throws IOException {
    	String[] imgUrlArr=splitFile(";","E:\\XCR_CODE\\backend\\trunk\\xcr-web\\xcr-web-app\\src\\test\\java\\test\\com\\yatang\\xc\\xcr\\util\\nh_enroll_info_storephoto.txt");
		for (int i = 0; i < imgUrlArr.length; i++) {
			try{  
				System.out.println("现在正在下载第"+i+"条");
				ThroughUrlDownloadImgUtil.downLoadFromUrl(imgUrlArr[i], imgUrlArr[i].substring(44),"G:/storephoto/");  
	        }catch (Exception e) {  
	           System.err.println("下载第"+i+"条失败\t图片名称:"+ imgUrlArr[i].substring(44)+"\t图片链接为:"+imgUrlArr[i]);
	        } 
		}
	}
}
