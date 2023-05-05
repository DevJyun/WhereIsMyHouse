package com.ssafy.house.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.house.model.dto.Qna;
import com.ssafy.house.exception.QnaException;
import com.ssafy.house.model.dto.User;
import com.ssafy.house.model.service.QnaService;
import com.ssafy.house.model.service.UserService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class QnaRestController {
	private Logger logger = LoggerFactory.getLogger(QnaRestController.class);
	
	@Autowired
	private QnaService qnaService;
	
	@Autowired
	private UserService userService;
	
	private static final String SUCCESS = "success";
	
	@ExceptionHandler
	public ResponseEntity<String> handler(Exception e){
		logger.error("ErrorHandler..........");
		logger.error("ErrorMessage..........{}", e.getMessage());
		e.printStackTrace();
		
		HttpHeaders resHeaders = new HttpHeaders();
		if (e instanceof QnaException) {
			return new ResponseEntity<String>(e.getMessage(), resHeaders, HttpStatus.FAILED_DEPENDENCY);
		}else {
			return new ResponseEntity<String>("처리 중 오류 발생", resHeaders, HttpStatus.FAILED_DEPENDENCY);
		}
	}
	
	@ApiOperation(value = "QnA 목록 정보", notes = "모든 QnA 정보 반환")
	@GetMapping("/qna")
	public ResponseEntity<?> qnaList(String key, String word, Integer pageNo){
		logger.debug("QnaList..........");
		int pageNum = 1;
		int recordsPerPage = 10;
		if(pageNo != null) {
			pageNum = pageNo;
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("start", (pageNum-1)*recordsPerPage);
		map.put("len", recordsPerPage);
		map.put("key", key);
		map.put("word", word);
		
		Map<String, String> searchMap = new HashMap<>();
		map.put("key", key);
		map.put("word", word);
		
		List<Qna> qnaList = qnaService.searchAll(searchMap);
		List<Qna> dividedQnAList = qnaService.pagination(map);
		int noOfRecords = qnaList.size();
		int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("noOfPages", noOfPages);
		paramMap.put("currentPage", pageNum);
		
		logger.debug("QnaList..........{}", qnaList);
		if(qnaList != null && !qnaList.isEmpty()) {
			User search;
			for (Qna qna : dividedQnAList) {
				search = userService.idSearch(qna.getId());
				if (search != null) qna.setName(search.getName());
			}
			paramMap.put("qnaList", dividedQnAList);
			
			return new ResponseEntity<Map<String, Object>>(paramMap, HttpStatus.OK);
		}else {
			return new ResponseEntity<List<Void>>(HttpStatus.NO_CONTENT);
		}
	}
	
	@ApiOperation(value = "QnA 정보 검색", notes = "전달 받은 데이터를 이용해서 QnA 정보 검색")
	@GetMapping("/qna/{no}")
	public ResponseEntity<?> find(@PathVariable String no){
		logger.debug("Qna search.......... no:{}", no);
		Qna qna = qnaService.searchByNo(no);
		logger.debug("Qna search.......... qna:{}", qna);
		if(qna != null) {
			System.out.println(qna.getId());
			qna.setName(userService.idSearch(qna.getId()).getName());
			return new ResponseEntity<Qna>(qna, HttpStatus.OK);
		}else {
			return new ResponseEntity<List<Void>>(HttpStatus.NO_CONTENT);
		}
	}
	
	@ApiOperation(value = "QnA 등록", notes = "전달 받은 데이터를 이용해서 QnA 정보 등록")
	@PostMapping("/qna")
	public ResponseEntity<?> regist(@RequestBody Qna qna){
		logger.debug("Qna regist..........{}", qna);
		qnaService.insert(qna);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@ApiOperation(value = "QnA 수정", notes = "전달 받은 데이터를 이용해서 QnA 정보 수정")
	@PutMapping("/qna")
	public ResponseEntity<?> update(@RequestBody Qna qna){
		logger.debug("Qna update..........{}", qna);
		qnaService.update(qna);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@ApiOperation(value = "QnA 삭제", notes = "전달 받은 데이터를 이용해서 QnA 정보 삭제")
	@DeleteMapping("/qna/{no}")
	public ResponseEntity<?> delete(@PathVariable String no){
		logger.debug("Qna delete..........{}", no);
		qnaService.delete(no);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
//	@ApiOperation(value = "QnA 검색", notes = "전달 받은 데이터로 검색된 QnA 정보 반환")
//	@GetMapping("/qna/{key}/{pageNo}/{word}")
//	public ResponseEntity<?> search(@PathVariable String key, @PathVariable int pageNo, @PathVariable String word){
//		logger.debug("Qna search with condition..........");
//		List<Qna> qnaList = null;
////		no, title, id, all
//		switch(key) {
//			case "no":
//				qnaList = qnaService.searchListByNo(word);
//				break;
//			case "title":
//				qnaList = qnaService.searchListByTitle(word);
//				break;
//			case "id":
//				qnaList = qnaService.searchListById(word);
//				break;
//			case "all":
//				qnaList = qnaService.searchListByAll(word);
//				break;
//		}
//	}
}
