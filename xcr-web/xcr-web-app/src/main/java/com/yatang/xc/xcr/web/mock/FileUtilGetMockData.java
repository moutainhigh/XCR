package com.yatang.xc.xcr.web.mock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/** 
* @author gaodawei 
* @Date 2017年8月11日 下午5:30:03 
* @version 1.0.0
* @function 
*/
public class FileUtilGetMockData {
	/**
     * 将文本文件中的内容读入到buffer中
     * @param buffer buffer
     * @param filePath 文件路径
     * @throws IOException 异常
     * @author cn.outofmemory
     * @date 2013-1-7
     */
    public static StringBuffer readToBuffer(StringBuffer buffer, String filePath) throws IOException {
        InputStream is = new FileInputStream(filePath);
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            buffer.append(line); // 将读到的内容添加到 buffer 中
            buffer.append("/n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
        return buffer;
    }

    /**
     * 读取文本文件内容
     * @param filePath 文件所在路径
     * @return 文本内容
     * @throws IOException 异常
     * @author cn.outofmemory
     * @date 2013-1-7
     */
    public static String readFile(String filePath) throws IOException {
    	URL path=Thread.currentThread().getContextClassLoader().getResource("");
        StringBuffer sb = new StringBuffer();
        FileUtilGetMockData.readToBuffer(sb, filePath);
        return sb.toString();
    }
    
    public static void main(String[] args) throws IOException {
    	File directory = new File("");
    	System.err.println(directory.getAbsolutePath().replace('\\', '/'));
    	System.err.println(System.getProperty("user.dir"));
    	System.err.println("Thread.currentThread()"+Thread.currentThread());
    	System.err.println("Thread.currentThread().getContextClassLoader()"+Thread.currentThread().getContextClassLoader());
    	System.err.println("Thread.currentThread().getContextClassLoader().getResource()"+Thread.currentThread().getContextClassLoader().getResource(""));
    	URL str=Thread.currentThread().getContextClassLoader().getResource("");
    	System.err.println(str.toString().substring(6, str.toString().length()-16));
    	 URL classPath = Thread.currentThread().getContextClassLoader().getResource("");  
         String proFilePath = classPath.toString();  
    	
//    	String path2 = Thread.currentThread().getContextClassLoader().getResource("/").getPath();  
//    	System.out.println("path2 = " + path2); 
	}
}
 