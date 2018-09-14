package com.gft.spark.common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.gft.spark.utils.HttpClient;
import com.gft.spark.utils.MyLogger;

public class LoginFilter implements Filter {

	private Logger logger = MyLogger.getLogger(LoginFilter.class);

	@Override
	public void destroy() {
		logger.info("登录过滤器销毁！");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpSession session = request.getSession();
		
		System.out.println("loginFilter：" + session.getId());
		
		String clientUrl = request.getParameter("clientUrl");
		
		if(null == session || null == session.getAttribute("LOGIN_USER")){
			filterChain.doFilter(req, resp);
		}else{
			session.setAttribute("LOGIN_USER", session.getAttribute("LOGIN_USER"));
			session.setAttribute("token", session.getAttribute("token"));
			String token = (String) session.getAttribute("token");
			
			HttpClient http = new HttpClient(response);
			http.setParameter("isLogin", "true");
			http.setParameter("token", token);
			http.sendByPost(clientUrl);
			return;
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("登录过滤器初始化！");
		
	}

}
