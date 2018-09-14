package test;

import java.util.Arrays;
import java.util.Iterator;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;

public class Test3 {

	public static void main(String[] args) {
		String fileName = "F:\\PeopleInfo.txt";
		
		SparkConf conf = new SparkConf();
		conf.setAppName("Test3");
		conf.setMaster("local");
		
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaRDD<String> lines = sc.textFile(fileName, 1);
		
		JavaRDD<String> maleLines = lines.filter(new Function<String, Boolean>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Boolean call(String s) throws Exception {
				return s.contains("M") && s.split(" ").length > 1;
			}
		});
		
		
		Long count = maleLines.count();
		System.out.println("总数：" + count);
		
		JavaRDD<String> maleheightLines = maleLines.flatMap(new FlatMapFunction<String, String>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Iterator<String> call(String s) throws Exception {
				return Arrays.asList(s.split(" ")[1]).iterator();
			}
		});
		
		JavaRDD<Integer> maleheightLinesInt = maleheightLines.map(new Function<String, Integer>(){
			private static final long serialVersionUID = 1L;
			@Override
			public Integer call(String s) throws Exception {
				return Integer.parseInt(String.valueOf(s));
			}
		});
		
		JavaRDD<Integer> maleheightLinesIntSort = maleheightLinesInt.sortBy(new Function<Integer, Integer>(){
			private static final long serialVersionUID = 1L;
			@Override
			public Integer call(Integer i) throws Exception {
				return i;
			}
		}, false, 3);
		
		Integer first = maleheightLinesIntSort.first();
		
		System.out.println("排序后第一个值：" + first);
		
		sc.close();
	}
	
	
	
}
