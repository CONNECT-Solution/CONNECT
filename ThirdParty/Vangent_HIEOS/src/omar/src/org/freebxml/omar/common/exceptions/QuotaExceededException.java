/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/exceptions/QuotaExceededException.java,v 1.3 2005/04/19 19:47:58 joehw Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.exceptions;

import org.freebxml.omar.common.CommonResourceBundle;
import javax.xml.registry.RegistryException;


public class QuotaExceededException extends RegistryException {
    protected static CommonResourceBundle resourceBundle = CommonResourceBundle.getInstance();

    public QuotaExceededException(String userId, long repositoryQuota) {
        this(resourceBundle.getString("message.quotaExceededException", 
                new String[]{userId, Long.toString(repositoryQuota)}));
    }
    
    public QuotaExceededException(String msg) {
        super(msg);
    }
    
}
