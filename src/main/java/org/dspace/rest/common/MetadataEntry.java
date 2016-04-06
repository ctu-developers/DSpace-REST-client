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
 * Date: 9/20/13
 * Time: 5:51 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * @author peterdietz, Rostislav Novak
 *
 */
@XmlRootElement(name = "metadataentry")
public class MetadataEntry {
    String key;
    String value;
    String language;
    String authority;

    public MetadataEntry() {}

    public MetadataEntry(String key, String value, String language) {
        this.key = key;
        this.value = value;
        this.language = language;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MetadataEntry [key=");
		builder.append(key);
		builder.append(", value=");
		builder.append(value);
		builder.append(", language=");
		builder.append(language);
        builder.append(", authority=");
        builder.append(authority);
		builder.append("]");
		return builder.toString();
	}

}
