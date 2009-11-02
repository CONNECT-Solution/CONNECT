/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/exceptions/MissingAttributeException.java,v 1.2 2005/04/19 19:47:58 joehw Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.exceptions;

import org.freebxml.omar.common.CommonResourceBundle;
import javax.xml.registry.InvalidRequestException;

/**
 * This Exception signifies that the caller did not provide a
 * repository item as an attachment to this request when the Service
 * requires it.
 *
 */
public class MissingAttributeException extends InvalidRequestException {
    protected static CommonResourceBundle resourceBundle = CommonResourceBundle.getInstance();

    public MissingAttributeException(String msg) {
        super(msg);        
    }

    public MissingAttributeException(Object obj, String id, String attributeName) {
        super(getFormatedMesage(obj, id, attributeName));        
    }
    
    private static String getFormatedMesage(Object obj, String id, String attributeName) {
        String className = org.freebxml.omar.common.Utility.getInstance().getClassNameNoPackage(obj);
        if (className.endsWith("Impl")) {
            className = className.substring(0, className.length()-4);
        }        
        return resourceBundle.getString("message.missingAttributeException", new String[]{className, id, attributeName});        
    }
}
