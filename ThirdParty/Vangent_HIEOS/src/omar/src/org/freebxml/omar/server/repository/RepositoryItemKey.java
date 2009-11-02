/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/repository/RepositoryItemKey.java,v 1.2 2004/09/10 15:37:13 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.repository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freebxml.omar.common.HashCodeUtil;

/**
 * A composite id for a RepositoryItem.
 * Consists of the lid and versionName columns.
 *
 * @author Farrukh S. Najmi
 */
public class RepositoryItemKey implements java.io.Serializable {
    
    private static Log log = LogFactory.getLog(RepositoryItemKey.class.getName()); 
    String lid;
    String versionName;
    
    /** Creates a new instance of RepositoryItemBean */
    public RepositoryItemKey() {
    }
    
    /** Creates a new instance of RepositoryItemBean */
    public RepositoryItemKey(String lid, String versionName) {
        this.lid = lid;
        this.versionName = versionName;
    }
    
    /**
     * Getter for property lid.
     * @return Value of property lid.
     */
    public java.lang.String getLid() {
        return lid;
    }
    
    /**
     * Setter for property lid.
     * @param lid New value of property lid.
     */
    public void setLid(java.lang.String lid) {
        this.lid = lid;
    }
    
    /**
     * Getter for property versionName.
     * @return Value of property versionName.
     */
    public java.lang.String getVersionName() {
        return versionName;
    }
    
    /**
     * Setter for property versionName.
     * @param versionName New value of property versionName.
     */
    public void setVersionName(java.lang.String versionName) {
        this.versionName = versionName;
    }
            
    public boolean equals(Object obj) {
        boolean isEqual = false;
        
        if ( this == obj ) return true;
        
        if (obj instanceof RepositoryItemKey) {
            RepositoryItemKey key = (RepositoryItemKey)obj;
            if ((key.lid.equals(lid)) && (key.versionName.equals(versionName))) {
                isEqual = true;
            }
        }
        
        return isEqual;
    }
        
    public int hashCode() {
        int result = HashCodeUtil.SEED;
        result = HashCodeUtil.hash( result, lid );
        result = HashCodeUtil.hash( result, versionName );
        return result;
    }
    
    public String toString() {
        String str = super.toString();
        str = "RepositoryItemKey: lid=" + getLid() + " versionName=" + getVersionName();

        return str;
    }
}
