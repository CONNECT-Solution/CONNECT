/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/exceptions/SlotsParentNotExistException.java,v 1.2 2005/04/19 19:47:59 joehw Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.exceptions;

import org.freebxml.omar.common.CommonResourceBundle;

/**
 * This exception will be thrown if the parent of the Slots does not exist when
 * adding Slot.
 *
 * @author Adrian Chong
*/
public class SlotsParentNotExistException extends javax.xml.registry.RegistryException {
    protected static CommonResourceBundle resourceBundle = CommonResourceBundle.getInstance();

    private String parentId;

    public SlotsParentNotExistException(String parentId) {
        super(resourceBundle.getString("message.slotsParentNotExistException", new String[]{parentId}));
        this.parentId = parentId;
    }

    public String getParentId() {
        return parentId;
    }
}
