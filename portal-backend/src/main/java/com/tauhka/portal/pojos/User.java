package com.tauhka.portal.pojos;

import java.util.UUID;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbTransient;

public class User {
	@JsonbProperty("name")
	private String nickName;
	@JsonbProperty("activeLoginId")
	private String activeLoginId;
	@JsonbProperty("email")
	private String email;

	@JsonbTransient
	private UUID userId;
	@JsonbTransient
	private String status;
	@JsonbTransient
	private boolean forcePasswordChange = true;

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public String getActiveLoginId() {
		return activeLoginId;
	}

	public void setActiveLoginId(String activeLoginId) {
		this.activeLoginId = activeLoginId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isForcePasswordChange() {
		return forcePasswordChange;
	}

	public void setForcePasswordChange(boolean forcePasswordChange) {
		this.forcePasswordChange = forcePasswordChange;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "User [name=" + nickName + ", activeLoginId=" + activeLoginId + ", userId=" + userId + ", status="
				+ status + ", forcePasswordChange=" + forcePasswordChange + "]";
	}

}
