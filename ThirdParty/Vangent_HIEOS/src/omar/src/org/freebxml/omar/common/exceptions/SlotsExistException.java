/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/exceptions/SlotsExistException.java,v 1.2 2005/04/19 19:47:59 joehw Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.exceptions;

import org.freebxml.omar.common.CommonResourceBundle;
import java.util.List;
import javax.xml.registry.RegistryException;


/**
 * This exception will be thrown if some Slots for a RegistryObject already exist.
 *
 * @author Adrian Chong
*/
public class SlotsExistException extends RegistryException {
    protected static CommonResourceBundle resourceBundle = CommonResourceBundle.getInstance();

    private String parentId;
    private List slotsNames;

    public SlotsExistException(String message) {
        super(message);
    }

    public SlotsExistException(String parentId, List slotsNames) {
        super(resourceBundle.getString("message.slotsExistException"));
        this.parentId = parentId;
        this.slotsNames = slotsNames;
    }

    public String getParentId() {
        return parentId;
    }

    /**
    *        Get the names of those Slots that already exist
    */
    public List getSlotsNames() {
        return slotsNames;
    }
}
