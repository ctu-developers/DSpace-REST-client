package cz.cvut.dspace.rest.client;

import java.io.InputStream;
import java.util.List;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;

import org.dspace.rest.common.*;

/**
 * Interface for DSpace rest client.
 *
 * @author Petr Karel
 * @author Rostislav Novak
 * @version $Revision: 5 $
 */
public interface DSpaceRESTClient {

    /**
     * Login client into rest api.
     *
     * @return Returns token for logged user in DSpace rest api.
     *
     * @throws ProcessingException ...
     * @throws WebApplicationException ..
     */
    String login() throws ProcessingException, WebApplicationException;

    /**
     * Logout client into rest api.
     *
     * @throws ProcessingException ...
     * @throws WebApplicationException ..
     */
    void logout() throws ProcessingException, WebApplicationException;

    /* COMMUNITIES */
    /**
     * Create top community in DSpace.
     *
     * @param community Community which will be created in DSpace.
     * @return Returns new created community in DSpace.
     * @throws javax.ws.rs.WebApplicationException Is thrown when was problem with creating community in DSpace.
     *                                 Exception can any child class of WebApplicationException, which corresponds to HTTP code. It is
     *                                 thrown in this cases: server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
    Community createCommunity(Community community) throws ProcessingException, WebApplicationException;

    /**
     * Read community from DSpace.
     *
     * @param communityId Id of community which will be read.
     * @param expand Expand fields for community, which can be: parentCommunity, subCommunities, collections,
     *               logo and all. It must be separated by comma.
     * @return Returns specific community if was found, otherwise it returns null.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with reading community from DSpace.
     *                                 Problem can be: community not found, server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
    Community findCommunityById(Integer communityId, String expand) throws ProcessingException, WebApplicationException;

    /**
     * Update community in DSpace.
     *
     * @param communityId     Id of community, which will be updated.
     * @param community Updated community. Does not matter which id will be in instance of community.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with updating community in DSpace.
     *                                 Problem can be: community not found, server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
    void updateCommunity(Integer communityId, Community community) throws ProcessingException, WebApplicationException;

    /**
     * Delete community from DSpace.
     *
     * @param communityId Id of community, which will be deleted.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with deleting community in DSpace.
     *                                 Problem can be: community not found, server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
    void deleteCommunity(Integer communityId) throws ProcessingException, WebApplicationException;

    /**
     * Read all communities in DSpace.
     *
     * @param expand Expand fields for community, which can be: parentCommunity, subCommunities, collections,
     *               logo and all. It must be separated by comma.
     * @param limit  Limit of communities in List.
     * @param offset Offset for List of communities in DSpace database.
     * @return It returns filled or empty List of communities.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with reading communities from DSpace.
     *                                 Problem can be: Server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
    List<Community> readAllCommunities(String expand, Integer limit, Integer offset) throws ProcessingException, WebApplicationException;

    /**
     * Read all top communities in DSpace.
     *
     * @param expand Expand fields for community, which can be: parentCommunity, subCommunities, collections,
     *               logo and all. It must be separated by comma.
     * @param limit  Limit of communities in List.
     * @param offset Offset of List of communities in DSpace database.
     * @return It returns List of all top communities in DSpace.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with reading communities from DSpace.
     *                                             Problem can be: server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
    List<Community> readTopCommunities(String expand, Integer limit, Integer offset) throws ProcessingException, WebApplicationException;

    /**
     * Creates subcommunity in community.
     *
     * @param parentCommunityId Parent community Id, in which will be community created.
     * @param subcommunity      Community, which will be created.
     * @return It returns new created subcommunity.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with creating subcommunity in community or problem with
     *                                             finding community in DSpace. Problem can be: Parent community not found, server problem, access denied, bad URL
     *                                             and so on.
     * @throws ProcessingException ...
     */
    Community createSubcommunityOfCommunity(Integer parentCommunityId, Community subcommunity) throws ProcessingException, WebApplicationException;

    /**
     * Read all subcommunities from parent community.
     *
     * @param parentCommunityId Parent community id.
     * @param expand            Expand fields for community, which can be: parentCommunity, subCommunities, collections,
     *                          logo and all. It must be separated by comma.
     * @param limit             Limit of communities in List.
     * @param offset            Offset of List of communities in parent community list.
     * @return It returns List of subcommunities.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with reading subcommunities from community or problem
     *                                             with finding parent community. Problem can be: parent community not found, server problem, access denied,
     *                                             bad URL and so on.
     * @throws ProcessingException ...
     */
    List<Community> readSubcommunitiesOfCommunity(Integer parentCommunityId, String expand, Integer limit, Integer offset) throws ProcessingException, WebApplicationException;

    /**
     * Delete subcommunity from parent community.
     *
     * @param parentCommunityId Parent community Id.
     * @param subcommunityId    Subcommunity Id of parent community.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with deleting subcommunity from community.
     *                                             Problem can be: parent community not found, server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
    void deleteSubcommunityOfCommunity(Integer parentCommunityId, Integer subcommunityId) throws ProcessingException, WebApplicationException;

    /**
     * Read all subcollections from parent community.
     *
     * @param parentCommunityId Parent community Id
     * @param expand            Expand fields for collection, which can be: parentCommunityList, parentCommunity, items, license
     *                          logo and all. It must be separated by comma.
     * @param limit             Limit of subcollections in List.
     * @param offset            Offset of List of collections in parent community.
     * @return It returns List of subcollections from parent community.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with reading subcollections from community.
     *                                             Problem can be: parent community not found, server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
    List<Collection> readSubcollectionsOfCommunity(Integer parentCommunityId, String expand, Integer limit, Integer offset) throws ProcessingException, WebApplicationException;

    /**
     * Delete subcollection from parent community.
     *
     * @param parentCommunityId Parent community id.
     * @param subcollectionId   Subcollection Id of parent community.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with deleting subcollection from community.
     *                                             Problem can be: parent community not found, server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
    void deleteSubcollectionOfCommunity(Integer parentCommunityId, Integer subcollectionId) throws ProcessingException, WebApplicationException;

	/* COLLECTIONS */

    /**
     * Create collection in DSpace.
     *
     * @param parentCommunityId    Id of parent community of new collection.
     * @param collection Collection which will be created in community.
     * @return Returns new created collection in community.
     * @throws javax.ws.rs.WebApplicationException Is thrown when was problem with creating collection in DSpace.
     *                                 Exception can any child class of WebApplicationException, which corresponds to HTTP code. It is
     *                                 thrown in this cases: Parent community not found, server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
    Collection createCollection(Integer parentCommunityId, Collection collection) throws ProcessingException, WebApplicationException;

    /**
     * Read collection from DSpace.
     *
     * @param collectionId Id of Collection which will be read.
     * @param expand Expand fields for collection, which can be: parentCommunityList, parentCommunity, items, license
     *               logo and all. It must be separated by comma.
     * @return Returns collection if was found, otherwise it returns null.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with reading collection from DSpace.
     *                                 Problem can be: collection  not found, server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
    Collection findCollectionById(Integer collectionId, String expand) throws ProcessingException, WebApplicationException;

    /**
     * Update collection in DSpace.
     *
     * @param collectionId Id of Collection, which will be updated.
     * @param collection Updated collection. Does not matter which id will be in instance of Collection
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with updating collection in DSpace.
     *                                 Problem can be: collection not found, server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
    void updateCollection(Integer collectionId, Collection collection) throws ProcessingException, WebApplicationException;

    /**
     * Delete collection from DSpace.
     *
     * @param collectionId Id of collection, which will be deleted.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with deleting collection in DSpace.
     *                                 Problem can be: collection not found, server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
    void deleteCollection(Integer collectionId) throws ProcessingException, WebApplicationException;

    /**
     * Read all collections.
     *
     * @param expand Expand fields for collection, which can be: parentCommunityList, parentCommunity, items, license
     *               logo and all. It must be separated by comma.
     * @param limit Limit of collections in List.
     * @param offset Offset for List of collection in DSpace database.
     * @return It returns filled or empty List of collections.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with reading collections from DSpace.
     *                                 Problem can be: Server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
    List<Collection> readAllCollections(String expand, Integer limit, Integer offset) throws ProcessingException, WebApplicationException;

    /**
     * Look for collection in DSpace by name.
     *
     * @param name Name of collection.
     * @return Returns first collection with that name.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with searching collection DSpace.
     *                                             Problem can be: collection not found, server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
    Collection findCollectionByName(String name) throws ProcessingException, WebApplicationException;

    /**
     * Read items from collection.
     *
     * @param parentCollectionId Parent collection id.
     * @param expand             Expand fields for item, which can be: metadata, parentCollection, parentCollectionList,
     *                           parentCommunityList, bitstreams and all. It must be separated by comma.
     * @param limit              Limit of items from parent collection.
     * @param offset             Offset of List of items in parent collection list.
     * @return It returns List of items from parent collection.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with reading items from collection or problem
     *                                             with finding parent collection. Problem can be: parent collection not found, server problem, access denied,
     *                                             bad URL and so on.
     * @throws ProcessingException ...
     */
    List<Item> readCollectionItems(Integer parentCollectionId, String expand, Integer limit, Integer offset) throws ProcessingException, WebApplicationException;

	/* ITEMS */
    /**
     * Read item from DSpace.
     *
     * @param dspaceId Id of item which will be read.
     * @param includeMetadata Returns with filled metadata.
     * @param includeBitstreams Returns with filled bitstreams.
     * @return Returns item if was found, otherwise it returns null.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with reading item from DSpace.
     *                                 Problem can be: item not found, server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
	Item findItemById(Integer dspaceId, boolean includeMetadata, boolean includeBitstreams) throws ProcessingException, WebApplicationException;

    /**
     * Find all items, which has passed metadata entry.
     *
     * @param metadataEntry MetadataEntry by which will be finding.
     * @return Returns List of items which has that metadata entry, otherwise empty list.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with searching item.
     *                                             Problem can be: server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
	List<Item> findItemsByMetadataEntry(MetadataEntry metadataEntry) throws ProcessingException, WebApplicationException;

    /**
     * Create item in DSpace.
     *
     * @param collectionId Id of parent collection of new Item.
     * @param metadata Metadata which will be in Item.
     * @return Returns new created item in DSpace.
     * @throws javax.ws.rs.WebApplicationException Is thrown when was problem with creating item in DSpace.
     *                                 Exception can any child class of WebApplicationException, which corresponds to HTTP code. It is
     *                                 thrown in this cases: Parent collection not found, server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
	Item createItem(Integer collectionId, List<MetadataEntry> metadata) throws ProcessingException, WebApplicationException;

    /**
     * Update item in collection.
     *
     * @param itemId Id of item, which will be updated.
     * @param metadata Updated metadata.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with updating item in DSpace.
     *                                 Problem can be: item not found, server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
	void updateItem(Integer itemId, List<MetadataEntry> metadata) throws ProcessingException, WebApplicationException;

    /**
     * Delete item from DSpace.
     *
     * @param itemId Id of item, which will be deleted.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with deleting item in DSpace.
     *                                 Problem can be: Item not found, server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
	void deleteItem(Integer itemId) throws ProcessingException, WebApplicationException;

    /**
     * Read all item.
     *
     * @param expand Expand fields for item, which can be: metadata, parentCollection, parentCollectionList,
     *               parentCommunityList, bitstreams and all. It must be separated by comma.
     * @param limit  Limit of items in List.
     * @param offset Offset of List of items in DSpace database.
     * @return It returns filled or empty List of items.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with reading items from DSpace.
     *                                 Problem can be: Server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
    List<Item> readAllItems(String expand, Integer limit, Integer offset) throws ProcessingException, WebApplicationException;

    /**
     * Add item to existing collection.
     *
     * @param itemId Id of item.
     * @param collectionId Collection in which will by item add.
     * @throws javax.ws.rs.WebApplicationException Is thrown when was problem with adding item to collection.
     *                                 Exception can any child class of WebApplicationException, which corresponds to HTTP code. It is
     *                                 thrown in this cases: Parent collection not found, server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
	void addItemToCollection(Integer itemId, Integer collectionId) throws ProcessingException, WebApplicationException;

    /**
     * Delete item from collection.
     *
     * @param itemId             Id of item from parent collection.
     * @param collectionId Parent collection id.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with deleting item from collection.
     *                                             Problem can be: parent collection not found, server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
	void deleteItemFromCollection(Integer itemId, Integer collectionId) throws ProcessingException, WebApplicationException;

	/* BITSTREAMS */

    /**
     * Read bitstream from DSpace.
     *
     * @param bitstreamId Id of bitstream which will be read.
     * @return Returns bitstream if was found, otherwise it returns null.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with reading bitstream from DSpace.
     *                                 Problem can be: bitstream not found, server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
	Bitstream findBitstreamById(Integer bitstreamId) throws ProcessingException, WebApplicationException;

	//public InputStream getBitstreamBinaryData(Integer bitstreamId) throws ProcessingException, WebApplicationException;

    /**
     * Create bitstream in DSpace.
     *
     * @param itemId  Id of parent item of new bitstream.
     * @param bitstream Bitstream which will be created in item.
     * @param is Data which will be in created bitstream.
     * @return Returns new created bitstream in DSpace.
     * @throws javax.ws.rs.WebApplicationException Is thrown when was problem with creating bitstream in DSpace.
     *                                 Exception can any child class of WebApplicationException, which corresponds to HTTP code. It is
     *                                 thrown in this cases: Parent item not found, server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
	Bitstream addBitstream(Integer itemId, Bitstream bitstream, InputStream is) throws ProcessingException, WebApplicationException;

    /**
     * Update bitstream in DSpace.
     *
     * @param bitstreamId Id of bitstream, which will be updated.
     * @param bitstream Updated bitstream. Does not matter which id will be in instance of Bitstream
     * @param is Data which will be in replaced in bitstream.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with updating bitstream in DSpace.
     *                                 Problem can be: bitstream not found, server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
	void updateBitstream(Integer bitstreamId, Bitstream bitstream, InputStream is) throws ProcessingException, WebApplicationException;

    /**
     * Delete bitstream in item.
     *
     * @param itemId Id of item.
     * @param bitstreamId  Id of bitstream, which will be deleted.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with deleting bitstream from item.
     *                                             Problem can be: item not found, bitstream not found, server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
	void deleteBitstream(Integer itemId, Integer bitstreamId) throws ProcessingException, WebApplicationException;

    /**
     * Read all bitstreams.
     *
     * @param expand Expand fields for bitstream, which can be: parent, policies and all. It must be separated by comma.
     * @param limit  Limit of bitstream in List.
     * @param offset Offset of List bitstream in DSpace database.
     * @return It returns filled or empty List of specific Bitstreams.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with reading bitstream from DSpace.
     *                                 Problem can be: Server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
    List<Bitstream> readAllBitstreams(String expand, Integer limit, Integer offset) throws ProcessingException, WebApplicationException;

    /**
     * Read all bitstream resource policies.
     *
     * @param bitstreamId Id of bitstream.
     * @return It returns List of resource policies of bitstream.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with reading resource policies of bitstream.
     *                                             Problem can be: bitstream not found, server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
    List<ResourcePolicy> getBitstreamPolicies(Integer bitstreamId) throws ProcessingException, WebApplicationException;

    /**
     * Add resource policy to bitstream.
     *
     * @param bitstreamId Id of bitstream
     * @param policy      Resource policy which will be added to bitstream.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with adding resource policy of bitstream.
     *                                             Problem can be: bitstream not found, server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
	void addBitstreamPolicy(Integer bitstreamId, ResourcePolicy policy) throws ProcessingException, WebApplicationException;

    /**
     * Delete resource policy in bitstream.
     *
     * @param bitstreamId      Id of bitstream.
     * @param policyId Id of resource policy which will be deleted.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with deleting resource policy of bitstream.
     *                                             Problem can be: bitstream not found, server problem, access denied, bad URL and so on.
     * @throws ProcessingException ...
     */
	void deleteBitstreamPolicy(Integer bitstreamId, Integer policyId) throws ProcessingException, WebApplicationException;
}
