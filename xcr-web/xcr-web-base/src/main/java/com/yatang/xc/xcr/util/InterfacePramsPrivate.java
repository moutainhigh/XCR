package com.yatang.xc.xcr.util;

import com.alibaba.fastjson.JSON;
import com.yatang.xc.xcr.annotations.GenericParadigmType;
import com.yatang.xc.xcr.enums.ErrorMessageList;
import com.yatang.xc.xcr.exceptions.ErrorMessageResponseException;
import com.yatang.xc.xcr.exceptions.YTBusinessException;
import com.yatang.xc.xcr.model.RequestMap;
import org.springframework.core.annotation.AnnotationUtils;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author : BobLee
 * @CreateTime : 2017年9月27日 下午4:04:34
 * @Summary : 别乱动
 */
public class InterfacePramsPrivate {

	public static Object isNumber(Object obj, String key, Field[] fields) {
		if (obj == null)return null;
        key = new StringBuilder().append(Character.toLowerCase(key.charAt(0))).append(key.substring(1)).toString();
		for (Field field : fields) {
			Class<?> clazz = field.getType();
			if (key.equals(field.getName())) {
				if (int.class.isAssignableFrom(clazz) || clazz.getName().equals(Integer.class.getName())) {
					String number = obj.toString();
					try {
						return Integer.parseInt(number);
					} catch (Exception e) {
						if(Assert.isNumeric(number) && number.contains(".")){
                            number = number.trim();
                            number = number.substring(0, number.indexOf("."));
                        }
                        if( Numbers.isFolat(number) && Numbers.isDouble(number) ) {
                            number = number.trim();
                            number = number.substring(0, number.indexOf("."));
                        }
						return Integer.parseInt(number);
					}
				}
				
				if (long.class.isAssignableFrom(clazz) || clazz.getName().equals(Long.class.getName())) {
					return (Long.parseLong(obj.toString()));
				}
				
				if (double.class.isAssignableFrom(clazz) || clazz.getName().equals(Double.class.getName())) {
					return (Double.parseDouble(obj.toString()));
				}
				
				if (float.class.isAssignableFrom(clazz) || clazz.getName().equals(Float.class.getName())) {
					return (Float.parseFloat(obj.toString()));
				}
				
				if (String.class.isAssignableFrom(clazz) || clazz.getName().equals(String.class.getName())) {
					String va = obj.toString();
					if (va.contains("-9999") || va.indexOf("-9999") != -1 || "-9999".equals(va)) { return "-1"; }
					return va;
				}
				
				if (boolean.class.isAssignableFrom(clazz) || clazz.getName().equals(Boolean.class.getName())) {
					return Boolean.valueOf(obj.toString());
				}

				return obj;
			}
		}
		return obj;
	}

	public static <T> T convert(Class<T> clz, Map<String, Object> data) {
		if (data == null || data.size() == 0 || data.isEmpty()) {return null;}
		try {
			T obj = clz.newInstance();
			Field[] fields = clz.getDeclaredFields();
			Field.setAccessible(fields, true);
			BeanInfo beanInfo = Introspector.getBeanInfo(clz);
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
				String key = propertyDescriptor.getName();
				if ("class".equals(key) || key.equals("_isAsc") || key.equals(RequestMap._PAGENO) || key.equals(RequestMap._ORDERBY) || key.equals(RequestMap._PAGESIZE) || "serialVersionUID".equals(key)) {
					continue;
				}
				Object value = data.get(key);
				if (value == null) {
                    String newKey = key;
					key = new StringBuilder().append(Character.toUpperCase(key.charAt(0))).append(key.substring(1)).toString();
					value = data.get(key);
					if(value == null){
					    key = newKey.toUpperCase();
						value = data.get(key);
						if(value == null){continue;}
					}
				}

                Type genericSuperclass = propertyDescriptor.getPropertyType().getGenericSuperclass();
                if (propertyDescriptor.getPropertyType().isAssignableFrom(List.class) || genericSuperclass instanceof List) {
                    GenericParadigmType annotation =AnnotationUtils.findAnnotation(propertyDescriptor.getReadMethod(), GenericParadigmType.class);
                    if(annotation != null){
                        Class declaredGenericParadigmType = annotation.value();
                        List<Object>  genericParadigmDatas = JSON.parseObject(JSON.toJSONString(value), List.class);
                        if(genericParadigmDatas != null && genericParadigmDatas.size() > 0){
                            List<Object> objects = new ArrayList<>(genericParadigmDatas.size());
                            for (Object genericParadigmData : genericParadigmDatas) {
                                Map<String, Object> classPropertyList = JSON.parseObject(JSON.toJSONString(genericParadigmData), Map.class);
                                objects.add(convert(declaredGenericParadigmType, classPropertyList));
                            }
                            value = objects;
                        }
                    }
                }

                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(obj, isNumber(value, key, fields));
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorMessageResponseException(ErrorMessageList.SERVER);
		}
	}

}
