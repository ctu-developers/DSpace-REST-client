package cz.cvut.dspace.rest.client;

import cz.cvut.dspace.rest.client.impl.PooledDSpaceRESTClient;
import cz.cvut.dspace.rest.client.impl.BasicDSpaceRESTClient;

/**
* @author Petr Karel
* @version $Revision: 4 $
*/
public class DSpaceRESTClientFactory {

	private final Configuration configuration;
	
	public DSpaceRESTClientFactory(Configuration configuration) {
		this.configuration = configuration;
	}
	
	public DSpaceRESTClient getClient(Class<? extends DSpaceRESTClient> type) {
		if (BasicDSpaceRESTClient.class.isAssignableFrom(type)) {
			BasicDSpaceRESTClient client = new BasicDSpaceRESTClient(configuration);
			client.create();
			return client;
		} else if (PooledDSpaceRESTClient.class.isAssignableFrom(type)) {
			PooledDSpaceRESTClient client = new PooledDSpaceRESTClient(configuration);
			client.create();
			return client;
		} else {
			throw new IllegalArgumentException(type + " is not supported. Use BasicDSpaceRESTClient.class or PooledDSpaceRESTClient.class.");
		}
	}

}
