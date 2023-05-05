package com.ssafy.house.model.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Qna implements Serializable{
	private String no;
	private String id;
	private String title;
	private String question;
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private String regtime;
	private String completed;
	private String answer;
	private String name;
	
	public Qna() {
		super();
	}

	public Qna(String no, String id, String title, String question, String regtime, String completed, String answer, String name) {
		super();
		this.no = no;
		this.id = id;
		this.title = title;
		this.question = question;
		this.regtime = regtime;
		this.completed = completed;
		this.answer = answer;
		this.name = name;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getRegtime() {
		return regtime;
	}

	public void setRegtime(String regtime) {
		this.regtime = regtime;
	}

	public String getCompleted() {
		return completed;
	}

	public void setCompleted(String completed) {
		this.completed = completed;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Qna [no=" + no + ", id=" + id + ", title=" + title + ", question=" + question + ", regtime=" + regtime
				+ ", completed=" + completed + ", answer=" + answer + ", name=" + name + "]";
	}

}
