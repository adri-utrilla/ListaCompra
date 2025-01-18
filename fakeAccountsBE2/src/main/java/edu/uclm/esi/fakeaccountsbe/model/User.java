package edu.uclm.esi.fakeaccountsbe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "Usuario")
public class User {
	
	@Id
	private String email;
	private String pwd;

	@JsonIgnore
	@Column(length = 36)
	private String token;

	@JsonIgnore
	@Transient
	private long creationTime;

	@Transient
	private String ip;

	@Column(length = 36)
	private String cookie;

	@Column
	private boolean premium;

	@Column(length = 36)
	private String recoveryToken;

	@Column
	private String caducidadRecoveryToken;

	public String getCaducidadRecoveryToken() {
		return caducidadRecoveryToken;
	}

	public void setCaducidadRecoveryToken(String caducidadRecoveryToken) {
		this.caducidadRecoveryToken = caducidadRecoveryToken;
	}

	public String getRecoveryToken() {
		return recoveryToken;
	}

	public void setRecoveryToken(String recoveryToken) {
		this.recoveryToken = recoveryToken;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}

	public void setCookie(String fakeUserId) {
		this.cookie = fakeUserId;
	}

	public String getCookie() {
		return cookie;
	}

	public boolean isPremium() {
		return premium;
	}

	public void setPremium(boolean premium) {
		this.premium = premium;
	}
}
