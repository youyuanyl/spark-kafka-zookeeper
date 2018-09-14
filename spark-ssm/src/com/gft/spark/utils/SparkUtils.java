package com.gft.spark.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.springframework.web.context.WebApplicationContext;

import com.gft.spark.dto.AqLog;
import com.gft.spark.service.LogService;
import com.gft.spark.service.UserService;

import scala.Tuple2;

public class SparkUtils {

	private static List<AqLog> list;
	
	public static JavaSparkContext getJavaSparkContext(Class<?> clazz){
		System.out.println("className：" + clazz.getSimpleName());
		SparkConf conf = new SparkConf();
		conf.setAppName(clazz.getSimpleName());
		conf.setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		return sc;
	}
	
	public static JavaStreamingContext getJavaStreamingContext(Class<?> clazz){
		System.out.println("className：" + clazz.getSimpleName());
		SparkConf conf = new SparkConf();
		conf.setAppName(clazz.getSimpleName());
		conf.setMaster("local[2]");
		JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));
		return jssc;
	}
	
	public static void receiveAndSaveData(WebApplicationContext web){
		System.out.println("接收消息开始！");
		// 接收数据的地址和端口
		String zkQuorum = "localhost:2181";
		// 话题所在的组
		String group = "1";
		// 话题名称以“，”分隔
		String topics = "top1,top2";
		// 每个话题的分片数
		int numThreads = 2;
		SparkConf sparkConf = new SparkConf().setAppName("KafkaWordCount").setMaster("local[2]");
		JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, new Duration(10*60*1000));
		
		// jssc.checkpoint("checkpoint"); //设置检查点
		// 存放话题跟分片的映射关系
		Map<String, Integer> topicmap = new HashMap<>();
		String[] topicsArr = topics.split(",");
		int n = topicsArr.length;
		for (int i = 0; i < n; i++) {
			topicmap.put(topicsArr[i], numThreads);
		}
		
		// 设置匹配模式，以空格分隔
		final Pattern SPACE = Pattern.compile(" ");
		// 从Kafka中获取数据转换成RDD
		JavaPairReceiverInputDStream<String, String> lines = KafkaUtils.createStream(jssc, zkQuorum, group, topicmap);
		
		JavaDStream<String> words = lines.map(t -> t._2);
		words.foreachRDD(rdd ->{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			LogService logService = web.getBean(LogService.class);
			list = new ArrayList<>();
			
			JavaRDD<String> filter = rdd.filter(x -> {
				String ipReg = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
				Pattern p = Pattern.compile(ipReg);
				boolean isContentsIp = false;
				if(x.split(" ").length > 1){
					Matcher m = p.matcher(x.split(" ")[0]);
					isContentsIp = m.matches();
				}
				return isContentsIp;
			});
			
			JavaRDD<String> aaa = filter.flatMap(x -> Arrays.asList(x.split(" ")[0]).iterator());
			JavaPairRDD<String, Integer> pair = aaa.mapToPair(p -> new Tuple2<>(p, 1));
			JavaPairRDD<String, Integer> reduce = pair.reduceByKey((i1, i2) -> i1 + i2);
			JavaRDD<Tuple2<String, Integer>> map = reduce.map(tuple -> new Tuple2(tuple._1, tuple._2));
			map.foreach(m -> {
				String value = m._1;
				Integer count = m._2;
				
				AqLog log = new AqLog();
				log.setId(SysUtils.getUuid());
				log.setIp(value);
				log.setVisitcount(count);
				log.setVisitdate(new Date());
				log.setUpdatetime(new Date());
				list.add(log);
			});
			logService.insert(list);
		});
		
		jssc.start();
		try {
			jssc.awaitTermination();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
	}
	
}
