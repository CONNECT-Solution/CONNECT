/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/exceptions/ObjectNotFoundException.java,v 1.3 2006/05/31 01:18:44 dougb62 Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.exceptions;

import org.freebxml.omar.common.CommonResourceBundle;
import javax.xml.registry.RegistryException;

/**
 * The Exception that should be thrown when the RegistryObject of specified type
 * cannot be found.
 *
 * @author Adrian Chong
*/
public class ObjectNotFoundException extends RegistryException {
    protected static CommonResourceBundle resourceBundle = CommonResourceBundle.getInstance();

    private String id;
    private String objectType;

    public ObjectNotFoundException(String msg) {
        super(msg);
    }
    
    public ObjectNotFoundException(String id, String objectType) {
        super(resourceBundle.getString("message.objectNotFoundException",
				       new String[]{objectType, id}));
        this.id = id;
	this.objectType = objectType;
    }

    public String getId() {
        return id;
    }

    public String getObjectType() {
        return objectType;
    }
}
