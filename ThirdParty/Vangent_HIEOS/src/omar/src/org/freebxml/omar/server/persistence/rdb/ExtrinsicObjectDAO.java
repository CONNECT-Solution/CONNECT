/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/ExtrinsicObjectDAO.java,v 1.33 2006/08/24 20:42:29 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.persistence.rdb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freebxml.omar.common.BindingUtility;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.common.RepositoryItem;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.repository.RepositoryItemKey;
import org.freebxml.omar.server.repository.RepositoryManager;
import org.freebxml.omar.server.repository.RepositoryManagerFactory;
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.oasis.ebxml.registry.bindings.rim.ExtrinsicObject;
import org.oasis.ebxml.registry.bindings.rim.ExtrinsicObjectType;
import org.oasis.ebxml.registry.bindings.rim.VersionInfoType;

class ExtrinsicObjectDAO extends RegistryObjectDAO {
    private static final Log log = LogFactory.getLog(ExtrinsicObjectDAO.class);

    RepositoryManager rm = RepositoryManagerFactory.getInstance()
                                                   .getRepositoryManager();
    /**
     * Use this constructor only.
     */
    ExtrinsicObjectDAO(ServerRequestContext context) {
        super(context);
    }
    
    public static String getTableNameStatic() {
        return "ExtrinsicObject";
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

        ExtrinsicObjectType extrinsicObject = (ExtrinsicObjectType)ro;
        String id = extrinsicObject.getId();
            
        String stmtFragment = null;
        String isOpaque = "'F'";

        if (extrinsicObject.isIsOpaque()) {
            isOpaque = "'T'";
        }

        String mimeType = extrinsicObject.getMimeType();

        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        String contentVersionName = null;
        String contentVersionComment = null;
        
        //Set contentVersion only if RI submitted or if contentVersion
        //matches an existing RI version for same lid
        //
        //Note that contentVersion must be honoured if specified and matches an existing RO version
        //even when there is no RepositoryItem since the new EO
        //could reference an existing RO version.
        
        VersionInfoType contentVersionInfo = extrinsicObject.getContentVersionInfo();
        if (contentVersionInfo != null) {
            contentVersionName = contentVersionInfo.getVersionName();
            contentVersionComment = contentVersionInfo.getComment();
        }
        
        RepositoryItem roNew = (RepositoryItem)context.getRepositoryItemsMap().get(id);
        boolean setContentVersionInfo = false;
        if (roNew == null) {
            //Check if existing RI with same lid and contentVersion
            if ((contentVersionName != null) && (contentVersionName.length() > 0)) {
                RepositoryItemKey key = new RepositoryItemKey(extrinsicObject.getLid(), contentVersionName);
                setContentVersionInfo = rm.itemExists(key);
            }
        } else {
            setContentVersionInfo = true;
        }
        
        if (setContentVersionInfo) {
            if ((contentVersionName != null) && (contentVersionName.length() > 0)) {
                contentVersionName = "'" + contentVersionName + "'";
            } else {
                contentVersionName = null;
            }

            if ((contentVersionComment != null) && (contentVersionComment.length() > 0)) {
                contentVersionComment = "'" + contentVersionComment + "'";
            } else {
                contentVersionComment = null;
            }
        } else {
            contentVersionName = null;
            contentVersionComment = null;
        }
        
        
        if (action == DAO_ACTION_INSERT) {
            stmtFragment = "INSERT INTO ExtrinsicObject " +
                super.getSQLStatementFragment(ro) +
                    ", " + isOpaque + 
                    ", '" + mimeType + 
                    "', " + contentVersionName + 
                    ", " + contentVersionComment +
                    " ) ";
        }
        else if (action == DAO_ACTION_UPDATE) {
            stmtFragment = "UPDATE ExtrinsicObject SET " +
                super.getSQLStatementFragment(ro) +
                    ", isOpaque=" + isOpaque + 
                    ", mimeType='" + mimeType + 
                    "', contentVersionName=" + contentVersionName +
                    ", contentVersionComment=" + contentVersionComment +
                    " WHERE id = '" + id + "' ";
        }
        else if (action == DAO_ACTION_DELETE) {
            stmtFragment = super.getSQLStatementFragment(ro);
        }
        
        return stmtFragment;
    }
    

    protected void loadObject( Object obj, ResultSet rs) throws RegistryException {
        try {
            if (!(obj instanceof org.oasis.ebxml.registry.bindings.rim.ExtrinsicObject)) {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.ExtrinsicObjectExpected",
                        new Object[]{obj}));
            }
            
            ExtrinsicObject eo = (ExtrinsicObject) obj;
            super.loadObject( obj, rs);

            String isOpaque = rs.getString("isOpaque");

            if (isOpaque.equals("T")) {
                eo.setIsOpaque(true);
            } else {
                eo.setIsOpaque(false);
            }

            String mimeType = rs.getString("mimeType");
            eo.setMimeType(mimeType);
            
            //Now set contentVersionInfo if either contentComment and contentVersionName are non-null
            //Make sure to not set contentVersionInfo if both contentComment and contentVersionName are null
            VersionInfoType contentVersionInfo = BindingUtility.getInstance().rimFac.createVersionInfoType();
            String contentVersionName = rs.getString("contentVersionName");
            String contentComment = rs.getString("contentVersionComment");            
            
            if ((contentVersionName != null) || (contentComment != null)) {
                if (contentVersionName != null) {
                    contentVersionInfo.setVersionName(contentVersionName);
                }

                if (contentComment != null) {
                    contentVersionInfo.setComment(contentComment);
                }
                eo.setContentVersionInfo(contentVersionInfo);
            }
            
        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        } catch (javax.xml.bind.JAXBException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        }
    }

    /**
     * Creates an unitialized binding object for the type supported by this DAO.
     */
    Object createObject() throws JAXBException {
        ExtrinsicObject obj = bu.rimFac.createExtrinsicObject();
        
        return obj;
    }

}
