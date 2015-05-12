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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created with IntelliJ IDEA.
 * User: peterdietz
 * Date: 5/22/13
 * Time: 9:41 AM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "community")
public class Community extends DSpaceObject{

    //Exandable relationships
    private Bitstream logo;

    private Community parentCommunity;

    private String copyrightText, introductoryText, shortDescription, sidebarText;
    private Integer countItems;

    private List<Community> subcommunities = new ArrayList<Community>();

    private List<Collection> collections = new ArrayList<Collection>();

    public Community(){}

    public List<Collection> getCollections() {
        return collections;
    }

    public void setCollections(List<Collection> collections) {
        this.collections = collections;
    }

    public Integer getCountItems() {
        return countItems;
    }

    public void setCountItems(Integer countItems) {
        this.countItems = countItems;
    }

    public String getSidebarText() {
        return sidebarText;
    }

    public void setSidebarText(String sidebarText) {
        this.sidebarText = sidebarText;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getIntroductoryText() {
        return introductoryText;
    }

    public void setIntroductoryText(String introductoryText) {
        this.introductoryText = introductoryText;
    }

    public String getCopyrightText() {
        return copyrightText;
    }

    public void setCopyrightText(String copyrightText) {
        this.copyrightText = copyrightText;
    }

    public Community getParentCommunity() {
        return parentCommunity;
    }

    public void setParentCommunity(Community parentCommunity) {
        this.parentCommunity = parentCommunity;
    }

    public Bitstream getLogo() {
        return logo;
    }

    // Renamed because of xml annotation exception with this attribute and getSubCommunities.
    @XmlElement(name = "subcommunities", required = true)
    public List<Community> getSubcommunities() {
        return subcommunities;
    }

    public void setSubcommunities(List<Community> subcommunities) {
        this.subcommunities = subcommunities;
    }
    
}
