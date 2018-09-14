package com.gft.spark.utils;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public final class JsonUtil {
	private JsonUtil(){}
	
	public static String JsonToString(JSONObject json){
		
		return "";
	}
	
	public static String ObjectToJsonStr(Object obj){
		return JSONObject.fromObject(obj).toString();
	}
	
	public static void writeJsonObject(Object obj, HttpServletResponse response){
		if(null != obj){
			JSONObject json = JSONObject.fromObject(obj);
			System.out.println("json：" + json);
			try {
				response.setContentType("text/plain");
				response.setCharacterEncoding("utf-8");
				Writer out = response.getWriter();
				out.write(json.toString());
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void writeJsonArray(Object obj, HttpServletResponse response){
		if(null != obj){
			JSONArray json = JSONArray.fromObject(obj);
			System.out.println("json：" + json);
			try {
				response.setContentType("text/plain");
				response.setCharacterEncoding("utf-8");
				Writer out = response.getWriter();
				out.write(json.toString());
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
