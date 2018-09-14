package com.gft.spark.utils;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

public final class RedisTools {

	/**
	 * 获取redis服务器连接
	 * 
	 * @return
	 */
	public static Jedis connectionRedis() {
//		System.out.println("开始连接redis服务。。。。。。");
		String host = "211.88.30.7";// ip
		int port = 6379;// 端口
		String password = "123456";// 密码
		try {
			// 连接本地的 Redis 服务
			Jedis jedis = new Jedis(host, port);
			if (StringUtils.isNotBlank(password)) {// 如果配置了密码则使用密码登录
				jedis.auth(password);
			}
			// 查看服务是否运行
//			System.out.println("服务正在运行: " + jedis.ping());
			return jedis;
		} catch (Exception e) {
//			System.out.println("获取redis服务器连接失败！");
			e.printStackTrace();
		}
		return null;
	}

	
	/**
	 * 保存数据至redis服务器
	 * @param key
	 * @param value 字符串数据（可用json）
	 * @param seconds key值有效期（如果永久有效设置为-1）
	 * @return
	 */
	public static boolean saveSessionToRedis(String key, String value, int seconds) {
		Jedis jedis = connectionRedis();
		if (null != jedis) {
			// 设置 redis 字符串数据
			jedis.set(key, value);
			if(seconds != -1 && seconds >= 0){
				jedis.expire(key, seconds);
			}
			jedis.close();
			return true;
		}
		return false;
	}
	
	/**
	 * 从redis服务器获取数据
	 * 
	 * @param key
	 * @return session的JSON格式数据
	 */
	public static JSONObject getSessionFromRedis(String key) {
		Jedis jedis = connectionRedis();
		if (null != jedis) {
			String sessionJsonStr = jedis.get(key);
			
			jedis.close();
			if (null != sessionJsonStr) {
				JSONObject sessionJson = JSONObject.fromObject(sessionJsonStr);
				return sessionJson;
			}
		}
		return null;
	}
	
	/**
	 * 从redis服务器获取数据
	 * 
	 * @param key
	 */
	public static String getStringSessionFromRedis(String key) {
		Jedis jedis = connectionRedis();
		if (null != jedis) {
			String sessionJsonStr = jedis.get(key);
			jedis.close();
			return sessionJsonStr;
		}
		return null;
	}

	/**
	 * 获取key的有效期剩余时间
	 * 
	 * @param key
	 * @return key的有效期剩余time（秒）
	 */
	public static long getKeysTime(String key) {
		Jedis jedis = connectionRedis();
		if (null != jedis) {
			long time = jedis.ttl(key);
			jedis.close();
			return time;
		}
		return 0L;
	}
	
	/**
	 * 删除键
	 * @param key
	 */
	public static void delKeyFromRedis(String key){
		Jedis jedis = connectionRedis();
		if(null != jedis){
			jedis.del(key);
//			System.out.println("删除redis键返回值：" + result);
		}
	}
	
	/**
	 * 删除键
	 * @param key
	 */
	public static void delKeysFromRedis(String[] keys){
		Jedis jedis = connectionRedis();
		if(null != jedis && null != keys){
			jedis.del(keys);
		}
	}
	
	/**
	 * 设置key的有效期
	 * @param key
	 * @param seconds
	 */
	public static void setKeysTime(String key, int seconds){
		Jedis jedis = connectionRedis();
		if(null != jedis){
			jedis.expire(key, seconds);
			jedis.close();
		}
	}
	
	/**
	 * 设置key的有效期
	 * @param key
	 * @param seconds
	 */
	public static void setKeysTime(List<String> keys, int seconds){
		Jedis jedis = connectionRedis();
		if(null != jedis && null != keys && keys.size() > 0){
			for (int i = 0; i < keys.size(); i++) {
				jedis.expire(keys.get(i), seconds);
			}
			jedis.close();
		}
	}
}
