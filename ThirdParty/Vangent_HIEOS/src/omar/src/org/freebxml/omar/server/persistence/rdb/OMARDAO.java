/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/OMARDAO.java,v 1.5 2005/02/23 22:57:43 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.persistence.rdb;

import java.sql.ResultSet;
import java.util.List;

import javax.xml.registry.RegistryException;

/**
 * Interface for all DAOs in OMAR server.
 *
 * @author Farrukh S. Najmi
 */
interface OMARDAO {

    /**
     * Does a bulk delete of a Collection of objects identified
     * by registryObjectIds.
     *
     * @param registryObjectIds the ids for objects to delete
     * @throws RegistryException
     */
    public void delete(List registryObjectIds) throws RegistryException;

    /**
     * Gets the List of Objects for specified ResultSet.
     * The ResultSet is the result of a query.
     *
     * @param rs The JDBC ResultSet specifying the rows for desired objects
     * @return the List of objects matching the ResultSet.
     * @throws RegistryException
     */
    public List getObjects(ResultSet rs, int startIndex, int maxResults) throws RegistryException;

    /**
     * Gets the name of the table in relational schema for this DAO.
     *
     * @return the table name
     */
    public String getTableName();

    /**
     * Does a bulk insert of a Collection of objects.
     *
     * @param registryObjectIds the ids for objects to delete
     * 
     * @throws RegistryException
     */
    public void insert(List registryObjects) throws RegistryException;

    /**
     * Does a bulk update of a Collection of objects that match the type for this DAO.
     *
     * @param registryObjectIds the ids for objects to delete
     * 
     * @throws RegistryException
     */
    public void update(List registryObjects) throws RegistryException;

    // HIEOS/BHT (ADDED): MySQL View bug workaround.
    public String getSQLStatementFragmentForMirrorImage(Object obj) throws RegistryException;
}
