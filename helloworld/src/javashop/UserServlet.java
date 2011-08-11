package javashop;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.util.Streams;

import javashop.SettingsBean;
import javashop.UserInfoBean;
import javashop.ShopUserBean;
import javashop.PMF;

@SuppressWarnings("serial")
public class UserServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
		if (req.getPathInfo().toString().equals("/login/")) {
			getLogin(req, resp);
		}
		else if (req.getPathInfo().toString().equals("/photo/")) {
			getPhoto(req, resp);
		}
		else if(req.getPathInfo().toString().equals("/")) {
			getUserInfo(req, resp);
		}
		else {
			resp.sendRedirect("/");
		}
		return;
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
		if(req.getPathInfo().toString().equals("/")) {
			boolean isMultipart = ServletFileUpload.isMultipartContent(req);
			if (isMultipart)
				postUserInfo(req, resp);
			else
				resp.sendRedirect("/user/");
		}
		return;
	}
		
	public void getLogin(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if (user == null ){
        	resp.sendRedirect(userService.createLoginURL("/user/login/"));
        	return;
        }
        else {
        	Boolean exist = false;
    	    PersistenceManager pm = PMF.get().getPersistenceManager();
    	    Query query = pm.newQuery(ShopUserBean.class);
    	    query.setFilter("email == emailParam");
    	    query.declareParameters("String emailParam");
    	    try {
    	    	List<ShopUserBean> u = (List<ShopUserBean>) query.execute(user.getEmail());    	    	
    	    	if (u.size() > 0)
    	    		exist = true;
    	    } finally {
    	    	query.closeAll();
    	    }
        	
    	    if (!exist) {
	        	UUID uid = UUID.randomUUID();
	        	String api_key = new String(Base64.encodeBase64(uid.toString().getBytes()));        	
	            ShopUserBean shopuser = new ShopUserBean(user,
	            										user.getEmail(),
	            										user.getNickname());
	            shopuser.setApikey(api_key);
	            try {
	                pm.makePersistent(shopuser);
	            } finally {
	                pm.close();
	            }   
	            resp.sendRedirect("/user/");
    	    }
    	    else {
    	    	resp.sendRedirect("/");
    	    }
        }
        return;
	}
	
	public void getUserInfo(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if (user == null ){
        	resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
        }
    	UserInfoBean userInfo = new UserInfoBean(
    								user.getNickname(),
    								user.getEmail());
    	req.setAttribute("user_info", userInfo);
               
		SettingsBean settings = new SettingsBean();
		req.setAttribute("settings", settings);
		
	    PersistenceManager pm = PMF.get().getPersistenceManager();
	    Query query = pm.newQuery(ShopUserBean.class);
	    query.setFilter("email == emailParam");
	    query.declareParameters("String emailParam");
	    try {
	    	List<ShopUserBean> u = (List<ShopUserBean>) query.execute(user.getEmail());
		    req.setAttribute("user", u.get(0));
			String url = "/templates/user_modify.jsp";
			try { 
				req.getRequestDispatcher(url).forward(req, resp);
			} catch (ServletException e) { 
				e.printStackTrace(); 
			}
	    } finally {
	    	query.closeAll();
	    }
	    return;
	}
	
	public void postUserInfo(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if (user == null ){
        	resp.sendRedirect(userService.createLoginURL("/user/login/"));
        	return;
        }
        
        ShopUserBean shopuser = null;
	    PersistenceManager pm = PMF.get().getPersistenceManager();
	    Query query = pm.newQuery(ShopUserBean.class);
	    query.setFilter("email == emailParam");
	    query.declareParameters("String emailParam");
	    try {
	    	List<ShopUserBean> u = (List<ShopUserBean>) query.execute(user.getEmail());    	    	
	    	if (u.size() < 1)
	    		resp.sendRedirect(userService.createLoginURL("/user/login/"));
	    	else
	    		shopuser = u.get(0);
	    } finally {
	    	query.closeAll();
	    }	
	    	    
	    try {
	        ServletFileUpload upload = new ServletFileUpload();
	        FileItemIterator iterator = upload.getItemIterator(req);
	        while (iterator.hasNext()) {
	          FileItemStream item = iterator.next();
	          InputStream istream = item.openStream();
	          if (item.isFormField()) {
	        	  if (item.getFieldName().equals("realname")) {
	        		  shopuser.setRealname(Streams.asString(istream));
	        	  }
	        	  else if (item.getFieldName().equals("phone")) {
	        		  shopuser.setPhone(Streams.asString(istream));
	        	  }
	        	  else if (item.getFieldName().equals("address")) {
	        		  shopuser.setAddress(Streams.asString(istream));
	        	  }
	          } else {
	        	  int len;
	              byte[] buffer = new byte[8192];
	              ByteArrayOutputStream oStream = new ByteArrayOutputStream();
	              while ((len = istream.read(buffer, 0, buffer.length)) != -1) {
	                  oStream.write(buffer, 0, len);
	              }
	              
	              ImagesService imagesService = ImagesServiceFactory.getImagesService();
	              Image oldImage = ImagesServiceFactory.makeImage(oStream.toByteArray());
	              Transform resize = ImagesServiceFactory.makeResize(200, 300);
	              Image newImage = imagesService.applyTransform(resize, oldImage);
	              byte[] newImageData = newImage.getImageData();
	              Blob photofile = new Blob(newImageData);
	              shopuser.setPhotofile(photofile);
	          }
	        }
	    } catch (Exception ex) {
	        throw new IOException();
	    }

        try {
            pm.makePersistent(shopuser);
        } finally {
            pm.close();
        }
        
        resp.sendRedirect("/user/");
        return;
	}
	
	public void getPhoto(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if (user == null ){
        	resp.sendRedirect(userService.createLoginURL("/user/login/"));
        	return;
        }

	    PersistenceManager pm = PMF.get().getPersistenceManager();
	    Query query = pm.newQuery(ShopUserBean.class);
	    query.setFilter("email == emailParam");
	    query.declareParameters("String emailParam");
	    try {
	    	List<ShopUserBean> u = (List<ShopUserBean>) query.execute(user.getEmail()); ;
	    	if (u.size() > 0) {
	    		resp.setContentType("image/jpeg");
	    		resp.getOutputStream().write(u.get(0).getPhotofile().getBytes());
	    	}
	    } finally {
	    	query.closeAll();
            pm.close();
	    }
		return;
	}
}