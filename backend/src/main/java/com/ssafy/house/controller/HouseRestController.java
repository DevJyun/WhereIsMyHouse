package com.ssafy.house.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.house.model.dto.Favorite;
import com.ssafy.house.model.dto.House;
import com.ssafy.house.model.dto.User;
import com.ssafy.house.model.service.FavoriteService;
import com.ssafy.house.model.service.HouseService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class HouseRestController {
	private Logger logger = LoggerFactory.getLogger(HouseRestController.class);
	
	@Autowired
	private HouseService houseService;
	
	@Autowired
	private FavoriteService favoriteService;
	
	private static final String SUCCESS = "success";
	
	@ApiOperation(value = "아파트 이름(aptName)과 페이지(page)로 검색된 아파트 정보", notes = "검색된 아파트 정보 반환")
	@GetMapping("/aptFind")
	public ResponseEntity<?> aptFind(String aptName, Integer pageNo){
		logger.debug("aptFind..........");
		int pageNum = 1;
		int recordsPerPage = 10;
		if(pageNo != null) {
			pageNum = pageNo;
		}
		System.out.println(pageNum);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("aptName", aptName);
		map.put("start", (pageNum-1)*recordsPerPage);
		map.put("len", recordsPerPage);
		
		List<House> aptList = houseService.searchByApt(aptName);
		List<House> dividedAptList = houseService.pagination(map);
		int noOfRecords = aptList.size();
		int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("aptName", aptName);
		paramMap.put("aptList", dividedAptList);
		paramMap.put("noOfPages", noOfPages);
		paramMap.put("currentPage", pageNum);

		if(dividedAptList != null & !dividedAptList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(paramMap, HttpStatus.OK);
		}else {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
		
	}
	
	
	@ApiOperation(value = "동코드(dongCode)로 검색된 아파트 정보", notes = "검색된 아파트 정보 반환")
	@GetMapping("/dongFind")
	public ResponseEntity<?> dongfind(String dongCode, String id, Integer pageNo) {
		int pageNum = 1;
		int recordsPerPage = 10;
		if(pageNo != null) {
			pageNum = pageNo;
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("dongCode", dongCode);
		map.put("start", (pageNum-1)*recordsPerPage);
		map.put("len", recordsPerPage);
		
		List<House> houseList = houseService.searchByDong(dongCode);
		List<House> dividedHouseList = houseService.dongPagination(map);
		int noOfRecords = houseList.size();
		int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		House findName = houseService.searchAddressName(dongCode);
		if (findName != null) {
			paramMap.put("addressName", findName.getSidoName() + " " + findName.getGugunName() + " " + findName.getDongName());
		}
		
		Map<String, Object> favMap = new HashMap<String, Object>();
		favMap.put("id", id);
		favMap.put("dongCode", dongCode);
		
		if (favoriteService.searchFavorite(favMap)) {
			paramMap.put("isFav", 1);
		} else {
			paramMap.put("isFav", 0);
		}
		
		paramMap.put("dongCode", dongCode);
		paramMap.put("houseList", dividedHouseList);
		paramMap.put("noOfPages", noOfPages);
		paramMap.put("currentPage", pageNum);
		
//		if(dividedHouseList != null & !dividedHouseList.isEmpty()) {
//			return new ResponseEntity<Map<String, Object>>(paramMap, HttpStatus.OK);
//		}else {
//			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
//		}

		return new ResponseEntity<Map<String, Object>>(paramMap, HttpStatus.OK);
	}
	
	@ApiOperation(value = "번호(no)로 검색된 집 세부 정보", notes = "검색된 집 정보 반환")
	@GetMapping("/showHouseDetail/{no}")
	public ResponseEntity<?> showHouseDetail(@PathVariable int no) {
		House house = houseService.searchByNo(no);
		
		if (house != null) {
			return new ResponseEntity<House>(house, HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
		
	}

	@ApiOperation(value = "아이디(id)와 페이지(page)로 즐겨찾기 등록한 집 정보", notes = "검색된 집 정보 반환")
	@PostMapping("/favorite")
	public ResponseEntity<?> favorite(@RequestBody HashMap<String, Object> reqMap) {
		String id = (String) reqMap.get("id");
		Integer page = (Integer) reqMap.get("page");
		
		List<Favorite> favoritesAll = favoriteService.getFavorites(id);
		
		if(favoritesAll.isEmpty()) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        } 
		
		Map<String , Object> paramMap = new HashMap<String, Object>();
		
		String dong = favoritesAll.get(0).getDongCode();
		paramMap.put("dongCode", dong);
    	List<House> houses = houseService.searchByDong(dong);
    	paramMap.put("mapResult", houses);
    	
    	int pageNum = 1;
		int recordsPerPage = 10;
		if(page != null) {
			pageNum = page;
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", id);
		map.put("start", (pageNum-1)*recordsPerPage);
		map.put("len", recordsPerPage);
		
		List<Favorite> dividedFavorites = favoriteService.doPagination(map);
        int noOfRecords = favoritesAll.size();
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
		
		paramMap.put("id", id);
		paramMap.put("favorites", dividedFavorites);
		paramMap.put("noOfPages", noOfPages);
		paramMap.put("currentPage", page);
		return new ResponseEntity<Map<String, Object>>(paramMap, HttpStatus.OK);
	}
	
	@ApiOperation(value = "동코드(dongCode)와 아이디(id)와 페이지(page)로 지도에 필요한 정보", notes = "검색된 정보 반환")
	@PostMapping("/showMap")
	public ResponseEntity<?> showMap(@RequestBody HashMap<String, Object> reqMap) {
		String dongCode = (String) reqMap.get("dongCode");
		String id = (String) reqMap.get("id");
		Integer page = (Integer) reqMap.get("page");
		List<House> houses = houseService.searchByDong(dongCode);
		
		if(houses.isEmpty()) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
		
		List<Favorite> favoritesAll = favoriteService.getFavorites(id);
		
		int pageNum = 1;
		int recordsPerPage = 10;
		if(page != null) {
			pageNum = page;
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", id);
		map.put("start", (pageNum-1)*recordsPerPage);
		map.put("len", recordsPerPage);
		
		List<Favorite> dividedFavorites = favoriteService.doPagination(map);
        int noOfRecords = favoritesAll.size();
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
        
        Map<String, Object> paramMap = new HashMap<String, Object>();
        
		paramMap.put("id", id);
		paramMap.put("dongCode", dongCode);
		paramMap.put("favorites", dividedFavorites);
		paramMap.put("noOfPages", noOfPages);
		paramMap.put("currentPage", page);
		paramMap.put("mapResult", houses);
		
		return new ResponseEntity<Map<String, Object>>(paramMap, HttpStatus.OK);
	}
	
	@ApiOperation(value = "아파트 이름(aptName)로 검색된 아파트 정보", notes = "검색된 거래량 반환")
	@GetMapping("/dealCountFind/{aptName}")
	public ResponseEntity<?> dealCountFind(@PathVariable("aptName") String aptName) {
		int[] dealCount = new int[12];
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("aptName", aptName);
		
		for (int month = 1; month <= 12; month++) {
			map.put("dealMonth", month);
			dealCount[month-1] = houseService.searchDealCount(map);
		}

		return new ResponseEntity<int[]>(dealCount, HttpStatus.OK);
	}
}
