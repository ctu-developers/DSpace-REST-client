package cz.cvut.dspace.rest.client;

import cz.cvut.dspace.rest.client.impl.AbstractDSpaceRESTClient;
import cz.cvut.dspace.rest.client.impl.BasicDSpaceRESTClient;
import org.dspace.rest.common.Status;
import org.junit.BeforeClass;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.Properties;
import org.junit.*;
import org.junit.rules.ExpectedException;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;

/**
 * This test tests login methods against running dspace.
 *
 * @author Rostislav Novak (Computing and Information Centre, CTU in Prague)
 * @version 1.0
 * @since 2016-04-11
 */
public class LoginMethodsTest {

    /** Class of rest client which will be created to run tests. */
    private static final Class<? extends DSpaceRESTClient> CLIENT_TYPE = BasicDSpaceRESTClient.class;
    /** Rest client to which will run tests. */
    private static DSpaceRESTClient client;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Initialize test class.
     *
     * @throws IOException Is thrown when was problem with reading properties for testing.
     */
    @BeforeClass
    public static void init() throws IOException {
        final String propertyFilePath = URLDecoder.decode(DSpaceRESTClientTest.class.getClassLoader().getResource("dspace-restapi.properties").getFile(), "UTF-8");
        final Properties properties = new Properties();
        properties.load(new FileInputStream(propertyFilePath));
        client = new DSpaceRESTClientFactoryBuilder().build(properties).getClient(CLIENT_TYPE);
    }

    @After
    public void cleanUp() throws NotFoundException {
        client.logout();
    }

    @Test
    public void testLogin() {
        String token = client.login();
        Assert.assertNotNull(token);
        Assert.assertFalse(token.isEmpty());

        Status status = client.status();
        Assert.assertTrue(status.isOkay());
        Assert.assertTrue(status.isAuthenticated());
        Assert.assertEquals(token, status.getToken());
    }

    @Test
    public void testLoginLogin() {
        String token = client.login();
        Assert.assertNotNull(token);
        Assert.assertFalse(token.isEmpty());

        String token2 = client.login();
        Assert.assertNotNull(token2);
        Assert.assertFalse(token2.isEmpty());

        Assert.assertEquals(token, token2);

        Status status = client.status();
        Assert.assertTrue(status.isOkay());
        Assert.assertTrue(status.isAuthenticated());
        Assert.assertEquals(token2, status.getToken());
    }

    @Test
    public void testLoginLogoutLogin() {
        String token = client.login();
        Assert.assertNotNull(token);
        Assert.assertFalse(token.isEmpty());

        client.logout();

        Status status = client.status();
        Assert.assertTrue(status.isOkay());
        Assert.assertFalse(status.isAuthenticated());

        String token2 = client.login();
        Assert.assertNotNull(token2);
        Assert.assertFalse(token2.isEmpty());

        Assert.assertNotSame(token, token2);

        status = client.status();
        Assert.assertTrue(status.isOkay());
        Assert.assertTrue(status.isAuthenticated());
        Assert.assertEquals(token2, status.getToken());
    }

    @Test
    public void testBadLogin() throws IOException {
        final String propertyFilePath = URLDecoder.decode(DSpaceRESTClientTest.class.getClassLoader().getResource("dspace-restapi.properties").getFile(), "UTF-8");
        final Properties properties = new Properties();
        properties.load(new FileInputStream(propertyFilePath));
        properties.setProperty("password", "nothing");
        DSpaceRESTClient client = new DSpaceRESTClientFactoryBuilder().build(properties).getClient(CLIENT_TYPE);

        thrown.expect(ForbiddenException.class);
        client.login();

        Status status = client.status();
        Assert.assertTrue(status.isOkay());
        Assert.assertFalse(status.isAuthenticated());
    }

    @Test
    public void testLoginBadTockenLogin() throws IllegalAccessException, NoSuchFieldException {
        String token = client.login();
        Assert.assertNotNull(token);
        Assert.assertFalse(token.isEmpty());

        Status status = client.status();
        Assert.assertTrue(status.isOkay());
        Assert.assertTrue(status.isAuthenticated());
        Assert.assertEquals(token, status.getToken());

        Field field = AbstractDSpaceRESTClient.class.getDeclaredField("token");
        field.setAccessible(true);
        field.set(client, "token");

        String token2 = client.login();
        Assert.assertNotNull(token2);
        Assert.assertFalse(token2.isEmpty());

        Assert.assertNotSame(token, token2);

        status = client.status();
        Assert.assertTrue(status.isOkay());
        Assert.assertTrue(status.isAuthenticated());
        Assert.assertEquals(token2, status.getToken());
    }
}
