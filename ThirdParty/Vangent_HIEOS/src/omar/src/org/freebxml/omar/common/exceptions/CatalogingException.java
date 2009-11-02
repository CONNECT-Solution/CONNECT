/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/exceptions/CatalogingException.java,v 1.4 2006/04/13 02:29:30 psterk Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.exceptions;

import org.freebxml.omar.common.CommonResourceBundle;
import javax.xml.registry.RegistryException;

/**
 * This Exception signifies that an exception was encountered in the
 * Cataloging algorithm for the service.
 */
public class CatalogingException extends RegistryException {
    protected static CommonResourceBundle resourceBundle = CommonResourceBundle.getInstance();
    public CatalogingException() {
        super(resourceBundle.getString("message.catalogingException"));
    }
    
    public CatalogingException(String msg) {
        super(msg);
    }
    
    public CatalogingException(Throwable t) {
        super(resourceBundle.getString("message.catalogingException"), t);
    }
    
    public CatalogingException(String msg, Throwable t) {
        super(msg, t);
    }    
}
