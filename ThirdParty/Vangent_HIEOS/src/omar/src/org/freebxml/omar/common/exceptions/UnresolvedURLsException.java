/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/exceptions/UnresolvedURLsException.java,v 1.3 2005/04/19 19:47:59 joehw Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.exceptions;

import org.freebxml.omar.common.CommonResourceBundle;
import java.util.List;


/**
 * The Exception that should be thrown when Http URLs of ExternalLink or ServiceBinding
 * cannot be resolved
 *
 * @author Adrian Chong
 */
public class UnresolvedURLsException extends javax.xml.registry.RegistryException {
    protected static CommonResourceBundle resourceBundle = CommonResourceBundle.getInstance();

    private List sourceRegistryObjects;
    
    /**
     * 
     *
     * @param sourceRegistryObjects The List of ExternalLink or ServiceBinding having unresolvable Http URLs
     */
    public UnresolvedURLsException(List sourceRegistryObjects) {
        this(resourceBundle.getString("message.unresolvedURLsException"));
        this.sourceRegistryObjects = sourceRegistryObjects;
    }
    
    public UnresolvedURLsException(String msg) {
        super(msg);
    }
    
    
    public List getSourceRegistryObjects() {
        return sourceRegistryObjects;
    }
}
