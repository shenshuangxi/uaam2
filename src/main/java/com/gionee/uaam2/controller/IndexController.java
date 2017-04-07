package com.gionee.uaam2.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.uaam2.dao.ResponseFields;
import com.gionee.uaam2.service.ServiceManager;
import com.gionee.uaam2.util.SearchFields;
import com.gionee.uaam2.util.SearchFieldsUtil;

@Controller
public class IndexController {

	
	@Resource
	private ServiceManager serviceManager;
	private Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	public void setServiceManager(ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
		logger.debug("");
	}
	
	@RequestMapping("/index")
	public String Index(){
		return "index";
	}
	
	@RequestMapping("/validateTicket")
	public void validateTicket(HttpServletRequest request,HttpServletResponse response,Model model) throws IOException{
		Assertion assertion = (Assertion) request.getSession().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
		String name = assertion.getPrincipal().getName();
		model.addAttribute("user", name);
		response.sendRedirect("index");
	}
	
	@RequestMapping("/orgInfoPage")
	public String orgInfoPage(String orgDn,Model model){
		model.addAttribute("orgDn", orgDn);
		return "orgInfo";
	}
	
	@RequestMapping("/search")
	public @ResponseBody List search(String objSearch,String value){
		
		return null;
	}
	
	@RequestMapping("/findSearchCategory")
	public @ResponseBody List<ResponseFields> findSearchCategory(String category){
		if(category==null||category.equals("")){
			return SearchFieldsUtil.getCategorys();
		}else{
			return SearchFieldsUtil.getFieldsByCategory(category);
		}
	}
	
	
	
	
	
	
	
	
}
