/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/cache/RoleCache.java,v 1.10 2006/02/25 02:18:18 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.cache;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.registry.RegistryException;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Element;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.oasis.ebxml.registry.bindings.rim.AuditableEventType;
import org.oasis.ebxml.registry.bindings.rim.Classification;
import org.oasis.ebxml.registry.bindings.rim.ClassificationNodeType;
import org.oasis.ebxml.registry.bindings.rim.ObjectRefType;
import org.oasis.ebxml.registry.bindings.rim.UserType;

/**
 * The server side role cache for the registry server.
 * Cache for relationships between a small set of the objectCache content;
 * specifically, from User objects to SubjectRole ClassificationNodes using
 * their ids.
 * 
 * @author Farrukh Najmi
 * @author Doug Bunting
 */
class RoleCache extends AbstractCache {
    private static final Log log = LogFactory.getLog(RoleCache.class);
    
    private static RoleCache instance = null;            
    
    protected RoleCache() {
	cacheIsPrimed = true;
        
        //Key is userId, value is a Set of roles associated with userId. 
        //Roles are identified by ids to SubjectRole ClassificationNodes.
        internalCache = cacheMgr.getCache(RoleCache.class.getName());
    }
    
    public synchronized static RoleCache getInstance() {
        if (instance == null) {
            instance = new org.freebxml.omar.server.cache.RoleCache();
        }

        return instance;
    }
    
    /**
     * Initialize the cache, a no-op for this cache.
     */
    protected void initialize() {
    }

    /**
     * Prime the cache, a no-op for this cache.
     */
    protected void primeCache(ServerRequestContext context) {
    }
    
    public void putRoles(String userId, Set roles) {
        Element elem = new Element(userId, (Serializable)roles);
        internalCache.put(elem);        
    }
        
    /**
     * Gets the roles associated with the user associated with specified
     * ServerRequestContext.
     */
    public Set getRoles(ServerRequestContext context)
	throws RegistryException {

        Set roles = null;
        
        try {
            UserType user = context.getUser();
            if (user != null) {
                Element elem = internalCache.get(user.getId());
                if (elem == null) {
                    //Cache miss. Get from registry
                    //log.trace("RoleCache: cache miss for id: " + id);                            
                    roles = getRoles(context, user);
                    putRoles(user.getId(), roles);
                } else {
                    roles = (Set)elem.getValue();
                }
            } else {
                roles = new HashSet();
            }
        } catch (CacheException e) {
            throw new RegistryException(e);
        }
        return roles;
    }
    
    private Set getRoles(ServerRequestContext context, UserType user)
	throws RegistryException {

        Set roles = new HashSet();
        List classifications = user.getClassification();

        Iterator iter = classifications.iterator();
        while (iter.hasNext()) {
            Classification classification = (Classification)iter.next();
            String classificationNodeId =
		classification.getClassificationNode();
            ClassificationNodeType node =
		(ClassificationNodeType)objectCache.
		getRegistryObject(context,
				  classificationNodeId,
				  "ClassificationNode");
	    // Note: ObjectNotFoundException thrown above when node missing.

	    if (node.getPath().
		startsWith("/urn:oasis:names:tc:ebxml-regrep:classificationScheme:SubjectRole/")) {
		roles.add(node.getPath());
            }
        }
        return roles;
    }            
    
    /**
     * Clear all affectedObjects in AuditableEvent from cache regardless of
     * the event type.
     */
    public void onEvent(ServerRequestContext context, AuditableEventType ae) {
        List affectedObjects = ae.getAffectedObjects().getObjectRef();
        
        Iterator iter = affectedObjects.iterator();
        while (iter.hasNext()) {
            ObjectRefType oref = (ObjectRefType)iter.next();
            internalCache.remove(oref.getId());
        }        
    }
}
