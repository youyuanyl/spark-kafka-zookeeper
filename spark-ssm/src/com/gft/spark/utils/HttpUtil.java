package com.gft.spark.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.gft.spark.dto.RequestMessage;

import net.sf.json.JSONObject;


public class HttpUtil {

	public static RequestMessage POST(String url, String params) throws Exception {
		RequestMessage message = null;
		BufferedReader reader = null;
		OutputStream outs = null;
		try {
			String strMessage = "";
			StringBuffer buffer = new StringBuffer();
			// 接报文的地址
			URL uploadServlet = new URL(url);
			
			HttpURLConnection sevlCon = (HttpURLConnection) uploadServlet.openConnection();
			// 设置连接参数
			sevlCon.setRequestMethod("POST");
			sevlCon.setDoOutput(true);
			sevlCon.setDoInput(true);
			sevlCon.setAllowUserInteraction(true);
			sevlCon.setRequestProperty("content-type", "text/html");

			outs = sevlCon.getOutputStream();
			outs.write(params.getBytes());
			outs.flush();
			outs.close();

			// 获取返回的数据
			InputStream inputStream = sevlCon.getInputStream();
			reader = new BufferedReader(new InputStreamReader(inputStream));
			while ((strMessage = reader.readLine()) != null) {
				buffer.append(strMessage);
			}
			System.out.println("接收返回值:" + buffer);
			
			JSONObject result = JSONObject.fromObject(buffer.toString());
			message = (RequestMessage) JSONObject.toBean(result, RequestMessage.class);
		} catch (java.net.ConnectException e) {
			e.printStackTrace();
			throw e;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return message;
	}

}
