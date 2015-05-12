/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.rest.common;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created with IntelliJ IDEA.
 * User: peterdietz
 * Date: 9/19/13
 * Time: 4:50 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "item")
public class Item extends DSpaceObject {

    String isArchived;
    String isWithdrawn;
    String lastModified;

    Collection parentCollection;
    List<Collection> parentCollectionList;
    List<Community> parentCommunityList;
    List<MetadataEntry> metadata;
    List<Bitstream> bitstreams;

    public Item(){}

    public String getArchived() {
        return isArchived;
    }

    public void setArchived(String archived) {
        isArchived = archived;
    }

    public String getWithdrawn() {
        return isWithdrawn;
    }

    public void setWithdrawn(String withdrawn) {
        isWithdrawn = withdrawn;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public Collection getParentCollection() {
        return parentCollection;
    }

    public List<Collection> getParentCollectionList() {
        return parentCollectionList;
    }

    public List<MetadataEntry> getMetadata() {
        return metadata;
    }

    public List<Bitstream> getBitstreams() {
        return bitstreams;
    }

    public List<Community> getParentCommunityList() {
        return parentCommunityList;
    }

	public void setParentCollection(Collection parentCollection) {
		this.parentCollection = parentCollection;
	}

	public void setParentCollectionList(List<Collection> parentCollectionList) {
		this.parentCollectionList = parentCollectionList;
	}

	public void setParentCommunityList(List<Community> parentCommunityList) {
		this.parentCommunityList = parentCommunityList;
	}

	@XmlElement(required = true)
	public void setMetadata(List<MetadataEntry> metadata) {
		this.metadata = metadata;
	}

	public void setBitstreams(List<Bitstream> bitstreams) {
		this.bitstreams = bitstreams;
	}
}
