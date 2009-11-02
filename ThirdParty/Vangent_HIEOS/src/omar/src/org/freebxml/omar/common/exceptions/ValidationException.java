/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/exceptions/ValidationException.java,v 1.2 2005/04/19 19:48:00 joehw Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.exceptions;

import org.freebxml.omar.common.CommonResourceBundle;
import javax.xml.registry.RegistryException;

/**
 * This Exception signifies that an exception was encountered in the
 * Cataloging algorithm for the service.
 */
public class ValidationException extends RegistryException {
    protected static CommonResourceBundle resourceBundle = CommonResourceBundle.getInstance();

    public ValidationException() {
        super(resourceBundle.getString("message.validationException"));
    }
    
    public ValidationException(String msg) {
        super(msg);
    }
    
}
