/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/repository/RepositoryManager.java,v 1.16 2006/12/19 13:08:09 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.repository;

import java.util.List;

import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

import javax.xml.registry.RegistryException;
import org.freebxml.omar.common.RepositoryItem;
import org.freebxml.omar.server.common.ServerRequestContext;


/**
 * The Repository Manager interface implemented by RepositoryManagers that
 * control inserting, updating and deleting RepositoryItems.
 *
 * @author Adrian Chong
 * @author Peter Burgess
 *
 */
public interface RepositoryManager {
    /**
     * Gets the RepositoryItem as a StreamSource given its key.
     *
     * @param id Unique id for ExtrinsicObject whose RepositoryItem is desired
     * @return a <code>StreamSource</code> value
     * @throws RegistryException if there are any processing errors
     */
    public StreamSource getAsStreamSource(String id) throws RegistryException;

    /**
     * Gets a <code>URIResolver</code> that handles locating RepositoryItems
     * given a URI that represents the id of the ExtrinsicObject for that
     * RepositoryItem.
     *
     * @return an <code>URIResolver</code> value
     */
    public URIResolver getURIResolver();

    /**
     * Insert the RepositoryItem.
     *
     * @param context the ServerRequestContext associated with this request.
     * @param item The RepositoryItem.
     * @throws RegistryException if there are any processing errors
     */
    public void insert(ServerRequestContext context, RepositoryItem item) throws RegistryException;

    /**
     * Returns the RepositoryItem associated with the ExtrinsicObject specified by id.
     *
     * @param id Unique id for ExtrinsicObject whose RepositoryItem is desired.
     * @return the RepositoryItem instance for ExtrinsicObject that matches specified id
     * @throws RegistryException if there are any processing errors
     */
    public RepositoryItem getRepositoryItem(String id)
        throws RegistryException;
    
    /**
     * Delete the RepositoryItem associated with the ExtrinsicObject with specified id.
     * Do not delete RepositoryItem if it is in use by a different version of the ExtrinsicObject.
     *
     * @param id The unique id for ExtrinsicObject associated with RepositoryItem being deleted.
     * @throws RegistryException if the item does not exist
     */
    public void delete(String id) throws RegistryException;
    
    /**
     * Delete the RepositoryItem.
     * @param key Unique key for RepositoryItem being deleted
     * @throws RegistryException if the item does not exist
     */
    public void delete(RepositoryItemKey key) throws RegistryException;

    /**
     * Delete multiple RepositoryItems.
     * @param ids List of ids of ExtrinsicObjects whose RepositoryItems are being deleted.
     * @throws RegistryException if any of the item do not exist
     */
    public void delete(List ids) throws RegistryException;
    
    /**
     * Updates a RepositoryItem.
     *
     * @param context the ServerRequestContext associated with this request.
     * @param item the RepositoryItem being updated
     * @throws RegistryException if there are any processing errors
     */
    public void update(ServerRequestContext context, RepositoryItem item) throws RegistryException;

    /**
     * Return a List of non-existent RepositoryItems
     *
     * @param ids The List of RepositoryItem keys.
     * @return the List of ids that do not have associated RepositoryItems
     * @throws RegistryException if there are any processing errors
     */
    public List itemsExist(List ids) throws RegistryException;

    /**
     * Determines if RepositoryItem exists for specified key. 
     *
     * @return true if a RepositoryItem exists for specified key, false otherwise 
     * @param key The RepositoryItemKey.
     * @throws RegistryException if there are any processing errors
     */
    public boolean itemExists(RepositoryItemKey key) throws RegistryException;

    /**
     * Get the size in of a RepositoryItem in bytes.
     *
     * @param id Unique id for ExtrinsicObject whose RepositoryItem size is desired
     * @return the size in bytes for the RepositoryItem or 0 if it does not exists.
     *    TODO: Need to throw RepoositoryItemNotFoundException rather than retirn 0 in future.
     * @throws RegistryException if there are any processing errors
     */
    public long getItemSize(String id) throws RegistryException;

    /**
     * Get the total size an List of RepositoryItem in bytes.
     * @param ids List of Unique id for ExtrinsicObject whose RepositoryItem size is desired
     * @return the total size of RepositoryItems that matches specified ids. 
     *   Ignores RepositoryItems that were not found.
     * @throws RegistryException if there are any processing errors
     */
    public long getItemsSize(List ids) throws RegistryException;
    
    /**
     * Gets the versionName of the latest version of RepositoryItem
     * that matches the specified lid.
     *
     * @param context the ServerRequestContext associated with this request.
     * @param lid The lid for an ExtrinsicObject.
     * @throws RegistryException if there are any processing errors
     */
    public String getLatestVersionName(ServerRequestContext context, String lid) throws RegistryException;
    
}
