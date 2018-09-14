package com.gft.spark.common;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


public class InitListener implements ServletContextListener {

	Logger logger = Logger.getLogger(InitListener.class);
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		//ServletContext sc = sce.getServletContext();
		
		logger.error("系统强制关闭中。。。。。。");
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("系统初始化开始。。。。。。");
		
		Logger.getLogger("org.apache.hadoop").setLevel(Level.ERROR);
		Logger.getLogger("org.apache.zookeeper").setLevel(Level.ERROR);
		Logger.getLogger("org.apache.hive").setLevel(Level.ERROR);
		Logger.getLogger("org.apache.spark").setLevel(Level.ERROR);
		Logger.getLogger("kafka.utils").setLevel(Level.ERROR);
		Logger.getLogger("kafka.consumer").setLevel(Level.ERROR);
		Logger.getLogger("org.spark_project").setLevel(Level.ERROR);
		Logger.getLogger("org.I0Itec.zkclient").setLevel(Level.ERROR);
		Logger.getLogger("kafka.producer").setLevel(Level.ERROR);
		Logger.getLogger("kafka.client").setLevel(Level.ERROR);
		Logger.getLogger("kafka.javaapi.producer").setLevel(Level.ERROR);
		
		//ServletContext sc = sce.getServletContext();
		//WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(sc);

		
		
		logger.info("系统初始化完成！");
	}

}
