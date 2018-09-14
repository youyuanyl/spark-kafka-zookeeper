package com.gft.spark.common;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class IpFiter implements Filter {

	private Logger logger = Logger.getLogger(IpFiter.class);
	
	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		
		String ip = request.getHeader("x-forwarded-for");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        } 
        String msg = ip + " " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        
        Properties props = new Properties();   
        props.setProperty("metadata.broker.list","localhost:9092");   
        props.setProperty("serializer.class","kafka.serializer.StringEncoder");   
        props.put("request.required.acks","1");   
        ProducerConfig config = new ProducerConfig(props);  
        //创建生产这对象
        Producer<String, String> producer = new Producer<String, String>(config);
        //生成消息
        KeyedMessage<String, String> data = new KeyedMessage<String, String>("top1",msg);
        producer.send(data);    
        producer.close(); 
		filterChain.doFilter(request, resp);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

}
