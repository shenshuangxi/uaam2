package com.gionee.uaam2.util;

import javax.servlet.ServletResponse;

public class ResponseHolder {
	
	private static ThreadLocal<ServletResponse> responseThreadLocal = new ThreadLocal<ServletResponse>();
	
	public static void set(ServletResponse response){
		responseThreadLocal.set(response);
	}
	
	public static ServletResponse get(){
		return responseThreadLocal.get();
	}
	
	public static void clear(){
		responseThreadLocal.remove();
	}
	
	
}
