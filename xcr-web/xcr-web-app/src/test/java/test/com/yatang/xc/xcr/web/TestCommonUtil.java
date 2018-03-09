package test.com.yatang.xc.xcr.web;

import java.net.URLDecoder;
import java.security.PrivateKey;
import java.util.List;
import java.util.Map;

import org.apache.xerces.impl.dv.util.Base64;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.yatang.xc.oles.biz.business.dto.ElecContractDTO;
import com.yatang.xc.xcr.pojo.Key;
import com.yatang.xc.xcr.util.Bases64;
import com.yatang.xc.xcr.util.DESEncrype;
import com.yatang.xc.xcr.util.RSAHelper;
import com.yatang.xc.xcr.util.StringUtils;
import com.yatang.xc.xcr.web.ApplyJoinXCAction;

/** 
* @author gaodawei 
* @Date 2017年8月3日 下午3:46:05 
* @version 1.0.0
* @function 
*/
public class TestCommonUtil {
	
	
	
	@Test
	public void testRSAHelperDecryptData(){
		String decStr="fHvs44eFsrtcW93bkJQpnncXR9P5FA5g1Fl7QBdb+Nunno3VxwciZKCp6mw/GMDFJbfbsMDwHcaXh9WEoXOdhbxdg1AJub3GZX08Wb50Jpo+KL7vx4aVSMDud46s+PXakw51ND6uVZorrmZku/ViqkQm9hHPf5IUekUbXLcrtvY=";
		String keyStr="{\"privateKey\":\"PFJTQUtleVZhbHVlPjxNb2R1bHVzPkFKN2dSU0RBWWRKUGp6MGdwTlEzSzdQOFF2V2ROeEpZV3phYWZnMm16WlBsMkhrOHRSRFhUaVNTcFMvRGNWSUZLZDZPY0JsbTNMajlJVTBEMElTdFVqNVUyK1pUMXZ4clpGbFZqYmVoQ2pGWm4yaGxMdS9OMjJYSkRLQk9tMC8rZVFtQ0ZZOEVDdFNUVlp4eUdscXNZWHZyeGZVOG5GRk5mSUZIZWUzbm54Tno8L01vZHVsdXM+PEV4cG9uZW50PkFRQUI8L0V4cG9uZW50PjxEPlZ5d280ZUtOQ2U4N1ljZmlpTXBJd1hYZGE1UzZuZWM3RFFmUnVEM1l1Smk1b1RQdENYT0pYY25VdHhOZXZ5azBJdmcyaThtRHZFQ3ZuK2xDNVRtUVVPUEFDSlZ6SGE3eDdhSTZQaEFLQlQ4eGlXaFRoQ0pyVFZsc3dReVg0WXZGcGtEVTFqK1YxQVZwSTN5MzFTMHc0L0VVeHpadHM3NGJzaS9NalIvSXZTaz08L0Q+PFA+QU1xbUVTbWdQYkNSbE9YTllkODljYWZ0MFVHeEZ1MFpmWlhVVUJzRHRGZXRvQUN5UTExTEVYZCtnQy9KblBiY2ovVWl4amNLZC9ZTDI3VDRpYXBkYjJVPTwvUD48UT5BTWkwRGRacWFJUCtiT1hDcDE2b0xyV3VtSlcyZjZjN0ppT2tjMnIzUytQcFpCZGxjN3JFakk5Qmc5U1pUUWFneFd1UXc2QjZPQlhpRlJIcHNFeHpKZmM9PC9RPjxEUD5DZjJiOVFCb2FpOFROeTBqK1J5UFlPY1JuUHA5RmQxZTJxY245OXo3NUNLME03U1dLNkJLMFNoVWE0YTJEVm1jWTBwK21USVQ3Zlp4czM5WlVpUGZIUT09PC9EUD48RFE+QUlNWTJWRU5BR2YzY0xwRGczL1k2Mm5ST2VuUTQyRWd5YkRPWW5Xa1F2WUVKVlVDOTYwdlpZRE5rb2J6KzlRd2Y4UGJNMjFIYUFzak9oQVJUUThpeDlFPTwvRFE+PEludmVyc2VRPkFKZ0t1YVBrbzVrcXZoNStXZk50SzNTTmZwVTVWQm94VGZ4dWp3QUdXb1FoZDV1UzV3eE54S0QySnZDU0FDL2ZpWDRqendZZWxmQ1VMYVYxWUQ1ZG0zbz08L0ludmVyc2VRPjwvUlNBS2V5VmFsdWU+\"}";
		JSONObject jsonObj=JSONObject.parseObject(keyStr);
		Key key=JSONObject.toJavaObject(jsonObj, Key.class);
		PrivateKey priKey = RSAHelper.decodePrivateKeyFromXml(StringUtils.bytesToString(Bases64.decode(key.getPrivateKey().getBytes())));
		System.err.println("\n******priKey:"+priKey);
		byte[] bt2 = RSAHelper.decryptData(Bases64.decode(decStr.getBytes()), priKey);
        JSONObject jsonDESKey = JSONObject.parseObject(StringUtils.bytesToString(bt2));
        System.err.println("\n******jsonDESKey:"+jsonDESKey);
	}

	public static void testBaiFenHao(){
		String str="特殊商品名称测试%";
		String encodeStr=Base64.encode(str.getBytes());
		System.err.println(encodeStr);
		encodeStr.replace(' ', '+');
		System.err.println(new String(Base64.decode(Base64.encode(str.getBytes()))));
	}
}
 