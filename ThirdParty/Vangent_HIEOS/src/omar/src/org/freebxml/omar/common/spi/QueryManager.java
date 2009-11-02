/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/spi/QueryManager.java,v 1.1 2005/11/21 04:27:25 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.spi;

import java.security.cert.X509Certificate;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.common.RepositoryItem;

import org.oasis.ebxml.registry.bindings.query.AdhocQueryRequestType;
import org.oasis.ebxml.registry.bindings.query.AdhocQueryResponseType;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;
import org.oasis.ebxml.registry.bindings.rim.UserType;

public interface QueryManager {
    public AdhocQueryResponseType submitAdhocQuery(RequestContext context) throws 
         RegistryException;
    
    //Following are not part of WSDL interface but used originally in server
    //TODO: Reconcile these with WSDL interface
    /** getRegistryObject */
    public RegistryObjectType getRegistryObject(RequestContext context, String id)
        throws RegistryException;
    
    public RegistryObjectType getRegistryObject(RequestContext context, String id, String type)
        throws RegistryException;

    /** getRepositoryItem */
    public RepositoryItem getRepositoryItem(RequestContext context, String id)
        throws RegistryException;
        
    /**
     * Looks up the server side User object based upon specified public key certificate.
     */
    public UserType getUser(X509Certificate cert) throws RegistryException;        
}
