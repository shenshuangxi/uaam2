package com.gionee.uaam2.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutFilter implements Filter {

	private String filterProcessesUrl;
	private String logoutSuccessUrl;
	
	public String getFilterProcessesUrl() {
		return filterProcessesUrl;
	}

	public void setFilterProcessesUrl(String filterProcessesUrl) {
		this.filterProcessesUrl = filterProcessesUrl;
	}

	public String getLogoutSuccessUrl() {
		return logoutSuccessUrl;
	}

	public void setLogoutSuccessUrl(String logoutSuccessUrl) {
		this.logoutSuccessUrl = logoutSuccessUrl;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		filterProcessesUrl = filterConfig.getInitParameter("filterProcessesUrl");
		logoutSuccessUrl = filterConfig.getInitParameter("logoutSuccessUrl");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		if (requiresLogout(request, response)) {
			request.getSession().invalidate();
			response.sendRedirect(logoutSuccessUrl);
			return;
		}
		chain.doFilter(request, response);
	}

	private boolean requiresLogout(HttpServletRequest request,
			HttpServletResponse response) {
		String url = request.getServletPath();
		if(filterProcessesUrl.contains(url)){
			return true;
		}
		return false;
	}
	

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
