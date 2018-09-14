package test;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import com.gft.spark.dto.AqUser;

public class Test7 {

	public static void main(String[] args) {
		String sql = "select username, password from aq_user where 1=1 limit 1, 10 ";
		
		SparkConf conf = new SparkConf();
		conf.setAppName("Test7");
		conf.setMaster("local");
		
		JavaSparkContext sc = new JavaSparkContext(conf);
		SQLContext sqlContext = SQLContext.getOrCreate(JavaSparkContext.toSparkContext(sc));
		
		DataFrameReader reader = sqlContext.read().format("jdbc");
        reader.option("driver","com.mysql.jdbc.Driver");
        reader.option("url","jdbc:mysql://127.0.0.1:3306/data_dev");//数据库路径
        reader.option("user","root");
        reader.option("password","root");
        reader.option("dbtable","aq_user");//数据表名
		
		Dataset<Row> dataset = reader.load();
		dataset.show();
		
		JavaRDD<Row> javaRDD = dataset.javaRDD();
		
		JavaRDD<AqUser> userJavaRDD = javaRDD.map(new Function<Row, AqUser>() {
			private static final long serialVersionUID = 6870169754033066810L;
			@Override
			public AqUser call(Row row) throws Exception {
				AqUser user = new AqUser();
				user.setUsername(row.getString(1));
				user.setPassword(row.getString(2));
				return user;
			}
		});
		
		userJavaRDD.foreach(new VoidFunction<AqUser>() {
			private static final long serialVersionUID = -6158728000909125204L;
			@Override
			public void call(AqUser user) throws Exception {
				System.out.println(user.getUsername() + "----" + user.getPassword());
			}
		});
		
	}
	
}
