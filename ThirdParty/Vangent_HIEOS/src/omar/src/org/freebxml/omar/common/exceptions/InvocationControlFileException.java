/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/exceptions/InvocationControlFileException.java,v 1.3 2005/04/19 19:47:58 joehw Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.exceptions;

import org.freebxml.omar.common.CommonResourceBundle;
import javax.xml.registry.RegistryException;

/**
 * This Exception signifies that the InvocationControlFile(s) provided
 * by the caller do not match the InvocationControlFile(s) expected by
 * the Service.
 */
public class InvocationControlFileException extends RegistryException {
    protected static CommonResourceBundle resourceBundle = CommonResourceBundle.getInstance();

    public InvocationControlFileException() {
        this(resourceBundle.getString("message.invocationControlFileException"));
    }

    public InvocationControlFileException(String msg) {
        super(msg);
    }
    
}
