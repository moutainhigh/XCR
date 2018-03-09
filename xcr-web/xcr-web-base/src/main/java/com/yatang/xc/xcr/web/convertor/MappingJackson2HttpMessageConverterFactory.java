package com.yatang.xc.xcr.web.convertor;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.StreamUtils;

/**
 * @Author : BobLee
 * @CreateTime : 2017年11月30日 上午10:27:13
 * @Summary : 重写Jackson消息转换器的writeInternal方法 SpringMVC选定了具体的消息转换类型后,会调用具体类型的write方法,将Java对象转换后写入返回内容
 */
public class MappingJackson2HttpMessageConverterFactory  {

	public MappingJackson2HttpMessageConverter init() {
		return new MappingJackson2HttpMessageConverter(){
			@Override
			protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
				if (object instanceof String){
					Charset charset = this.getContentTypeCharset(outputMessage.getHeaders().getContentType());
					StreamUtils.copy((String)object, charset, outputMessage.getBody());
				}else{
					super.writeInternal(object, type, outputMessage);
				}
			}
			private Charset getContentTypeCharset(MediaType contentType) {
				return contentType != null && contentType.getCharset() != null?contentType.getCharset():this.getDefaultCharset();
			}
		};
	}
}

