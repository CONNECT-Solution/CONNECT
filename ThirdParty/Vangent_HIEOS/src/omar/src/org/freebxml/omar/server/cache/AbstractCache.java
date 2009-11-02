/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/cache/AbstractCache.java,v 1.9 2006/05/22 01:24:44 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.cache;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBException;
import javax.xml.registry.RegistryException;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freebxml.omar.common.BindingUtility;
import org.freebxml.omar.common.Utility;
import org.freebxml.omar.server.common.RegistryProperties;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.event.AuditableEventListener;
import org.freebxml.omar.server.persistence.PersistenceManager;
import org.freebxml.omar.server.persistence.PersistenceManagerFactory;
import org.freebxml.omar.server.security.authentication.AuthenticationServiceImpl;
import org.oasis.ebxml.registry.bindings.query.ResponseOption;
import org.oasis.ebxml.registry.bindings.query.ReturnType;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;

/**
 * The abstract base class for all server Cache classes.
 * 
 * @author Farrukh Najmi
 * @author Doug Bunting
 */
abstract class AbstractCache implements AuditableEventListener {
    private static final Log log = LogFactory.getLog(AbstractCache.class);

    /**
     * AuthenticationServiceImpl instance used when initializing a new
     * ServerRequestContext instance for cache-specific queries.  Since the
     * AuthenticationServiceImpl class is implemented as a singleton, need
     * only one for all AbstractCache (and sub class) instances.
     */
    protected static AuthenticationServiceImpl ac = null;
    protected static BindingUtility bu = BindingUtility.getInstance();

    /**
     * ObjectCache instance which contains all accessed registry objects
     * except classification schemes.
     */
    protected static ObjectCache objectCache = ObjectCache.getInstance();

    /**
     * PersistenceManager instance used when querying the database.  Since
     * the PersistenceManager class is implemented as a singleton, need
     * only one for all AbstractCache (and sub class) instances.
     */
    protected static PersistenceManager pm = null;

    protected String primeCacheEvent = "onCacheInit";
    protected boolean cacheIsPrimed = false;
    
    protected static CacheManager cacheMgr = null;
    protected Cache internalCache = null;

    protected AbstractCache()  {
        primeCacheEvent = RegistryProperties.getInstance()
            .getProperty("omar.server.cache.primeCacheEvent", "onCacheInit");
                
        if (cacheMgr == null) {
            try {
                cacheMgr = CacheManager.getInstance();
            } catch (CacheException e) {
                throw new UndeclaredThrowableException(e);
            }
        }
    }

    /**
     * Update the protected variables for use of sub-classes.  Calls to
     * this method should be done as late as possible since both
     * getInstance() calls included will lock some databases (for example,
     * the default Derby embedded mode configuration).
     */
    protected static synchronized void initializeQueryVars(boolean wantAC) {
	if (null == pm) {
	    pm = PersistenceManagerFactory.getInstance().
		getPersistenceManager();
	}
	if (wantAC && null == ac) {
	    try {
		ac = AuthenticationServiceImpl.getInstance();
	    } catch (Exception e) {
		// AuthenticationServiceImpl likely was unable to load
		// users prior to dB load, ignore exception
	    }
	}
    }

    protected static ServerRequestContext getCacheContext(String contextId)
	throws RegistryException {

	ServerRequestContext context =
	    new ServerRequestContext(contextId, null);

	initializeQueryVars(true);
	if (null != ac) {
	    context.setUser(ac.registryOperator);
	}
	return context;
    }

    protected abstract void initialize();

    protected abstract void primeCache(ServerRequestContext context);
    
    protected void primeCacheOnFirstUse(ServerRequestContext context) {
        if (!cacheIsPrimed && primeCacheEvent.equalsIgnoreCase("onFirstUse")) {
            primeCache(context);
        }
    }
    
    protected RegistryObjectType getRegistryObjectInternal(ServerRequestContext
							   context,
							   String id,
							   String typeName)
	throws RegistryException {

        RegistryObjectType ro = null;

        typeName = bu.mapJAXRNameToEbXMLName(typeName);
        String tableName = Utility.getInstance().mapTableName(typeName);

        try {
            if (id != null) {
                // COMMENT 1:
                // HIEOS/AMS: Commented the following lines of code. No need to convert 'id' to upper case
                // and subsequently compare using SQL's UPPER function (Using this prevents
                /// evaluation of indices on 'id').
                // String sqlQuery = "Select * from " + tableName + " WHERE UPPER(id) = ?";
                String sqlQuery = "Select * from " + tableName + " WHERE id = ?";
                ArrayList queryParams = new ArrayList();

                // HIEOS/AMS: See COMMENT 1.
                //queryParams.add(id.toUpperCase());
                queryParams.add(id);
                List results = executeQueryInternal(context, sqlQuery, queryParams, tableName);

                if (results.size() > 0) {
                    ro = (RegistryObjectType) results.get(0);
                }
                else {
                    ro = null;
                }
            }
        } catch (RegistryException e) {
            throw e;
        } catch (Exception e) {
            //e.printStackTrace();
            throw new RegistryException(e);
        }

        return ro;
    }
    
    protected List executeQueryInternal(ServerRequestContext context,
					String sqlQuery,
                                        List queryParams,
					String tableName)
	throws RegistryException {

        List results = null;
	initializeQueryVars(false);

        try {
            ResponseOption responseOption = bu.queryFac.createResponseOption();
            responseOption.setReturnComposedObjects(true);
            responseOption.setReturnType(ReturnType.LEAF_CLASS);

            List objectRefs = new ArrayList();
            results = pm.executeSQLQuery(context,
					 sqlQuery,
                                         queryParams,
					 responseOption,
					 tableName,
					 objectRefs);
        } catch (JAXBException e) {
            throw new RegistryException(e);
        }

        return results;
    }
    
    /**
     * Check if object exists in cache.
     */
    public boolean contains(String objectId) throws RegistryException {
        boolean found = false;
        
        try {
            found = (internalCache.get(objectId) != null);
        } catch (CacheException e) {
            throw new RegistryException(e);
        }
        return found;
    }
}
