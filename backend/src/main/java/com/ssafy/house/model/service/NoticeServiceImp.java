package com.ssafy.house.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.house.model.dao.NoticeDao;
import com.ssafy.house.model.dto.Notice;

@Service
public class NoticeServiceImp implements NoticeService {

	@Autowired
	private NoticeDao dao;
	
	@Override
	public List<Notice> searchAll(Map<String, String> map) {
		return dao.searchAll(map);
	}

	@Override
	public Notice searchByNo(String no) {
		return dao.searchByNo(no);
	}

	@Override
	public void insert(Notice notice) {
		dao.insert(notice);
	}

	@Override
	public void update(Notice notice) {
		dao.update(notice);
	}

	@Override
	public void delete(String no) {
		dao.delete(no);
	}

	@Override
	public void increaseHit(String no) {
		dao.increaseHit(no);
	}
	
	@Transactional(readOnly = true)
	public List<Notice> pagination(Map<String,Object> map) {
		return dao.pagination(map);
	}

}
