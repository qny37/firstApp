package javashop;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

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
                
    public ShopUserBean(User user_info,
    					String email,
    					String nickname) {
        this.user = user_info;
        this.email = email;
        this.nickname = nickname;
    }

    public Key getKey() {
        return this.key;
    }

    public User getUser() {
    	return this.user;
    }
    
    public String getNickname() {
    	return this.nickname;
    }
    
    public String getEmail() {
    	return this.email;
    }
            
    public void setUser(User userInfo) {
    	this.user = userInfo;
    }
    
    public void setNickname(String nickname) {
    	this.nickname = nickname;
    }
    
    public void setEmail(String email) {
    	this.email = email;
    }
}