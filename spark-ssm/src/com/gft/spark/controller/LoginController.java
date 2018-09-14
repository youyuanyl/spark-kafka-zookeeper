package com.gft.spark.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gft.spark.dto.RequestMessage;
import com.gft.spark.utils.HttpClient;
import com.gft.spark.utils.JsonUtil;

@Controller
public class LoginController {

	
	@RequestMapping("/toLogin.do")
	public String toLogin(HttpServletRequest request, HttpServletResponse response){
		
		String clientUrl = request.getParameter("clientUrl");
		request.setAttribute("clientUrl", clientUrl);
		
		return "login";
	}
	
	@RequestMapping("/login.do")
	public String login(HttpServletRequest request, HttpServletResponse response){
		
		String clientUrl = request.getParameter("clientUrl");
		
		HttpSession session = request.getSession();
		
		System.out.println("login.do：" + session.getId());
		
		session.setAttribute("LOGIN_USER", "USER");
		session.setAttribute("token", "token");
		try {
			HttpClient http = new HttpClient(response);
			http.setParameter("isLogin", "true");
			http.setParameter("token", "token");
			http.sendByPost(clientUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return "login";
	}
	
	@RequestMapping("/checkToken.do")
	public String checkToken(HttpServletRequest request, HttpServletResponse response) throws IOException{
		HttpSession session = request.getSession();
		
		System.out.println("checkToken.do：" + session.getId());
		
		session.setAttribute("LOGIN_USER", session.getAttribute("LOGIN_USER"));
		session.setAttribute("token", session.getAttribute("token"));
		
		RequestMessage message = new RequestMessage();
		message.setTokenValue((String)session.getAttribute("token"));
		JsonUtil.writeJsonObject(message, response);
		
		return null;
	}
}
