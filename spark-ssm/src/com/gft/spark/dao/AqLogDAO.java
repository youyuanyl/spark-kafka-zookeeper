package com.gft.spark.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gft.spark.dto.AqLog;

@Repository
public class AqLogDAO extends BaseDAO<AqLog> {
	
	
	
    public int deleteByPrimaryKey(String id) {
		return 0;
	}

    public int insert(AqLog record) {
		return 0;
	}

    public int insertSelective(AqLog record) {
		return 0;
	}

    public AqLog selectByPrimaryKey(String id) {
		return null;
	}

    public int updateByPrimaryKeySelective(AqLog record) {
		return 0;
	}

    public int updateByPrimaryKey(AqLog record) {
		return 0;
	}

	public void insert(List<AqLog> list) {
		this.insert("com.gft.app.dao.AqLogMapper.insertByBatch", list);
	}
}