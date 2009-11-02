/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/PersistenceManager.java,v 1.21 2006/07/26 17:25:25 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.persistence;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.xml.registry.RegistryException;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.oasis.ebxml.registry.bindings.query.ResponseOptionType;
import org.oasis.ebxml.registry.bindings.rim.ObjectRefType;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;
import org.freebxml.omar.common.IterativeQueryParams;


/**
 * Interface exposed by all PersistenceManagers.
 * This is the contract implemented by the persistence layer of the
 * registry architecture.
 *
 * @author Farrukh Najmi
 */
public interface PersistenceManager {
    /**
     * Does a bulk insert of a heterogeneous Collection of RegistryObjects.
     *
     */
    public void insert(ServerRequestContext context, List registryObjects)
        throws RegistryException;

    /**
     * Does a bulk update of a heterogeneous Collection of RegistryObjects.
     *
     */
    public void update(ServerRequestContext context, List registryObjects)
        throws RegistryException;

    /**
     * Update the status of specified objects to the specified status.
     *
     */
    public void updateStatus(ServerRequestContext context, List registryObjectsIds,
        String status) throws RegistryException;

    /**
     * Does a bulk delete of a Collection of ObjectRefTypes.
     *
     */
    public void delete(ServerRequestContext context, List objectRefs)
        throws RegistryException;
    
    /**
     * Updates the idToLidMap in context entries with RegistryObject id as Key and RegistryObject lid as value 
     * for each object that matches specified id.
     *
     */    
    public void updateIdToLidMap(ServerRequestContext context, Set ids, String tableName) throws RegistryException;       
    
    /**
     * Checks each object being deleted to make sure that it does not have any currently existing references.
     *
     * @throws ReferencesExistException if references exist to any of the RegistryObject ids specified in roIds
     */
    public void checkIfReferencesExist(ServerRequestContext context, List roIds) throws RegistryException;
    
    /**
     * Gets the specified object using specified id and className
     *
     */
    public RegistryObjectType getRegistryObject(ServerRequestContext context, String id, String className)
        throws RegistryException;

    /**
     * Gets the specified object using specified ObjectRef
     *
     */
    public RegistryObjectType getRegistryObject(ServerRequestContext context, ObjectRefType ref)
        throws RegistryException;
    
    /**
     * Executes and SQL query using specified parameters.
     *
     * @return An List of RegistryObjectType instances
     */
    public List executeSQLQuery(ServerRequestContext context, String sqlQuery,
        ResponseOptionType responseOption, String tableName, List objectRefs)
        throws RegistryException;
    
    /**
     * Executes and SQL query using specified parameters.
     *
     * @return An List of RegistryObjectType instances
     */
    public List executeSQLQuery(ServerRequestContext context, String sqlQuery,
        ResponseOptionType responseOption, String tableName, List objectRefs,
        IterativeQueryParams paramHolder)
        throws RegistryException;
    

    /**
     * Executes an SQL Query.
     */
    public List executeSQLQuery(ServerRequestContext context, String sqlQuery, List queryParams,
        ResponseOptionType responseOption, String tableName, List objectRefs)
        throws RegistryException;
    
    /**
     * Executes and SQL query using specified parameters.
     * This variant is used to invoke stored queries.
     *
     * @return An List of RegistryObjectType instances
     */
    public List executeSQLQuery(ServerRequestContext context, String parametrizedQuery, List queryParams,
        ResponseOptionType responseOption, String tableName, List objectRefs,
        IterativeQueryParams paramHolder)
        throws RegistryException;
    
    /**
    *     Get a HashMap with registry object id as key and owner id as value
    */
    public HashMap getOwnersMap(ServerRequestContext context, List ids) throws RegistryException;
    
    /**
     * Sets the owner on the specified objects based upon RequestContext.
     */
    public void changeOwner(ServerRequestContext context, List objects) throws RegistryException;
    
    /**
     * Gets a JDBC Connection.
     */
    public Connection getConnection(ServerRequestContext context) throws RegistryException;
    
    /**
     * Releases or relinqueshes a JDBC connection.
     */
    public void releaseConnection(ServerRequestContext context, Connection connection) throws RegistryException;
}
