package cz.cvut.dspace.rest.client.impl;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;

import cz.cvut.dspace.rest.client.Configuration;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJacksonProvider;

import java.util.concurrent.TimeUnit;

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
		ResteasyJacksonProvider resteasyJacksonProvider = new ResteasyJacksonProvider();
		ResteasyClientBuilder builder = new ResteasyClientBuilder().register(resteasyJacksonProvider);
		connectionManager = new PoolingClientConnectionManager();
		if (configuration.getMaxTotal() != null) {
			connectionManager.setMaxTotal(configuration.getMaxTotal());
		}
		if (configuration.getMaxPerRoute() != null) {
			connectionManager.setDefaultMaxPerRoute(configuration.getMaxPerRoute());
		}
		httpClient = new DefaultHttpClient(connectionManager);
		engine = new ApacheHttpClient4Engine(httpClient);
		client =  builder.httpEngine(engine).disableTrustManager().establishConnectionTimeout(120, TimeUnit.SECONDS).build();
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
