/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/AuditableEventDAO.java,v 1.30 2006/08/24 20:42:29 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.persistence.rdb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freebxml.omar.common.BindingUtility;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.oasis.ebxml.registry.bindings.rim.AuditableEvent;
import org.oasis.ebxml.registry.bindings.rim.AuditableEventType;
import org.oasis.ebxml.registry.bindings.rim.ObjectRef;
import org.oasis.ebxml.registry.bindings.rim.ObjectRefListType;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;
import org.oasis.ebxml.registry.bindings.rim.UserType;


class AuditableEventDAO extends RegistryObjectDAO {
    private static final Log log = LogFactory.getLog(AuditableEventDAO.class);

    /**
     * @link
     * @shapeType PatternLink
     * @pattern Singleton
     * @supplierRole Singleton factory
     */


    /**
     * Use this constructor only.
     */
    AuditableEventDAO(ServerRequestContext context) {
        super(context);
    }
    
    public static String getTableNameStatic() {
        return "AuditableEvent";
    }

    public String getTableName() {
        return getTableNameStatic();
    }

    /**
     * Delete composed objects that have the specified registryObject
     * as parent.
     */
    protected void deleteComposedObjects(Object object) throws RegistryException {
        super.deleteComposedObjects(object);
        AuditableEventType ae = (AuditableEventType)object;
        
        AffectedObjectDAO affectedObjectDAO = new AffectedObjectDAO(context);
        affectedObjectDAO.setParent(object);        
        
        //Delete affectedObjects
        affectedObjectDAO.deleteByParent();
        
    }
    
    /**
     * Insert the composed objects for the specified registryObject
     */
    protected void insertComposedObjects(Object object) throws RegistryException {
        super.insertComposedObjects(object);
        AuditableEventType ae = (AuditableEventType)object;
        String id = ae.getId();
        
        
        AffectedObjectDAO affectedObjectDAO = new AffectedObjectDAO(context);
        affectedObjectDAO.setParent(object);        
        List affectedObjects = ae.getAffectedObjects().getObjectRef();
        
        //Insert affectedObjects
        affectedObjectDAO.insert(affectedObjects);        
    }
    
    /**
     * Returns the SQL fragment string needed by insert or update statements 
     * within insert or update method of sub-classes. This is done to avoid code
     * duplication.
     */
    protected String getSQLStatementFragment(Object ro) throws RegistryException {

        AuditableEventType auditableEvent = (AuditableEventType)ro;
            
        String stmtFragment = null;
               
        String requestId = auditableEvent.getRequestId();
        String eventType = auditableEvent.getEventType();
        
        if (auditableEvent.getTimestamp() == null) {
            Calendar timeNow = Calendar.getInstance();
            auditableEvent.setTimestamp(timeNow);
        }
        Timestamp timestamp = new Timestamp(auditableEvent.getTimestamp()
                                                          .getTimeInMillis());

        //??The timestamp is being truncated to work around a bug in PostgreSQL 7.2.2 JDBC driver
        String timestampStr = timestamp.toString().substring(0, 19);
        
        String aeUser = auditableEvent.getUser();
        if (aeUser == null) {
            UserType user = context.getUser();
            if (user != null) {
                aeUser = user.getId();
            }
        }
        
        if (action == DAO_ACTION_INSERT) {
            stmtFragment = "INSERT INTO AuditableEvent " +
                super.getSQLStatementFragment(ro) +
                    ", '" + requestId + 
                    "', '" + eventType + 
                    "', '" + timestampStr + 
                    "', '" + aeUser + 
                    "' ) ";            
        }
        else if (action == DAO_ACTION_UPDATE) {
            stmtFragment = "UPDATE AuditableEvent SET " +
                super.getSQLStatementFragment(ro) +
                    ", requestId='" + requestId + 
                    "', eventType='" + eventType + 
                    "', timestamp_='" + timestampStr + 
                    "', user_='" + aeUser + 
                    "' WHERE id = '" + ((RegistryObjectType)ro).getId() + "' ";
        }
        else if (action == DAO_ACTION_DELETE) {
            stmtFragment = super.getSQLStatementFragment(ro);
        }
        
        return stmtFragment;
    }
    
    protected void loadObject( Object obj, ResultSet rs) throws RegistryException {
        try {
            if (!(obj instanceof AuditableEvent)) {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.AuditableEventExpected",
                        new Object[]{obj}));
            }

            AuditableEvent ae = (AuditableEvent) obj;
            super.loadObject( obj, rs);

            //TODO: Fix so requestId is properly supported
            String requestId = rs.getString("requestId");
            if (requestId == null) {
                requestId = "Unknown";
            }
            ae.setRequestId(requestId);
            
            String eventType = rs.getString("eventType");
            ae.setEventType(eventType);
            
            //Workaround for bug in PostgreSQL 7.2.2 JDBC driver
            //java.sql.Timestamp timeStamp = rs.getTimestamp("timeStamp_"); --old
            String timestampStr = rs.getString("timeStamp_").substring(0,19);
            Timestamp timeStamp = Timestamp.valueOf(timestampStr);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(timeStamp.getTime());
            ae.setTimestamp(cal);
            
            String userId = rs.getString("user_");
            ObjectRef ref = bu.rimFac.createObjectRef();
            ref.setId(userId);
            context.getObjectRefs().add(ref);
            ae.setUser(userId);
            
            AffectedObjectDAO affectedDAO = new AffectedObjectDAO(context);
            affectedDAO.setParent(ae);
            List affectedObjects = affectedDAO.getByParent();
            ObjectRefListType orefList = BindingUtility.getInstance().rimFac.createObjectRefListType();
            orefList.getObjectRef().addAll(affectedObjects);
            ae.setAffectedObjects(orefList);
            
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
        AuditableEvent obj = bu.rimFac.createAuditableEvent();
        Calendar cal = Calendar.getInstance();
        obj.setTimestamp(cal);
        
        return obj;
    }
    
}
