package cz.cvut.dspace.rest.client;

import java.util.Properties;

/**
* @author Petr Karel
* @version $Revision: 4 $
*/
public class DSpaceRESTClientFactoryBuilder {

	public DSpaceRESTClientFactory build(String endpointURL, String username, String password) {		
		Configuration configuration = new Configuration(endpointURL, username, password, null, null);
		return build(configuration);
	}
	
	public DSpaceRESTClientFactory build(String endpointURL, String username, String password,
			Integer maxTotal, Integer maxPerRoute) {		
		Configuration configuration = new Configuration(endpointURL, username, password, maxTotal, maxPerRoute);
		return build(configuration);
	}

	public DSpaceRESTClientFactory build(Properties properties) {		
		Configuration configuration = new Configuration(properties.getProperty("endpointURL"),
				properties.getProperty("username"),
				properties.getProperty("password"),
				properties.getProperty("maxTotal") != null ? Integer.valueOf(properties.getProperty("maxTotal")) : null,
				properties.getProperty("maxPerRoute") != null ? Integer.valueOf(properties.getProperty("maxPerRoute")) : null);
		return build(configuration);
	}
	
	public DSpaceRESTClientFactory build(Configuration configuration) {				
		return new DSpaceRESTClientFactory(configuration);
	}
	
}
