/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/NotifyActionDAO.java,v 1.15 2006/08/24 20:42:29 farrukh_najmi Exp $
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
import org.oasis.ebxml.registry.bindings.rim.NotifyAction;
import org.oasis.ebxml.registry.bindings.rim.NotifyActionType;
import org.oasis.ebxml.registry.bindings.rim.SubscriptionType;


/**
 *
 * @author Farrukh S. Najmi
 * @author Adrian Chong
 */
class NotifyActionDAO extends AbstractDAO {
    private static final Log log = LogFactory.getLog(NotifyActionDAO.class);

    /**
     * Use this constructor only.
     */
    NotifyActionDAO(ServerRequestContext context) {
        super(context);
    }
    
    public static String getTableNameStatic() {
        return "NotifyAction";
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

        NotifyActionType notifyAction = (NotifyActionType)object;
            
        String stmtFragment = null;
        
        String notificationOption = notifyAction.getNotificationOption();
        if (notificationOption != null) {
            notificationOption = "'" + notificationOption + "'";
        }
        
        SubscriptionType subscription = (SubscriptionType)parent;
        String parent = subscription.getId();
        
        String endPoint = notifyAction.getEndPoint();
        
        if (action == DAO_ACTION_INSERT) {
            stmtFragment = "INSERT INTO NotifyAction VALUES(" +
                    " " + notificationOption + 
                    ", '" + endPoint + 
                    "', '" + parent + 
                    "' ) ";
        }
        else if (action == DAO_ACTION_UPDATE) {
            stmtFragment = "UPDATE NotifyAction SET " +
                    " notificationOption=" + notificationOption + 
                    ", endPoint='" + endPoint + 
                    "' WHERE parent = '" + parent + "' ";
        }
        else if (action == DAO_ACTION_DELETE) {
            stmtFragment = super.getSQLStatementFragment(object);
        }
        
        return stmtFragment;
    }
    
    protected void loadObject( Object obj, ResultSet rs) throws RegistryException {
        try {
            if (!(obj instanceof org.oasis.ebxml.registry.bindings.rim.NotifyAction)) {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.NotifyActionTypeExpected",
                        new Object[]{obj}));
            }

            NotifyAction notifyAction = (NotifyAction) obj;

            String notificationOption = rs.getString("notificationOption");
            if (notificationOption != null) {
                notifyAction.setNotificationOption(notificationOption);
            }
            
            String endPoint = rs.getString("endPoint");
            notifyAction.setEndPoint(endPoint);
        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        }
    }

    /**
     * Creates an unitialized binding object for the type supported by this DAO.
     */
    Object createObject() throws JAXBException {
        NotifyAction obj = bu.rimFac.createNotifyAction();
        
        return obj;
    }
}
