package test;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;

public class Test {

	public static void main(String[] args) throws Exception {
		SparkConf conf = new SparkConf().setAppName("WordCounter").setMaster("local");

		String fileName = "e:/fangwei.txt";

		JavaSparkContext sc = new JavaSparkContext(conf);

		JavaRDD<String> lines = sc.textFile(fileName, 1);

		JavaRDD<Integer> add = lines.map(new Function<String, Integer>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Integer call(String v1) throws Exception {
				return v1.length();
			}
		});

		add.foreach(new VoidFunction<Integer>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void call(Integer t) throws Exception {
				System.out.println(t);
			}
		});

		sc.close();
	}
	
	
	
	
}
