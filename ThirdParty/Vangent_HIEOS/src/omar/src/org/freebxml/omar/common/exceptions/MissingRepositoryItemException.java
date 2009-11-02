/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/exceptions/MissingRepositoryItemException.java,v 1.2 2005/04/19 19:47:58 joehw Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.exceptions;

import org.freebxml.omar.common.CommonResourceBundle;
import javax.xml.registry.RegistryException;

/**
 * This Exception signifies that the caller did not provide a
 * repository item as an attachment to this request when the Service
 * requires it.
 *
 */
public class MissingRepositoryItemException extends RegistryException {
    protected static CommonResourceBundle resourceBundle = CommonResourceBundle.getInstance();

    public MissingRepositoryItemException(String objectId) {
        super(resourceBundle.getString("message.missingRepositoryItemException", new String[]{objectId}));
    }

    private int attribute1;
}
