package com.yatang.xc.xcr.util;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.busi.common.exception.BusinessException;

/**
 * @Author : BobLee
 * @CreateTime : 2017年11月22日 16:00
 * @Summary : 主要是判断对象是否为空 涵盖属性
 */
public class ObjectUtils {

    /**
     * <h4>判断对象属性是否为空</h4></br>
     * <Author>BobLee</Author>
     * <CreateTime>2017年8月23日 下午1:37:28</CreateTime></br>
     * <MethodSignature>org.bitbucket.boblee.utils.domain . Empty.java .True( ... ) </MethodSignature>
     * <Description>
     * <small>Return:</small><strong >Boolean</strong ></br>
     * <small>Parameter:</small><strong >Parameter</strong ></br>
     * <small>Function Summary：</small><strong >对象所有属性都为空返回TRUE不包含序列ID</strong ></br>
     * </Description>
     */
    public static <T> boolean isNull(T obj) {
        if (obj == null) return true;
        if (obj instanceof List) {
            return list((List<?>) obj);
        }
        if (obj instanceof Map) {
            return map((Map<?, ?>) obj);
        }
        if (obj instanceof Object[]) {
            return array((Object[]) obj);
        }
        return obj(obj);
    }

    private static boolean map(Map<?, ?> map) {
        if (map == null || map.isEmpty() || map.size() == 0) return true;
        int len = map.size();
        Integer emptyCount = 0;
        Iterator<?> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            if (isNull(iterator.next())) {
                emptyCount++;
            }
        }
        if (emptyCount == len) return true;
        return false;
    }

    private static boolean list(List<?> list) {
        if (list == null || list.isEmpty() || list.size() == 0) return true;
        int len = list.size();
        Integer emptyCount = 0;
        for (Object object : list) {
            if (isNull(object)) {
                emptyCount++;
            }
        }
        if (emptyCount == len) return true;
        return false;
    }

    private static boolean array(Object[] array) {
        if (array == null || array.length == 0) return true;
        int len = array.length;
        Integer emptyCount = 0;
        for (Object object : array) {
            if (isNull(object)) {
                emptyCount++;
            }
        }
        if (emptyCount == len) return true;
        return false;
    }

    private static <T> boolean obj(T obj) {
        Integer emptyCount = 0;
        try {
            Class<?> clz = obj.getClass();
            Field[] fields = clz.getDeclaredFields();
            int leng = fields.length;
            Field.setAccessible(fields, true);
            PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(clz).getPropertyDescriptors();clz=null;
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                String key = propertyDescriptor.getName();
                if ("serialVersionUID".equalsIgnoreCase(key) || "Hash".equalsIgnoreCase(key) || "class".equalsIgnoreCase(key)) {leng=leng-1;key=null;continue;}
                Method readMethod = propertyDescriptor.getReadMethod();
                String methedName = readMethod.getName();
                if("getClass".equalsIgnoreCase(methedName)
                        || "getSerialversionuid".equalsIgnoreCase(methedName)
                        || "toString".equalsIgnoreCase(methedName)
                        || "hashCode".equalsIgnoreCase(methedName)) {readMethod=null;continue;}
                Object res = readMethod.invoke(obj);
                if(res == null){emptyCount++;continue;}
                if(res instanceof Collection){
                    Collection<?> c = (Collection<?>)res;
                    if(c.isEmpty() || c.size() == 0){emptyCount++;c=null;}continue;
                }
                if(res instanceof Object[]){
                    Object[] c = (Object[])res;
                    if(c.length==0){emptyCount++;c=null;}continue;
                }
            }
            if(leng == emptyCount){return true;}
        } catch (Exception e) {
            e.printStackTrace();
            throw new  BusinessException("500", e.getMessage() == null ? e.getClass().getName() : e.getMessage());
        }
        return false;
    }

}