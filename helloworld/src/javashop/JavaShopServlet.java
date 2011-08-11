package javashop;

import java.io.IOException;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.servlet.*;
import javax.servlet.http.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import javashop.SettingsBean;
import javashop.JavaShopBean;

@SuppressWarnings("serial")
public class JavaShopServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
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

		PersistenceManager pm = PMF.get().getPersistenceManager();
	    String query = "SELECT FROM " + JavaShopBean.class.getName();
	    try {
	    	List<JavaShopBean> s = (List<JavaShopBean>) pm.newQuery(query).execute();	    	
	    	if (s.size() > 0)
	    		req.setAttribute("shops", s);
			String url = "/templates/index.jsp";
			try { 
				req.getRequestDispatcher(url).forward(req, resp);
			} catch (ServletException e) { 
				e.printStackTrace(); 
			}
	    } finally {
	    	pm.close();
	    }
		return;
	}	
}
