package com.ssafy.house.model.dto;

public class Notice {
	private String no;
	private String title;
	private String content;
	private String id;
	private String hit;
	private String regtime;
	private String name;
	public Notice() {
		super();
	}
	public Notice(String no, String title, String content, String id, String hit, String regtime, String name) {
		super();
		this.no = no;
		this.title = title;
		this.content = content;
		this.id = id;
		this.hit = hit;
		this.regtime = regtime;
		this.name = name;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHit() {
		return hit;
	}
	public void setHit(String hit) {
		this.hit = hit;
	}
	public String getRegtime() {
		return regtime;
	}
	public void setRegtime(String regtime) {
		this.regtime = regtime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Notice [no=" + no + ", title=" + title + ", content=" + content + ", id=" + id + ", hit=" + hit
				+ ", regtime=" + regtime + ", name=" + name + "]";
	}
}
