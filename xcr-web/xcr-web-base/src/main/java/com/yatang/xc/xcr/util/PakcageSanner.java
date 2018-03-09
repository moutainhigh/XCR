package com.yatang.xc.xcr.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * @author BobLee
 * @date 2017年01月16日
 * @summary :  命名就不讲究了哈  这个类原先确实是扫描包用的
 */
public class PakcageSanner {

	/**
	 * 把action下面的所有method遍历一次，标记他们是否需要进行敏感词验证 如果需要，放入cache中
	 */
	public static Set<Field> findAnnotationField(Class<?> clz, Class<? extends Annotation> anno){
		Set<Field> methodSet = new HashSet<Field>();
		Field[] methods = clz.getDeclaredFields();
		for (Field f : methods) {
			f.setAccessible(true); 
			Annotation annotation = f.getAnnotation(anno);
			if (annotation != null) {
				methodSet.add(f);
			}
		}
		return methodSet;
	}

	/**检查类方法上是否包含该注解*/
	public static boolean methodHasAnnotation(Class<?> clz, Class<? extends Annotation> anno){
		return hasAnnotation(clz, anno, true, false, false);
	}
	
	/**检查属性上是否包含该注解*/
	public static boolean fieldHasAnnotation(Class<?> clz, Class<? extends Annotation> anno){
		return hasAnnotation(clz, anno, false, true, false);
	}
	
	/**检查类上是否包含该注解*/
	public static boolean classHasAnnotation(Class<?> clz, Class<? extends Annotation> anno){
		return hasAnnotation(clz, anno, false, false, true);
	}
	
	/***
	 *  类中是否包含有该注解
	 * @param clz
	 * @param anno
	 * @return
	 */
	private static boolean hasAnnotation(Class<?> clz, Class<? extends Annotation> anno,boolean method,boolean field,boolean type){
		if(type){
			if(clz.getAnnotation(anno) != null){
				return true;
			}
		}

		if(field){
			Field[] fields = clz.getDeclaredFields();
			for (Field f : fields) {
				f.setAccessible(true); 
				if (f.getAnnotation(anno) != null) {
					return true;
				}
			}
		}
		
		if(method){
			Method[] methods = clz.getDeclaredMethods();
			for (Method m : methods) {
				if (m.getModifiers() != Modifier.PUBLIC) {continue;}
				if (m.getAnnotation(anno) != null) {
					return true;
				}
			}
		}
		return false;
	}
}
