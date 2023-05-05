package com.ssafy.house.model.dto;

public class User {
	String id;
	String password;
	String name;
	String email;
	String token;
	int platform;
	int isAdmin;
	
	public User() {}
	
	public User(String id, String password, String name, String email, String token, int platform, int isAdmin) {
		this.id = id;
		this.password = password;
		this.name = name;
		this.email = email;
		this.token = token;
		this.platform = platform;
		this.isAdmin = isAdmin;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getPlatform() {
		return platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}
	
	public int getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", password=" + password + ", name=" + name + ", email=" + email + ", token=" + token
				+ ", platform=" + platform + ", isAdmin=" + isAdmin + "]";
	}

}
