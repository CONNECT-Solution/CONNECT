/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/exceptions/DuplicateSlotsException.java,v 1.1 2005/02/23 16:04:16 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.exceptions;

import java.util.List;
import javax.xml.registry.RegistryException;


/**
 * This exception will be thrown if there exists more than one Slot within a single
 * RegistryObject have the same name.
 * 
 * @author Adrian Chong
*/
public class DuplicateSlotsException extends RegistryException { //?? InvalidRequestException {
    private String parentId;
    private List slotsNames;

    public DuplicateSlotsException(String message) {
        super(message);
    }

    public DuplicateSlotsException(String parentId, List slotsNames) {
        this.parentId = parentId;
        this.slotsNames = slotsNames;
    }

    public String getParentId() {
        return parentId;
    }

    /**
    *        Get the names of those Slots that are duplicate
    */
    public List getSlotsNames() {
        return slotsNames;
    }
}
