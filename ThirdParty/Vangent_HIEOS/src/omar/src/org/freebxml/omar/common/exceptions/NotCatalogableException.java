/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/exceptions/NotCatalogableException.java,v 1.1 2006/04/24 14:18:30 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.exceptions;

import org.freebxml.omar.common.CommonResourceBundle;
import javax.xml.registry.RegistryException;

/**
 * This Exception signifies that an exception was encountered in the
 * Cataloging algorithm for the service.
 */
public class NotCatalogableException extends CatalogingException {
    protected static CommonResourceBundle resourceBundle = CommonResourceBundle.getInstance();
    public NotCatalogableException() {
        super(resourceBundle.getString("message.notCatalogableException"));
    }
    
    public NotCatalogableException(String msg) {
        super(msg);
    }
    
    public NotCatalogableException(Throwable t) {
        super(resourceBundle.getString("message.notCatalogableException"), t);
    }
    
    public NotCatalogableException(String msg, Throwable t) {
        super(msg, t);
    }    
}
