package test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;

import scala.Tuple2;

public class Test5 {

	private static Map<String, Integer> result;
	
	public static void main(String[] args) {
		
		String fileName = "F:\\sparktest.log";
		
		SparkConf conf = new SparkConf();
		conf.setAppName("Test5");
		conf.setMaster("local");
		
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaRDD<String> lines = sc.textFile(fileName, 1);
		
		System.out.println("lines：" + lines.count());
		
		JavaRDD<String> filter = lines.filter(new Function<String, Boolean>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Boolean call(String s) throws Exception {
				String ipReg = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
				Pattern p = Pattern.compile(ipReg);
				boolean isContentsIp = false;
				if(s.split("-").length > 1){
					Matcher m = p.matcher(s.split("-")[0]);
					isContentsIp = m.matches();
				}
				return isContentsIp;
			}
		});
		
		System.out.println("filter：" + filter.count());
		
		JavaRDD<String> flatMap = filter.flatMap(new FlatMapFunction<String, String>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Iterator<String> call(String s) throws Exception {
				return Arrays.asList(s.split("-")[0]).iterator();
			}
		});
		
		System.out.println("flatMap：" + flatMap.count());
		
		JavaPairRDD<String, Integer> mapToPair = flatMap.mapToPair(new PairFunction<String, String, Integer>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Tuple2<String, Integer> call(String s) throws Exception {
				return new Tuple2<String, Integer>(s, 1);
			}
		});
		
		JavaPairRDD<String, Integer> reduceByKey = mapToPair.reduceByKey(new Function2<Integer, Integer, Integer>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Integer call(Integer i1, Integer i2) throws Exception {
				return i1 + i2;
			}
		});
		
		JavaRDD<Tuple2<String, Integer>> map = reduceByKey.map(new Function<Tuple2<String,Integer>, Tuple2<String,Integer>>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Tuple2<String, Integer> call(Tuple2<String, Integer> t) throws Exception {
				return new Tuple2<String, Integer>(t._1, t._2);
			}
		});
		
		JavaRDD<Tuple2<String, Integer>> sort = map.sortBy(new Function<Tuple2<String,Integer>, Integer>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Integer call(Tuple2<String, Integer> t) throws Exception {
				return t._2;
			}
		}, false, 1);
		
		
		result = new HashMap<>();
		sort.foreach(new VoidFunction<Tuple2<String,Integer>>() {
			private static final long serialVersionUID = 1L;
			@Override
			public void call(Tuple2<String, Integer> t) throws Exception {
				result.put(t._1, t._2);
			}
		});
		
		System.out.println("日志分析结果：" + result);
		sc.close();
	}

}
