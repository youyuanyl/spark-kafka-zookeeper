package test2;

import java.util.Arrays;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.StorageLevels;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import com.gft.spark.utils.SparkUtils;

import scala.Tuple2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.HasOffsetRanges;
import org.apache.spark.streaming.kafka.KafkaCluster;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.apache.spark.streaming.kafka.OffsetRange;
 
 
import kafka.common.TopicAndPartition;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;
import scala.Predef;
import scala.Tuple2;
import scala.collection.JavaConversions;

public class KafkaSparkMysql {

	
	public static void main(String[] args) throws InterruptedException {
		
		String dataDirectory = "E:\\logs\\test";
		
		//JavaSparkContext sc = SparkUtils.getJavaSparkContext(KafkaSparkMysql.class);
		
		JavaStreamingContext jssc = SparkUtils.getJavaStreamingContext(KafkaSparkMysql.class);
		System.out.println(jssc);
		
		//JavaReceiverInputDStream<String> lines = jssc.socketTextStream("localhost", 8083, StorageLevels.MEMORY_AND_DISK_SER);
		//JavaDStream<String> words = lines.flatMap(x -> Arrays.asList(x.split(" ")).iterator());
		
		JavaDStream<String> words = jssc.textFileStream(dataDirectory);
		
		// Count each word in each batch
		JavaPairDStream<String, Integer> pairs = words.mapToPair(s -> new Tuple2<>(s, 1));
		JavaPairDStream<String, Integer> wordCounts = pairs.reduceByKey((i1, i2) -> i1 + i2);

		// Print the first ten elements of each RDD generated in this DStream to the console
		wordCounts.print();
		jssc.start();              // Start the computation
		jssc.awaitTermination();   // Wait for the computation to terminate
		
	}
	
}
