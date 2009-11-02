/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/exceptions/InvalidContentException.java,v 1.3 2005/04/19 19:47:58 joehw Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.exceptions;
import org.freebxml.omar.common.CommonResourceBundle;

/**
 * This Exception signifies that the specified content was found to be
 * invalid.
 */
public class InvalidContentException extends ValidationException {
    protected static CommonResourceBundle resourceBundle = CommonResourceBundle.getInstance();

    public InvalidContentException() {
        this(resourceBundle.getString("message.InvalidContentException"));
    }
    
    public InvalidContentException(String msg) {
        super(msg);
    }
    
}
