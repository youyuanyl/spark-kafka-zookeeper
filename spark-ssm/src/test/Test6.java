package test;

import java.sql.ResultSet;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.rdd.JdbcRDD;

import com.gft.spark.common.MyConnectionFactory;
import com.gft.spark.dto.AqUser;

import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;

public class Test6 {

	public static void main(String[] args) {
		
		SparkConf conf = new SparkConf();
		conf.setAppName("Test6");
		conf.setMaster("local");
		
		JavaSparkContext sc = new JavaSparkContext(conf);
		
		
		
		
		
		
		String sql = "select username, password from aq_user limit ?,? ";
        JavaRDD<AqUser> jdbcRDD = JdbcRDD.create(sc, new MyConnectionFactory(), sql, 0, 100, 1, new Function<ResultSet, AqUser>() {
            private static final long serialVersionUID = -3862012009705676636L;

            public AqUser call(ResultSet v1) throws Exception {
                
            	AqUser user = new AqUser();
            	
            	//System.out.println(v1.getString(1));
            	//System.out.println(v1.getString(2));
            	user.setUsername(v1.getString(1));
            	user.setPassword(v1.getString(2));
            	return user;
            }
        });
        
        jdbcRDD.foreach(new VoidFunction<AqUser>() {
			private static final long serialVersionUID = -5161089297074894200L;
			
			@Override
			public void call(AqUser user) throws Exception {
				System.out.println(user.getUsername() + "----" + user.getPassword());
			}
		});
        
        
        sc.close();
	}
	
}
