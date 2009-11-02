/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/repository/AbstractRepositoryManager.java,v 1.22 2006/12/20 02:19:45 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBException;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freebxml.omar.common.BindingUtility;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.common.RepositoryItem;
import org.freebxml.omar.common.Utility;
import org.freebxml.omar.common.exceptions.ObjectNotFoundException;
import org.freebxml.omar.common.exceptions.RepositoryItemNotFoundException;
import org.freebxml.omar.common.spi.QueryManager;
import org.freebxml.omar.common.spi.QueryManagerFactory;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.persistence.PersistenceManagerFactory;
import org.freebxml.omar.server.security.authentication.AuthenticationServiceImpl;
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.oasis.ebxml.registry.bindings.query.ResponseOption;
import org.oasis.ebxml.registry.bindings.query.ReturnType;
import org.oasis.ebxml.registry.bindings.rim.ExtrinsicObjectType;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;
import org.oasis.ebxml.registry.bindings.rim.VersionInfoType;


/**
 * Abstract class implementing RepositoryManager interface that can be
 * extended by RepositoryManagers that control inserting, updating and
 * deleting repository items. Provides minimal implementation of some
 * generic methods.
 *
 * @author Adrian Chong
 * @author Peter Burgess
 *
 * @version $Version: $ [$Date: 2006/12/20 02:19:45 $]
 */
public abstract class AbstractRepositoryManager implements RepositoryManager {
    private static URIResolver uriResolver;

    private static final Log log = LogFactory.getLog(AbstractRepositoryManager.class);

    /** QueryManager, initially used to get ExtrinsicObject details. */
    protected static QueryManager qm = QueryManagerFactory.getInstance().getQueryManager();

    protected static BindingUtility bu = BindingUtility.getInstance();
    
    /**
     * A URIResolver for RepositoryItem objects
     *
     */
    class RepositoryManagerURIResolver implements URIResolver {
        public Source resolve(String href,
        String base)
        throws TransformerException {
            Source source = null;
            try {
                // Should this check that href is UUID URN first?
                source = getAsStreamSource(href);
            } catch (Exception e) {
                source = null;
            }
            
            return source;
        }
    }
    
    /**
     * Gets a <code>URIResolver</code> that handles locating repository items
     *
     * @return an <code>URIResolver</code> value
     */
    public URIResolver getURIResolver() {
        if (uriResolver == null) {
            synchronized (AbstractRepositoryManager.class) {
                if (uriResolver == null) {
                    uriResolver = new RepositoryManagerURIResolver();
                }
            }
        }
        
        return uriResolver;
    }
    
    /**
    * Delete the repository item associated with the ExtrinsocObject with specified id.
    * Do not delete repository item if it is in use by a different version of the ExtrinsicObject.
    *
    * @param key Unique key for repository item
    * @throws RegistryException if the item does not exist
    */
    public void delete(String id) throws RegistryException {
        RepositoryItemKey key = getRepositoryItemKey(id);
        delete(key);
    }        
    
    /**
     * Delete multiple repository items.
     * @param keys List of RepositoryItemKey.
     * @throws RegistryException if any of the item do not exist
     */
    public void delete(List keys) throws RegistryException {
        Iterator iter = keys.iterator();
        
        while (iter.hasNext()) {
            delete((RepositoryItemKey) iter.next());
        }
    }
    
    /**
     * Updates a RepositoryItem.
     * @param item repository item to be updated.
     * @exception RegistryException
     */
    public void update(ServerRequestContext context, RepositoryItem item) throws RegistryException {
        log.debug(
        "[FileSystemRepositoryManager::update()] item.getId() = " +
        item.getId());
        
        String id = item.getId();
        delete(id);
        insert(context, item);
    }
    
    public boolean itemExists(RepositoryItemKey key) throws RegistryException {
        boolean found = false;
        
        List results = null;
        
        ServerRequestContext context = null;
        try {
            context = new ServerRequestContext("AbstractRepositoryManager:itemExists", null);
            //Access control is check in qm.getRepositoryItem using actual request context
            //This internal request context has total access.
            context.setUser(AuthenticationServiceImpl.getInstance().registryOperator);
            
            ResponseOption responseOption = bu.queryFac.createResponseOption();
            responseOption.setReturnComposedObjects(true);
            responseOption.setReturnType(ReturnType.LEAF_CLASS);

            List objectRefs = new ArrayList();
            String sqlQuery = "SELECT eo.* FROM ExtrinsicObject eo WHERE eo.lid= ? AND eo.contentVersionName= ?";
            ArrayList queryParams = new ArrayList();
            queryParams.add(key.getLid());
            queryParams.add(key.getVersionName());
            String tableName = Utility.getInstance().mapTableName("ExtrinsicObject");
            
            results = PersistenceManagerFactory.getInstance().
                getPersistenceManager().executeSQLQuery(context,
                sqlQuery,
                queryParams,
                responseOption,
                tableName,
                objectRefs);
            
                                    
            if (results.size() > 0) {
                found = true;
            }
            
        } catch (JAXBException e) {
            throw new RegistryException(e);
        } finally {
            context.rollback();
        }
                
        return found;
    }
    
    
    /**
     * Return a List of non-existent repository items
     * @param ids The List of repository items keys.
     */
    public List itemsExist(List ids) throws RegistryException {
        try {
            List notFound = new ArrayList();
            if (ids.size() == 0) {
                return notFound;
            }
            
            Iterator iter = ids.iterator();
            while (iter.hasNext()) {
                String id = (String)iter.next();
                try {
                    RepositoryItem ri = getRepositoryItem(id);
                } catch (RepositoryItemNotFoundException e) {
                    notFound.add(id);
                }  catch (ObjectNotFoundException e) {
                    notFound.add(id);
                }
            }
            
            return notFound;
            
        } catch (Exception e) {
            String msg = ServerResourceBundle.getInstance().getString("message.FailedToVerifyRepositoryItemExistence");
            log.error(e, e);
            throw new RegistryException(ServerResourceBundle.getInstance().getString("message.seeLogsForDetails", new Object[]{msg})); 
        }
    }
        
    /**
     * Get the size of a repository item in bytes.
     * @param id Unique id for ExtrinsicObject whose repository item size is desired
     * @return 0 if the file does not exist.
     */
    public long getItemSize(String id) throws RegistryException {
        long itemSize = 0;
        try {
            RepositoryItem ri = getRepositoryItem(id);
            itemSize = (long) ri.getSize();
        } catch (RepositoryItemNotFoundException e) {
            //return 0;
        } catch (Exception e) {
            log.error(e, e);
            String msg = ServerResourceBundle.getInstance().getString("message.FailedToGetSizeForRepositoryItem",
                new Object[]{id, null});
            throw new RegistryException(ServerResourceBundle.getInstance().getString("message.seeLogsForDetails", new Object[]{msg})); 
        }
        
        return itemSize;
    }        
    
    /**
     * Get the total size an List of repository item in bytes.
     * @param id Unique id for ExtrinsicObject whose repository item size is desired
     * @return 0 if the no items (or items do not exist).
     */
    public long getItemsSize(List ids) throws RegistryException {
        long totalLength = 0;
        for (Iterator it = ids.iterator(); it.hasNext(); ) {
            String id = (String)it.next();
            totalLength = totalLength + getItemSize(id);
        }
        return  totalLength;
    }
    
    
    /**
     * Gets the RepositoryItem as a stream of XML markup given its id.
     *
     * @param id Unique if for ExtrinsicObject whose repository item is desired
     * @return a <code>StreamSource</code> value
     * @exception RegistryException if an error occurs
     */
    public StreamSource getAsStreamSource(String id) throws RegistryException {
        try {
            return new StreamSource(getRepositoryItem(id).getDataHandler().getInputStream());
        } catch (Exception e) {
            throw new RegistryException(e);
        }
    }            
        
    /**
     * Gets the versionName of the latest version of RepositoryItem
     * that matches the specified lid.
     */
    public String getLatestVersionName(ServerRequestContext context, String lid) throws RegistryException {
        String latestVersion = null;
        
        try {
            ResponseOption responseOption = bu.queryFac.createResponseOption();
            responseOption.setReturnComposedObjects(true);
            responseOption.setReturnType(ReturnType.LEAF_CLASS);

            List objectRefs = new ArrayList();
            String sqlQuery = "SELECT eo.* FROM ExtrinsicObject eo WHERE eo.lid= ? AND eo.contentVersionName IS NOT NULL";
            ArrayList queryParams = new ArrayList();
            queryParams.add(lid);
            String tableName = Utility.getInstance().mapTableName("ExtrinsicObject");
            
            List eos = PersistenceManagerFactory.getInstance().
                getPersistenceManager().executeSQLQuery(context,
                sqlQuery,
                queryParams,
                responseOption,
                tableName,
                objectRefs);
                                                
            if (eos.size() == 0) {
                return null;
            }
            
            ExtrinsicObjectType latestEO = null;
            for (Iterator it = eos.iterator(); it.hasNext(); ) {
                if (latestEO == null) {
                    latestEO = (ExtrinsicObjectType)it.next();
                    latestVersion = latestEO.getContentVersionInfo().getVersionName();
                    continue;
                }

                ExtrinsicObjectType next = (ExtrinsicObjectType)it.next();
                String nextVersion = next.getContentVersionInfo().getVersionName();

                if (compareVersions(nextVersion, latestVersion) > 0) {
                    latestEO = next;
                    latestVersion = latestEO.getContentVersionInfo().getVersionName();
                }
            }

        } catch (javax.xml.bind.JAXBException e) {
            throw new RegistryException(e);
        }
        
        return latestVersion;
    }

    /**
     * Compares 2 version Strings, with major/minor versions separated by '.'
     * Example: "1.10"
     *
     * @param v1 String for version 1.
     * @param v2 String for version 2.
     * @return int = 0 if params are equal; int > 0 if 1st is grater than 2nd;
     *         int < 0 if 2st is grater than 1nd.
     */
    private int compareVersions(String v1, String v2) {
        String parts1 [] = v1.split("\\.", 2);
        String parts2 [] = v2.split("\\.", 2);
        
        int iCompare = Integer.parseInt(parts1[0]) - Integer.parseInt(parts2[0]);
        if (iCompare == 0) {
            // equal.. try subversions
            if (parts1.length == 1 && parts2.length == 1) {
                // really equal
                return 0;
            } else if (parts1.length == 1) {
                // other is bigger (v2)
                return -1;
            } else if (parts2.length == 1) {
                // other is bigger (v1)
                return +1;
            } else {
                // try subversions
                return compareVersions(parts1[1], parts2[1]);
            }
        } else {
            return iCompare;
        }
    }
    
    protected RepositoryItemKey getRepositoryItemKey(String id) throws RegistryException {
        RepositoryItemKey key = null;
        ServerRequestContext context = null;
        try {
            context = new ServerRequestContext("AbstractRepositoryManager:getRepositoryItemKey", null);
            //Access control is check in qm.getRepositoryItem using actual request context
            //This internal request context has total access.
            context.setUser(AuthenticationServiceImpl.getInstance().registryOperator);
            
            RegistryObjectType ro = qm.getRegistryObject(context, id, "ExtrinsicObject");
            if (!(ro instanceof ExtrinsicObjectType)) {
                throw new ObjectNotFoundException(id);
            }

            ExtrinsicObjectType eo = (ExtrinsicObjectType)ro;
            if (eo == null) {
                throw new ObjectNotFoundException(id);
            }
            VersionInfoType contentVersionInfo = eo.getContentVersionInfo();
            if (contentVersionInfo == null) {
                // no Repository Item to find for this EO
                throw new RepositoryItemNotFoundException(id,
                                             eo.getVersionInfo().getVersionName());
            }

            key = new RepositoryItemKey(eo.getLid(), contentVersionInfo.getVersionName());
        } finally {
            context.rollback();
        }
        return key;
    }
        
    
}
