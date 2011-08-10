package test;

import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import javashop.PMF;
import javashop.ShopUserBean;

public class ShopUserTestCase extends BaseTestCase {
    public void testDBPut() throws Exception {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
    	ShopUserBean shopUser = new ShopUserBean(user,
    											 user.getEmail(),
    											 user.getNickname());
    	PersistenceManager pm = PMF.get().getPersistenceManager();
    	try {
    		ShopUserBean result = pm.makePersistent(shopUser);
    		assertEquals(result.getEmail(), "test@example.com");
    	} finally {
    		pm.close();
    	}
		return;
    }
    
    public void testDBGet() throws Exception {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
    	ShopUserBean shopUser = new ShopUserBean(user,
    											 user.getEmail(),
    											 user.getNickname());
    	PersistenceManager pm = PMF.get().getPersistenceManager();
    	pm.makePersistent(shopUser);

    	/*
    	String jdoql = "SELECT FROM "
    		         + ShopUserBean.class.getName()
    		         + " WHERE email == 'test@example.com'";

    	List<ShopUserBean> jdoql_u = (List<ShopUserBean>) pm.newQuery(jdoql).execute();
    	assertEquals(1, jdoql_u.size());
    	*/
    	
        Query query = pm.newQuery(ShopUserBean.class);
    	query.setFilter("email == emailParam");
    	query.declareParameters("String emailParam");
    	try {
    		List<ShopUserBean> results = (List<ShopUserBean>) query.execute("test@example.com");
    		assertEquals(1, results.size());
    		assertEquals(results.get(0).getEmail(), "test@example.com");
    	} finally {
    		query.closeAll();
    		pm.close();
    	}

    	return;
    }
    
    public void testDBUpdate() throws Exception {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
    	ShopUserBean shopUser = new ShopUserBean(user,
    											 user.getEmail(),
    											 user.getNickname());
    	PersistenceManager pm1 = PMF.get().getPersistenceManager();
    	pm1.makePersistent(shopUser);
        Query q1 = pm1.newQuery(ShopUserBean.class);
    	q1.setFilter("email == emailParam");
    	q1.declareParameters("String emailParam");
    	try {
    		List<ShopUserBean> results = (List<ShopUserBean>) q1.execute("test@example.com");
    		assertEquals(results.get(0).getEmail(), "test@example.com");
    		results.get(0).setEmail("test2@example.com");
    	} finally {
    		q1.closeAll();
    		pm1.close();
    	}

    	PersistenceManager pm2 = PMF.get().getPersistenceManager();    	
//    	pm2.makePersistent(shopUser);
        Query q2 = pm2.newQuery(ShopUserBean.class);
    	q2.setFilter("email == emailParam");
    	q2.declareParameters("String emailParam");
    	try {
    		List<ShopUserBean> results = (List<ShopUserBean>) q2.execute("test2@example.com");
    		assertEquals(results.get(0).getEmail(), "test2@example.com");
    	} finally {
    		q2.closeAll();
    		pm2.close();
    	}
    	
    	return;
    }
    
    public void testDBDelte() throws Exception {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
    	ShopUserBean shopUser = new ShopUserBean(user,
    											 user.getEmail(),
    											 user.getNickname());
    	PersistenceManager pm1 = PMF.get().getPersistenceManager();
//    	pm1.makePersistent(shopUser);    	
    	try {
    		ShopUserBean result = pm1.makePersistent(shopUser);
    		assertEquals(result.getEmail(), "test@example.com");
    	} finally {
    		pm1.close();
    	}
    	
    	PersistenceManager pm2 = PMF.get().getPersistenceManager();
//    	pm2.makePersistent(shopUser);
        Query q2 = pm2.newQuery(ShopUserBean.class);
    	q2.setFilter("email == emailParam");
    	q2.declareParameters("String emailParam");
    	try {
    		List<ShopUserBean> results = (List<ShopUserBean>) q2.execute("test@example.com");
    		assertEquals(results.size(), 1);
    		pm2.deletePersistent(results.get(0));
    	} finally {
    		q2.closeAll();
    		pm2.close();
    	}

    	PersistenceManager pm3 = PMF.get().getPersistenceManager();
//    	pm3.makePersistent(shopUser);    	
        Query q3 = pm3.newQuery(ShopUserBean.class);
    	q3.setFilter("email == emailParam");
    	q3.declareParameters("String emailParam");
    	try {
    		List<ShopUserBean> results = (List<ShopUserBean>) q3.execute("test@example.com");
    		assertEquals(0, results.size());
    	} finally {
    		q3.closeAll();
    		pm3.close();
    	}
    	
    	return;
    }
}
