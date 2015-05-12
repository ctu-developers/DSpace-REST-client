/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.rest.common;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created with IntelliJ IDEA.
 * User: peterdietz
 * Date: 5/22/13
 * Time: 9:41 AM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "collection")
public class Collection extends DSpaceObject {

    //Relationships
    private Bitstream logo;
    private Community parentCommunity;
    private List<Community> parentCommunityList = new ArrayList<Community>();

    private List<Item> items = new ArrayList<Item>();

    //Collection-Metadata
    private String license;
    private String copyrightText, introductoryText, shortDescription, sidebarText;

    //Calculated
    private Integer numberItems;

    public Collection(){}

    public Bitstream getLogo() {
        return logo;
    }

    public Integer getNumberItems() {
        return numberItems;
    }

    public void setNumberItems(Integer numberItems) {
        this.numberItems = numberItems;
    }

    public Community getParentCommunity() {
        return parentCommunity;
    }

    public void setParentCommunity(Community parentCommunity) {
        this.parentCommunity = parentCommunity;
    }

    public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public void setParentCommunityList(List<Community> parentCommunityList) {
		this.parentCommunityList = parentCommunityList;
	}

	public List<Community> getParentCommunityList() {
        return parentCommunityList;
    }

    public void addParentCommunityList(Community parentCommunity) {
        this.parentCommunityList.add(parentCommunity);
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

	public String getCopyrightText() {
		return copyrightText;
	}

	public void setCopyrightText(String copyrightText) {
		this.copyrightText = copyrightText;
	}

	public String getIntroductoryText() {
		return introductoryText;
	}

	public void setIntroductoryText(String introductoryText) {
		this.introductoryText = introductoryText;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getSidebarText() {
		return sidebarText;
	}

	public void setSidebarText(String sidebarText) {
		this.sidebarText = sidebarText;
	}
}
