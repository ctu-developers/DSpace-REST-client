package org.dspace.rest.common;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement(name = "resourcepolicy")
public class ResourcePolicy{
	
	public enum Action {
		READ, WRITE, DELETE;
	}
	
	private Integer id;
	private Action action;
	private Integer epersonId;
	private Integer groupId;
	private Integer resourceId;
	private String resourceType;
	private String rpDescription;
	private String rpName;
	private String rpType;
	private Date startDate;
	private Date endDate;
	
	public ResourcePolicy() {}
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public Integer getEpersonId() {
		return epersonId;
	}

	public void setEpersonId(Integer epersonId) {
		this.epersonId = epersonId;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getResourceId() {
		return resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getRpDescription() {
		return rpDescription;
	}

	public void setRpDescription(String rpDescription) {
		this.rpDescription = rpDescription;
	}

	public String getRpName() {
		return rpName;
	}

	public void setRpName(String rpName) {
		this.rpName = rpName;
	}

	public String getRpType() {
		return rpType;
	}

	public void setRpType(String rpType) {
		this.rpType = rpType;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ResourcePolicy [id=");
		builder.append(id);
		builder.append(", action=");
		builder.append(action);
		builder.append(", epersonId=");
		builder.append(epersonId);
		builder.append(", groupId=");
		builder.append(groupId);
		builder.append(", resourceId=");
		builder.append(resourceId);
		builder.append(", resourceType=");
		builder.append(resourceType);
		builder.append(", rpDescription=");
		builder.append(rpDescription);
		builder.append(", rpName=");
		builder.append(rpName);
		builder.append(", rpType=");
		builder.append(rpType);
		builder.append(", startDate=");
		builder.append(startDate);
		builder.append(", endDate=");
		builder.append(endDate);
		builder.append("]");
		return builder.toString();
	}
	
	
}