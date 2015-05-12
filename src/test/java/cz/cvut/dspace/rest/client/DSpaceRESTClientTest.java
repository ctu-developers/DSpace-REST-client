package cz.cvut.dspace.rest.client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;

import org.dspace.rest.common.*;
import org.dspace.rest.common.ResourcePolicy.Action;
import org.joda.time.LocalDate;
import org.junit.*;

import cz.cvut.dspace.rest.client.impl.AbstractDSpaceRESTClient;
import cz.cvut.dspace.rest.client.impl.PooledDSpaceRESTClient;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class DSpaceRESTClientTest {

	private static Class<? extends DSpaceRESTClient> CLIENT_TYPE = PooledDSpaceRESTClient.class; // BasicDSpaceRESTClient.class
	private static DSpaceRESTClient client;
	private Item mockItem;
	private List<MetadataEntry> mockMetadata;
	private String collectionName = "Bakalářské práce - 11101"; // Fill with existing collection.
    private Community testCommunity;
    private Collection testCollection;

    @Rule
    public ExpectedException thrown= ExpectedException.none();
	
	@BeforeClass
	public static void init() throws FileNotFoundException, IOException {
		String propertyFilePath = URLDecoder.decode(DSpaceRESTClientTest.class.getClassLoader().getResource("dspace-restapi.properties").getFile(),"UTF-8");
		Properties properties = new Properties();
		properties.load(new FileInputStream(propertyFilePath));
		client = new DSpaceRESTClientFactoryBuilder().build(properties).getClient(CLIENT_TYPE);
	}
	
	@Before
	public void setup() {					
		mockMetadata = new ArrayList<MetadataEntry>();
        mockMetadata.add(new MetadataEntry("dc.contributor.advisor","Vedouci REST api",null));
        mockMetadata.add(new MetadataEntry("dc.contributor.author","REST api",null));
        mockMetadata.add(new MetadataEntry("dc.date.issued","2014-03-13",null));        
        mockMetadata.add(new MetadataEntry("dc.publisher","České vysoké učení technické v Praze. Vypočetní a informační centrum.","cze"));
        mockMetadata.add(new MetadataEntry("dc.rights","Vysokoškolská závěrečná práce je dílo chráněné autorským zákonem. Je možné pořizovat z něj na své náklady a pro svoji osobní potřebu výpisy, opisy a rozmnoženiny. Jeho využití musí být v souladu s autorským zákonem http://www.mkcr.cz/assets/autorske-pravo/01-3982006.pdf  a citační etikou http://knihovny.cvut.cz/vychova/vskp.html.","cze"));
        mockMetadata.add(new MetadataEntry("dc.rights","A university thesis is a work protected by the Copyright Act. Extracts, copies and transcripts of the thesis are allowed for personal use only and at one’s own expense. The use of thesis should be in compliance with the Copyright Act http://www.mkcr.cz/assets/autorske-pravo/01-3982006.pdf and the citation ethics http://knihovny.cvut.cz/vychova/vskp.html.","en"));
        mockMetadata.add(new MetadataEntry("dc.title","REST práce NG","cze"));
        mockMetadata.add(new MetadataEntry("dc.title.alternative","Toto je prace pres REST","cze"));
        mockMetadata.add(new MetadataEntry("dc.title","REST work NG","en"));
        mockMetadata.add(new MetadataEntry("dc.title.alternative","This is work over REST","en"));
        mockMetadata.add(new MetadataEntry("dc.type","Bakalářská práce","cze"));
        mockMetadata.add(new MetadataEntry("dc.date.accepted","2014-03-15",null));
        mockMetadata.add(new MetadataEntry("dc.contributor.referee","Oponent REST api",null));
        mockMetadata.add(new MetadataEntry("dc.language.iso","cze",null));
        mockMetadata.add(new MetadataEntry("dc.subject","hůlka, stonožka, matika, košík, náhoda, upír, zeď, polyp, boty, přechod","cze"));
        mockMetadata.add(new MetadataEntry("dc.subject","Wand, centipede, math, cart, chance, vampire, wall, polyp, shoes, transition","en"));
        mockMetadata.add(new MetadataEntry("dc.description.abstract","Google+ positive! Rychle jsem skočil ke dveřím, zamkl a zatáhl závěsy. Všichni pokradmu pokukovali k prázdnému místu u stolu, kde sedával. Generátor náhodných čísel Generátor náhodných čísel je zařízení nebo procedura, která generuje čísla která opravdu jsou nebo jen připomínají čísla náhodná. Až mě napadá, nebyl to záměr? Díky jeho odvaze ho přezdívali „bořitel měst“. Mirda nás nepustil k CB a to ani v neděli ráno vyhřátá místnost probudila i zazimovaného motýla Anténní systémy postaveny Jindrovo auto sloužilo jako kotvící kolík, no že je to konečně užitečné použití hihi a že to z Dvorské lítá hezky svědčí tento obrazek Ranní vysílání bylo pohodové, vyzobali jsme všechny opozdilce a kolem 11 začali uklízet.","cze"));
        mockMetadata.add(new MetadataEntry("dc.description.abstract","Google+ positive! I quickly jumped to the door, locked the door and drew the curtains. All sly glances to an empty seat at the table where he sat. Random Number Generator Random Number Generator is a device or procedure that generates numbers that really are or just remind random numbers. When I wonder, was it intentional? Thanks to his courage nicknamed him \"destroyer of cities\". Mirda kept us off the CB and even on Sunday morning warmed the room and woke zazimovaného butterfly antenna systems built JINDROVÁ auto served as an anchor pin, well it's finally useful hihi and use it from Dvorská flies nicely demonstrated this file Morning broadcast was relaxed, vyzobali we all stragglers and began to clean around 11.","en"));
        mockMetadata.add(new MetadataEntry("theses.degree.discipline","Informatika","cze"));
        mockMetadata.add(new MetadataEntry("theses.degree.grantor","Fakulta informačních technologií","cze"));
        mockMetadata.add(new MetadataEntry("theses.degree.name","Bc.","cze"));
        mockMetadata.add(new MetadataEntry("theses.degree.programme","Informační systémy a management","cze"));
        
        Collection parentCollection = client.findCollectionByName(collectionName);
		assertNotNull(parentCollection.getID());
		mockItem = client.createItem(parentCollection.getID(), mockMetadata);

        // Setup testCommunity
        testCommunity = new Community();
        testCommunity.setName("Top community");
        testCommunity.setCopyrightText("Copyright text");
        testCommunity.setIntroductoryText("Introductory text");
        testCommunity.setShortDescription("Short description");
        testCommunity.setSidebarText("Sidebar text");
        testCommunity.setID(0);

        // Setup testCollection
        testCollection = new Collection();
        testCollection.setName("Top community");
        testCollection.setCopyrightText("Copyright text");
        testCollection.setIntroductoryText("Introductory text");
        testCollection.setShortDescription("Short description");
        testCollection.setSidebarText("Sidebar text");
        testCollection.setID(0);
	}

    private static boolean compareCommunities(Community a, Community b, boolean testId) {
        if(!a.getCopyrightText().equals(b.getCopyrightText())
                || !a.getIntroductoryText().equals(b.getIntroductoryText())
                || !a.getShortDescription().equals(b.getShortDescription())
                || !a.getSidebarText().equals(b.getSidebarText())
                || !a.getName().equals(b.getName())) {
            return false;
        }

        if(testId) {
            if(!a.getID().equals(b.getID())) {
                return false;
            }
        }
        return true;
    }

    private static boolean compareCollections(Collection a, Collection b, boolean testId) {
        if(!a.getCopyrightText().equals(b.getCopyrightText())
                || !a.getIntroductoryText().equals(b.getIntroductoryText())
                || !a.getShortDescription().equals(b.getShortDescription())
                || !a.getSidebarText().equals(b.getSidebarText())
                || !a.getName().equals(b.getName())) {
            return false;
        }

        if(testId) {
            if(!a.getID().equals(b.getID())) {
                return false;
            }
        }

        return true;
    }


    @Test
	public void testFindItemsById() {
		Item item = client.findItemById(mockItem.getID(), true, true);
		assertNotNull(item);
		Assert.assertNotNull(item.getID());
	}
	
	@Test
	public void testFindItemsByMetadataEntry() {
		List<Item> list = client.findItemsByMetadataEntry(new MetadataEntry("dc.contributor.author", "REST api", null));
		Assert.assertTrue(!list.isEmpty());
	}

    @Test
    public void testReadAllItems() {
        List<Item> list = client.readAllItems("",100,0);
        assertFalse(list.isEmpty());
    }

	@Test
	public void testAddAndDeleteBitstream() throws ProcessingException, IOException {		
		Bitstream bitstream1 = new Bitstream();
		bitstream1.setName("pdf1.pdf");
		bitstream1.setDescription("Příliš žluťoučký kůň úpěl ďábelské ódy");		
		ResourcePolicy policy = new ResourcePolicy();
		policy.setGroupId(0);
		policy.setStartDate(new LocalDate().plusWeeks(1).toDate());		
		bitstream1.setPolicies(new ResourcePolicy[] {policy});
		
		bitstream1 = client.addBitstream(mockItem.getID(), bitstream1, new FileInputStream(getPath("pdf1.pdf")));		
		
		Bitstream bitstream2 = client.findBitstreamById(bitstream1.getID());		
		Assert.assertEquals(bitstream1.getID(), bitstream2.getID());
		Assert.assertEquals("pdf1.pdf", bitstream2.getName());
		Assert.assertEquals("Příliš žluťoučký kůň úpěl ďábelské ódy", bitstream2.getDescription());
		
		client.deleteBitstream(mockItem.getID(), bitstream1.getID());		
		Assert.assertNull(client.findBitstreamById(bitstream1.getID()));
	}
	
	@Test
	public void testModifyMetada() throws ProcessingException {
		mockMetadata = new ArrayList<MetadataEntry>();
        mockMetadata.add(new MetadataEntry("dc.title","Nový název","cze"));
        mockMetadata.add(new MetadataEntry("dc.title.alternative","New alternative title","cze"));
        mockMetadata.add(new MetadataEntry("dc.title","New title","en"));
        client.updateItem(mockItem.getID(), mockMetadata);
        
        Item item = client.findItemById(mockItem.getID(), true, true);
        
        for (MetadataEntry metadataEntry : item.getMetadata()) {
        	if ("dc.title".equals(metadataEntry.getKey()) && "cze".equals(metadataEntry.getLanguage())) {
        		Assert.assertEquals("Nový název", metadataEntry.getValue());
        	} else if ("dc.title".equals(metadataEntry.getKey()) && "en".equals(metadataEntry.getLanguage())) {
        		Assert.assertEquals("New title", metadataEntry.getValue());
        	} else if ("dc.title.alternative".equals(metadataEntry.getKey()) && "cze".equals(metadataEntry.getLanguage())) {
        		Assert.assertEquals("New alternative title", metadataEntry.getValue());
        	} else if ("dc.type".equals(metadataEntry.getKey()) && "cze".equals(metadataEntry.getLanguage())) {
        		Assert.assertEquals("Bakalářská práce", metadataEntry.getValue());
        	}         	
        }        
	}
	
	@Test
	public void testModifyBitstream() throws ProcessingException, FileNotFoundException {
		Bitstream bitstream1 = new Bitstream();
		bitstream1.setName("pdf1.pdf");
		bitstream1.setDescription("desc1");		
		ResourcePolicy policy = new ResourcePolicy();
		policy.setGroupId(0);
		policy.setStartDate(new LocalDate().plusWeeks(1).toDate());		
		bitstream1.setPolicies(new ResourcePolicy[] {policy});

		bitstream1 = client.addBitstream(mockItem.getID(), bitstream1, new FileInputStream(getPath("pdf1.pdf")));
		
		Bitstream bitstream2 = new Bitstream();
		bitstream2.setName("pdf2.pdf");
		bitstream2.setDescription("desc2");		
		policy = new ResourcePolicy();
		policy.setAction(Action.DELETE);
		policy.setGroupId(0);
		policy.setStartDate(new LocalDate().plusMonths(1).toDate());		
		bitstream2.setPolicies(new ResourcePolicy[] {policy});
		
		client.updateBitstream(bitstream1.getID(), bitstream2, new FileInputStream(getPath("pdf2.pdf")));		
		Bitstream bitstream3 = client.findBitstreamById(bitstream1.getID());
		
		Assert.assertEquals("pdf2.pdf", bitstream3.getName());
		Assert.assertEquals("desc2", bitstream3.getDescription());
	}

    @Test
    public void testCRUDCommunity() throws Exception {
        // Test create
        Community addedCommunity = client.createCommunity(testCommunity);
        assertTrue("Added community should be same as sent community.", compareCommunities(testCommunity, addedCommunity, false));
        assertNotNull("Id should be not null.", addedCommunity.getID());

        // Test read
        Community readCommunity = client.findCommunityById(addedCommunity.getID(), "");
        assertTrue("Read community, should be same as added.", compareCommunities(addedCommunity,readCommunity, true));

        // Test update
        testCommunity.setIntroductoryText("text");
        testCommunity.setSidebarText("sidebar");
        client.updateCommunity(addedCommunity.getID(), testCommunity);
        readCommunity = client.findCommunityById(addedCommunity.getID(), "");
        assertTrue("Read community should be same as updated community.", compareCommunities(testCommunity, readCommunity, false));

        // Test delete
        client.deleteCommunity(addedCommunity.getID());
        thrown.expect(NotFoundException.class);
        client.findCommunityById(addedCommunity.getID(), "");
        //assertNull("Read null community, because is deleted.", readCommunity);
    }

    @Test
    public void testReadAllAndTopCommunities()  throws Exception {
        // Add some top community and some subcommunities
        Community firstCommunity = client.createCommunity(testCommunity);
        testCommunity.setName("Second top community");
        Community addedCommunity = client.createCommunity(testCommunity);
        testCommunity.setName("Subcommunity");
        List<Community> oneSubcommunity = new ArrayList<>();
        oneSubcommunity.add(new Community());
        testCommunity.setSubcommunities(oneSubcommunity);
        client.createSubcommunityOfCommunity(addedCommunity.getID(), testCommunity);

        // Test read top communities.
        List<Community> topCommunities = client.readTopCommunities("", 100, 0);
        List<Community> communities = client.readAllCommunities("", 100, 0);
        assertTrue("Length of top communities should be lower than all communities.", communities.size() > topCommunities.size() );

        // Clean up
        client.deleteCommunity(firstCommunity.getID());
        client.deleteCommunity(addedCommunity.getID());
    }

    @Test
    public void testCreateReadDeleteSubcommunity()  throws Exception {
        // Setup structure
        testCommunity = client.createCommunity(testCommunity);
        Community subcommunity = testCommunity;
        subcommunity.setName("Subcommunity");
        subcommunity.setShortDescription("Short");

        // Test create subcommunity
        Community addedSubcommunity = client.createSubcommunityOfCommunity(testCommunity.getID(), subcommunity);
        assertTrue("Added subcommunity should be same.", compareCommunities(subcommunity, addedSubcommunity, false));

        // Test read subcommunities
        List<Community> subcommunities = client.readSubcommunitiesOfCommunity(testCommunity.getID(), "", 100, 0);
        assertEquals("Length of subcommunities should be 1.", 1, subcommunities.size());
        assertTrue("Subcommunities(0) should be same.", compareCommunities(subcommunity, subcommunities.get(0), false));

        // Test delete subcommunities
        client.deleteSubcommunityOfCommunity(testCommunity.getID(), addedSubcommunity.getID());
        subcommunities = client.readSubcommunitiesOfCommunity(testCommunity.getID(), "", 100, 0);
        assertEquals("Length of deleted subcommunities should be 0.", 0, subcommunities.size());

        // Cleanup
        client.deleteCommunity(testCommunity.getID());
    }

    @Test
    public void testCreateReadDeleteSubcollectionOfCommunity()  throws Exception {
        // Setup structure
        testCommunity = client.createCommunity(testCommunity);
        Collection subcollection = new Collection();
        subcollection.setName("Subcollection");

        // Setup subcollection
        Collection addedSubcollection = client.createCollection(testCommunity.getID(), subcollection);

        // Test read subcollections
        List<Collection> subcollections = client.readSubcollectionsOfCommunity(testCommunity.getID(), "", 100, 0);
        assertEquals("Length of subcollections should be 1.", 1, subcollections.size());

        // Test delete subcollections
        client.deleteSubcollectionOfCommunity(testCommunity.getID(), addedSubcollection.getID());
        subcollections = client.readSubcollectionsOfCommunity(testCommunity.getID(), "", 100, 0);
        assertEquals("Length of deleted subcommunities should be 0.", 0, subcollections.size());

        // Cleanup
        client.deleteCommunity(testCommunity.getID());
    }

    @Test
    public void testCRUDCollection() throws Exception {
        testCommunity = client.createCommunity(testCommunity);

        // Test create
        Collection addedCollection = client.createCollection(testCommunity.getID(), testCollection);
        assertTrue("Added collection should be same as sent collection.", compareCollections(testCollection, addedCollection, false));

        // Test read
        Collection readCollection = client.findCollectionById(addedCollection.getID(), "");
        assertTrue("Read collection should be same as added.", compareCollections(addedCollection, readCollection, true));

        // Test update
        testCollection.setIntroductoryText("text");
        testCollection.setSidebarText("sidebar");
        client.updateCollection(addedCollection.getID(), testCollection);
        readCollection = client.findCollectionById(addedCollection.getID(), "");
        assertTrue("Read collection should be same as updated collection.", compareCollections(testCollection, readCollection, false));

        // Test delete
        client.deleteCollection(addedCollection.getID());
        thrown.expect(NotFoundException.class);
        readCollection = client.findCollectionById(addedCollection.getID(), "");
        //assertNull("Read null collection, because is deleted.", readCollection);

        // Cleanup
        client.deleteCommunity(testCommunity.getID());
    }

    // May fails on older version of REST api on oracle database.
    @Test
    public void testReadAllCollections() {
        testCommunity = client.createCommunity(testCommunity);

        // Add some top community and some subcommunities
        client.createCollection(testCommunity.getID(), testCollection);
        client.createCollection(testCommunity.getID(), testCollection);
        client.createCollection(testCommunity.getID(), testCollection);

        // Test read top communities.
        List<Collection> collections = client.readAllCollections("", 100, 0);
        assertFalse("Collections should not be null.", collections.isEmpty());

        // Cleanup
        client.deleteCommunity(testCommunity.getID());
    }

	
	@After
	public void tearDown() {
		client.deleteItem(mockItem.getID());
	}

	@AfterClass
	public static void shutdown() {
		((AbstractDSpaceRESTClient)client).destroy();
	}
	
	public static String getPath(String path) {
		try {
			return  URLDecoder.decode(DSpaceRESTClientTest.class.getClassLoader().getResource(path).getFile(),"UTF-8");
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
