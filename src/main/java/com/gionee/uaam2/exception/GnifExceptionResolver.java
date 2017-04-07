package com.gionee.uaam2.exception;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

public class GnifExceptionResolver extends SimpleMappingExceptionResolver {

	@Override
	protected ModelAndView getModelAndView(String viewName, Exception ex) {
		ModelAndView mv = new ModelAndView(viewName);
		mv.addObject("success", false);
		mv.addObject("message", ex.getMessage());
		ex.printStackTrace();
		return mv;
	}

}
