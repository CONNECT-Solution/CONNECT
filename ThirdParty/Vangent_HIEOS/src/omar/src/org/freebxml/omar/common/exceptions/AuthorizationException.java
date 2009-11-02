/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/exceptions/AuthorizationException.java,v 1.1 2005/02/23 16:04:12 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.exceptions;

import javax.xml.registry.RegistryException;


/**
 * The Exception that should be thrown when the requestor attempted to
 * perform an operation for which she was not authorized.
 *
 * @author Tony Graham
*/
public class AuthorizationException extends RegistryException {

    public AuthorizationException(String msg) {
        super(msg);
    }


}
