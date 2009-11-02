/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/ReferenceInfo.java,v 1.1 2004/04/15 16:13:03 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.common;

import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;


/**
 * Class to encapsulate all information about a reference attribute value in a RegistryObject.
 * 
 */
public class ReferenceInfo {
    
        
    public String sourceObject;
    public String targetObject;
    public String attributeName;

    
    public ReferenceInfo(String sourceObject,
        String targetObject, String attributeName) {
                        
        this.sourceObject = sourceObject;
        this.targetObject = targetObject;
        this.attributeName = attributeName;
    }
}
