/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/ClassificationDAO.java,v 1.26 2006/08/24 20:42:29 farrukh_najmi Exp $
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
import org.oasis.ebxml.registry.bindings.rim.Classification;
import org.oasis.ebxml.registry.bindings.rim.ClassificationType;
import org.oasis.ebxml.registry.bindings.rim.ObjectRef;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;


class ClassificationDAO extends RegistryObjectDAO {
    private static final Log log = LogFactory.getLog(ClassificationDAO.class);

    /**
     * Use this constructor only.
     */
    ClassificationDAO(ServerRequestContext context) {
        super(context);
    }
    
    public static String getTableNameStatic() {
        return "Classification";
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

        ClassificationType classification = (ClassificationType)ro;
            
        String stmtFragment = null;
               
        String classificationNodeId = classification.getClassificationNode();

        if (classificationNodeId != null) {
            classificationNodeId = "'" + classificationNodeId + "'";
        }

        String schemeId = classification.getClassificationScheme();

        if (schemeId != null) {
            schemeId = "'" + schemeId + "'";
        }

        String classifiedObjectId = classification.getClassifiedObject();

        String nodeRep = classification.getNodeRepresentation();

        if (nodeRep != null) {
            nodeRep = "'" + nodeRep + "'";
        }
        
        if (action == DAO_ACTION_INSERT) {
            stmtFragment = "INSERT INTO Classification " +
                super.getSQLStatementFragment(ro) +
                    ", " + classificationNodeId + ", " + schemeId + 
                    ", '" + classifiedObjectId + 
                    "', " + nodeRep +
                    " ) ";            
        }
        else if (action == DAO_ACTION_UPDATE) {
            stmtFragment = "UPDATE Classification SET " +
                super.getSQLStatementFragment(ro) +
                    ", classificationNode=" + classificationNodeId + 
                    ", classificationScheme=" + schemeId + 
                    ", classifiedObject='" + classifiedObjectId + 
                    "', nodeRepresentation=" + nodeRep +                     
                    " WHERE id = '" + ((RegistryObjectType)ro).getId() + "' ";
        }
        else if (action == DAO_ACTION_DELETE) {
            stmtFragment = super.getSQLStatementFragment(ro);
        }
        
        return stmtFragment;
    }
    
    /*
     * Gets the column name that is foreign key ref into parent table.
     * Must be overridden by derived class if it is not 'parent'
     */
    protected String getParentAttribute() {
        return "classifiedObject";
    }
    
    protected void loadObject( Object obj, ResultSet rs) throws RegistryException {
        try {
            if (!(obj instanceof org.oasis.ebxml.registry.bindings.rim.Classification)) {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.ClassificationExpected",
                        new Object[]{obj}));
            }

            Classification cl = (Classification) obj;
            super.loadObject( cl, rs);

            ObjectRef or = null;

            String classificationNodeId = rs.getString("classificationNode");

            if (classificationNodeId != null) {
                or = bu.rimFac.createObjectRef();
                or.setId(classificationNodeId);
                context.getObjectRefs().add(or);
                cl.setClassificationNode(classificationNodeId);
            }

            String classificationSchemeId = rs.getString("classificationScheme");

            if (classificationSchemeId != null) {
                or = bu.rimFac.createObjectRef();
                or.setId(classificationSchemeId);
                context.getObjectRefs().add(or);
                cl.setClassificationScheme(classificationSchemeId);
            }

            String classifiedObjectId = rs.getString("classifiedObject");

            or = bu.rimFac.createObjectRef();
            or.setId(classifiedObjectId);
            context.getObjectRefs().add(or);
            cl.setClassifiedObject(classifiedObjectId);

            String nodeRep = rs.getString("nodeRepresentation");
            cl.setNodeRepresentation(nodeRep);
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
        Classification obj = bu.rimFac.createClassification();
        
        return obj;
    }
    
}
