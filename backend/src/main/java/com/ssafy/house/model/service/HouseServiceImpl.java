package com.ssafy.house.model.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.house.model.dao.HouseDao;
import com.ssafy.house.model.dto.House;

@Service
public class HouseServiceImpl implements HouseService {
	
	@Autowired
	private HouseDao dao;
	
	@Transactional(readOnly = true)
	public List<House> searchByApt(String aptName) {
		return dao.searchByApt(aptName);
	}
	
	@Transactional(readOnly = true)
	public List<House> searchByDong(String dongCode) {
		return dao.searchByDong(dongCode);
	}
	
	@Transactional(readOnly = true)
	public House searchByNo(int no) {
		return dao.searchByNo(no);
	}
	
	@Transactional(readOnly = true)
	public House searchAddressName(String dongCode) {
		return dao.searchAddressName(dongCode);
	}
	
	@Transactional(readOnly = true)
	public int searchDealCount(Map<String, Object> map) {
		return dao.searchDealCount(map);
	}
	
	@Transactional(readOnly = true)
	public List<House> pagination(Map<String,Object> map) {
		return dao.pagination(map);
	}
	
	@Transactional(readOnly = true)
	public List<House> dongPagination(Map<String,Object> map) {
		return dao.dongPagination(map);
	}

}
