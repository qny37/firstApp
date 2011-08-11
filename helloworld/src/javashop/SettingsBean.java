package javashop;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class SettingsBean {
	private String cssPath;
	private String lang;
	private String logoutPath;
	
	public SettingsBean() {
		setCssPath(System.getProperty("css_path"));
		setLang("ko");		
		UserService userService = UserServiceFactory.getUserService();
		setLogoutPath(userService.createLogoutURL("/"));
	}

	public void setCssPath(String cssPath) {
		this.cssPath = cssPath;
	}
	
	public String getCssPath() {
		return this.cssPath;
	}
	
	public void setLang(String lang) {
		this.lang = lang;
	}
	
	public String getLang() {
		return this.lang;
	}

	public void setLogoutPath(String logoutPath) {
		this.logoutPath = logoutPath;
	}

	public String getLogoutPath() {
		return logoutPath;
	}
}
