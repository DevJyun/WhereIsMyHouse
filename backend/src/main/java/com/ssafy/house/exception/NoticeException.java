package com.ssafy.house.exception;


public class NoticeException extends RuntimeException {
	public NoticeException() {
		super("Notice 정보를 처리 중 오류 발생");
	}
	public NoticeException(String msg) {
		super(msg);
	}
}
