package com.ssafy.house.model.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.house.model.dao.FavoriteDao;
import com.ssafy.house.model.dto.Favorite;

@Service
public class FavoriteServiceImpl implements FavoriteService {

	@Autowired
	private FavoriteDao dao;
	
	@Transactional(readOnly = true)
	public List<Favorite> getFavorites(String id) {
		return dao.getFavorites(id);
	}
	
	@Transactional(readOnly = true)
	public boolean searchFavorite(Map<String,Object> map) {
		return dao.searchFavorite(map);
	}
	
	@Transactional
	public void insertFavorite(Map<String,Object> map) {
		dao.insertFavorite(map);
	}
	
	@Transactional
	public void removeFavorite(Map<String,Object> map) {
		dao.removeFavorite(map);
	}
	
	@Transactional(readOnly = true)
	public List<Favorite> doPagination(Map<String,Object> map) {
		return dao.doPagination(map);
	}

}
