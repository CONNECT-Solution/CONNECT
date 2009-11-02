/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/SubscriptionDAO.java,v 1.16 2006/08/24 20:42:29 farrukh_najmi Exp $
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
import javax.xml.registry.RegistryException;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;
import org.oasis.ebxml.registry.bindings.rim.Subscription;
import org.oasis.ebxml.registry.bindings.rim.SubscriptionType;


/**
 *
 * @author Farrukh S. Najmi
 * @author Nikola Stojanovic
 */
class SubscriptionDAO extends RegistryObjectDAO {
    private static final Log log = LogFactory.getLog(SubscriptionDAO.class);

    /**
     * Use this constructor only.
     */
    SubscriptionDAO(ServerRequestContext context) {
        super(context);
    }
    
    public static String getTableNameStatic() {
        return "Subscription";
    }

    public String getTableName() {
        return getTableNameStatic();
    }
                        
            
    protected void deleteComposedObjects(Object object)  throws RegistryException {
        SubscriptionType subscription = (SubscriptionType)object;
            
        super.deleteComposedObjects(subscription);
        
        NotifyActionDAO notifyActionDAO = new NotifyActionDAO(context);
        notifyActionDAO.setParent(subscription);
        notifyActionDAO.deleteByParent();
    }
    
    protected void insertComposedObjects(Object object)  throws RegistryException {
        SubscriptionType subscription = (SubscriptionType)object;
            
        super.insertComposedObjects(subscription);
        
        NotifyActionDAO notifyActionDAO = new NotifyActionDAO(context);
        notifyActionDAO.setParent(subscription);
        notifyActionDAO.insert(subscription.getAction());        
    }
    
    /**
     * Returns the SQL fragment string needed by insert or update statements 
     * within insert or update method of sub-classes. This is done to avoid code
     * duplication.
     */
    protected String getSQLStatementFragment(Object ro) throws RegistryException {

        SubscriptionType subscription = (SubscriptionType)ro;
            
        String stmtFragment = null;
        String selector = subscription.getSelector();        

        Calendar endTime = subscription.getEndTime();
        String endTimeAsString = null;

        if (endTime != null) {
            endTimeAsString = " '" +
                (new Timestamp(endTime.getTimeInMillis())) + "'";
        }
        
        String notificationInterval = subscription.getNotificationInterval();
        if (notificationInterval != null) {
            notificationInterval = "'" + notificationInterval + "'";
        }        
        
        Calendar startTime = subscription.getStartTime();
        String startTimeAsString = null;

        if (startTime != null) {
            startTimeAsString = " '" +
                (new Timestamp(startTime.getTimeInMillis())) + "'";
        }
        
        if (action == DAO_ACTION_INSERT) {
            stmtFragment = "INSERT INTO Subscription " +
                super.getSQLStatementFragment(ro) +
                    ", '" + selector + 
                    "', " + endTimeAsString + 
                    ", " + notificationInterval + 
                    ", " + startTimeAsString + 
                    " ) ";
        }
        else if (action == DAO_ACTION_UPDATE) {
            stmtFragment = "UPDATE Subscription SET " +
                super.getSQLStatementFragment(ro) +
                    ", selector='" + selector + 
                    "', endTime=" + endTimeAsString + 
                    ", notificationInterval=" + notificationInterval + 
                    ", startTime=" + startTimeAsString + 
                    " WHERE id = '" + ((RegistryObjectType)ro).getId() + "' ";
        }
        else if (action == DAO_ACTION_DELETE) {
            stmtFragment = super.getSQLStatementFragment(ro);
        }
        
        return stmtFragment;
    }
    
    


    protected void loadObject( Object obj, ResultSet rs) throws RegistryException {
        try {
            if (!(obj instanceof org.oasis.ebxml.registry.bindings.rim.Subscription)) {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.SubscriptionTypeExpected",
                        new Object[]{obj}));
            }

            Subscription subscription = (Subscription) obj;
            super.loadObject( obj, rs);

            String selector = rs.getString("selector");
            subscription.setSelector(selector);

            //Need to work around a bug in PostgreSQL and loading of ClassificationScheme data from NIST tests
            try {
                Timestamp endTime = rs.getTimestamp("endTime");

                if (endTime != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(endTime.getTime());
                    subscription.setEndTime(calendar);
                }
            } catch (StringIndexOutOfBoundsException e) {
                String id = rs.getString("id");
                log.error(ServerResourceBundle.getInstance().getString("message.SubscriptionDAOId", new Object[]{id}), e);
            }

            String notificationInterval = rs.getString("notificationInterval");

            if (notificationInterval != null) {
                subscription.setNotificationInterval(notificationInterval);
            }            
            
            //Need to work around a bug in PostgreSQL and loading of ClassificationScheme data from NIST tests
            try {
                Timestamp startTime = rs.getTimestamp("startTime");

                if (startTime != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(startTime.getTime());
                    subscription.setStartTime(calendar);
                }
            } catch (StringIndexOutOfBoundsException e) {
                String id = rs.getString("id");
                log.error(ServerResourceBundle.getInstance().getString("message.SubscriptionDAOId", new Object[]{id}), e);
            }
            
            NotifyActionDAO notifyActionDAO = new NotifyActionDAO(context);
            notifyActionDAO.setParent(subscription);
            List notifyActions = notifyActionDAO.getByParent();
            if (notifyActions != null) {
                subscription.getAction().addAll(notifyActions);
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
        Subscription obj = bu.rimFac.createSubscription();
        
        return obj;
    }
}
