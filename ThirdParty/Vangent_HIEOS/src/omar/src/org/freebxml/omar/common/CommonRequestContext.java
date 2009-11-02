/*
 * AbstractRequestContext.java
 *
 * Created on November 17, 2005, 3:22 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.freebxml.omar.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.common.spi.RequestContext;
import org.oasis.ebxml.registry.bindings.rim.UserType;
import org.oasis.ebxml.registry.bindings.rs.RegistryRequestType;

/**
 *
 * @author najmi
 */
public class CommonRequestContext implements RequestContext {
    
    //The stack of RegistryRequests associated with the context
    private Stack requestStack = new Stack();   
    
    //The user associated with the context
    private UserType user = null;   
    
    //The context id
    private String id = null;
    
    //Map of  repositoryItems within the request with id keys and RepositoryItem values
    private Map repositoryItemsMap = new HashMap();
    
    
    private CommonRequestContext() {
    }

    public CommonRequestContext(String id, RegistryRequestType request) {
        this.id = id;
        if (request != null) {
            this.pushRegistryRequest(request);
        }
    }

    public RegistryRequestType getCurrentRegistryRequest() {
        return (RegistryRequestType)getRegistryRequestStack().peek();
    }

    public void pushRegistryRequest(RegistryRequestType req) {
        getRegistryRequestStack().push(req);
    }    
    
    public RegistryRequestType popRegistryRequest() {
        return (RegistryRequestType)getRegistryRequestStack().pop();
    }    
    
    public UserType getUser() {
        return user;
    }

    public void setUser(UserType user) throws RegistryException {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
    }

    public Map getRepositoryItemsMap() {
        return repositoryItemsMap;
    }

    public void setRepositoryItemsMap(Map repositoryItemsMap) {
        this.repositoryItemsMap = repositoryItemsMap;
    }

    public Stack getRegistryRequestStack() {
        return requestStack;
    }
}
