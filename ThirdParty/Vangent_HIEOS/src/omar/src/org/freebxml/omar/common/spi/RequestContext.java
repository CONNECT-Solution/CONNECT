/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/spi/RequestContext.java,v 1.2 2005/11/28 20:17:33 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.spi;

import java.util.Map;
import java.util.Stack;
import javax.xml.registry.RegistryException;
import org.oasis.ebxml.registry.bindings.rim.UserType;
import org.oasis.ebxml.registry.bindings.rs.RegistryRequestType;

/**
 * The interface that carries Request specific context information.
 * Implemented differently by client and server to serve their 
 * specific needs for providing state and context during the 
 * processing of a request.
 * 
 * @author Farrukh Najmi
 * @author Diego Ballve
 *
 */
public interface RequestContext {
    /**
     * Gets the current RegistryRequest being processed by the context.
     * The context keeps a stack of RegistryRequests internally and return the top
     * of the stack.
     *
     * @return the current RegistryRequestType at the top of the stack of RegistryRequests.
     */
    public RegistryRequestType getCurrentRegistryRequest();
    
    /**
     * Pushes a new request on the stack of RegistryRequests.
     * This is used when client needs to issue a new RegistryRequest
     * while processing an existing RegistryRequest.
     *
     */
    public void pushRegistryRequest(RegistryRequestType req);
    
    /**
     * Pops the last request on the stack of RegistryRequests.
     * This is used when client is done with issuing a new RegistryRequest
     * while processing an existing RegistryRequest.
     *
     * @return the current RegistryRequestType at the top of the stack of RegistryRequests.
     */
    public RegistryRequestType popRegistryRequest();
    
    /**
     * Gets the Stack of RegistryRequests associated with this context.
     *
     */
    public Stack getRegistryRequestStack();
    
    /**
     * Gets the user associated with the RequestContext.
     *
     * @return the user associated with the request.
     */
    public UserType getUser();
    
    
    /**
     * Sets the user associated with this context.
     */
    public void setUser(UserType user) throws RegistryException;
    
    /**
     * Gets the id associated with this context.
     *
     */
    public String getId();
    
    /**
     * Sets the id associated with this context.
     * Typically this SHOULD be a URN that identifies the class and method where this context was created
     * or the registry interface method where it is being used.
     */
    public void setId(String id);

    /**
     * Gets the Map that keeps track of association between and ExtrinsicObjects and its RepositoryItem.
     *
     * @return the Map where key is RegistryObject id and value is a RepositoryItem.
     */
    public Map getRepositoryItemsMap();

    /**
     * Sets the Map that keeps track of association between and ExtrinsicObjects and its RepositoryItem.
     *
     * @param repositoryItemsMap the Map where key is RegistryObject id and value is a RepositoryItem.
     */
    public void setRepositoryItemsMap(Map repositoryItemsMap);
    
}
