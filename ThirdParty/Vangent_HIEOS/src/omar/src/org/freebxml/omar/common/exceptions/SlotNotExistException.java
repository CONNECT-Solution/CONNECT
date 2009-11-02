/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/exceptions/SlotNotExistException.java,v 1.2 2005/04/19 19:47:59 joehw Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.exceptions;

import org.freebxml.omar.common.CommonResourceBundle;
import javax.xml.registry.RegistryException;


/**
 * This exception will be thrown if any of the slots of a parent within RemoveSlotsRequest
 * does not exist
 *
 * @author Adrian Chong
*/
public class SlotNotExistException extends RegistryException {
    protected static CommonResourceBundle resourceBundle = CommonResourceBundle.getInstance();

    private String parentId;

    public SlotNotExistException(String parentId) {
        super(resourceBundle.getString("message.slotNotExistException", new String[]{parentId}));
        this.parentId = parentId;
    }

    public String getParentId() {
        return parentId;
    }
}
