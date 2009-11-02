/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/AdhocQueryDAO.java,v 1.19 2007/07/25 14:56:42 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.persistence.rdb;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freebxml.omar.common.BindingUtility;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.server.common.RegistryProperties;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.oasis.ebxml.registry.bindings.rim.AdhocQuery;
import org.oasis.ebxml.registry.bindings.rim.AdhocQueryType;
import org.oasis.ebxml.registry.bindings.rim.QueryExpressionType;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;

/**
 *
 * @author Farrukh S. Najmi
 * @author Adrian Chong
 */
class AdhocQueryDAO extends RegistryObjectDAO {
    private static final Log log = LogFactory.getLog(AdhocQueryDAO.class);
        
    public static final String DEFAULT_ADHOCQUERY_QUERY_LENGTH = "4096";
    
    public static final int adhocQueryQueryLength = Integer.parseInt(RegistryProperties.getInstance().getProperty(
        "omar.persistence.rdb.adhocQueryQueryLength", 
        DEFAULT_ADHOCQUERY_QUERY_LENGTH));
    
    public static final String QUERY_COL_COLUMN_INFO = "adhocQuery:query";

    /**
     * Use this constructor only.
     */
    AdhocQueryDAO(ServerRequestContext context) {
        super(context);
    }
    
    public static String getTableNameStatic() {
        return "AdhocQuery";
    }

    public String getTableName() {
        return getTableNameStatic();
    }
    
    protected void prepareToInsert(Object object) throws RegistryException {        
        AdhocQueryType ro = (AdhocQueryType)object;
        super.prepareToInsert(object);
        validateQuery(ro);
    }
                
    protected void prepareToUpdate(Object object) throws RegistryException {        
        AdhocQueryType ro = (AdhocQueryType)object;
        super.prepareToUpdate(object);
        validateQuery(ro);
    }
    
    protected void validateQuery(AdhocQueryType object) throws RegistryException {        
        //TODO: Put query syntax validation code here.
    }
                    
    /**
     * Returns the SQL fragment string needed by insert or update statements 
     * within insert or update method of sub-classes. This is done to avoid code
     * duplication.
     */
    protected String getSQLStatementFragment(Object ro) throws RegistryException {

        AdhocQueryType ahq = null;
            
        String stmtFragment = null;
        String query = null;
        String queryLang = null;
                
        if (ro instanceof AdhocQueryType) {
            ahq = (AdhocQueryType)ro;
            
            QueryExpressionType queryExp = ahq.getQueryExpression();
            queryLang = queryExp.getQueryLanguage();
            query = (String)queryExp.getContent().get(0);
            
            /*
            if (queryLang.equals(BindingUtility.CANONICAL_QUERY_LANGUAGE_ID_SQL_92)) {
                query = (String)queryExp.getContent().get(0);
            } else {
                //TODO: filter query persistence
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.noSupportForQueryLanguage",
                        new Object[]{queryLang}));
            }*/
        }
        
        if (action == DAO_ACTION_INSERT) {
            query = spillOverQueryIfNeeded(ahq);
            stmtFragment = "INSERT INTO AdhocQuery " +
                super.getSQLStatementFragment(ro) +
                    ", '" + queryLang + 
                    "', '" + query + 
                    "' ) ";
        }
        else if (action == DAO_ACTION_UPDATE) {
            query = spillOverQueryIfNeeded(ahq);
            stmtFragment = "UPDATE AdhocQuery SET " +
                super.getSQLStatementFragment(ro) +
                    ", query='" + query + 
                    "' WHERE id = '" + ((RegistryObjectType)ro).getId() + "' ";
        }
        else if (action == DAO_ACTION_DELETE) {
            deleteSpillOverQueryIfNeeded(ahq);
            stmtFragment = super.getSQLStatementFragment(ro);
        }
        
        return stmtFragment;
    }
    
    
    private String spillOverQueryIfNeeded(AdhocQueryType ahq) throws RegistryException {
        QueryExpressionType queryExp = ahq.getQueryExpression();
        String query = (String)queryExp.getContent().get(0);
        if (query == null) {
            return query="";
        }
        //Check if query will fit in column size available.                
        if (query.length() > adhocQueryQueryLength) {
            //Need to store query in a repository item and store its id in the query column
            //This introduces a level of indirection which is resolved when the query is read.
            String queryId = ahq.getId();

            query = marshalToRepositoryItem(queryId, QUERY_COL_COLUMN_INFO, query);                    
        }
        
        return query;
    }

    private void deleteSpillOverQueryIfNeeded(AdhocQueryType ahq) throws RegistryException {
        QueryExpressionType queryExp = ahq.getQueryExpression();
        String query = (String)queryExp.getContent().get(0);

        //Check if query will fit in column size available.                
        if (query.length() > adhocQueryQueryLength) {
            removeSpillOverRepositoryItem(ahq.getId(), QUERY_COL_COLUMN_INFO);
        }        
    }
    
    protected void loadObject( Object obj, ResultSet rs) throws RegistryException {
        try {
            //TODO: Add support for filter query type later
            if (!(obj instanceof AdhocQuery)) {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.SQLQueryExpected",
                        new Object[]{obj}));
            }

            AdhocQuery ahq = (AdhocQuery) obj;
            super.loadObject( obj, rs);
            
            String query = rs.getString("query");

            //Check if query is actually a ref to a query stored in a repository item 
            //If so we needs to dereference it to get actual query string
            if (query.startsWith("urn:freebxml:registry:spillOverId:")) {
                //query is actually in a repository item and this is just the id of that repository item
                //Fetch the actual query by dereferencing this id
                
                
                try {
                    query = unmarshallFromRepositoryItem(query);
                    
                    //We escape single quotes in the query with another single quote when storing as VARCHAR. 
                    //JDBC strips the first single quote on read from VARCHAR column
                    //When reading from content of a RepositoryItem replace "''" with "'"
                    query = query.replaceAll("''", "'");
                                        
                } catch (Exception e) {
                    //throw new RegistryException(e);
                    log.error(e, e);
                }
            }
            
            String queryLang = rs.getString("queryLanguage");
            
            QueryExpressionType queryExp = bu.rimFac.createQueryExpression();
            queryExp.setQueryLanguage(queryLang);
            queryExp.getContent().add(query); 
            ahq.setQueryExpression(queryExp);
        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        } catch (JAXBException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        }
    }

    /**
     * Creates an unitialized binding object for the type supported by this DAO.
     */
    Object createObject() throws JAXBException {
        AdhocQuery obj = bu.rimFac.createAdhocQuery();
        
        return obj;
    }
    
}
