package com.ssafy.house.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.house.model.dto.Favorite;
import com.ssafy.house.model.dto.House;
import com.ssafy.house.model.service.FavoriteService;
import com.ssafy.house.model.service.HouseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class FavoriteRestController {
	private Logger logger = LoggerFactory.getLogger(FavoriteRestController.class);
	
	@Autowired
	private HouseService houseService;
	
	@Autowired
	private FavoriteService favoriteService;
	
	@ApiOperation(value = "즐겨찾기 목록 정보", notes = "유저의 즐겨찾기 정보 반환")
	@GetMapping("/favorite/{id}/{page}")
	public ResponseEntity<?> favorite(@PathVariable String id, @PathVariable Integer page){
		List<Favorite> favoriteList = favoriteService.getFavorites(id);
		logger.debug("favorite.......... favoriteList : {}", favoriteList);
		
		if(favoriteList != null && !favoriteList.isEmpty()) {
			Map<String, Object> map = new HashMap<String, Object>();
			String dong = favoriteList.get(0).getDongCode();
			List<House> houseList = houseService.searchByDong(dong);
			map.put("dongCode", dong);
			map.put("mapResult", houseList);
			
//			System.out.println("mapResult : "+houseList.size());
			
			int pageNum = 1;
			int recordsPerPage = 10;
			if(page != null) {
				pageNum = page;
			}
			
			map.put("id", id);
			map.put("start", (pageNum-1)*recordsPerPage);
			map.put("len", recordsPerPage);
			
			List<Favorite> dividedFavorites = favoriteService.doPagination(map);
			int noOfRecords = favoriteList.size();
	        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
	        
			map.put("favorites", dividedFavorites);
			map.put("noOfPages", noOfPages);
			map.put("currentPage", page);
			
			logger.debug("favorite.......... dividedFavorites : {}", dividedFavorites);
			
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		}else {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
	}
	
	@ApiOperation(value = "카카오맵 정보", notes = "카카오맵 정보 반환")
	@GetMapping("/showMap/{dongCode}/{id}")
	public ResponseEntity<?> showMap(@PathVariable String dongCode, @PathVariable String id){
		List<House> houses = houseService.searchByDong(dongCode);
		logger.debug("showMap.......... houses : {}", houses);
		
		if(houses != null && !houses.isEmpty()) {
			Map<String,Object> map = new HashMap<String,Object>();        
			map.put("mapResult", houses);
			
			logger.debug("showMap.......... mapResult : {}", houses);
			
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		}else {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
		
	}
}
