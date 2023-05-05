package com.ssafy.house.model.dao;

import java.util.Map;

import com.ssafy.house.model.dto.User;

public interface UserDao {
	void insert(User user);
	User search(User user);
	void update(User user);
	void remove(String id);
	String findPassword(User user);
	User idSearch(String id);
	void saveRefreshToken(Map<String, String> map);
	Object getRefreshToken(String userid);
	void deleteRefreshToken(Map<String, String> map);
}
