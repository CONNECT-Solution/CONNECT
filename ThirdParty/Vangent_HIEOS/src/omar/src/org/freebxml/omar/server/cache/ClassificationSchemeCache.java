/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/cache/ClassificationSchemeCache.java,v 1.24 2007/06/06 21:54:13 psterk Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.cache;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.xml.bind.JAXBException;
import javax.xml.registry.JAXRException;
import javax.xml.registry.RegistryException;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Element;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freebxml.omar.common.CanonicalConstants;
import org.freebxml.omar.common.Utility;
import org.freebxml.omar.server.common.RegistryProperties;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.oasis.ebxml.registry.bindings.rim.AuditableEventType;
import org.oasis.ebxml.registry.bindings.rim.ClassificationNodeType;
import org.oasis.ebxml.registry.bindings.rim.ClassificationSchemeType;
import org.oasis.ebxml.registry.bindings.rim.ObjectRefType;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;

/**
 * The server side classification scheme cache for registry server.
 * Specific cache for classification schemes.
 *
 * TODO:
 *  Need to execute queries as RegistryOperator so cache is complete and then later do filtering when returning results
 *  based on user privileges.
 *
 * @author Farrukh Najmi
 * @author Doug Bunting
 */
class ClassificationSchemeCache extends AbstractCache {
    private static final Log log = LogFactory.getLog(ClassificationSchemeCache.class);

    String defaultSchemeCacheDepth = "4";

    private static ClassificationSchemeCache instance = null;
    private Cache pathToNodeCache = null;

    protected ClassificationSchemeCache() {
        defaultSchemeCacheDepth = RegistryProperties.getInstance()
            .getProperty("omar.server.cache.ClassificationSchemeCache.depth",
			 defaultSchemeCacheDepth);

        //Key is id, value is a ClassificationSchemeType
        internalCache = cacheMgr.getCache(ClassificationSchemeCache.class.getName());
    }

    /*
     * This method is used to get the Cache used to map a CN key to a CN 
     * instance value
     */
    private Cache getPathToNodeCache() throws RegistryException {
        // Use lazy instantiation
        if (pathToNodeCache == null) {
            String cacheName = ClassificationSchemeCache.class.getName()+".pathToNodeCache";
            pathToNodeCache = cacheMgr.getCache(cacheName);
            if (pathToNodeCache == null) {
                try {
                    cacheMgr.addCache(cacheName);
                } catch (Throwable t) {
                    throw new RegistryException(t);
                }
                pathToNodeCache = cacheMgr.getCache(cacheName);
            }
        }
        return pathToNodeCache;
    }
    
    public synchronized static ClassificationSchemeCache getInstance() {
        if (instance == null) {
            instance = new ClassificationSchemeCache();
        }

        return instance;
    }

    /**
     * Initializes the cache.
     */
    protected void initialize() {
	if (primeCacheEvent.equalsIgnoreCase("onCacheInit")) {
	    ServerRequestContext context = null;

	    try {
		context =
		    getCacheContext("ClassificationSchemeCache.initialize");
		primeCache(context);
	    } catch (RegistryException e) {
		// Ignore whatever caused getCacheContext() to fail.
	    } finally {
		try {
		    if (null != context) {
			// Caches never write to dB so no need to commit
			context.rollback();
		    }
		} catch (RegistryException e) {
		    log.warn(e);
		}
	    }
	}
    }

    /**
     * Re-initialize this cache, preloading unless such is completely
     * turned off.
     *
     * May be slightly incomplete since onEvent() context may
     * be owned by an unprivileged user.
     *
     */
    private void reset(ServerRequestContext context) {
        try {
            synchronized (internalCache) {
                internalCache.removeAll();
                cacheIsPrimed = false;
            }

            if (!primeCacheEvent.equalsIgnoreCase("never")) {
                primeCache(context);
            }
        } catch (IOException e) {
            //Should not happen ever.
            log.error(e);
        }
    }

    /**
     * Prime the cache, preloading all schemes and many of their
     * classification nodes.
     */
    protected void primeCache(ServerRequestContext context) {
	long startTime = 0;
	if (log.isTraceEnabled()) {
	    log.trace("primeCache: started");
	    startTime = System.currentTimeMillis();
	}

        // Prime cache by pre-loading all schemes and (to configured
        // depths) classification nodes below them.
	try {
	    updateScheme(context, "%", true);
	} catch (Exception e) {
	    // Exceptions during priming are most likely of the "table not
	    // found" ilk, especially during dB load.  In any case, no
	    // priming exception should prevent use of public methods from
	    // this class.
	}

        if (log.isTraceEnabled()) {
	    long endTime = System.currentTimeMillis();
	    log.trace("primeCache: ended. elapsedTime:" + (endTime-startTime));
	}
    }

    private int getDepthForScheme(String schemeId) {
        String depthProp =
	    "omar.server.cache.ClassificationSchemeCache.depth." +
	    schemeId.replace(':', '.');
        int schemeDepth = Integer.parseInt(RegistryProperties.getInstance()
            .getProperty(depthProp, defaultSchemeCacheDepth));

        //log.trace("SchemeId: " + schemeId + " depth: " + schemeDepth);
        return schemeDepth;
    }

    /**
     * Gets all schemes.  Note that descendent nodes of schemes up to the
     * depth level that is configured for the cache may also be loaded into
     * the object cache.
     */
    public List getAllClassificationSchemes(ServerRequestContext context)
	throws RegistryException {

	List result = null;

        try {
            primeCacheOnFirstUse(context);

            synchronized (internalCache) {
                if (cacheIsPrimed) {
                    result = getAllClassificationSchemesFromCache();
                }
            }
            // If not, we mostly update the cache outside the lock.
            if (null == result) {
                result = updateScheme(context, "%", true /* false */);
            }
        } catch (CacheException e) {
            throw new RegistryException(e);
        }

	return result;
    }

    /*
     * Gets all ClassificationSchemes from internal cache.
     * Assumes cache has been primed.
     *
     */
    private List getAllClassificationSchemesFromCache() throws CacheException {
        List schemes = new ArrayList();

        List keys = internalCache.getKeys();
        Iterator iter = keys.iterator();
        while (iter.hasNext()) {
            Serializable key = (Serializable)iter.next();
            Element elem = internalCache.get(key);
            if (elem != null) {
                ClassificationSchemeType scheme = (ClassificationSchemeType)elem.getValue();
                schemes.add(scheme);
            }
        }

        return schemes;
    }

    /**
     * Gets schemes that match id pattern.  Note that descendent nodes of
     * schemes up to the depth level that is configured for the cache may
     * also be loaded into the object cache.
     */
    public List getClassificationSchemesById(ServerRequestContext context,
					     String idPattern)
	throws RegistryException {

	List matchedSchemes = null;

        try {
            primeCacheOnFirstUse(context);

            if (Utility.getInstance().isValidRegistryId(idPattern)) {
                //Valid id: check if it is in cache
                Element elem = internalCache.get(idPattern);
                if (null == elem) {
                    //Cache miss. Get from dB
                    matchedSchemes = updateScheme(context, idPattern, true /* false */ );
                } else {
                    ClassificationSchemeType scheme = (ClassificationSchemeType)elem.getValue();
                    //Cache hit
                    matchedSchemes = Collections.singletonList(scheme);
                }
            } else {
                //Not a valid id: must be a pattern
                List schemes = getAllClassificationSchemes(context);
                if (idPattern.equals("%")) {
                    matchedSchemes = schemes;
                } else {
                    matchedSchemes = new ArrayList();
                    Iterator iter = schemes.iterator();
                    while (iter.hasNext()) {
                        ClassificationSchemeType scheme =
                            (ClassificationSchemeType) iter.next();
                        String schemeId =  scheme.getId();
                        if (idPattern.equalsIgnoreCase(schemeId)) {
                            matchedSchemes.add(scheme);
                        } else if (schemeId.matches(idPattern)) {
                            matchedSchemes.add(scheme);
                        }
                    }
                }
            }
        } catch (CacheException e) {
            throw new RegistryException(e);
        }

	return matchedSchemes;
    }

    /**
     * Gets ClassificationNodes whose parent's id matches the specified id.
     *
     * @param parentId The id of parent ClassificationNode or ClassificationScheme. Must be an exact id rather than an id pattern.
     */
    public List getClassificationNodesByParentId(ServerRequestContext context,
					     String parentId)
	throws RegistryException {
        List childConcepts = null;


        return childConcepts;
    }


    protected void updateCacheEntry(ServerRequestContext context,
				    RegistryObjectType ro)
	throws RegistryException {

	// ??? i18n
        throw new RegistryException("Internal error. Unimplemented function should not have been called.");
        /*
        RegistryObjectType scheme = null;
        ClassificationSchemeType schemeParent = null;
        ClassificationNodeType nodeParent = null;
        if (ro instanceof ClassificationSchemeType) {
            scheme = ro;
        } else if (ro instanceof ClassificationNodeType) {
            scheme = ((ClassificationNodeType)ro).;
            children = nodeParent.getClassificationNode();
        }
        */
    }

    /**
     * Put specified schemes into this cache.  Optionally, add descendent
     * nodes of schemes (up to the depth that is configured for the cache)
     * to the object cache, hopefully improving later cache tree
     * traversals.
     */
    private void putClassificationSchemes(ServerRequestContext context,
					  List schemes,
					  final boolean loadChildren,
					  final boolean nowPrimed)
	throws RegistryException {

	if (schemes != null) {
	    HashMap map = new HashMap(50);

	    // Convert provided List into a Map.
	    Iterator iter = schemes.iterator();
	    while (iter.hasNext()) {
		ClassificationSchemeType scheme =
		    (ClassificationSchemeType)iter.next();
		String schemeId = scheme.getId();

		map.put(schemeId, scheme);

		if (loadChildren) {
		    // Add any child nodes to the object cache
		    loadChildren(context,
				 scheme,
				 getDepthForScheme(schemeId));
		}
	    }
	    synchronized (internalCache) {
                iter = map.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry)iter.next();
                    Element elem = new Element((Serializable)entry.getKey(), (Serializable)entry.getValue());
                    internalCache.put(elem);
                }
		if (nowPrimed) {
		    cacheIsPrimed = true;
		}
	    }
        }
    }

    /**
     * Loads all schemes from the database into the cache.<br>
     * This method is not expected to be used after the priming event (if
     * the cache is primed).  However, when this method is used more than
     * once, this cache imitates the TRANSACTION_NONE transaction isolation
     * because the objects are generally read from the database in the
     * current context.  Updated but not committed objects are stored in
     * the cache and will not be removed if the transaction rolls back.
     */
    private List updateScheme(ServerRequestContext context,
			      String schemeIdPattern,
			      final boolean loadChildren)
	throws RegistryException {

	String sqlQuery =
	    "SELECT DISTINCT scheme.* FROM ClassScheme scheme WHERE scheme.id LIKE ?";
        ArrayList queryParams = new ArrayList();
        queryParams.add(schemeIdPattern);
	List newSchemes =
	    executeQueryInternal(context, sqlQuery, queryParams, "ClassScheme");

	// If the pattern was just "%", the cache will now be primed.
	putClassificationSchemes(context,
				 newSchemes,
				 loadChildren,
				 schemeIdPattern.equals("%"));

	return newSchemes;
    }

    /**
     * Loads the child nodes of specified parent as nested objects within parent.
     *
     * @param parent the ClassificationScheme or ClassificationNode parent whose children are desired.
     *
     * @param depth the number of levels of descendents caches for each ClassificationScheme in ClassificationSchemeCache<br />
     * depth of 0 means cache parent only<br />
     * depth of 1 means cache immediate child nodes of parent<br />
     * depth of 2 means cache immediate child nodes and grandchild nodes of parent<br />
     * depth of -1 means cache all descendent nodes of parent
     */
    private void loadChildren(ServerRequestContext context,
			      RegistryObjectType parent,
			      final int depth)
	throws RegistryException {

        if (depth != 0) {
            String sqlQuery = "SELECT node.* FROM ClassificationNode node WHERE node.parent=? ORDER BY node.code";
            ArrayList queryParams = new ArrayList();
            queryParams.add(parent.getId());
            List childNodes =
		executeQueryInternal(context, sqlQuery, queryParams, "ClassificationNode");

            if (childNodes.size() == 0) {
                try {
                    // Add a transient slot to indicate that child count is
                    // 0, making sure to not add a duplicate slot.  This
                    // will alow JAXR Provider to optimize by not trying to
                    // fetch these non-existent children
		    // ??? How long will this change last?  For example,
		    // ??? will the next context rollback remove this
		    // ??? transient slot?  May also be inefficient if
		    // ??? child node count is already 0.  Finally, we have
		    // ??? seen inconsistent Derby errors in
		    // ??? SlotDAO.getSlotsByParent() which may relate to a
		    // ??? problem in this code.
                    HashMap slotsMap = bu.getSlotsFromRegistryObject(parent);
                    parent.getSlot().clear();
                    slotsMap.put(bu.
				 CANONICAL_SLOT_NODE_PARENT_CHILD_NODE_COUNT,
				 "0");
                    bu.addSlotsToRegistryObject(parent, slotsMap);
                } catch (JAXBException e) {
                    //No big harm done as this is a optmization flag only.
                    log.warn(e);
                }
            } else {
                // Put child nodes in objectCache
                objectCache.putRegistryObjects(childNodes);

                //Now recurse and load each child.
                int newDepth = depth - 1;
		Iterator iter = childNodes.iterator();
                while (iter.hasNext()) {
                    ClassificationNodeType childNode =
			(ClassificationNodeType) iter.next();
                    loadChildren(context, childNode, newDepth);
                }
            }
        }
    }

    /**
     * Clear all affectedObjects in AuditableEvent from cache.  When
     * called, internalCache may be out of date with respect to dB (where
     * transaction has been committed) and objectCache (where affected
     * classification nodes have already been processed).<br>
     * This code keeps the cache primed if it was primed earlier.  The side
     * effect of this choice is every other context (separate transaction)
     * immediately knows about the just-committed changes.  That is, this
     * cache imitates TRANSACTION_READ_COMMITTED transaction isolation
     * unless the caching event setting is "never".
     */
    public void onEvent(ServerRequestContext context, AuditableEventType ae) {
	final String eventType = ae.getEventType();
	final boolean justRemove = primeCacheEvent.equalsIgnoreCase("never");
	final boolean wasChanged =
	    eventType.equalsIgnoreCase(CanonicalConstants.
				       CANONICAL_EVENT_TYPE_ID_Created) ||
	    eventType.equalsIgnoreCase(CanonicalConstants.
				       CANONICAL_EVENT_TYPE_ID_Updated) ||
	    eventType.equalsIgnoreCase(CanonicalConstants.
				       CANONICAL_EVENT_TYPE_ID_Versioned);
	final boolean wasRemoved =
	    eventType.equalsIgnoreCase(CanonicalConstants.
				       CANONICAL_EVENT_TYPE_ID_Deleted);

	Set schemesToRemove = new HashSet();
	HashMap schemesToUpdate = new HashMap();

	primeCacheOnFirstUse(context);

	if (wasChanged || wasRemoved) {
	    try {
		List affectedObjects = ae.getAffectedObjects().getObjectRef();
		Iterator iter = affectedObjects.iterator();

		while (iter.hasNext()) {
		    ObjectRefType ref = (ObjectRefType)iter.next();
		    String objectId = ref.getId();

		    RegistryObjectType ro = (RegistryObjectType)
			context.getAffectedObjectsMap().get(objectId);

		    if (null == ro) {
			// In case missing (removed?) object was a scheme
			schemesToRemove.add(objectId);
		    } else {
			if (ro instanceof ClassificationSchemeType) {
			    if (wasRemoved || justRemove) {
				schemesToRemove.add(objectId);
			    } else {
				schemesToUpdate.put(objectId, ro);
			    }
			} else if (ro instanceof ClassificationNodeType) {
			    String schemeId =
				bu.getSchemeIdForRegistryObject(ro);

			    // Handle case where a node in a scheme has been
			    // added, deleted or updated.
			    if (justRemove) {
				schemesToRemove.add(schemeId);
			    } else if (! (schemesToRemove.contains(schemeId) ||
					  schemesToUpdate.
					  containsKey(schemeId))) {
				ClassificationSchemeType scheme =
				    (ClassificationSchemeType)
				    getRegistryObjectInternal(context,
							      schemeId,
							      "ClassScheme");

				if (null != scheme) {
				    schemesToUpdate.put(schemeId, scheme);

				    // ??? Why is this necessary for all
				    // ??? schemes loaded?
				    loadChildren(context,
						 scheme,
						 getDepthForScheme(schemeId));
				}
			    }
			}
		    }
		}
	    } catch (JAXRException e) {
		log.error(e);
		//Just update all schemes to be safe in case of any errors
		reset(context);

		// Make following block a no-op.
		schemesToRemove.clear();
		schemesToUpdate.clear();
	    }
	}

	synchronized(internalCache) {
        final int oldSize = internalCache.getSize();

	    Iterator iter = schemesToRemove.iterator();
	    while (iter.hasNext()) {
		String objectId = (String)iter.next();
		internalCache.remove(objectId);
	    }

	    if (justRemove) {

		// Cache may become primed regardless of primeCacheEvent
		// setting, pay attention if we have undone that.
		if (oldSize != internalCache.getSize()) {
		    cacheIsPrimed = false;
		}
	    } else if (schemesToUpdate.size() > 0) {
		addClassificationSchemesToCache(schemesToUpdate.values());
	    }
	}
    }

    private void addClassificationSchemesToCache(Collection schemes) {
        Iterator iter = schemes.iterator();
        while (iter.hasNext()) {
            ClassificationSchemeType scheme = (ClassificationSchemeType)iter.next();
            Element elem = new Element(scheme.getId(), (Serializable)scheme);
            internalCache.put(elem);
        }
    }
    
    /**
     * This method is used to retrieve a ClassificationNode based on its path.
     * Note: if an invalid path is passed to this method, this method will
     * return null
     * 
     * @param path
     * A String containing the ClassificationNodeType path
     * @return
     * A ClassificationNodeType instance that matches the path
     */
    public ClassificationNodeType getClassificationNodeByPath(String path) 
        throws RegistryException {
        ClassificationNodeType cn = null;
        ServerRequestContext context = null;
        try {
            Element elem = getPathToNodeCache().get(path);
            if (elem == null) {
                String likeOrEqual = "=";
                if (path.indexOf('%') != -1) {
                    likeOrEqual = "LIKE";
                }
                String tableName = "ClassificationNode";

                // COMMENT 1:
                // HIEOS/AMS: Commented the following lines of code. No need to convert 'path' to upper case
                // and subsequently compare using SQL's UPPER function (Using this prevents
                // evaluation of indices on 'cn.path').
                // String sqlQuery = "SELECT cn.* from ClassificationNode cn WHERE UPPER(cn.path) " +
                // likeOrEqual + " ? ORDER BY cn.path ASC";
                String sqlQuery = "SELECT cn.* from ClassificationNode cn WHERE cn.path " +
                   likeOrEqual + " ? ORDER BY cn.path ASC";
                ArrayList queryParams = new ArrayList();

                // HIEOS/AMS: See COMMENT 1
                // queryParams.add(path.toUpperCase());
                queryParams.add(path);

                context = new ServerRequestContext("ClassificationSchemeCache.getClassificationNodeByPath", null);
                List results = executeQueryInternal(context, sqlQuery, queryParams, tableName);
                if (results.size() == 0) {
                    return null;
                }
                cn = (ClassificationNodeType)results.get(0);
                elem = new Element((Serializable)path, (Serializable)cn);
                getPathToNodeCache().put(elem);
            } else {
                cn = (ClassificationNodeType)elem.getValue();
            }
        } catch (RegistryException re) {
            throw re;
        } catch (Throwable t) {
            throw new RegistryException(t);
        } finally {
            if (context != null) {
                context.rollback();
            }
        }
        return cn;
    }
    
}
