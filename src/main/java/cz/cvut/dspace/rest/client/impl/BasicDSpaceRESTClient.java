package cz.cvut.dspace.rest.client.impl;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import cz.cvut.dspace.rest.client.Configuration;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJacksonProvider;

import java.util.concurrent.TimeUnit;

/**
* @author Petr Karel
* @version $Revision: 4 $
*/
public class BasicDSpaceRESTClient extends AbstractDSpaceRESTClient {

	public BasicDSpaceRESTClient(Configuration configuration) {
		super(configuration);
	}

	@Override
	public void create() {
		ResteasyJacksonProvider resteasyJacksonProvider = new ResteasyJacksonProvider();
		ResteasyClientBuilder builder = new ResteasyClientBuilder().register(resteasyJacksonProvider);
		builder.disableTrustManager().establishConnectionTimeout(120, TimeUnit.SECONDS);
		client = builder.build();
	}
	
	@Override
	public void destroy() {
		client.close();
	}
}
