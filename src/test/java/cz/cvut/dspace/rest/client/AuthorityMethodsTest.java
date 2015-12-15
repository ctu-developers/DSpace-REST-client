package cz.cvut.dspace.rest.client;

import cz.cvut.dspace.rest.client.impl.BasicDSpaceRESTClient;
import org.dspace.rest.common.authority.Authority;
import org.dspace.rest.common.authority.AuthorityPerson;
import org.junit.*;
import org.junit.rules.ExpectedException;

import javax.ws.rs.NotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Properties;

/**
 * This test tests authority methods against running dspace.
 *
 * @author Rostislav Novak (Computing and Information Centre, CTU in Prague)
 * @version 1.0
 * @since 2015-12-15
 */
public class AuthorityMethodsTest {

    /** Class of rest client which will be created to run tests. */
    private static final Class<? extends DSpaceRESTClient> CLIENT_TYPE = BasicDSpaceRESTClient.class;
    /** Rest client to which will run tests. */
    private static DSpaceRESTClient client;

    /** Authority person which will be used to test. */
    private static AuthorityPerson person;
    /** Authority person which will be used to test. */
    private static AuthorityPerson person2;
    /** Authority which will be used to test. */
    private static Authority authority;
    /** Authority which will be used to test. */
    private static Authority authority2;

    @Rule
    public ExpectedException thrown= ExpectedException.none();

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

        person = new AuthorityPerson();
        person.setFirstName("Jan");
        person.setLastName("Novak");

        person2 = new AuthorityPerson();
        person2.setFirstName("Jan");
        person2.setLastName("Volny");
        person2.setUid("123-456-789");

        authority = new Authority("ctu", "789");
        authority2 = new Authority("ocr", "abc-456");
    }

    /** Test crud methods over authority persons. */
    @Test
    public final void testAuthorityPersonCRUD() {
        // Test create
        final AuthorityPerson createdPerson2 = client.createAuthorityPerson(person2);
        final AuthorityPerson createdPerson = client.createAuthorityPerson(person);
        Assert.assertEquals(person.getFirstName(), createdPerson.getFirstName());
        Assert.assertEquals(person.getLastName(), createdPerson.getLastName());
        Assert.assertNotNull(createdPerson.getUid());
        Assert.assertEquals(person2.getFirstName(), createdPerson2.getFirstName());
        Assert.assertEquals(person2.getLastName(), createdPerson2.getLastName());
        Assert.assertEquals(person2.getUid(), createdPerson2.getUid());

        // Test read
        AuthorityPerson readPerson = client.readAuthorityPerson(createdPerson.getUid());
        Assert.assertEquals(createdPerson.getFirstName(), readPerson.getFirstName());
        Assert.assertEquals(createdPerson.getLastName(), readPerson.getLastName());
        Assert.assertEquals(createdPerson.getUid(), readPerson.getUid());

        // Test update
        createdPerson.setFirstName("John");
        client.updateAuthorityPerson(createdPerson.getUid(), createdPerson);
        readPerson = client.readAuthorityPerson(createdPerson.getUid());
        Assert.assertEquals(createdPerson.getFirstName(), readPerson.getFirstName());
        Assert.assertEquals(createdPerson.getLastName(), readPerson.getLastName());
        Assert.assertEquals(createdPerson.getUid(), readPerson.getUid());

        // Test read all
        final List<AuthorityPerson> personList = client.readAuthorityPersons(null,null);
        Assert.assertTrue(personList.size() >= 2);

        // Test delete
        client.deleteAuthorityPerson(createdPerson.getUid());
        client.deleteAuthorityPerson(createdPerson2.getUid());
        thrown.expect(NotFoundException.class);
        client.readAuthorityPerson(createdPerson.getUid());
    }

    /** Test crud methods over authorities in authority person. */
    @Test
    public final void testAuthorityCRUD() {
        // Test create
        final AuthorityPerson createdPerson = client.createAuthorityPerson(person);
        client.createAuthorityPersonAuthority(createdPerson.getUid(), authority);

        // Test read
        final String key = client.readAuthorityPersonsAuthorityKey(createdPerson.getUid(), authority.getName());
        Assert.assertEquals(authority.getKey(), key);
        List<Authority> authorityList = client.readAuthorityPersonAuthorities(createdPerson.getUid(), null, null);
        Assert.assertEquals(1, authorityList.size());
        Authority readAuthority = authorityList.get(0);
        Assert.assertEquals(authority.getName(), readAuthority.getName());
        Assert.assertEquals(authority.getKey(), readAuthority.getKey());

        // Test update
        final String nameOfauthority = authority.getName();
        authority.setKey("qwerty");
        authority.setName("pop");
        client.updateAuthorityPersonAuthority(createdPerson.getUid(), nameOfauthority, authority);
        authorityList = client.readAuthorityPersonAuthorities(createdPerson.getUid(), null, null);
        Assert.assertEquals(1, authorityList.size());
        readAuthority = authorityList.get(0);
        Assert.assertEquals(authority.getName(), readAuthority.getName());
        Assert.assertEquals(authority.getKey(), readAuthority.getKey());

        // Test read all
        client.createAuthorityPersonAuthority(createdPerson.getUid(), authority2);
        authorityList = client.readAuthorityPersonAuthorities(createdPerson.getUid(), null, null);
        Assert.assertTrue(authorityList.size() >= 2);

        // Test delete
        client.deleteAuthorityInAuthorityPerson(createdPerson.getUid(), authority2.getName());
        thrown.expect(NotFoundException.class);
        client.readAuthorityPersonsAuthorityKey(createdPerson.getUid(), authority2.getName());

        // Clean up
        client.deleteAuthorityPerson(createdPerson.getUid());
    }

    /** Test searching methods for authority persons. */
    @Test
    public final void testSearch() {
        // Test create
        client.createAuthorityPerson(person2);
        final AuthorityPerson createdPerson = client.createAuthorityPerson(person);
        Authority testAuthority = authority;
        testAuthority.setName("beep");
        client.createAuthorityPersonAuthority(createdPerson.getUid(), authority);

        final AuthorityPerson searchedPerson = client.searchForAuthorityPersonByAuthority(authority);
        Assert.assertEquals(createdPerson.getFirstName(), searchedPerson.getFirstName());
        Assert.assertEquals(createdPerson.getLastName(), searchedPerson.getLastName());
        Assert.assertEquals(createdPerson.getUid(), searchedPerson.getUid());

        final List<AuthorityPerson> personList = client.searchForAuthorityPersonByName("Novak,Jan", null, null);
        Assert.assertTrue(personList.size() >= 1);

        //Clean up
        client.deleteAuthorityPerson(person2.getUid());
        client.deleteAuthorityPerson(createdPerson.getUid());
    }
}
