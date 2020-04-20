package com.ksy.restaptemplate.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CosFilter implements Filter{
	
	@Override
	public void init(FilterConfig filterConfig ) throws ServletException{
		
	}
	@Override
	public void doFilter(ServletRequest servletrequest, ServletResponse servletrresponse, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("여기여기");
		// TODO Auto-generated method stub
		HttpServletResponse response = (HttpServletResponse) servletrresponse;
		HttpServletRequest request = (HttpServletRequest) servletrequest;
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		response.setHeader("Access-Control-Allow-Methods", "PUT, GET, POST, DELETE, OPTIONS");
		chain.doFilter(servletrequest, servletrresponse);
		
	}
	

}
