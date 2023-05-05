package com.ssafy.house.model.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ssafy.house.model.dto.Favorite;

public interface FavoriteService {
	List<Favorite> getFavorites(String id);
	boolean searchFavorite(Map<String,Object> map);
	void insertFavorite(Map<String,Object> map);
	void removeFavorite(Map<String,Object> map);
	List<Favorite> doPagination(Map<String,Object> map);
}
