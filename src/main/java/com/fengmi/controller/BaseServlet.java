package com.fengmi.controller;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/base")
public class BaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String methodName = req.getParameter("methodName");
		
		System.out.println("methodName=" + methodName);
		try {
			Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
			if (null != method) {
				String returnValue = (String) method.invoke(this, req, resp);
				// String �账��null ������
				if (null != returnValue) {
					int index = returnValue.lastIndexOf(":");
					if (index > 0) {
						String path = returnValue.substring(index+1);
						if (returnValue.startsWith("redirect")) {
							resp.sendRedirect(req.getContextPath() + path);
						} else if(returnValue.startsWith("forward")) {
							req.getRequestDispatcher(path).forward(req, resp);
						}
					} else {
						// û�У�Ĭ��ת��
						req.getRequestDispatcher(returnValue).forward(req, resp);
					}
				} else {
					// ���账��
				}
			} else {
				System.out.println("û�и÷���:" + methodName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
