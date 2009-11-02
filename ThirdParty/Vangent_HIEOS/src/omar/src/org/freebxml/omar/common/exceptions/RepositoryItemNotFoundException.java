/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/exceptions/RepositoryItemNotFoundException.java,v 1.4 2006/05/31 01:18:44 dougb62 Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.exceptions;

import org.freebxml.omar.common.CommonResourceBundle;

/**
 * The Exception that should be thrown when the RepositoryItem with specified lid and versionName
 * cannot be found.
 *
 * @author Adrian Chong
*/
public class RepositoryItemNotFoundException extends ObjectNotFoundException {
    protected static CommonResourceBundle resourceBundle = CommonResourceBundle.getInstance();

    private String lid;
    private String versionName;
    
    public RepositoryItemNotFoundException(String lid, String versionName) {
        this(resourceBundle.
	     getString("message.repositoryItemNotFoundException",
		       new String[]{lid, versionName}));
        this.lid = lid;
	this.versionName = versionName;
    }
    
    public RepositoryItemNotFoundException(String msg) {
        super(msg);
    }
    

    public String getLid() {
        return lid;
    }

    public String getVersionName() {
        return versionName;
    }
}
