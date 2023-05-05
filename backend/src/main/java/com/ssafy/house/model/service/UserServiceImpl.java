package com.ssafy.house.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.house.model.dao.UserDao;
import com.ssafy.house.model.dto.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserDao dao;
	
	@Override
	public void insert(User user) {
		dao.insert(user);
	}

	@Override
	public User search(User user) {
		return dao.search(user);
	}

	@Override
	public void update(User user) {
		dao.update(user);
	}

	@Override
	public void remove(String id) {
		dao.remove(id);
	}

	@Override
	public String findPassword(User user) {
		return dao.findPassword(user);
	}

	@Override
	public User idSearch(String id) {
		return dao.idSearch(id);
	}
	
	@Override
	public void saveRefreshToken(String userid, String refreshToken) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("token", refreshToken);
		dao.saveRefreshToken(map);
	}

	@Override
	public Object getRefreshToken(String userid) {
		return dao.getRefreshToken(userid);
	}

	@Override
	public void deleteRefreshToken(String userid) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("token", null);
		dao.deleteRefreshToken(map);
	}

}
