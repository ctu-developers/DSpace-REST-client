package org.dspace.rest.common.authority;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents authority for authority person module.
 *
 * @author Rostislav Novak (Computing and Information Centre, CTU in Prague)
 * @version 1.0
 * @since 2015-12-03
 */
@XmlRootElement
public class Authority {

    /** Name of authority. */
    private String name;
    /** Key from authority. */
    private String key;

    /** This will create empty authority. */
    public Authority() {
        /* Constructor for rest. */
    }

    /**
     * Create authority with arguments.
     *
     * @param name Name of authority.
     * @param key Key of person in authority.
     */
    public Authority(final String name, final String key) {
        this.name = name;
        this.key = key;
    }

    /**
     * Returns name of authority.
     *
     * @return String filled with name.
     */
    public final String getName() {
        return name;
    }

    /**
     * Set name of authority.
     *
     * @param name Name of authority.
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
     * Returns key of person in authority.
     *
     * @return key
     */
    public final String getKey() {
        return key;
    }

    /**
     * Set key of authority.
     *
     * @param key Key of person in authority.
     */
    public final void setKey(final String key) {
        this.key = key;
    }

}
