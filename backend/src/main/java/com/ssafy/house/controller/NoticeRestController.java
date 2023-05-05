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

import com.ssafy.house.model.dto.Notice;
import com.ssafy.house.exception.NoticeException;
import com.ssafy.house.model.dto.User;
import com.ssafy.house.model.service.NoticeService;
import com.ssafy.house.model.service.UserService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class NoticeRestController {
	private Logger logger = LoggerFactory.getLogger(NoticeRestController.class);
	
	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private UserService userService;
	
	private static final String SUCCESS = "success";
	
	@ExceptionHandler
	public ResponseEntity<String> handler(Exception e){
		logger.error("ErrorHandler..........");
		logger.error("ErrorMessage..........{}", e.getMessage());
		e.printStackTrace();
		
		HttpHeaders resHeaders = new HttpHeaders();
		if (e instanceof NoticeException) {
			return new ResponseEntity<String>(e.getMessage(), resHeaders, HttpStatus.FAILED_DEPENDENCY);
		}else {
			return new ResponseEntity<String>("처리 중 오류 발생", resHeaders, HttpStatus.FAILED_DEPENDENCY);
		}
	}
	
	@ApiOperation(value = "공지사항 목록 정보", notes = "모든 공지사항 정보 반환")
	@GetMapping("/notice")
	public ResponseEntity<?> noticeList(String key, String word, Integer pageNo){
		logger.debug("NoticeList..........");
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
		
		List<Notice> noticeList = noticeService.searchAll(searchMap);
		List<Notice> dividedNoticeList = noticeService.pagination(map);
		int noOfRecords = noticeList.size();
		int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("noOfPages", noOfPages);
		paramMap.put("currentPage", pageNum);
		
		logger.debug("noticeList..........{}", noticeList);
		if(noticeList != null && !noticeList.isEmpty()) {
			User search;
			for (Notice notice : dividedNoticeList) {
				search = userService.idSearch(notice.getId());
				if (search != null) notice.setName(search.getName());
			}
			paramMap.put("noticeList", dividedNoticeList);
			return new ResponseEntity<Map<String, Object>>(paramMap, HttpStatus.OK);
		}else {
			return new ResponseEntity<List<Void>>(HttpStatus.NO_CONTENT);
		}
	}
	
	@ApiOperation(value = "공지사항 정보 검색", notes = "전달 받은 데이터를 이용해서 공지사항 정보 검색")
	@GetMapping("/notice/{no}")
	public ResponseEntity<?> find(@PathVariable String no){
		logger.debug("Notice search.......... no:{}", no);
		Notice notice = noticeService.searchByNo(no);
		logger.debug("Notice search.......... notice:{}", notice);
		if(notice != null) {
			notice.setName(userService.idSearch(notice.getId()).getName());
			return new ResponseEntity<Notice>(notice, HttpStatus.OK);
		}else {
			return new ResponseEntity<List<Void>>(HttpStatus.NO_CONTENT);
		}
	}
	
	@ApiOperation(value = "공지사항 등록", notes = "전달 받은 데이터를 이용해서 공지사항 정보 등록")
	@PostMapping("/notice")
	public ResponseEntity<?> regist(@RequestBody Notice notice){
		logger.debug("Notice regist..........{}", notice);
		noticeService.insert(notice);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@ApiOperation(value = "공지사항 수정", notes = "전달 받은 데이터를 이용해서 공지사항 정보 수정")
	@PutMapping("/notice")
	public ResponseEntity<?> update(@RequestBody Notice notice){
		logger.debug("Notice update..........{}", notice);
		noticeService.update(notice);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@ApiOperation(value = "공지사항 삭제", notes = "전달 받은 데이터를 이용해서 공지사항 정보 삭제")
	@DeleteMapping("notice/{no}")
	public ResponseEntity<?> delete(@PathVariable String no){
		logger.debug("Notice delete..........{}", no);
		noticeService.delete(no);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@ApiOperation(value = "공지사항 조회수 증가", notes = "전달 받은 글 번호를 이용해서 해당 공지사항의 조회수를 1증가")
	@PutMapping("notice/{no}")
	public ResponseEntity<?> increaseHit(@PathVariable String no){
		logger.debug("Notice increaseHit..........{}", no);
		noticeService.increaseHit(no);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
