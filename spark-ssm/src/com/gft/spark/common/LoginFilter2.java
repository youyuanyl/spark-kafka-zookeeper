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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.gft.spark.dto.RequestMessage;
import com.gft.spark.utils.HttpClient;
import com.gft.spark.utils.HttpUtil;
import com.gft.spark.utils.MyLogger;

public class LoginFilter2 implements Filter {

	private Logger logger = MyLogger.getLogger(LoginFilter2.class);
	
	@Override
	public void destroy() {
		logger.info("登录过滤器销毁！");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpSession session = request.getSession();
		
		String isLogin = (String) request.getParameter("isLogin");
		String token = (String) request.getParameter("token");
		if(StringUtils.isNotBlank(isLogin) && "true".equals(isLogin)){
			session.setAttribute("isLogin", isLogin);
			session.setAttribute("token", token);
		}
		
		if(null == session || null == session.getAttribute("isLogin") || !"true".equals(session.getAttribute("isLogin"))){
			HttpClient http = new HttpClient(response);
			http.setParameter("clientUrl", "http://211.88.30.6:8083/calendar/list.do");
			http.sendByPost("http://211.88.30.6:8083/spark/toLogin.do");
		}else{
			try {
				RequestMessage message = HttpUtil.POST("http://211.88.30.6:8083/spark/checkToken.do", "");
				String checktoken = message.getTokenValue();
				
				session.setAttribute("isLogin", session.getAttribute("isLogin"));
				session.setAttribute("token", checktoken);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		filterChain.doFilter(req, resp);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("登录过滤器初始化！");
	}

}
