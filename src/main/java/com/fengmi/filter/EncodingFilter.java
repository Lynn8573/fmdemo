package com.fengmi.filter;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;

@WebFilter("/*")
public class EncodingFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// 1.��� post ������������
		request.setCharacterEncoding("utf-8");
        
		// 2.��� ��Ӧ ��������
		response.setCharacterEncoding("utf-8"); 
		
		// 3.��� ��������� ��������(text/css ���� login.css ��ʽ�ļ��ļ���)
		response.setContentType("text/css;charset=utf-8");
		
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}

}
