package test;

import java.io.File;
   
import com.google.apphosting.api.ApiProxy;
import com.google.appengine.api.datastore.dev.LocalDatastoreService;
import com.google.appengine.tools.development.ApiProxyLocal;
import com.google.appengine.tools.development.ApiProxyLocalFactory;
import com.google.appengine.tools.development.LocalServerEnvironment;

import junit.framework.TestCase;

public class BaseTestCase extends TestCase {
    @Override
    public void setUp() throws Exception {
        super.setUp();
        ApiProxy.setEnvironmentForCurrentThread(new TestEnvironment());        
        ApiProxyLocalFactory apiProxyLocalFactory = new ApiProxyLocalFactory();        
        ApiProxyLocal proxy = apiProxyLocalFactory.create(new LocalServerEnvironment() {
		            public void waitForServerToStart()
		                    throws InterruptedException {
		    }
		    public int getPort() {
		            return 0;
		    }
		    public File getAppDir() {
		            return new File(".");
		    }
		    public String getAddress() {
		            return null;
		    }
			@Override
			public boolean enforceApiDeadlines() {				
				return false;
			}
			@Override
			public boolean simulateProductionLatencies() {			
				return false;
			}
		});
		proxy.setProperty(
			//LocalDatastoreService.BACKING_STORE_PROPERTY,
			LocalDatastoreService.NO_STORAGE_PROPERTY,
			Boolean.TRUE.toString());
        ApiProxy.setDelegate(proxy);        
        proxy.setProperty(LocalDatastoreService.NO_STORAGE_PROPERTY, Boolean.TRUE.toString()); 
    }

    @Override
    public void tearDown() throws Exception {
    	ApiProxyLocal proxy = (ApiProxyLocal) ApiProxy.getDelegate();
        LocalDatastoreService datastoreService = (LocalDatastoreService)proxy.getService("datastore_v3");
        datastoreService.clearProfiles();
        ApiProxy.setDelegate(null);
        ApiProxy.setEnvironmentForCurrentThread(null); 
        super.tearDown();
    }
}
