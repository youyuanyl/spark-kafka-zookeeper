package com.gft.spark.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;

public class BaseDAO<T> {
	
	@Resource
	private SqlSessionTemplate sqlSessionTemplate;

	public int delete(String statement, String parameter) {
		return sqlSessionTemplate.delete(statement, parameter);
	}

	
	public int insert(String statement, T parameter) {
		return sqlSessionTemplate.insert(statement, parameter);
	}
	
	public int insert(String statement, List<T> parameter) {
		return sqlSessionTemplate.insert(statement, parameter);
	}
	
	public int insertByBatch(String statement, List<T> list) {
		return sqlSessionTemplate.insert(statement, list);
	}
	
	
	public T get(String statement, String parameter) {
		return sqlSessionTemplate.selectOne(statement, parameter);
	}

	
	public int update(String statement, T parameter) {
		return sqlSessionTemplate.update(statement, parameter);
	}

	
	public List<T> find(String statement, T obj) {
		return sqlSessionTemplate.selectList(statement, obj);
	}
	
	
	public List<T> find(String statement, String parameter) {
		return sqlSessionTemplate.selectList(statement, parameter);
	}

	
	public List<T> find(String statement, Map<String, Object> map) {
		return sqlSessionTemplate.selectList(statement, map);
	}
	
	
	public List<T> findAll(String statement) {
		return sqlSessionTemplate.selectList(statement);
	}

	
	public T get(String statement, T obj) {
		return sqlSessionTemplate.selectOne(statement, obj);
	}

	
	public Integer count(String statement) {
		return sqlSessionTemplate.selectOne(statement);
	}

	
	public Integer count(String statement, T obj) {
		return sqlSessionTemplate.selectOne(statement, obj);
	}
}
