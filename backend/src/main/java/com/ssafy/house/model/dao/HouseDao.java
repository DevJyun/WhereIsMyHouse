package com.ssafy.house.model.dao;

import java.util.List;
import java.util.Map;

import com.ssafy.house.model.dto.House;

public interface HouseDao {
	List<House> searchByApt(String aptName);
	List<House> searchByDong(String dongCode);
	House searchByNo(int no);
	House searchAddressName(String dongCode);
	int searchDealCount(Map<String,Object> map);
	List<House> pagination(Map<String,Object> map);
	List<House> dongPagination(Map<String,Object> map);
}
