package javashop;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;
import com.google.appengine.api.datastore.Blob;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ShopUserBean {
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
    private String apikey;
    
    @Persistent
    private Integer apicount;
    
    @Persistent
    private String realname;
    
    @Persistent
    private String phone;
    
    @Persistent
    private String address;

    @Persistent
    private Blob photofile;

    public ShopUserBean(User user, String nickname, String email) {
    	setUser(user);
    	setNickname(nickname);
    	setEmail(email);
    }
    
	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Key getKey() {
		return key;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getNickname() {
		return nickname;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public String getApikey() {
		return apikey;
	}

	public void setApicount(Integer apicount) {
		this.apicount = apicount;
	}

	public Integer getApicount() {
		return apicount;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getRealname() {
		return realname;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return phone;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public void setPhotofile(Blob photofile) {
		this.photofile = photofile;
	}

	public Blob getPhotofile() {
		return photofile;
	}
}