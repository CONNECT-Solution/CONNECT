/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/exceptions/UnsupportedContentException.java,v 1.3 2005/04/19 19:47:59 joehw Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.exceptions;

import org.freebxml.omar.common.CommonResourceBundle;
import javax.xml.registry.RegistryException;


/**
 * This Exception signifies that this Service does not support the
 * content provided by the caller.
 */
public class UnsupportedContentException extends RegistryException {
    protected static CommonResourceBundle resourceBundle = CommonResourceBundle.getInstance();

    public UnsupportedContentException() {
        this(resourceBundle.getString("message.unsupportedContentException"));
    }
    
    public UnsupportedContentException(String msg) {
        super(msg);
    }
    
}
