package com.yatang.xc.xcr.web.action;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSON;
import com.yatang.xc.xcr.vo.SingleUploadResult;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 上传图片的类
 * @author dongshengde
 *
 */
@Controller
@RequestMapping(value = "xcr/image")
public class UploadImageAction {

	private OkHttpClient client = new OkHttpClient();

	@Value("${fileserver.singleupload.url}")
	private String singleFileUploadUrl;

	@Value("${image.url.ip}")
	private String imageUrl;
	@Value("${fileserver.multiupload.url}")
	private String multiFileUploadUrl;

	/**
	 * 前端调用上传图片的接口，
	 * @param imageFile
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "uploadImage.htm", method = { RequestMethod.POST })
	public void uploadImage(@RequestParam(value = "upfile") MultipartFile imageFile, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		/*
		 * response.setContentType("application/json;charset=UTF-8"); String
		 * fileName = imageFile.getOriginalFilename(); InputStream inputStream =
		 * imageFile.getInputStream(); FileOutputStream foStream = new
		 * FileOutputStream(new File("D:\\dong.jpg")); byte[] b = new
		 * byte[1024]; while (inputStream.read(b) != -1) { foStream.write(b); }
		 * foStream.close(); inputStream.close(); String json =
		 * "{ \"state\": \"SUCCESS\",\"url\": \"/ueditor/upload/" +
		 * fileName.toString() + "/" + imageFile.getOriginalFilename() +
		 * "\",\"title\": \"" + imageFile.getOriginalFilename() +
		 * "\",\"original\": \"" + imageFile.getOriginalFilename() + "\"}";
		 * response.getWriter().write(json);
		 */

		response.setContentType("application/json;charset=UTF-8");
		CommonsMultipartFile cf = (CommonsMultipartFile) imageFile;
		FileItem fi = cf.getFileItem();
		fi.setFieldName("uploadify");
		// System.out.println("2------------------------");
		// // 获取图片的文件名
		// String fileName = imageFile.getOriginalFilename();
		// // 获取图片的扩展名
		// String extensionName = fileName.substring(fileName.lastIndexOf(".") +
		// 1);
		// // 新的图片文件名 = 获取时间戳+"."图片扩展名
		// String newFileName = String.valueOf((int) (System.currentTimeMillis()
		// / 100)) + "." + extensionName;
		SingleUploadResult suResult = singleFileUpload(imageFile, request);
		String url = suResult.getFileOnServerUrl();

		if (url != null) {
			String json = "{ \"state\": \"SUCCESS\",\"url\": \"" + imageUrl + "/" + url + "\",\"title\": \""
					+ imageFile.getOriginalFilename() + "\",\"original\": \"" + imageFile.getOriginalFilename() + "\"}";
			response.getWriter().write(json);
		} else {
			String json = "{ \"state\": \"ERROR\",\"url\": \"" + imageUrl + url + "\",\"title\": \""
					+ imageFile.getOriginalFilename() + "\",\"original\": \"" + imageFile.getOriginalFilename() + "\"}";
			response.getWriter().write(json);
		}

	}

	/**
	 * 提取上传方法
	 * @param file
	 * @param request
	 * @return
	 */
	public SingleUploadResult singleFileUpload(@RequestParam(value = "uploadify", required = false) MultipartFile file,
			HttpServletRequest request) {
		SingleUploadResult result = new SingleUploadResult();
		try {
			String systemCode = "train";
			System.out.println("code" + systemCode);
			// Part uploadFile = request.getPart("uploadFile");

			InputStream inputStream = null;
			String fileName = null;
			// if(uploadFile == null){
			inputStream = file.getInputStream();
			fileName = file.getOriginalFilename();
			// }else{
			// inputStream = uploadFile.getInputStream();
			// fileName = getFilename(uploadFile);
			// }

			byte[] body = new byte[inputStream.available()];
			inputStream.read(body);
			RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), body);
			RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
					.addFormDataPart("uploadFile", fileName, fileBody).addFormDataPart("systemCode", systemCode)
					.build();
			Request requestHttp = new Request.Builder().url(singleFileUploadUrl).post(requestBody).build();
			Response response = client.newCall(requestHttp).execute();
			String string = response.body().string();
			result = JSON.parseObject(string, SingleUploadResult.class);
		} catch (Exception e) {
			e.printStackTrace();
			result.setResponseCode("304");
			result.setErrMsg(e.getMessage());
		}
		return result;
	}
}
