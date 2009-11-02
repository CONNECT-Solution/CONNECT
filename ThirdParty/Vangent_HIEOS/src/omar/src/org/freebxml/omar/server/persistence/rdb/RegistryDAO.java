/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/RegistryDAO.java,v 1.8 2006/08/24 20:42:29 farrukh_najmi Exp $
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
import org.oasis.ebxml.registry.bindings.rim.Registry;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;
import org.oasis.ebxml.registry.bindings.rim.RegistryType;


/**
 * An Registry instance represents an ebXML Registry.
 * Every ebXML Registry must have a Registry instance 
 * describing itself.
 *
 * @author Farrukh S. Najmi
 * 
 */
class RegistryDAO extends RegistryObjectDAO {
    private static final Log log = LogFactory.getLog(RegistryDAO.class);

    /**
     * Use this constructor only.
     */
    RegistryDAO(ServerRequestContext context) {
        super(context);
    }
    
    public static String getTableNameStatic() {
        return "Registry";
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

        RegistryType  registry = ( RegistryType)ro;
            
        String stmtFragment = null;
        
        String catalogingSyncLatency = registry.getCatalogingLatency();                
        String operator = registry.getOperator();

        String replicationSyncLatency = registry.getReplicationSyncLatency();
        String specificationVersion = registry.getSpecificationVersion();
        String conformanceProfile = registry.getConformanceProfile();
                       
        if (action == DAO_ACTION_INSERT) {
            stmtFragment = "INSERT INTO Registry " +
                super.getSQLStatementFragment(ro) +
                    ", '" +  catalogingSyncLatency + 
                    "', '" + conformanceProfile +                     
                    "', '" + operator + 
                    "', '" + replicationSyncLatency + 
                    "', '" + specificationVersion + 
                    "' ) ";            
        }
        else if (action == DAO_ACTION_UPDATE) {
            stmtFragment = "UPDATE Registry SET " +
                super.getSQLStatementFragment(ro) +
                    ",  catalogingSyncLatency='" +  catalogingSyncLatency +
                    "', conformanceProfile='" + conformanceProfile +                     
                    "', operator='" + operator + 
                    "', replicationSyncLatency='" + replicationSyncLatency + 
                    "', specificationVersion='" + specificationVersion + 
                    "' WHERE id = '" + ((RegistryObjectType)ro).getId() + "' ";
        }
        else if (action == DAO_ACTION_DELETE) {
            stmtFragment = super.getSQLStatementFragment(ro);
        }
        
        return stmtFragment;
    }
    
    protected void loadObject( Object obj, ResultSet rs) throws RegistryException {
        try {
            if (!(obj instanceof org.oasis.ebxml.registry.bindings.rim.Registry)) {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.RegistryTypeExpected",
                        new Object[]{obj}));
            }

            Registry registry = (Registry) obj;
            super.loadObject(obj, rs);

            String catalogingSyncLatency = rs.getString("catalogingSyncLatency");
            registry.setCatalogingLatency(catalogingSyncLatency);
                
            String conformanceProfile = rs.getString("conformanceProfile");
            registry.setConformanceProfile(conformanceProfile);

            String operator = rs.getString("operator");
            registry.setOperator(operator);

            String replicationSyncLatency = rs.getString("replicationSyncLatency");
            registry.setReplicationSyncLatency(replicationSyncLatency);
            
            String specificationVersion = rs.getString("specificationVersion");
            registry.setSpecificationVersion(specificationVersion);            
        
        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        } 
    }

    /**
     * Creates an unitialized binding object for the type supported by this DAO.
     */
    Object createObject() throws JAXBException {
        Registry obj = bu.rimFac.createRegistry();
        
        return obj;
    }
}
