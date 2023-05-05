package com.ssafy.house.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.house.model.dao.QnaDao;
import com.ssafy.house.model.dto.Qna;

@Service
public class QnaServiceImp implements QnaService {

	@Autowired
	private QnaDao dao;
	
	@Transactional(readOnly = true)
	public List<Qna> searchAll(Map<String, String> map) {
		return dao.searchAll(map);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Qna searchByNo(String no) {
		return dao.searchByNo(no);
	}

	@Transactional
	@Override
	public void insert(Qna qna) {
		dao.insert(qna);
	}

	@Transactional
	@Override
	public void update(Qna qna) {
		dao.update(qna);
	}

	@Transactional
	@Override
	public void delete(String no) {
		dao.delete(no);
	}
	
	@Transactional(readOnly = true)
	public List<Qna> pagination(Map<String,Object> map) {
		return dao.pagination(map);
	}
}
