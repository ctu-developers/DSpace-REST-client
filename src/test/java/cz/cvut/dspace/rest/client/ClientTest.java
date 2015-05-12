package cz.cvut.dspace.rest.client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Properties;

import org.dspace.rest.common.Item;
import org.dspace.rest.common.MetadataEntry;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import cz.cvut.dspace.rest.client.impl.AbstractDSpaceRESTClient;
import cz.cvut.dspace.rest.client.impl.PooledDSpaceRESTClient;

public class ClientTest {

	private static Class<? extends DSpaceRESTClient> CLIENT_TYPE = PooledDSpaceRESTClient.class; // BasicDSpaceRESTClient.class
	private static DSpaceRESTClient client;
	
	@BeforeClass
	public static void init() throws FileNotFoundException, IOException {
		String propertyFilePath = URLDecoder.decode(ClientTest.class.getClassLoader().getResource("dspace-restapi.properties").getFile(),"UTF-8");
		Properties properties = new Properties();
		properties.load(new FileInputStream(propertyFilePath));
		client = new DSpaceRESTClientFactoryBuilder().build(properties).getClient(CLIENT_TYPE);
	}
		
	@Test
	public void test() {
		MetadataEntry metadataEntry = new MetadataEntry();
		metadataEntry.setKey("dc.identifier");
		metadataEntry.setValue("KOS-556502429505");
		Item item = client.findItemsByMetadataEntry(metadataEntry).get(0);
		System.out.println(item.getParentCollectionList().get(0).getName());
	}
	
	
	@AfterClass
	public static void shutdown() {
		((AbstractDSpaceRESTClient)client).destroy();
	}
	
	public static String getPath(String path) {
		try {
			return  URLDecoder.decode(ClientTest.class.getClassLoader().getResource(path).getFile(),"UTF-8");
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
