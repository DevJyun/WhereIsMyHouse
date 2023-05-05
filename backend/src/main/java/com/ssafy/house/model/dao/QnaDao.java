package com.ssafy.house.model.dao;

import java.util.List;
import java.util.Map;

import com.ssafy.house.model.dto.Qna;

public interface QnaDao {
	public List<Qna> searchAll(Map<String, String> map);
	public Qna searchByNo(String no);
	public void insert(Qna qna);
	public void update(Qna qna);
	public void delete(String no);
	public List<Qna> pagination(Map<String,Object> map);
}
