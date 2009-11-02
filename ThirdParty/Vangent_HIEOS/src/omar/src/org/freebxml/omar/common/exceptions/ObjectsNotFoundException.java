/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/exceptions/ObjectsNotFoundException.java,v 1.3 2005/04/19 19:47:58 joehw Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.exceptions;

import org.freebxml.omar.common.CommonResourceBundle;
import java.util.List;
import javax.xml.registry.RegistryException;


/**
 * The Exception that should be thrown when the Registry Objects or repository
 * items cannot be found.
 *
 * @author Adrian Chong
*/
public class ObjectsNotFoundException extends RegistryException {
    protected static CommonResourceBundle resourceBundle = CommonResourceBundle.getInstance();

    private List idList;

    /**
    *         @param idList The list of id of objects which does not exist
    */
    public ObjectsNotFoundException(String msg) {
        super(msg);
    }
    
    /**
    *         @param idList The list of id of objects which does not exist
    */
    public ObjectsNotFoundException(List idList) {
        super(resourceBundle.getString("message.objectsNotFoundException"));
        this.idList = idList;
    }

    public List getNotExistIds() {
        return idList;
    }
}
