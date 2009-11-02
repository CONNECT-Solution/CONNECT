/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/AssociationDAO.java,v 1.29 2006/08/24 20:42:29 farrukh_najmi Exp $
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
import org.oasis.ebxml.registry.bindings.rim.Association;
import org.oasis.ebxml.registry.bindings.rim.AssociationType1;
import org.oasis.ebxml.registry.bindings.rim.ObjectRef;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;
import org.oasis.ebxml.registry.bindings.rim.UserType;


class AssociationDAO extends RegistryObjectDAO {
    private static final Log log = LogFactory.getLog(AssociationDAO.class);

    /**
     * Use this constructor only.
     */
    AssociationDAO(ServerRequestContext context) {
        super(context);
    }
    
    public static String getTableNameStatic() {
        return "Association";
    }

    public String getTableName() {
        return getTableNameStatic();
    }
    
    
    //TODO: Remove association confirmation in spec and replace with ACP
    //TODO: put associationConfirmation here if still in spec
    
    /**
     * Returns the SQL fragment string needed by insert or update statements 
     * within insert or update method of sub-classes. This is done to avoid code
     * duplication.
     */
    protected String getSQLStatementFragment(Object ro) throws RegistryException {

        AssociationType1 ass = (AssociationType1)ro;
            
        String stmtFragment = null;
                
        String srcId = ass.getSourceObject();
        String targetId = ass.getTargetObject();

        String associationType = ass.getAssociationType();

        if (associationType != null) {
            associationType = "'" + associationType + "'";
        }

        UserType sourceOwner = null;
        UserType targetOwner = null;

        //TODO: Illegal workaround until AE is fixed for V3
        UserType user = context.getUser();
        sourceOwner = user;
        targetOwner = user;
        
        if (action == DAO_ACTION_INSERT) {
            stmtFragment = "INSERT INTO Association " +
                super.getSQLStatementFragment(ro) +
                    ", " + associationType + 
                    ",'" + srcId + 
                    "', '" + targetId + 
                    "' ) ";
        }
        else if (action == DAO_ACTION_UPDATE) {
            stmtFragment = "UPDATE Association SET " +
                super.getSQLStatementFragment(ro) +
                    ", associationType=" + associationType + 
                    ", sourceObject='" + srcId + 
                    "', targetObject='" + targetId + 
                    "' WHERE id = '" + ((RegistryObjectType)ro).getId() + "' ";
        }
        else if (action == DAO_ACTION_DELETE) {
            stmtFragment = super.getSQLStatementFragment(ro);
        }
        
        return stmtFragment;
    }
    
    protected void loadObject(Object obj, ResultSet rs) throws RegistryException {
        try {
            if (!(obj instanceof org.oasis.ebxml.registry.bindings.rim.Association)) {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.AssociationExpected",
                        new Object[]{obj}));
            }

            Association ass = (Association) obj;

            super.loadObject( ass, rs);

            String associationType = rs.getString("associationType");
            ass.setAssociationType(associationType);

            String sourceObjectId = rs.getString("sourceObject");

            ObjectRef so = bu.rimFac.createObjectRef();
            so.setId(sourceObjectId);
            context.getObjectRefs().add(so);
            ass.setSourceObject(sourceObjectId);

            String targetObjectId = rs.getString("targetObject");
            ObjectRef to = bu.rimFac.createObjectRef();
            to.setId(targetObjectId);
            context.getObjectRefs().add(to);
            ass.setTargetObject(targetObjectId);
        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        } catch (JAXBException j) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), j);
            throw new RegistryException(j);
        }
    }

    /**
     * Creates an unitialized binding object for the type supported by this DAO.
     */
    Object createObject() throws JAXBException {
        Association obj = bu.rimFac.createAssociation();
        
        return obj;
    }
    
}
