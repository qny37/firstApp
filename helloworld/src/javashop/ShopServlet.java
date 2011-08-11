package javashop;

import java.io.IOException;
import java.util.List;
import java.util.Date;
import java.util.regex.*;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.*;
import javax.servlet.http.*;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import javashop.SettingsBean;
import javashop.ShopUserBean;
import javashop.JavaShopBean;

@SuppressWarnings("serial")
public class ShopServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
		Pattern pRead = Pattern.compile("^/\\d+/read/$");
		Matcher mRead = pRead.matcher(req.getPathInfo().toString());
		
		Pattern pModify = Pattern.compile("^/\\d+/modify/$");
		Matcher mModify = pModify.matcher(req.getPathInfo().toString());

		Pattern pDelete = Pattern.compile("^/\\d+/delete/$");
		Matcher mDelete = pDelete.matcher(req.getPathInfo().toString());
		
		if (req.getPathInfo().toString().equals("/new/")) {
			getNew(req, resp);
		}
		else if (mRead.find()) {
			getRead(req, resp);
		}
		else if (mModify.find()) {
			getModify(req, resp);
		}
		else if (mDelete.find()) {
			getDelete(req, resp);
		}				
		else {
			resp.sendRedirect("/");
		}
		return;
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
		Pattern pModify = Pattern.compile("^/\\d+/modify/$");
		Matcher mModify = pModify.matcher(req.getPathInfo().toString());
		
		if (req.getPathInfo().toString().equals("/new/")) {
			postNew(req, resp);
		}
		else if (mModify.find()) {
			postModify(req, resp);
		}
		else {
			resp.sendRedirect("/");
		}
		return;
	}
	
	public void getNew(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if (user == null ){
        	resp.sendRedirect(userService.createLoginURL("/user/login/"));
        	return;
        }
        
    	UserInfoBean userInfo = new UserInfoBean(
										user.getNickname(),
										user.getEmail());
    	req.setAttribute("user_info", userInfo);
    	
		SettingsBean settings = new SettingsBean();
		req.setAttribute("settings", settings);
     		
		String url = "/templates/shop_new.jsp";
		ServletContext sc = getServletContext(); 
		RequestDispatcher rd = sc.getRequestDispatcher(url); 
		try { 
			rd.forward(req, resp); 
		} catch (ServletException e) { 
			e.printStackTrace(); 
		}
		return;
	}

	public void postNew(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if (user == null ){
        	resp.sendRedirect(userService.createLoginURL("/user/login/"));
        	return;
        }
        
	    PersistenceManager pm = PMF.get().getPersistenceManager();
	    JavaShopBean shop = new JavaShopBean(user,
	    									 user.getNickname(),
	    									 req.getParameter("title"),
	    									 req.getParameter("content"));
	    if (req.getParameter("geo_point").length() > 0) {
	    	String regex = "[()]";
	    	String geoPoint = req.getParameter("geo_point").replaceAll(regex, "");
	    	shop.setGeopoint(geoPoint);
	    }
	    Date posttime = new Date();
	    shop.setPosttime(posttime);
	    
        try {
            pm.makePersistent(shop);
        } finally {
            pm.close();
        }
        
        resp.sendRedirect("/shop/" + shop.getKey().getId() + "/read/");
        return;
	}
	
	public void getRead(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if (user != null ){
        	UserInfoBean userInfo = new UserInfoBean(
					user.getNickname(),
					user.getEmail());
        	req.setAttribute("user_info", userInfo);
        }
               
		SettingsBean settings = new SettingsBean();
		req.setAttribute("settings", settings);

		String delem = "/";
		String[] path = req.getPathInfo().toString().split(delem);
		
		JavaShopBean s = null;
	    PersistenceManager pm = PMF.get().getPersistenceManager();
	    try {
	    	s = pm.getObjectById(JavaShopBean.class, Long.parseLong(path[1]));
	    } finally {
	    	pm.close();
	    }
	    
	    if ( s != null)
		    req.setAttribute("shop", s);
		String url = "/templates/shop_read.jsp";
		ServletContext sc = getServletContext(); 
		RequestDispatcher rd = sc.getRequestDispatcher(url); 
		try { 
			rd.forward(req, resp); 
		} catch (ServletException e) { 
			e.printStackTrace(); 
		}
		
		return;
	}
	
	public void getModify(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if (user == null ){
        	resp.sendRedirect(userService.createLoginURL("/user/login/"));
        	return;
        }
        
        UserInfoBean userInfo = new UserInfoBean(
									user.getNickname(),
									user.getEmail());
        req.setAttribute("user_info", userInfo);
               
		SettingsBean settings = new SettingsBean();
		req.setAttribute("settings", settings);

		String delem = "/";
		String[] path = req.getPathInfo().toString().split(delem);
		
		JavaShopBean s = null;
	    PersistenceManager pm = PMF.get().getPersistenceManager();
	    try {
	    	s = pm.getObjectById(JavaShopBean.class, Long.parseLong(path[1]));
	    	if (!s.getUser().getEmail().equals(user.getEmail())) {
		    	resp.sendRedirect("/");
	    	}
	    } finally {
	    	pm.close();
	    }
	    
	    if ( s != null)
		    req.setAttribute("shop", s);
		String url = "/templates/shop_modify.jsp";
		ServletContext sc = getServletContext(); 
		RequestDispatcher rd = sc.getRequestDispatcher(url); 
		try { 
			rd.forward(req, resp); 
		} catch (ServletException e) { 
			e.printStackTrace(); 
		}
		
		return;
	}
	
	public void postModify(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if (user == null ){
        	resp.sendRedirect(userService.createLoginURL("/user/login/"));
        	return;
        }
        
		String delem = "/";
		String[] path = req.getPathInfo().toString().split(delem);
		
		JavaShopBean s = null;
	    PersistenceManager pm = PMF.get().getPersistenceManager();
	    try {
	    	s = pm.getObjectById(JavaShopBean.class, Long.parseLong(path[1]));
	    	if (s.getUser().getEmail().equals(user.getEmail())) {
		    	s.setTitle(req.getParameter("title"));
		    	s.setContent(new Text(req.getParameter("content")));		
		    	if (req.getParameter("geo_point").length() > 0) {
		    		String regex = "[()]";
		    		String geoPoint = req.getParameter("geo_point").replaceAll(regex, "");
		    		s.setGeopoint(geoPoint);
		    	}

		    	pm.makePersistent(s);
		    	resp.sendRedirect("/shop/" + s.getKey().getId() + "/read/");	    		
	    	}
	    	else {
		    	resp.sendRedirect("/");
	    	}
		} finally {
			pm.close();
		}
		return;
	}
	
	public void getDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if (user == null ){
        	resp.sendRedirect(userService.createLoginURL("/user/login/"));
        	return;
        }
        
		String delem = "/";
		String[] path = req.getPathInfo().toString().split(delem);
		JavaShopBean s = null;
	    PersistenceManager pm = PMF.get().getPersistenceManager();
	    try {
	    	s = pm.getObjectById(JavaShopBean.class, Long.parseLong(path[1]));
	    	if (s.getUser().getEmail().equals(user.getEmail())) {
		    	pm.deletePersistent(s);
	    	}
	    } catch (Exception e) {
            e.printStackTrace();
		} finally {
			pm.close();
		}
		
		resp.sendRedirect("/");
	}
}
