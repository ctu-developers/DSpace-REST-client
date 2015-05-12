package cz.cvut.dspace.rest.client.impl;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;

import cz.cvut.dspace.rest.client.Configuration;

/**
* @author Petr Karel
* @version $Revision: 4 $
*/
public class PooledDSpaceRESTClient extends AbstractDSpaceRESTClient {

	private PoolingClientConnectionManager connectionManager;
	private HttpClient httpClient;
	private ApacheHttpClient4Engine engine;
	
	public PooledDSpaceRESTClient(Configuration configuration) {
		super(configuration);
	}
	
	public void create() {
		connectionManager = new PoolingClientConnectionManager();
		if (configuration.getMaxTotal() != null) {
			connectionManager.setMaxTotal(configuration.getMaxTotal());
		}
		if (configuration.getMaxPerRoute() != null) {
			connectionManager.setDefaultMaxPerRoute(configuration.getMaxPerRoute());
		}
		httpClient = new DefaultHttpClient(connectionManager);
		engine = new ApacheHttpClient4Engine(httpClient);
		client = new ResteasyClientBuilder().httpEngine(engine).disableTrustManager().build();
	}
	
	@Override
	public void destroy() {
		try {
			//client.close();
			//engine.finalize();
			connectionManager.shutdown();
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}
}
