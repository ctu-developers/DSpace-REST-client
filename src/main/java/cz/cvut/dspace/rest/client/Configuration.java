package cz.cvut.dspace.rest.client;

/**
* @author Petr Karel
* @version $Revision: 4 $
*/
public class Configuration {

	protected String endpointURL;
	protected String username;	
	protected String password;
	protected Integer maxTotal;
	protected Integer maxPerRoute;
	
	public Configuration() {
		super();
	}

	public Configuration(String endpointURL, String username, String password) {
		super();
		this.endpointURL = endpointURL;
		this.username = username;
		this.password = password;
	}

	public Configuration(String endpointURL, String username, String password,
			Integer maxTotal, Integer maxPerRoute) {
		super();
		this.endpointURL = endpointURL;
		this.username = username;
		this.password = password;
		this.maxTotal = maxTotal;
		this.maxPerRoute = maxPerRoute;
	}

	public String getEndpointURL() {
		return endpointURL;
	}

	public void setEndpointURL(String endpointURL) {
		this.endpointURL = endpointURL;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(Integer maxTotal) {
		this.maxTotal = maxTotal;
	}

	public Integer getMaxPerRoute() {
		return maxPerRoute;
	}

	public void setMaxPerRoute(Integer maxPerRoute) {
		this.maxPerRoute = maxPerRoute;
	}

}
