package javashop;

public class UserInfoBean {
	private String nickname;
	private String email;
	private String logoutPath;
	
	public UserInfoBean(String nickname, String mail) {
		setEmail(mail);
		setNickname(nickname);
	}
	
	void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public String getNickname() {
		return this.nickname;
	}
	
	void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return this.email;
	}

	public void setLogoutPath(String logoutPath) {
		this.logoutPath = logoutPath;
	}

	public String getLogoutPath() {
		return logoutPath;
	}
}
