/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/exceptions/ReferencesExistException.java,v 1.1 2005/02/23 16:04:19 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.exceptions;

import javax.xml.registry.RegistryException;


/**
 * When deleting a RegistryObject, this exception will be thrown if there exists
 * any objects that are referencing the RegistryObject to be deleted
 *
 * @author Adrian Chong
*/
public class ReferencesExistException extends RegistryException {
    /**
    *         @param idList The list of id of objects which does not exist
    */
    public ReferencesExistException(String message) {
        super(message);
    }
}
