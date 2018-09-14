package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

public class Test4 {

	private static List<Map<String, Integer>> result;
	
	public static void main(String[] args) {
		String fileName = "e:\\fangwei.txt";
		
		SparkConf conf = new SparkConf();
		conf.setAppName("Test3");
		conf.setMaster("local");
		
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaRDD<String> lines = sc.textFile(fileName, 1);
		
		JavaRDD<String> filter = lines.filter(new Function<String, Boolean>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Boolean call(String s) throws Exception {
				return s.split(" ").length > 1;
			}
		});
		
		
		Long count = filter.count();
		System.out.println("总数：" + count);
		
		JavaRDD<String> map = filter.flatMap(new FlatMapFunction<String, String>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Iterator<String> call(String s) throws Exception {
				return Arrays.asList(s.split(" ")[0]).iterator();
			}
		});
		
		JavaPairRDD<String, Integer> pair = map.mapToPair(new PairFunction<String, String, Integer>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Tuple2<String, Integer> call(String s) throws Exception {
				return new Tuple2<String, Integer>(s, 1);
			}
		});
		
		JavaPairRDD<String, Integer> reduce = pair.reduceByKey(new Function2<Integer, Integer, Integer>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Integer call(Integer i1, Integer i2) throws Exception {
				return i1 + i2;
			}
		});
		
		JavaRDD<Tuple2<String, Integer>> map2 = reduce.map(new Function<Tuple2<String,Integer>, Tuple2<String,Integer>>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Tuple2<String, Integer> call(Tuple2<String, Integer> tuple) throws Exception {
				// TODO Auto-generated method stub
				return new Tuple2<String, Integer>(tuple._1, tuple._2);
			}
		});
		
		JavaRDD<Tuple2<String, Integer>> sort = map2.sortBy(new Function<Tuple2<String,Integer>, Integer>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Integer call(Tuple2<String, Integer> t) throws Exception {
				return t._2;
			}
		}, false, 1);
		
		//Map<String, Integer> result = new HashMap<String, Integer>();
		
		result = new ArrayList<Map<String, Integer>>();
		
		sort.foreach(new VoidFunction<Tuple2<String,Integer>>() {
			private static final long serialVersionUID = 1L;
			@Override
			public void call(Tuple2<String, Integer> t) throws Exception {
				Map<String, Integer> rempMap = new HashMap<String, Integer>();
				rempMap.put(t._1, t._2);
				result.add(rempMap);
			}
		});
		
		long resultCount = 0;
		for (int i = 0; i < result.size(); i++) {
			Map<String, Integer> remp = result.get(i);
			
			Set<String> keys = remp.keySet();
			Iterator<String> it = keys.iterator();
			while(it.hasNext()){
				String key = it.next();
				Integer value = remp.get(key);
				resultCount += value;
			}
		}
		
		System.out.println("结果：" + result);
		System.out.println("结果总数：" + resultCount);
		
		sc.close();
	}
	
	
	
}
