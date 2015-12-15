package cz.cvut.dspace.rest.client.impl;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.RedirectionException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.dspace.rest.common.*;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.cvut.dspace.rest.client.Configuration;
import cz.cvut.dspace.rest.client.DSpaceRESTClient;

/**
 * Implementation of DSpace rest client in RESTEasy.
 *
 * @author Petr Karel
 * @author Rostislav Novak
 * @version $Revision: 5 $
 */
public abstract class AbstractDSpaceRESTClient implements DSpaceRESTClient {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	protected final Configuration configuration;
	protected final String ENDPOINT_URL;

    protected static final String COMMUNITIES = "/communities";
	protected static final String COLLECTIONS = "/collections";
	protected static final String ITEMS = "/items";
	protected static final String BITSTREAMS = "/bitstreams";
	protected static final String METADATA = "/metadata";
	protected static final String HEADER_TOKEN = "rest-dspace-token";
	
	protected ResteasyClient client;
	
	public AbstractDSpaceRESTClient(Configuration configuration) {
		this.configuration = configuration;
		if (configuration.getEndpointURL().endsWith("/")) {
			ENDPOINT_URL = configuration.getEndpointURL().substring(0, configuration.getEndpointURL().length()-1);
		} else {
			ENDPOINT_URL = configuration.getEndpointURL();
		}
	}
	
	public abstract void create();
	
	public abstract void destroy();
	
	public static <T> T extractResult(Class<T> responseType, Response response) {
		int status = response.getStatus();
		if (status >= 200 && status < 300) {
			return response.readEntity(responseType);
  		} else {
	  		handleErrorStatus(response);
  		}
  		return null;
	}
	
	public static void handleErrorStatus(Response response) throws WebApplicationException {
		final int status = response.getStatus();
		if (status >= 200 && status < 300) {
			return;
		} else if (status == 400) {
			throw new BadRequestException(response);
		} else if (status == 401) {
			throw new NotAuthorizedException(response);
		} else if (status == 404) {   
			throw new NotFoundException(response);
		} else if (status == 500) {
			throw new InternalServerErrorException(response);
		} else if (status == 503) {
			throw new ServiceUnavailableException(response);
		} else if (status >= 500) {
			throw new ServerErrorException(response);
		} else if (status >= 400) {
			throw new ClientErrorException(response);
		} else if (status >= 300) {
			throw new RedirectionException(response);
		} else {
			throw new WebApplicationException(response);
		}
	}

    public static String addArguments(String expand) {
        String arguments = "";
        if( !expand.isEmpty() || !expand.equals("")) {
            arguments += "?expand=" + expand;
        }
        return arguments;
    }

    public static String addArguments(String expand, Integer limit, Integer offset) {
        String arguments = "";
        if( !expand.isEmpty() || !expand.equals("")) {
            arguments += "expand=" + expand;
        }

        if(arguments.length() > 0) {
            arguments += "&";
        }

        if( limit != null) {
            arguments += "limit=" + limit;
        }

        if(arguments.length() > 0) {
            arguments += "&";
        }

        if( offset != null) {
            arguments += "offset=" + offset;
        }

        if(arguments.length() > 0) {
            arguments = "?" + arguments;
        }

        return arguments;
    }

	@Override
	public String login() throws ProcessingException, WebApplicationException {
		log.debug("Requesting authentication token [username={}, password={}].", configuration.getUsername(), "***");
		ResteasyWebTarget target = client.target(ENDPOINT_URL + "/login");
		Response response = target.request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(new User(configuration.getUsername(), configuration.getPassword()), MediaType.APPLICATION_JSON));
		try {
			return extractResult(String.class, response);
		} catch (WebApplicationException ex) {
			log.error("Requesting authentication token failed. Response code: {}.", response.getStatus());
			throw ex;
		} finally {
			response.close();
		}
	}

    @Override
    public Community createCommunity(Community community) throws ProcessingException, WebApplicationException {
        log.debug("Creating top community in DSpace.");
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COMMUNITIES);
        Response response = target.request().header(HEADER_TOKEN, token).accept(MediaType.APPLICATION_JSON).post(Entity.entity(community, MediaType.APPLICATION_JSON));
        try {
            community = extractResult(Community.class, response);
            log.info("Community (handle={},id={}) successfully created in DSpace.", community.getHandle(), community.getID());
            return community;
        } catch (WebApplicationException ex) {
            log.error("Creating community failed. Response code: {}.", response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    @Override
    public Community findCommunityById(Integer communityId, String expand) throws ProcessingException, WebApplicationException {
        log.debug("Reading community(id={}) from DSpace.", communityId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COMMUNITIES + "/" + communityId + addArguments(expand));
        Response response = target.request().header(HEADER_TOKEN, token).accept(MediaType.APPLICATION_JSON).get();
        try {
            Community community = extractResult(Community.class, response);
            log.info("Community (handle={}) successfully read from DSpace(id={}).", community.getHandle(), community.getID());
            return community;
        } catch (WebApplicationException ex) {
            log.error("Reading community(id={}) failed. Response code: {}.", communityId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    @Override
    public void updateCommunity(Integer communityId, Community community) throws ProcessingException, WebApplicationException {
        log.debug("Updating community(id={}) in DSpace.", communityId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COMMUNITIES + "/" + communityId);
        Response response = target.request().header(HEADER_TOKEN, token).accept(MediaType.APPLICATION_JSON).put(Entity.entity(community, MediaType.APPLICATION_JSON));
        try {
            handleErrorStatus(response);
            log.info("Community(id={}) successfully updated in DSpace.", community.getID());
        } catch (WebApplicationException ex) {
            log.error("Updating community(id={}) failed. Response code: {}.", communityId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    @Override
    public void deleteCommunity(Integer communityId) throws ProcessingException, WebApplicationException {
        log.debug("Deleting community(id={}) in DSpace.", communityId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COMMUNITIES + "/" + communityId);
        Response response = target.request().header(HEADER_TOKEN, token).accept(MediaType.APPLICATION_JSON).delete();
        try {
            handleErrorStatus(response);
            log.info("Community (id={}) successfully deleted in DSpace.", communityId);
        } catch (WebApplicationException ex) {
            log.error("Deleting community(id={}) failed. Response code: {}.", communityId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    @Override
    public List<Community> readAllCommunities(String expand, Integer limit, Integer offset) throws ProcessingException, WebApplicationException {
        log.debug("Reading communities from DSpace.");
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COMMUNITIES + addArguments(expand, limit, offset));
        Response response = target.request().header(HEADER_TOKEN, token).accept(MediaType.APPLICATION_JSON).get();
        try {
            Community[] communities = extractResult(Community[].class, response);
            log.info("Communities were successfully read from DSpace. (count={})", communities.length);
            return Arrays.asList(communities);
        } catch (WebApplicationException ex) {
            log.error("Reading communities failed. Response code: {}.", response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    @Override
    public List<Community> readTopCommunities(String expand, Integer limit, Integer offset) throws ProcessingException, WebApplicationException {
        log.debug("Reading top communities from DSpace.");
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COMMUNITIES + "/top-communities" + addArguments(expand, limit, offset));
        Response response = target.request().header(HEADER_TOKEN, token).accept(MediaType.APPLICATION_JSON).get();
        try {
            Community[] communities = extractResult(Community[].class, response);
            log.info("Top communities were successfully read from DSpace. (count={})", communities.length);
            return Arrays.asList(communities);
        } catch (WebApplicationException ex) {
            log.error("Reading top communities failed. Response code: {}.", response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    @Override
    public Community createSubcommunityOfCommunity(Integer parentCommunityId, Community subcommunity) throws ProcessingException, WebApplicationException {
        log.debug("Creating subcommunity in community(id={}).", parentCommunityId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COMMUNITIES + "/" + parentCommunityId + COMMUNITIES);
        Response response = target.request().header(HEADER_TOKEN, token).accept(MediaType.APPLICATION_JSON).post(Entity.entity(subcommunity, MediaType.APPLICATION_JSON));
        try {
            subcommunity = extractResult(Community.class, response);
            log.info("Subcommunity (handle={},id={}) successfully created in community(id={}).", new Object[]{subcommunity.getHandle(), subcommunity.getID(), parentCommunityId});
            return subcommunity;
        } catch (WebApplicationException ex) {
            log.error("Creating subcommunity in community(id={}) failed. Response code: {}.", parentCommunityId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    @Override
    public List<Community> readSubcommunitiesOfCommunity(Integer parentCommunityId, String expand, Integer limit, Integer offset) throws ProcessingException, WebApplicationException {
        log.debug("Reading subcommunities from community(id={}).", parentCommunityId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COMMUNITIES + "/" + parentCommunityId + COMMUNITIES + addArguments(expand, limit, offset));
        Response response = target.request().header(HEADER_TOKEN, token).accept(MediaType.APPLICATION_JSON).get();
        try {
            Community[] communities = extractResult(Community[].class, response);
            log.info("Subcommunities were successfully read from community(id={}). (count={})", parentCommunityId, communities.length);
            return Arrays.asList(communities);
        } catch (WebApplicationException ex) {
            log.error("Reading subcommunities from community(id={}) failed. Response code: {}.", parentCommunityId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    @Override
    public void deleteSubcommunityOfCommunity(Integer parentCommunityId, Integer subcommunityId) throws ProcessingException, WebApplicationException {
        log.debug("Deleting subcommunity(id={}) from community(id={}).", subcommunityId, parentCommunityId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COMMUNITIES + "/" + parentCommunityId + COMMUNITIES + "/" + subcommunityId );
        Response response = target.request().header(HEADER_TOKEN, token).accept(MediaType.APPLICATION_JSON).delete();
        try {
            handleErrorStatus(response);
            log.info("Subcommunity (id={}) successfully deleted in community(id={}). ).", subcommunityId, parentCommunityId);
        } catch (WebApplicationException ex) {
            log.error("Deleting subcommunity(id={}) from community(id={}) failed. Response code: {}.", new Object[]{subcommunityId, parentCommunityId, response.getStatus()});
            throw ex;
        } finally {
            response.close();
        }
    }

    @Override
    public List<Collection> readSubcollectionsOfCommunity(Integer parentCommunityId, String expand, Integer limit, Integer offset) throws ProcessingException, WebApplicationException {
        log.debug("Reading subcollections from community(id={}).", parentCommunityId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COMMUNITIES + "/" + parentCommunityId + COLLECTIONS + addArguments(expand, limit, offset));
        Response response = target.request().header(HEADER_TOKEN, token).accept(MediaType.APPLICATION_JSON).get();
        try {
            Collection[] collections = extractResult(Collection[].class, response);
            log.info("Subcollections were successfully read from community(id={}). (count={})", parentCommunityId, collections.length);
            return Arrays.asList(collections);
        } catch (WebApplicationException ex) {
            log.error("Reading subcollections from community(id={}) failed. Response code: {}.", parentCommunityId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    @Override
    public void deleteSubcollectionOfCommunity(Integer parentCommunityId, Integer subcollectionId) throws ProcessingException, WebApplicationException {
        log.debug("Deleting subcollection(id={}) from community(id={}).", subcollectionId, parentCommunityId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COMMUNITIES + "/" + parentCommunityId + COLLECTIONS + "/" + subcollectionId );
        Response response = target.request().header(HEADER_TOKEN, token).accept(MediaType.APPLICATION_JSON).delete();
        try {
            handleErrorStatus(response);
            log.info("subcollection (id={}) successfully deleted in community(id={}). ).", subcollectionId, parentCommunityId);
        } catch (WebApplicationException ex) {
            log.error("Deleting subcollection(id={}) from community(id={}) failed. Response code: {}.", new Object[]{subcollectionId, parentCommunityId, response.getStatus()});
            throw ex;
        } finally {
            response.close();
        }
    }

    @Override
    public Collection createCollection(Integer parentCommunityId, Collection collection) throws ProcessingException, WebApplicationException {
        log.debug("Creating collection in community(id={}).", parentCommunityId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COMMUNITIES + "/" + parentCommunityId + COLLECTIONS);
        Response response = target.request().header(HEADER_TOKEN, token).accept(MediaType.APPLICATION_JSON).post(Entity.entity(collection, MediaType.APPLICATION_JSON));
        try {
            collection = extractResult(Collection.class, response);
            log.info("Collection (handle={}, id={}) successfully created in community(id={}).", new Object[]{collection.getHandle(), collection.getID(), parentCommunityId});
            return collection;
        } catch (WebApplicationException ex) {
            log.error("Creating collection failed. Response code: {}.", response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    @Override
    public Collection findCollectionById(Integer collectionId, String expand) throws ProcessingException, WebApplicationException {
        log.debug("Reading collection(id={}) from DSpace.", collectionId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COLLECTIONS + "/" + collectionId + addArguments(expand));
        Response response = target.request().header(HEADER_TOKEN, token).accept(MediaType.APPLICATION_JSON).get();
        try {
            Collection collection = extractResult(Collection.class, response);
            log.info("Collection (handle={},id={}) successfully read from DSpace.", collection.getHandle(), collection.getID());
            return collection;
        } catch (WebApplicationException ex) {
            log.error("Reading collection(id={}) failed. Response code: {}.", collectionId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    @Override
    public void updateCollection(Integer collectionId, Collection collection) throws ProcessingException, WebApplicationException {
        log.debug("Updating collection(id={}) in DSpace.", collectionId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COLLECTIONS + "/" + collectionId);
        Response response = target.request().header(HEADER_TOKEN, token).accept(MediaType.APPLICATION_JSON).put(Entity.entity(collection, MediaType.APPLICATION_JSON));
        try {
            handleErrorStatus(response);
            log.info("Collection (id={}) successfully updated in DSpace.", collection.getID());
        } catch (WebApplicationException ex) {
            log.error("Updating collection(id={}) failed. Response code: {}.", collectionId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }

    }

    @Override
    public void deleteCollection(Integer collectionId) throws ProcessingException, WebApplicationException {
        log.debug("Deleting collection(id={}) in DSpace.", collectionId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COLLECTIONS + "/" + collectionId);
        Response response = target.request().header(HEADER_TOKEN, token).accept(MediaType.APPLICATION_JSON).delete();
        try {
            handleErrorStatus(response);
            log.info("Collection(id={}) successfully deleted in DSpace.", collectionId);
        } catch (WebApplicationException ex) {
            log.error("Deleting collection(id={}) failed. Response code: {}.", collectionId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    @Override
    public List<Collection> readAllCollections(String expand, Integer limit, Integer offset) throws ProcessingException, WebApplicationException {
        log.debug("Reading collections from DSpace.");
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COLLECTIONS + addArguments(expand, limit, offset));
        Response response = target.request().header(HEADER_TOKEN, token).accept(MediaType.APPLICATION_JSON).get();
        try {
            Collection[] collections = extractResult(Collection[].class, response);
            log.info("Collections were successfully read from DSpace. (count={})", collections.length);
            return Arrays.asList(collections);
        } catch (WebApplicationException ex) {
            log.error("Reading collections failed. Response code: {}.", response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    @Override
    public Collection findCollectionByName(String name) throws ProcessingException, WebApplicationException {
        log.debug("Looking for collection with name: {}", name);
        ResteasyWebTarget target = client.target(ENDPOINT_URL + COLLECTIONS + "/find-collection");
        Response response = target.request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(name, MediaType.APPLICATION_JSON));
        try {
            return extractResult(Collection.class, response);
        } catch (NotFoundException ex) {
            log.info("No collection was found with name={}.", name);
            return null;
        } catch (WebApplicationException ex) {
            log.error("Looking for collection (name={}) failed. Response code: {}.", name, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    @Override
    public List<Item> readCollectionItems(Integer parentCollectionId, String expand, Integer limit, Integer offset) throws ProcessingException, WebApplicationException {
        log.debug("Reading items from collection(id={}).", parentCollectionId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COLLECTIONS + "/" + parentCollectionId + ITEMS + addArguments(expand, limit, offset));
        Response response = target.request().header(HEADER_TOKEN, token).accept(MediaType.APPLICATION_JSON).get();
        try {
            Item[] items = extractResult(Item[].class, response);
            log.info("Items were successfully read from collection(id={}). (count={})", parentCollectionId, items.length);
            return Arrays.asList(items);
        } catch (WebApplicationException ex) {
            log.error("Reading items from collection(id={}) failed. Response code: {}.", parentCollectionId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }
	
	@Override
	public Item findItemById(Integer itemId, boolean includeMetadata, boolean includeBitstreams) throws ProcessingException, WebApplicationException {
		log.debug("Looking for item with id: {}", itemId);
		Item item = null;
		
		ResteasyWebTarget target = client.target(ENDPOINT_URL + ITEMS + "/" + itemId + "?expand=parentCollectionList,parentCollection,metadata,bitstreams");
	    Response response = target.request().accept(MediaType.APPLICATION_JSON).get();	    
	    try {
			item = extractResult(Item.class, response);
	    } catch (NotFoundException ex) {
	    	log.info("No item was found with id={}.", itemId);
	    	return null;
	    } catch (WebApplicationException ex) {
			log.error("Looking for item (id={}) failed. Response code: {}.", itemId, response.getStatus());
			throw ex;
		} finally {
			response.close();
		}
	    
	    if (includeMetadata) {
	    	log.debug("Getting metadata for item with id: {}", itemId);
	    	target = client.target(ENDPOINT_URL + ITEMS + "/" + itemId + "/metadata");
	    	response = target.request().accept(MediaType.APPLICATION_JSON).get();
	    	try {
	    		MetadataEntry[] metadataEntries = extractResult(MetadataEntry[].class, response);
	    		item.setMetadata(Arrays.asList(metadataEntries));
			} catch (WebApplicationException ex) {
				log.error("Looking for item's (id={}) metadata failed. Response code: {}.", itemId, response.getStatus());
				throw ex;
			} finally {
				response.close();
			}
	    }
	    
	    if (includeBitstreams) {
	    	log.debug("Getting bitstreams for item with id: {}", itemId);
	    	target = client.target(ENDPOINT_URL + ITEMS + "/" + itemId + "/bitstreams");
	    	response = target.request().accept(MediaType.APPLICATION_JSON).get();
	        try {
	        	Bitstream[] bitstreams = extractResult(Bitstream[].class, response);
	        	item.setBitstreams(Arrays.asList(bitstreams));
	        } catch (WebApplicationException ex) {
				log.error("Looking for item's (id={}) bitstreams failed. Response code: {}.", itemId, response.getStatus());
				throw ex;
			} finally {
				response.close();
			}
	    }

	    return item;
	}
	
	@Override
	public List<Item> findItemsByMetadataEntry(MetadataEntry metadata) throws ProcessingException, WebApplicationException {
		log.debug("Looking for items with metadataEntry: {}/{}", metadata.getKey(), metadata.getValue());
		Item[] items = null;
		
		ResteasyWebTarget target = client.target(ENDPOINT_URL + ITEMS + "/find-by-metadata-field" + "?expand=parentCollectionList,parentCollection");	    
	    Response response = target.request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(metadata, MediaType.APPLICATION_JSON));	    	    
	    try {
			items = extractResult(Item[].class, response);
	    } catch (WebApplicationException ex) {
			log.error("Looking for items failed (MetadataEntry={}/{}). Response code: {}.", new Object[]{metadata.getKey(), metadata.getValue(), response.getStatus()});
			throw ex;
		} finally {
			response.close();
		}

		if (items.length < 1) {
	        log.info("Item not found (MetadataEntry={}/{}).", metadata.getKey(), metadata.getValue());
	        return null;
	    }

	    return Arrays.asList(items);
	}
	
	@Override
	public Item createItem(Integer collectionId, List<MetadataEntry> metadata) throws ProcessingException, WebApplicationException {
		log.info("Creating a new item in collection (id={}).", collectionId);
		Item item = new Item();
		item.setMetadata(metadata);
		String token = login();
		
		ResteasyWebTarget target = client.target(ENDPOINT_URL + COLLECTIONS + "/" + collectionId + "/items");
		Response response = target.request().header(HEADER_TOKEN, token).accept(MediaType.APPLICATION_JSON).post(Entity.entity(item, MediaType.APPLICATION_JSON));
	    try {
			item = extractResult(Item.class, response);
			log.info("Item (handle={}) successfully created in collection (id={}).", item.getHandle(), collectionId);
	        return item;
		} catch (WebApplicationException ex) {
			log.error("Creating item failed. Response code: {}.", response.getStatus());
			throw ex;
		} finally {
			response.close();
		}		
	}
	
	@Override
	public void updateItem(Integer itemId, List<MetadataEntry> metadata) throws ProcessingException, WebApplicationException {
		log.info("Updating an existing item (id=" + itemId + ").");
		MetadataEntry[] metadataArray = metadata.toArray(new MetadataEntry[metadata.size()]);
		String token = login();		

		ResteasyWebTarget target = client.target(ENDPOINT_URL + ITEMS + "/" + itemId + METADATA);
		Response response = target.request().header(HEADER_TOKEN, token).accept(MediaType.APPLICATION_JSON).put(Entity.entity(metadataArray, MediaType.APPLICATION_JSON));
		try {
			handleErrorStatus(response);
			log.info("Item (id={}) successfully updated.", itemId);	        
		} catch (WebApplicationException ex) {
			log.error("Updating item (id={}) failed. Response code: {}.", itemId, response.getStatus());
			throw ex;
		} finally {
			response.close();
		}
	}
	
	@Override
	public void deleteItem(Integer itemId) throws ProcessingException, WebApplicationException {
		log.info("Deleting an existing item (id={}).", itemId);
        String token = login();        
        ResteasyWebTarget target = client.target(ENDPOINT_URL + ITEMS + "/" + itemId);
        Response response = target.request().header(HEADER_TOKEN, token).delete();
		try {
			handleErrorStatus(response);
			log.info("Item (id={}) successfully deleted.", itemId);
		} catch (WebApplicationException ex) {
			log.error("Deleting item (id={}) failed. Response code: {}", itemId, response.getStatus());
			throw ex;
		} finally {
			response.close();
		}        
	}

    @Override
    public List<Item> readAllItems(String expand, Integer limit, Integer offset) throws WebApplicationException {
        log.debug("Reading items from DSpace.");
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + ITEMS + addArguments(expand, limit, offset));
        Response response = target.request().header(HEADER_TOKEN, token).accept(MediaType.APPLICATION_JSON).get();
        try {
            Item[] items = extractResult(Item[].class, response);
            log.info("Items were successfully read from DSpace. (count={})", items.length);
            return Arrays.asList(items);
        } catch (WebApplicationException ex) {
            log.error("Reading items failed. Response code: {}.", response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

	@Override
	public void addItemToCollection(Integer itemId, Integer collectionId) throws ProcessingException, WebApplicationException {
		log.info("Adding an existing item (id={}) into collection (id={}).", itemId, collectionId);		
		String token = login();		
		
		ResteasyWebTarget target = client.target(ENDPOINT_URL + COLLECTIONS + "/" + collectionId + "/addItem/" + itemId);
		Response response = target.request().header(HEADER_TOKEN, token).accept(MediaType.APPLICATION_JSON).post(Entity.entity(itemId, MediaType.APPLICATION_JSON));
		try {
			handleErrorStatus(response);
			log.info("Item (id={}) successfully added into collection (id={}).", itemId, collectionId);	        
		} catch (WebApplicationException ex) {
			log.error("Adding item (id={}) into collection (id={}) failed. Response code: {}.", new Object[] {itemId, collectionId, response.getStatus()});
			throw ex;
		} finally {
			response.close();
		}
	}

	@Override
	public void deleteItemFromCollection(Integer itemId, Integer collectionId) throws ProcessingException, WebApplicationException {
		log.info("Deleting an existing item (id={}) from collection (id={}).", itemId, collectionId);		
		String token = login();		
		
		ResteasyWebTarget target = client.target(ENDPOINT_URL + COLLECTIONS + "/" + collectionId + "/items/" + itemId);
		Response response = target.request().header(HEADER_TOKEN, token).accept(MediaType.APPLICATION_JSON).delete();
		try {
			handleErrorStatus(response);
			log.info("Item (id={}) successfully deleted from collection (id={}).", itemId, collectionId);	        
		} catch (WebApplicationException ex) {
			log.error("Deleting item (id={}) from collection (id={}) failed. Response code: {}.", new Object[] {itemId, collectionId, response.getStatus()});
			throw ex;
		} finally {
			response.close();
		}
	}

	
	/* BITSTREAMS */
	@Override
	public Bitstream findBitstreamById(Integer bitstreamId) throws ProcessingException, WebApplicationException {
		log.debug("Looking for bitstream with id: {}", bitstreamId);
		String token = login();
		ResteasyWebTarget target = client.target(ENDPOINT_URL + BITSTREAMS + "/" + bitstreamId);
	    Response response = target.request().header(HEADER_TOKEN, token).accept(MediaType.APPLICATION_JSON).get();
	    try {
			return extractResult(Bitstream.class, response);
	    } catch (NotFoundException ex) {
	    	return null;
	    } catch (WebApplicationException ex) {			
			log.error("Looking for bitstream (id={}) failed. Response code: {}.", bitstreamId, response.getStatus());
			throw ex;
		} finally {
			response.close();
		}
	}

	@Override
	public Bitstream addBitstream(Integer itemId, Bitstream bitstream, InputStream is) throws ProcessingException, WebApplicationException {
		log.info("Adding bitstream to item (id={})", itemId);
		
		UriBuilder uriBuilder = UriBuilder.fromPath(ENDPOINT_URL + ITEMS + "/" + itemId + BITSTREAMS);
		uriBuilder.queryParam("name", bitstream.getName() != null ? bitstream.getName() : "");
		uriBuilder.queryParam("description", bitstream.getDescription() != null ? bitstream.getDescription() : "");	
		if (bitstream.getPolicies() != null && bitstream.getPolicies().length > 0) {
			ResourcePolicy defaultPolicy = bitstream.getPolicies()[0];
			uriBuilder.queryParam("groupId", defaultPolicy.getGroupId());
			if (defaultPolicy.getStartDate() != null) {
				LocalDate startDate = new LocalDate(defaultPolicy.getStartDate());
				uriBuilder.queryParam("year", startDate.getYear());
				uriBuilder.queryParam("month", startDate.getMonthOfYear());
				uriBuilder.queryParam("day", startDate.getDayOfMonth());
			}
		}
		
		String token = login();
		ResteasyWebTarget target = client.target(uriBuilder.build().toString());
		Response response = target.request().header(HEADER_TOKEN, token).accept(MediaType.APPLICATION_JSON).post(Entity.entity(is, MediaType.APPLICATION_JSON));
		try {			
			bitstream = extractResult(Bitstream.class, response);
			log.info("Bitstream (id={}) successfully added to item (id={}).", bitstream.getID(), itemId);
			return bitstream;
		} catch (WebApplicationException ex) {			
			log.error("Adding bitstream to item (id={}) failed. Response code: {}.", itemId, response.getStatus());
			throw ex;
		} finally {
			response.close();
		}		
	}
	
	@Override
	public void updateBitstream(Integer bitstreamId, Bitstream bitstream, InputStream is) throws ProcessingException, WebApplicationException {
		String token = login();
		ResteasyWebTarget target;
		Response response;
		
		if (is != null) {
			log.info("Updating bitstream's (id={}) binary data.", bitstreamId);
			target = client.target(ENDPOINT_URL + BITSTREAMS + "/" + bitstreamId + "/data");
			response = target.request().header(HEADER_TOKEN, token).accept(MediaType.WILDCARD).put(Entity.entity(is, MediaType.APPLICATION_JSON));
			try {
				handleErrorStatus(response);
				log.info("Bitstream's (id={}) binary data updated successfully.", bitstreamId);
			} catch (WebApplicationException ex) {
				log.error("Updating bitstream's (id={}) binary data failed. Response code: {}.", bitstreamId, response.getStatus());
				throw ex;
			} finally {
				response.close();
			}
		}
		
		if (bitstream != null) {
			log.info("Updating bitstream's (id={}) metadata.", bitstreamId);		
			target = client.target(ENDPOINT_URL + BITSTREAMS + "/" + bitstreamId);
	        response = target.request().header(HEADER_TOKEN, token).accept(MediaType.APPLICATION_JSON).put(Entity.entity(bitstream, MediaType.APPLICATION_JSON));
			try {
				handleErrorStatus(response);
				log.info("Bitstream's (id={}) metadata updated successfully.", bitstreamId);
			} catch (WebApplicationException ex) {
				log.error("Updating bitstream's (id={}) metadata failed. Response code: {}.", bitstreamId, response.getStatus());
				throw ex;
			} finally {
				response.close();
			}
		}
	}

	@Override
	public void deleteBitstream(Integer itemId, Integer bitstreamId) throws ProcessingException, WebApplicationException {
		log.info("Deleting an existing bitstream (id={}) in item (id={}).", bitstreamId, itemId);
		String token = login();		
		ResteasyWebTarget target = client.target(ENDPOINT_URL + ITEMS + "/" + itemId + BITSTREAMS + "/" + bitstreamId);
		Response response = target.request().header(HEADER_TOKEN, token).accept(MediaType.APPLICATION_JSON).delete();
		try {
			handleErrorStatus(response);
			log.info("Item's (id={}) bitstream (id={}) deleted successfully.", itemId, bitstreamId);
		} catch (WebApplicationException ex) {
			log.error("Deleting bitstream (id={}) in item (id={}) failed. Response code: {}.", new Object[] {bitstreamId, itemId, response.getStatus()});
			throw ex;
		} finally {
			response.close();
		}
	}

    @Override
    public List<Bitstream> readAllBitstreams(String expand, Integer limit, Integer offset) throws ProcessingException, WebApplicationException {
        log.debug("Reading bitstreams from DSpace.");
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + BITSTREAMS + addArguments(expand, limit, offset));
        Response response = target.request().header(HEADER_TOKEN, token).accept(MediaType.APPLICATION_JSON).get();
        try {
            Bitstream[] bitstreams = extractResult(Bitstream[].class, response);
            log.info("Bitstreams were successfully read from DSpace. (count={})", bitstreams.length);
            return Arrays.asList(bitstreams);
        } catch (WebApplicationException ex) {
            log.error("Reading bitstreams failed. Response code: {}.", response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

	@Override
	public List<ResourcePolicy> getBitstreamPolicies(Integer bitstreamId) throws ProcessingException {
		log.debug("Getting all bitstream's (id={}) policies.", bitstreamId);
		ResourcePolicy[] resourcePolicies = null;
		String token = login();		
		ResteasyWebTarget target = client.target(ENDPOINT_URL + BITSTREAMS + "/" + bitstreamId + "/policy");		
		Response response = target.request().header(HEADER_TOKEN, token).accept(MediaType.APPLICATION_JSON).get();
	    try {
			resourcePolicies = extractResult(ResourcePolicy[].class, response);
			return Arrays.asList(resourcePolicies);
		} catch (WebApplicationException ex) {
			log.error("Getting bitstream's (id={}) policies failed. Reponse code: {}.", bitstreamId, response.getStatus());
			throw ex;
		} finally {
			response.close();
		}
	}
		
	@Override
	public void addBitstreamPolicy(Integer bitstreamId, ResourcePolicy policy) throws ProcessingException {
		log.info("Adding a new policy to bitstream (id={}).", bitstreamId);
		String token = login();
		ResteasyWebTarget target = client.target(ENDPOINT_URL + BITSTREAMS + "/" + bitstreamId + "/policy");
		Response response = target.request().header(HEADER_TOKEN, token).accept(MediaType.APPLICATION_JSON).post(Entity.entity(policy, MediaType.APPLICATION_JSON));
		try {
			handleErrorStatus(response);
			log.info("Policy added to bitstream (id={}) successfully.", bitstreamId);
		} catch (WebApplicationException ex) {
			log.error("Adding policy to bitstream (id={}) failed. Reponse code: {}.", bitstreamId, response.getStatus());
			throw ex;
		} finally {
			response.close();
		}		
	}
	
	@Override
	public void deleteBitstreamPolicy(Integer bitstreamId, Integer policyId) throws ProcessingException {
		log.info("Deleting an existing policy (id={}) from bitstream (id={}).", policyId, bitstreamId);
		String token = login();
		ResteasyWebTarget target = client.target(ENDPOINT_URL + BITSTREAMS + "/" + bitstreamId + "/policy/" + policyId);
		Response response = target.request().header(HEADER_TOKEN, token).accept(MediaType.APPLICATION_JSON).delete();
		try {
			handleErrorStatus(response);
			log.info("Bitstream's (id={}) policy (id={}) deleted successfully.", bitstreamId, policyId);
		} catch (WebApplicationException ex) {
			log.error("Deleting policy (id={}) from bitstream (id={}) failed. Reponse code: {}.", new Object[]{policyId, bitstreamId, response.getStatus()});
			throw ex;
		} finally {
			response.close();
		}
	}


}
