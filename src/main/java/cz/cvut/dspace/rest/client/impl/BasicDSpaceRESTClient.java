package cz.cvut.dspace.rest.client.impl;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import cz.cvut.dspace.rest.client.Configuration;

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
		ResteasyClientBuilder builder = new ResteasyClientBuilder();
		builder.disableTrustManager();
		client = builder.build();
	}
	
	@Override
	public void destroy() {
		client.close();
	}
}
