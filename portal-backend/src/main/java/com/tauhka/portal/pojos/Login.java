package com.tauhka.portal.pojos;

import jakarta.json.bind.annotation.JsonbProperty;

//Login data input and output. Email not used atm.
public class Login {
	@JsonbProperty("userName")
	private String userName;
	@JsonbProperty("password")
	private String password;
	@JsonbProperty("email")
	private String email;

	@JsonbProperty("token")
	private String token;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Login() {
		super();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Login(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
