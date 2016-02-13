package cz.cvut.dspace.rest.client;

import cz.cvut.dspace.rest.client.impl.BasicDSpaceRESTClient;
import org.dspace.rest.common.authority.Authority;
import org.dspace.rest.common.authority.AuthorityPerson;
import org.junit.*;
import org.junit.rules.ExpectedException;

import javax.ws.rs.InternalServerErrorException;
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

    @After
    public void cleanUp() throws NotFoundException {
        try {
            client.deleteAuthorityPerson(person.getUid());
        } catch (NotFoundException e) {
        }
        person.setUid(null);
        try {
            client.deleteAuthorityPerson(person2.getUid());
        } catch (NotFoundException e) {
        }
    }

    @Test
    public final void testCreateAuthorityPersonWithoutUID() {
        final AuthorityPerson createdPerson = client.createAuthorityPerson(person);
        Assert.assertEquals(person.getFirstName(), createdPerson.getFirstName());
        Assert.assertEquals(person.getLastName(), createdPerson.getLastName());
        Assert.assertNotNull(createdPerson.getUid());
        person.setUid(createdPerson.getUid()); // For clean up
    }

    @Test
    public final void testCreateAuthorityPersonWithUID() {
        final AuthorityPerson createdPerson2 = client.createAuthorityPerson(person2);
        Assert.assertEquals(person2.getFirstName(), createdPerson2.getFirstName());
        Assert.assertEquals(person2.getLastName(), createdPerson2.getLastName());
        Assert.assertEquals(person2.getUid(), createdPerson2.getUid());
    }

    @Test
    public final void testCreateAuthorityPersonWithSameUID() {
        client.createAuthorityPerson(person2);

        // Test not allowed creation of person with same uid.
        thrown.expect(InternalServerErrorException.class);
        client.createAuthorityPerson(person2);
    }

    @Test
    public final void testReadAuthorityPerson() {
        final AuthorityPerson createdPerson = client.createAuthorityPerson(person2);

        // Test read
        final AuthorityPerson readPerson = client.readAuthorityPerson(createdPerson.getUid());
        Assert.assertEquals(createdPerson.getFirstName(), readPerson.getFirstName());
        Assert.assertEquals(createdPerson.getLastName(), readPerson.getLastName());
        Assert.assertEquals(createdPerson.getUid(), readPerson.getUid());
    }

    @Test
    public final void testReadNotExistingAuthorityPerson() {
        thrown.expect(NotFoundException.class);
        final AuthorityPerson readPerson = client.readAuthorityPerson("nothing-uid");
        Assert.assertNull(readPerson);
    }

    @Test
    public final void testReadAllAuthorityPersons() {
        final AuthorityPerson createdPerson = client.createAuthorityPerson(person2);

        // Test read all
        List<AuthorityPerson> personList = client.readAuthorityPersons(null, null);
        Assert.assertTrue(personList.size() >= 1);
        int lastSize = personList.size();

        boolean added = false;
        for (AuthorityPerson forPerson : personList) {
            if (forPerson.getUid().equals(createdPerson.getUid())) {
                Assert.assertEquals(createdPerson.getFirstName(), forPerson.getFirstName());
                Assert.assertEquals(createdPerson.getLastName(), forPerson.getLastName());
                Assert.assertEquals(createdPerson.getUid(), forPerson.getUid());
                added = true;
            }
        }
        Assert.assertTrue(added);

        person = client.createAuthorityPerson(person);
        personList = client.readAuthorityPersons(null, null);
        Assert.assertTrue(personList.size() > lastSize);
    }

    @Test
    public final void testUpdateAuthorityPerson() {
        final AuthorityPerson createdPerson = client.createAuthorityPerson(person2);

        // Test update
        createdPerson.setFirstName("John");
        client.updateAuthorityPerson(createdPerson.getUid(), createdPerson);
        final AuthorityPerson readPerson = client.readAuthorityPerson(createdPerson.getUid());
        Assert.assertEquals(createdPerson.getFirstName(), readPerson.getFirstName());
        Assert.assertEquals(createdPerson.getLastName(), readPerson.getLastName());
        Assert.assertEquals(createdPerson.getUid(), readPerson.getUid());
    }

    @Test
    public final void testUpdateNotExistingAuthorityPerson() {
        thrown.expect(NotFoundException.class);
        client.updateAuthorityPerson("nothing-uid", person);
    }

    @Test
    public final void testDeleteAuthorityPerson() {
        final AuthorityPerson createdPerson = client.createAuthorityPerson(person2);

        // Test delete
        client.deleteAuthorityPerson(createdPerson.getUid());
        thrown.expect(NotFoundException.class);
        client.readAuthorityPerson(createdPerson.getUid());
    }

    @Test
    public final void testDeleteNotExistingAuthorityPerson() {
        thrown.expect(NotFoundException.class);
        client.deleteAuthorityPerson("Nothing");
    }

    /** Test searching methods for authority persons. */
    @Test
    public final void testSearchAuthorityPersonByAuthority() {
        final AuthorityPerson createdPerson = client.createAuthorityPerson(person2);
        client.createAuthorityPersonAuthority(createdPerson.getUid(), authority);

        // Positive test
        final AuthorityPerson searchedPerson = client.searchForAuthorityPersonByAuthority(authority);
        Assert.assertEquals(createdPerson.getFirstName(), searchedPerson.getFirstName());
        Assert.assertEquals(createdPerson.getLastName(), searchedPerson.getLastName());
        Assert.assertEquals(createdPerson.getUid(), searchedPerson.getUid());
    }

    @Test
    public final void testSearchAuthorityPersonByNotExistingAuthority() {
        thrown.expect(NotFoundException.class);
        final AuthorityPerson searchedPerson = client.searchForAuthorityPersonByAuthority(authority2);
        Assert.assertNull(searchedPerson);
    }

    @Test
    public final void testSearchAuthorityPersonByName() {
        client.createAuthorityPerson(person2);
        final List<AuthorityPerson> personList = client.searchForAuthorityPersonByName("Volny,Jan", null, null);
        Assert.assertTrue(personList.size() >= 1);
    }

    @Test
    public final void testSearchAuthorityPersonByNameNot() {
        final List<AuthorityPerson> personList = client.searchForAuthorityPersonByName("Nj,Jan", null, null);
        Assert.assertTrue(personList.size() == 0);
    }

    @Test
    public final void testCreateAuthority() {
        final AuthorityPerson createdPerson = client.createAuthorityPerson(person2);
        client.createAuthorityPersonAuthority(createdPerson.getUid(), authority);
    }

    @Test
    public final void testCreateAuthorityNotExistingUID() {
        thrown.expect(NotFoundException.class);
        client.createAuthorityPersonAuthority("nothing", authority2);
    }

    @Test
    public final void testCreateAuthorityTwice() {
        final AuthorityPerson createdPerson = client.createAuthorityPerson(person2);
        client.createAuthorityPersonAuthority(createdPerson.getUid(), authority);

        thrown.expect(InternalServerErrorException.class);
        client.createAuthorityPersonAuthority(createdPerson.getUid(), authority);
    }

    @Test
    public final void testReadAuthority() {
        final AuthorityPerson createdPerson = client.createAuthorityPerson(person2);
        client.createAuthorityPersonAuthority(createdPerson.getUid(), authority);

        final String key = client.readAuthorityPersonsAuthorityKey(createdPerson.getUid(), authority.getName());
        Assert.assertEquals(authority.getKey(), key);
    }

    @Test
    public final void testReadAuthorityNotExistingUID() {
        final AuthorityPerson createdPerson = client.createAuthorityPerson(person2);

        thrown.expect(NotFoundException.class);
        final String key = client.readAuthorityPersonsAuthorityKey(createdPerson.getUid(), "nothing");
        Assert.assertNull(key);
    }


    @Test
    public final void testReadAllAuthorities() {
        final AuthorityPerson createdPerson = client.createAuthorityPerson(person2);
        client.createAuthorityPersonAuthority(createdPerson.getUid(), authority);

        List<Authority> authorityList = client.readAuthorityPersonAuthorities(createdPerson.getUid(), null, null);
        Assert.assertTrue(authorityList.size() >= 1);

        boolean added = false;
        for(Authority forAuthority : authorityList) {
            if(forAuthority.getName().equals(authority.getName()) &&
                    forAuthority.getKey().equals(authority.getKey())) {
                added = true;
            }
        }
        Assert.assertTrue(added);
    }

    @Test
    public final void testReadAllAuthoritiesNotExistingUID() {
        thrown.expect(NotFoundException.class);
        client.readAuthorityPersonAuthorities("nothing", null, null);
    }

    @Test
    public final void testUpdateAuthority() {
        final AuthorityPerson createdPerson = client.createAuthorityPerson(person2);
        client.createAuthorityPersonAuthority(createdPerson.getUid(), authority);

        final String nameOfAuthority = authority.getName();
        authority.setKey("qwerty");
        authority.setName("pop");
        client.updateAuthorityPersonAuthority(createdPerson.getUid(), nameOfAuthority, authority);

        final String key = client.readAuthorityPersonsAuthorityKey(createdPerson.getUid(), "pop");
        Assert.assertEquals(authority.getKey(), key);


    }

    @Test
    public final void testUpdateAuthorityNotExistingName() {
        final AuthorityPerson createdPerson = client.createAuthorityPerson(person2);
        client.createAuthorityPersonAuthority(createdPerson.getUid(), authority);

        thrown.expect(NotFoundException.class);
        client.updateAuthorityPersonAuthority(createdPerson.getUid(), "nothing", authority);
    }

    @Test
    public final void testUpdateAuthorityNotExistingUID() {
        final AuthorityPerson createdPerson = client.createAuthorityPerson(person2);
        client.createAuthorityPersonAuthority(createdPerson.getUid(), authority);

        thrown.expect(NotFoundException.class);
        client.updateAuthorityPersonAuthority("nothing", authority.getName(), authority);
    }

    @Test
    public final void testDeleteAuthority() {
        final AuthorityPerson createdPerson = client.createAuthorityPerson(person2);
        client.createAuthorityPersonAuthority(createdPerson.getUid(), authority);

        // Test delete
        client.deleteAuthorityInAuthorityPerson(createdPerson.getUid(), authority.getName());
        thrown.expect(NotFoundException.class);
        client.readAuthorityPersonsAuthorityKey(createdPerson.getUid(), authority.getName());
    }

    @Test
    public final void testDeleteAuthorityNotExisting() {
        final AuthorityPerson createdPerson = client.createAuthorityPerson(person2);
        client.createAuthorityPersonAuthority(createdPerson.getUid(), authority);

        thrown.expect(NotFoundException.class);
        client.deleteAuthorityInAuthorityPerson(createdPerson.getUid(), "nothing");
    }

    @Test
    public final void testDeleteAuthorityNotExistingUID() {
        thrown.expect(NotFoundException.class);
        client.deleteAuthorityInAuthorityPerson("nothing", authority.getName());
    }
}
