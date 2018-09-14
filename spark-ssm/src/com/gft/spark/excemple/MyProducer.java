package com.gft.spark.excemple;

import java.util.Properties;
import java.util.Random;

import kafka.javaapi.producer.Producer; 
import kafka.producer.KeyedMessage; 
import kafka.producer.ProducerConfig; 
   
public class MyProducer {   
     
        public static void main(String[] args) {
            System.out.println("开始发送消息！");
        	
        	Properties props = new Properties();   
            props.setProperty("metadata.broker.list","211.88.30.7:9092,");   
            props.setProperty("serializer.class","kafka.serializer.StringEncoder");   
            props.put("request.required.acks","1");   
            ProducerConfig config = new ProducerConfig(props);   
            //创建生产这对象
            Producer<String, String> producer = new Producer<String, String>(config);
            
            try {
            	Random random = new Random();// 生成随机数
                while(true){
                	int flag = random.nextInt(10);
                	if(flag % 3 == 1){
                		//生成消息
                        KeyedMessage<String, String> data2 = new KeyedMessage<String, String>("top1","除以3结果：余数为1！");
                        //发送消息
                        producer.send(data2);
                	}else if(flag % 3 == 2){
                		//生成消息
                        KeyedMessage<String, String> data2 = new KeyedMessage<String, String>("top1","除以3结果：余数为2！");
                        //发送消息
                        producer.send(data2);
                	}else{
                		//生成消息
                        KeyedMessage<String, String> data2 = new KeyedMessage<String, String>("top1","除以3结果：整除！");
                        //发送消息
                        producer.send(data2);
                	}
                    Thread.sleep(1000);
                } 
            } catch (Exception e) {   
                e.printStackTrace();   
            }   
            producer.close();   
            
            System.out.println("开始发送消息！");
        }   
}
