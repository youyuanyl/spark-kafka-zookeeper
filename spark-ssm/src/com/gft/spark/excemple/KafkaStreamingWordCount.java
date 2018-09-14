package com.gft.spark.excemple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.spark.*;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.*;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import scala.Tuple2;

import com.gft.service.RedisService;
import com.gft.spark.utils.RedisTools;
import com.gft.utils.AESUtil;
import com.google.common.collect.Lists;

import net.sf.json.JSONArray;

public class KafkaStreamingWordCount {

	private static List<Map<String, Object>> list;
	
	public static void main(String[] args) throws InterruptedException {
		System.out.println("接收消息开始！");
		
		// 设置匹配模式，以空格分隔
		final Pattern SPACE = Pattern.compile(" ");
		// 接收数据的地址和端口
		String zkQuorum = "211.88.30.7:2181";
		// 话题所在的组
		String group = "1";
		// 话题名称以“，”分隔
		String topics = "top1,top2";
		// 每个话题的分片数
		int numThreads = 2;
		SparkConf sparkConf = new SparkConf().setAppName("KafkaWordCount").setMaster("local[2]");
		JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, new Duration(10000));
		// jssc.checkpoint("checkpoint"); //设置检查点
		// 存放话题跟分片的映射关系
		Map<String, Integer> topicmap = new HashMap<>();
		String[] topicsArr = topics.split(",");
		int n = topicsArr.length;
		for (int i = 0; i < n; i++) {
			topicmap.put(topicsArr[i], numThreads);
		}
		// 从Kafka中获取数据转换成RDD
		JavaPairReceiverInputDStream<String, String> lines = KafkaUtils.createStream(jssc, zkQuorum, group, topicmap);
		
		JavaDStream<String> words = lines.map(t -> t._2);
		
		words.foreachRDD(rdd ->{
			JavaRDD<String> aaa = rdd.flatMap(x -> Arrays.asList(x.split(" ")).iterator());
			JavaPairRDD<String, Integer> pair = aaa.mapToPair(p -> new Tuple2<>(p, 1));
			JavaPairRDD<String, Integer> reduce = pair.reduceByKey((i1, i2) -> i1 + i2);
			JavaRDD<Tuple2<String, Integer>> map = reduce.map(tuple -> new Tuple2(tuple._1, tuple._2));
			
			list = new ArrayList<>();
			map.foreach(m -> {
				//System.out.println(m._1 + "---" + m._2);
				Map<String, Object> valuemap = new HashMap<>();
				valuemap.put("word", m._1);
				valuemap.put("count", m._2);
				list.add(valuemap);
			});
			
			if(list.size() > 0){
				JSONArray array = JSONArray.fromObject(list);
				String arrStr = array.toString();
				System.out.println("arrStr：" + arrStr);
				String jmStr = AESUtil.encrypt(arrStr, "kafka-words-count");
				//System.out.println(jmStr);
				RedisTools.saveSessionToRedis("kafka-words-count", jmStr, -1);
			}
		});
		
		//words.print();
		
		
		/*// 从话题中过滤所需数据
		JavaDStream<String> words = lines.flatMap(new FlatMapFunction<Tuple2<String, String>, String>() {

			@Override
			public Iterator<String> call(Tuple2<String, String> arg0) throws Exception {
				//return Lists.newArrayList(SPACE.split(arg0._2)).iterator();
				return Lists.newArrayList(arg0._2).iterator();
			}
		});
		
		
		// 对其中的单词进行统计
		JavaPairDStream<String, Integer> wordCounts = words.mapToPair(new PairFunction<String, String, Integer>() {
			@Override
			public Tuple2<String, Integer> call(String s) {
				return new Tuple2<String, Integer>(s, 1);
			}
		}).reduceByKey(new Function2<Integer, Integer, Integer>() {
			@Override
			public Integer call(Integer i1, Integer i2) {
				return i1 + i2;
			}
		});
		
		// 打印结果
		wordCounts.print();*/
		jssc.start();
		jssc.awaitTermination();

	}

}