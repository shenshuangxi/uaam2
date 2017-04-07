package com.gionee.uaam2.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;


public class Util {

	public static List<Field> getFields(Class<?> clazz){
		List<Field> fields = new ArrayList<Field>();
		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		Class<?> superClazz = clazz.getSuperclass();
		while(superClazz!=null&&!superClazz.isAssignableFrom(Object.class)){
			fields.addAll(Arrays.asList(superClazz.getDeclaredFields()));
			superClazz = superClazz.getSuperclass();
		}
		return fields;
	}

	public static Filter getFilter(Object obj) {
		AndFilter andFilter = new AndFilter();
		List<Field> fields = getFields(obj.getClass());
		for(Field field : fields){
			if(field.isAnnotationPresent(Attribute.class)){
				field.setAccessible(true);
				try {
					if(field.get(obj)!=null){
						andFilter.append(new EqualsFilter(field.getAnnotation(Attribute.class).name(), field.get(obj).toString()));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return andFilter;
	}
	
	
	public static String getBase(Class<?> clazz){
		if(clazz.isAnnotationPresent(Entry.class)){
			return clazz.getAnnotation(Entry.class).base();
		}
		return null;
	}

	public static <T> void UpdateTarget(T source,T target) throws IllegalArgumentException, IllegalAccessException {
		List<Field> fields = getFields(source.getClass());
		for(Field field : fields){
			field.setAccessible(true);
			Object value = field.get(source);
			if(value!=null){
				field.set(target, value);
			}
		}
	}
	
	public static String md5Base64ToLdap(String source){
		GioneePasswordEncoder gioneePasswordEncoder = new GioneePasswordEncoder();
		String md5Hex = gioneePasswordEncoder.getMD5ofStr(source);
		byte[] md5Bytes = byteFormHex(md5Hex);
		String retMd5Base64 = base64Encode(md5Bytes);
		return "{MD5}"+retMd5Base64;
	}
	
	public static String md5Base64FromLdap(String source){
		String retMd5Base64 = DecStringToHexString(source);
		return retMd5Base64;
	}
	
	
	
	private static String DecStringToHexString(String decStr){
		String[] dec = decStr.split(",");
		StringBuilder stringBuilder = new StringBuilder();
		for(int i=0;i<dec.length;i++){
			stringBuilder.append((char)Integer.parseInt(dec[i]));
		}
		return stringBuilder.toString();
	}
	
	private static byte[] byteFormHex(String hex) {
		  byte[] bytes = new byte[hex.length()/2];
		  for (int i = 0; i < bytes.length; i++) {
		   //16进制字符转换成int->位运算（取int(32位)低8位,即位与运算 &0xFF）->强转成byte
		   bytes[i] = (byte) (0xFF & Integer.parseInt(hex.substring(i*2, i*2+2), 16));
		  }
		  return bytes;
	}
	
	@SuppressWarnings("restriction")
	private static String base64Encode(byte[] baseBytes){
		sun.misc.BASE64Encoder encode = new sun.misc.BASE64Encoder();  
		String retStr = encode.encode(baseBytes);
		return retStr;
	}
	
	
}
