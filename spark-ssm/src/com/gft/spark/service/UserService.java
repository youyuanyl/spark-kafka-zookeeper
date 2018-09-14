package com.gft.spark.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gft.spark.dao.AqLogDAO;
import com.gft.spark.dao.AqUserDAO;
import com.gft.spark.dto.AqLog;
import com.gft.spark.dto.AqUser;

@Service
@Transactional
public class UserService implements Serializable {
	private static final long serialVersionUID = -378694286809289807L;
	
	@Autowired
	private AqUserDAO userDAO;
	
	
	public List<AqUser> getUserList(AqUser user) {
		List<AqUser> list = new ArrayList<>();
		list.add(new AqUser("user1", "pwd1"));
		list.add(new AqUser("user2", "pwd2"));
		list.add(new AqUser("user3", "pwd3"));
		list.add(new AqUser("用户1", "pwd4"));
		return list;
	}

}
