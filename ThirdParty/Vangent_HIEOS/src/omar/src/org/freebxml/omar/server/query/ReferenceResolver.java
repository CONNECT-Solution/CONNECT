/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2007 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/ReferenceResolver.java,v 1.1 2007/04/19 16:46:48 psterk Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query;

import java.util.Collection;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;

/**
 * This interface describes a reference resolver.
 */
public interface ReferenceResolver {

    /**
     * This method prefetches referenced objects up to the default specified 
     * depth level: 0 implies only fetch matched objects.
     *
     * @param context 
     * The ServerRequestContext used in this request
     * @param ro
     * The RegistryObjecType for which reference resolution is requested
     */
    public Collection getReferencedObjects(ServerRequestContext context, RegistryObjectType ro) 
        throws RegistryException;

    /**
     * This method prefetches referenced objects up to specified depth level.
     * Depth = 0 (default) implies only fetch matched objects.
     * Depth = n implies, also fetch all objects referenced by matched
     * objects upto depth of n
     * Depth = -1 implies, also fetch all objects referenced by matched
     * objects upto any level.
     *
     * @param context
     * The ServerRequestContext used in this request
     * @param ro
     * The target RegistryObjectType for which referenced objects are requested
     * @param depth int
     * The depth of the target RegistryObjectType dependency resolution
     */    
    public Collection getReferencedObjects(ServerRequestContext context, RegistryObjectType ro, int depth) 
        throws RegistryException;

}
