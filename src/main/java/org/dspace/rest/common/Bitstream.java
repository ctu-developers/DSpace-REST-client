/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.rest.common;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created with IntelliJ IDEA.
 * User: peterdietz
 * Date: 9/21/13
 * Time: 12:54 AM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "bitstream")
public class Bitstream extends DSpaceObject {

    private String bundleName;
    private String description;
    private String format;
    private String mimeType;
    private Long sizeBytes;
    private DSpaceObject parentObject;
    private String retrieveLink;
    private CheckSum checkSum;
    private Integer sequenceId;
    
    private ResourcePolicy[] policies = null;
    
    public Bitstream() {
    }

    public Integer getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(Integer sequenceId) {
		this.sequenceId = sequenceId;
	}

	public String getBundleName() {
        return bundleName;
    }

    public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public void setSizeBytes(Long sizeBytes) {
		this.sizeBytes = sizeBytes;
	}

	public void setParentObject(DSpaceObject parentObject) {
		this.parentObject = parentObject;
	}

	public void setRetrieveLink(String retrieveLink) {
		this.retrieveLink = retrieveLink;
	}

	public String getDescription() {
        return description;
    }

    public String getFormat() {
        return format;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Long getSizeBytes() {
        return sizeBytes;
    }

    public String getRetrieveLink() {
        return retrieveLink;
    }

    public DSpaceObject getParentObject() {
        return parentObject;
    }
    
    public CheckSum getCheckSum() {
		return checkSum;
	}
    
    public void setCheckSum(CheckSum checkSum) {
		this.checkSum = checkSum;
	}

	public ResourcePolicy[] getPolicies() {
		return policies;
	}

	public void setPolicies(ResourcePolicy[] policies) {
		this.policies = policies;
	}
    
    
}
