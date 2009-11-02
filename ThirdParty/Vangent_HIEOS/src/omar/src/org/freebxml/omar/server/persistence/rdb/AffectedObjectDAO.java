/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/AffectedObjectDAO.java,v 1.13 2006/08/24 20:42:29 farrukh_najmi Exp $
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
import org.oasis.ebxml.registry.bindings.rim.AuditableEventType;
import org.oasis.ebxml.registry.bindings.rim.ObjectRefType;


/**
 *
 * @author Farrukh S. Najmi
 * @author Adrian Chong
 */
class AffectedObjectDAO extends AbstractDAO {
    private static final Log log = LogFactory.getLog(AffectedObjectDAO.class);

    /**
     * Use this constructor only.
     */
    AffectedObjectDAO(ServerRequestContext context) {
        super(context);
    }
    
    public static String getTableNameStatic() {
        return "AffectedObject";
    }

    public String getTableName() {
        return getTableNameStatic();
    }
                        
    /**
     * Returns the SQL fragment string needed by insert or update statements 
     * within insert or update method of sub-classes. This is done to avoid code
     * duplication.
     */
    protected String getSQLStatementFragment(Object object) throws RegistryException {
        //object must be an ObjectRef for an objects affected by parent event
        ObjectRefType affectedObject = (ObjectRefType)object;
            
        String stmtFragment = null;
        
        
        String id = affectedObject.getId();
        String home = affectedObject.getHome();
        
        if (home != null) {
            home = "'" + home + "'";
        }
        
        String eventId = ((AuditableEventType)parent).getId();
        
        if (action == DAO_ACTION_INSERT) {
            stmtFragment = "INSERT INTO AffectedObject VALUES(" +
                    " '" + id + 
                    "', " + home + 
                    ", '" + eventId + 
                    "' ) ";
        }
        else if (action == DAO_ACTION_UPDATE) {
            throw new RegistryException(ServerResourceBundle.getInstance().getString("message.cannotUpdateComposedObject"));
        }
        else if (action == DAO_ACTION_DELETE) {
            stmtFragment = super.getSQLStatementFragment(object);
        }
        
        return stmtFragment;
    }
    

    protected void loadObject( Object obj, ResultSet rs) throws RegistryException {
        try {
            if (!(obj instanceof org.oasis.ebxml.registry.bindings.rim.ObjectRefType)) {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.ObjectRefTypeExpected",
                        new Object[]{obj}));
            }

            ObjectRefType ro = (ObjectRefType) obj;
            ro.setId(rs.getString("id"));
            
            String home = rs.getString("home");
            if (home != null) {
                ro.setHome(home);
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
        ObjectRefType obj = bu.rimFac.createObjectRef();
        
        return obj;
    }
    
    /*
     * Gets the column name that is foreign key ref into parent table.
     * Must be overridden by derived class if it is not 'parent'
     */
    protected String getParentAttribute() {
        return "eventId";
    }
        
}
