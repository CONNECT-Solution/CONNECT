/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/exceptions/DuplicateIdException.java,v 1.2 2005/04/19 19:47:57 joehw Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.exceptions;

import org.freebxml.omar.common.CommonResourceBundle;
import javax.xml.registry.RegistryException;

/**
 * This exception will be thrown if at least two Registry Objects within a
 * SubmitObjectsRequest have the same UUID (no matter it is proper or improper)
 *
 * @author Adrian Chong
*/
public class DuplicateIdException extends RegistryException {
    protected static CommonResourceBundle resourceBundle = CommonResourceBundle.getInstance();
    private String id;

    public DuplicateIdException(String id) {
        super(resourceBundle.getString("message.duplicateIdException", new String[]{id}));
        this.id = id;
    }

    public String getDuplicateId() {
        return id;
    }
}
