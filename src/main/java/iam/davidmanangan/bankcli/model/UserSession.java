package iam.davidmanangan.bankcli.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="USER_SESSION")
public class UserSession {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="SESSION_ID")
	private String sessionId;
	
	@Column(name="USERNAME")
	private String userName;
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="LOGIN_DATE")
	private Date loginDate;

	public UserSession() {
	}

	public UserSession(String userName, Date loginDate) {
		this.userName = userName;
		this.loginDate = loginDate;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}
}
