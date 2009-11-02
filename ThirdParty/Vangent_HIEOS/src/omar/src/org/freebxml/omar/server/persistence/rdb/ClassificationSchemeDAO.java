/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/ClassificationSchemeDAO.java,v 1.33 2006/08/24 20:42:29 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.persistence.rdb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.oasis.ebxml.registry.bindings.rim.ClassificationScheme;
import org.oasis.ebxml.registry.bindings.rim.ClassificationSchemeType;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;


class ClassificationSchemeDAO extends RegistryObjectDAO {
    private static final Log log = LogFactory.getLog(ClassificationSchemeDAO.class);
    
    /**
     * Use this constructor only.
     */
    ClassificationSchemeDAO(ServerRequestContext context) {
        super(context);
    }
    
    public static String getTableNameStatic() {
        return "ClassScheme";
    }

    public String getTableName() {
        return getTableNameStatic();
    }

    
    protected void deleteComposedObjects(Object object)  throws RegistryException {
        ClassificationSchemeType ro = (ClassificationSchemeType)object;
            
        super.deleteComposedObjects(ro);
        
        ArrayList parentIds = new ArrayList();
        parentIds.add(ro.getId());

    }
    
    protected void insertComposedObjects(Object object)  throws RegistryException {
            
        super.insertComposedObjects(object);
        
        ClassificationSchemeType scheme = (ClassificationSchemeType)object;
        
        ClassificationNodeDAO classificationNodeDAO = new ClassificationNodeDAO(context);
        classificationNodeDAO.setParent(scheme);
        classificationNodeDAO.insert(scheme.getClassificationNode());        
    }
    
    
    /**
     * Returns the SQL fragment string needed by insert or update statements 
     * within insert or update method of sub-classes. This is done to avoid code
     * duplication.
     */
    protected String getSQLStatementFragment(Object ro) throws RegistryException {

        ClassificationSchemeType scheme = (ClassificationSchemeType)ro;
            
        String stmtFragment = null;
               
        String isInternal = "'F'";

        if (scheme.isIsInternal()) {
            isInternal = "'T'";
        }

        String nodeType = scheme.getNodeType().toString();

        if (nodeType == null) {
            nodeType = "UniqueCode";
        }

        if (action == DAO_ACTION_INSERT) {
            stmtFragment = "INSERT INTO ClassScheme " +
                super.getSQLStatementFragment(ro) +
                    ", " + isInternal + ", '" + nodeType + 
                    "' ) ";            
        }
        else if (action == DAO_ACTION_UPDATE) {
            stmtFragment = "UPDATE ClassScheme SET " +
                super.getSQLStatementFragment(ro) +
                    ", isInternal=" + isInternal + 
                    ", nodeType='" + nodeType + 
                    "' WHERE id = '" + ((RegistryObjectType)ro).getId() + "' ";
        }
        else if (action == DAO_ACTION_DELETE) {
            stmtFragment = super.getSQLStatementFragment(ro);
        }
        
        return stmtFragment;
    }
    

    // Should we make index on ClassifcationScheme in Classification????
    protected String checkClassificationReferences(java.sql.Connection conn,
        String schemeId) throws RegistryException {
        String classId = null;
        PreparedStatement stmt = null;

        try {
            String sql = "SELECT id FROM Classification WHERE " +
                "classificationScheme=? AND classificationScheme IS NOT NULL";
            stmt = context.getConnection().prepareStatement(sql);
            stmt.setString(1, schemeId);
            log.trace("SQL = " + sql);  // HIEOS/BHT: (DEBUG)
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                classId = rs.getString(1);
            }

            return classId;
        } catch (SQLException e) {
            throw new RegistryException(e);
        } finally {
            closeStatement(stmt);
        }
    }

    // Should we make index on parent in ClassificationNode????
    protected String checkClassificationNodeReferences(
        java.sql.Connection conn, String schemeId) throws RegistryException {
        String nodeId = null;
        PreparedStatement stmt = null;

        try {
            String sql = "SELECT id FROM ClassificationNode WHERE parent=? AND parent IS NOT NULL";
            stmt = context.getConnection().prepareStatement(sql);
            stmt.setString(1, schemeId);
            log.trace("SQL = " + sql);  // HIEOS/BHT: (DEBUG)
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nodeId = rs.getString(1);
            }

            return nodeId;
        } catch (SQLException e) {
            throw new RegistryException(e);
        } finally {
            closeStatement(stmt);
        }
    }

    protected void loadObject( Object obj, ResultSet rs) throws RegistryException {
        try {
            if (!(obj instanceof org.oasis.ebxml.registry.bindings.rim.ClassificationScheme)) {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.ClassficationSchemeExpected",
                        new Object[]{obj}));
            }

            ClassificationScheme scheme = (ClassificationScheme) obj;
            super.loadObject(scheme, rs);

            String isInternal = rs.getString("isInternal");

            if (isInternal.equals("T")) {
                scheme.setIsInternal(true);
            } else {
                scheme.setIsInternal(false);
            }

            String nodeType = rs.getString("nodeType");
            scheme.setNodeType(nodeType);
        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        }
    }

    /**
     * Creates an unitialized binding object for the type supported by this DAO.
     */
    Object createObject() throws JAXBException {
        ClassificationScheme obj = bu.rimFac.createClassificationScheme();
        
        return obj;
    }
    
}
