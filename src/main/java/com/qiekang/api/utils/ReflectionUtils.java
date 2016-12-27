package com.qiekang.api.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.SortedMap;

import com.google.common.collect.Maps;
import com.qiekang.api.annotation.MapField;

public class ReflectionUtils {

	public static Map<String, Object> bean2Map(Object object){
		SortedMap<String, Object> resultMap = Maps.newTreeMap();
		
		Class<? extends Object> clazz = object.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields){
			if(field.isAnnotationPresent(MapField.class)){
				MapField mapField = field.getAnnotation(MapField.class);
				String fieldName = mapField.name();
				try {
					PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
					Method method = pd.getReadMethod();
					Object value = method.invoke(object);
					resultMap.put(fieldName, value);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return resultMap;
	}

}
