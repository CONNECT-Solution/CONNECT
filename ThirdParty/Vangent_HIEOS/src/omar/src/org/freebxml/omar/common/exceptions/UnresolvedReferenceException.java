/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/exceptions/UnresolvedReferenceException.java,v 1.5 2006/05/31 01:18:44 dougb62 Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.exceptions;

import java.util.Set;
import org.freebxml.omar.common.CommonResourceBundle;
import javax.xml.registry.RegistryException;


/**
 * This exception will be thrown when Association/Classification/ClassificationNode
 * /Organization is referencing a object that is <br>
 *
 * - not specified by ObjectRef but the referenced object does not exist within
 * the request <br>
 * - specified by ObjectRef but actually the object does not exist in Registry <br>
 *
 * @author Adrian Chong
*/
public class UnresolvedReferenceException extends RegistryException {
    protected static CommonResourceBundle resourceBundle = CommonResourceBundle.getInstance();

    private String referencingId;
    private String referencedId;

    public UnresolvedReferenceException(String message) {
        super(message);
    }

    public UnresolvedReferenceException(String referencingId,
        String referencedId) {
        this(resourceBundle.getString("message.unresolvedReferenceException", 
                new String[]{referencingId, referencedId}));
        this.referencingId = referencingId;
        this.referencedId = referencedId;
    }

    public UnresolvedReferenceException(Set unresolvedRefs) {
        this(resourceBundle.getString("message.unresolvedReferencesException", 
                new String[]{unresolvedRefs.toString()}));
    }

    public String getReferencingId() {
        return referencingId;
    }

    public String getReferencedId() {
        return referencedId;
    }
}
