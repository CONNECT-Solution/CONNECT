/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/FederationDAO.java,v 1.13 2006/08/24 20:42:29 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.persistence.rdb;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.oasis.ebxml.registry.bindings.rim.Federation;
import org.oasis.ebxml.registry.bindings.rim.FederationType;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;


class FederationDAO extends RegistryObjectDAO {
    private static final Log log = LogFactory.getLog(FederationDAO.class);

    /**
     * @link
     * @shapeType PatternLink
     * @pattern Singleton
     * @supplierRole Singleton factory
     */


    /**
     * Use this constructor only.
     */
    FederationDAO(ServerRequestContext context) {
        super(context);
    }
    
    public static String getTableNameStatic() {
        return "Federation";
    }

    public String getTableName() {
        return getTableNameStatic();
    }

    
    /**
     * Returns the SQL fragment string needed by insert or update statements 
     * within insert or update method of sub-classes. This is done to avoid code
     * duplication.
     */
    protected String getSQLStatementFragment(Object ro) throws RegistryException {

        FederationType federation = (FederationType)ro;
            
        String stmtFragment = null;
               
        String replicationSyncLatency = federation.getReplicationSyncLatency();
        
        //TODO: Need to persist Federation Members
                
        if (action == DAO_ACTION_INSERT) {
            stmtFragment = "INSERT INTO Federation " +
                super.getSQLStatementFragment(ro) +
                    ", '" + replicationSyncLatency + 
                    "' ) ";            
        }
        else if (action == DAO_ACTION_UPDATE) {
            stmtFragment = "UPDATE Federation SET " +
                super.getSQLStatementFragment(ro) +
                    ", replicationSyncLatency='" + replicationSyncLatency + 
                    "' WHERE id = '" + ((RegistryObjectType)ro).getId() + "' ";
        }
        else if (action == DAO_ACTION_DELETE) {
            stmtFragment = super.getSQLStatementFragment(ro);
        }
        
        return stmtFragment;
    }
    
    protected void loadObject( Object obj, ResultSet rs) throws RegistryException {
        try {
            if (!(obj instanceof Federation)) {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.FederationExpected",
                        new Object[]{obj}));
            }

            Federation federation = (Federation) obj;
            super.loadObject( obj, rs);
            
            String replicationSyncLatency = rs.getString("replicationSyncLatency");
            federation.setReplicationSyncLatency(replicationSyncLatency);

        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        }
    }

    /**
     * Creates an unitialized binding object for the type supported by this DAO.
     */
    Object createObject() throws JAXBException {
        Federation obj = bu.rimFac.createFederation();
        
        return obj;
    }
    
}
