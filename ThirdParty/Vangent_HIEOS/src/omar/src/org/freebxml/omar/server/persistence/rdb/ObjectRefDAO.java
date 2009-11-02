/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/ObjectRefDAO.java,v 1.10 2006/08/24 20:42:29 farrukh_najmi Exp $
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
import org.oasis.ebxml.registry.bindings.rim.IdentifiableType;
import org.oasis.ebxml.registry.bindings.rim.ObjectRef;
import org.oasis.ebxml.registry.bindings.rim.ObjectRefType;


/**
 *
 * @author Farrukh S. Najmi
 * @author Adrian Chong
 */
class ObjectRefDAO extends IdentifiableDAO {
    private static final Log log = LogFactory.getLog(ObjectRefDAO.class);

    /**
     * Use this constructor only.
     */
    ObjectRefDAO(ServerRequestContext context) {
        super(context);
    }
    
    public static String getTableNameStatic() {
        return "ObjectRef";
    }

    public String getTableName() {
        return getTableNameStatic();
    }
                        
    /**
     * Returns the SQL fragment string needed by insert or update statements
     * within insert or update method of sub-classes. This is done to avoid code
     * duplication.
     */
    protected String getSQLStatementFragment(Object object)
    throws RegistryException {
        
        ObjectRefType objectRef = (ObjectRefType)object;
        
        String stmtFragment = super.getSQLStatementFragment(object);                
                
        if (action == DAO_ACTION_INSERT) {
            stmtFragment =
            " INSERT INTO ObjectRef " +
            super.getSQLStatementFragment(object) + " )";
        }
        else if (action == DAO_ACTION_UPDATE) {
            stmtFragment = "UPDATE ObjectRef SET +" +
            super.getSQLStatementFragment(object) +
            "' WHERE id = '" + ((IdentifiableType)object).getId() + "' ";
            
        }
        else if (action == DAO_ACTION_DELETE) {
        }
        
        return stmtFragment;
    }
    
    
    protected void loadObject( Object obj, ResultSet rs) throws RegistryException {
        try {
            if (!(obj instanceof org.oasis.ebxml.registry.bindings.rim.ObjectRef)) {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.ObjectRefTypeExpected",
                        new Object[]{obj}));
            }

            ObjectRef objectRef = (ObjectRef) obj;

            String id = rs.getString("id");
            objectRef.setId(id);
            
            String home = rs.getString("home");
            if (home != null) {
                objectRef.setHome(home);
            }
        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        }
    }

    /**
     * Creates an unitialized binding object for the type supported by this DAO.
     */
    Object createObject() throws JAXBException {
        ObjectRef obj = bu.rimFac.createObjectRef();
        
        return obj;
    }
}
