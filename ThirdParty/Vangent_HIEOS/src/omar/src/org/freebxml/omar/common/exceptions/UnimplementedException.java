/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/exceptions/UnimplementedException.java,v 1.1 2005/02/23 16:04:27 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.exceptions;

import javax.xml.registry.JAXRException;


/**
 * The Exception that should be thrown when an Unimplemented features is requested.
 *
 * @author Farrukh S. Najmi
*/
public class UnimplementedException extends JAXRException {

    public UnimplementedException(String msg) {
        super(msg);
    }

}
