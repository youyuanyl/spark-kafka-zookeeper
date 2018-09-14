package com.gft.spark.common;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.spark.rdd.JdbcRDD.ConnectionFactory;

public class MyConnectionFactory implements ConnectionFactory {
	private static final long serialVersionUID = -1342165559673933771L;
	
	@Override
	public Connection getConnection() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/data_dev", "root","root");
		return conn;
	}

}
