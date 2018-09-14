package com.gft.spark.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gft.spark.dao.AqLogDAO;
import com.gft.spark.dto.AqLog;

@Service
public class LogService {
	
	@Autowired
	private AqLogDAO logDAO;
	
	public void insert(List<AqLog> list) {
		System.out.println(list.size());
		if(null != list && list.size() > 0){
			logDAO.insert(list);
		}
	}
	
}
