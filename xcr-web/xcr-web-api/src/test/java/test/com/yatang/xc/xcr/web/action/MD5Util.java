package test.com.yatang.xc.xcr.web.action;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    private static MessageDigest md;
    
    static{
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    
    public static String md5(String body){
        md.update(body.getBytes());
        byte messageDigest[] = md.digest();
        // Create Hex String
        StringBuilder hexString = new StringBuilder();
        // 字节数组转换为 十六进制 数
        for (int i = 0; i < messageDigest.length; i++) {
            String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
            if (shaHex.length() < 2) {
                hexString.append(0);
            }
            hexString.append(shaHex);
        }
        return hexString.toString();
    }
}
