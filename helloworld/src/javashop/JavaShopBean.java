package javashop;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class JavaShopBean {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private User user;
    
    @Persistent
    private String email;
    
    @Persistent
    private String nickname;
    
    @Persistent
    private Integer viewcount;
    
    @Persistent
    private String title;
    
    @Persistent(defaultFetchGroup="true")
    private Text content;
    
    @Persistent
    private String geopoint;
	
    @Persistent
    private Date posttime;

    public JavaShopBean(User user,
    					String nickname,
    					String title,
    					String content) {
    	setUser(user);
    	this.setNickname(nickname);
    	this.setTitle(title);
    	this.setContent(new Text(content));
    }

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getNickname() {
		return nickname;
	}

	public void setViewcount(Integer viewcount) {
		this.viewcount = viewcount;
	}

	public Integer getViewcount() {
		return viewcount;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setContent(Text content) {
		this.content = content;
	}

	public Text getContent() {
		return content;
	}

	public void setGeopoint(String geopoint) {
		this.geopoint = geopoint;
	}

	public String getGeopoint() {
		return geopoint;
	}

	public void setPosttime(Date posttime) {
		this.posttime = posttime;
	}

	public Date getPosttime() {
		return posttime;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Key getKey() {
		return key;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}
	
}
