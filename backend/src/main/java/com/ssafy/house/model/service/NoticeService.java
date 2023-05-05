package com.ssafy.house.model.service;

import java.util.List;
import java.util.Map;

import com.ssafy.house.model.dto.Notice;

public interface NoticeService {
	public List<Notice> searchAll(Map<String, String> map);
	public Notice searchByNo(String no);
	public void insert(Notice notice);
	public void update(Notice notice);
	public void delete(String no);
	public void increaseHit(String no);
	public List<Notice> pagination(Map<String,Object> map);
}
