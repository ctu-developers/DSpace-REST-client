package org.dspace.rest.common;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "edge")
public class Edge {
    private String parentCvutId;
    private String childCvutId;

    public String getParentCvutId() {
        return parentCvutId;
    }

    public void setParentCvutId(String parentCvutId) {
        this.parentCvutId = parentCvutId;
    }

    public String getChildCvutId() {
        return childCvutId;
    }

    public void setChildCvutId(String childCvutId) {
        this.childCvutId = childCvutId;
    }
}
