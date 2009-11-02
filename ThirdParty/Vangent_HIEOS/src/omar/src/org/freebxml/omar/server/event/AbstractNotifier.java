/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org. All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/event/AbstractNotifier.java,v 1.13 2006/06/23 13:58:43 farrukh_najmi Exp $
 *
 * ====================================================================
 */

package org.freebxml.omar.server.event;

import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.freebxml.omar.common.BindingUtility;
import javax.xml.registry.JAXRException;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.util.ServerResourceBundle;

import org.oasis.ebxml.registry.bindings.rim.ActionType;
import org.oasis.ebxml.registry.bindings.rim.AuditableEventType;
import org.oasis.ebxml.registry.bindings.rim.NotificationType;
import org.oasis.ebxml.registry.bindings.rim.NotifyActionType;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectListType;
import org.oasis.ebxml.registry.bindings.rim.SubscriptionType;

/**
 * Abstract base class for all Notifiers.
 * @author <a href="mailto:Farrukh.Najmi@Sun.COM">Farrukh S. Najmi</a>
 */
public abstract class AbstractNotifier implements Notifier {
    
    protected static BindingUtility bu = BindingUtility.getInstance();
        
    /**
     * @see org.freebxml.omar.server.event.Notifier#sendNotifications
     */
    public void sendNotifications(ServerRequestContext context, SubscriptionType subscription, 
        List matchedObjects, AuditableEventType ae) throws RegistryException {
        
        //Iterate over actions and send notifications for each action
        List actions = subscription.getAction();
        Iterator actionsIter = actions.iterator();
        while (actionsIter.hasNext()) {
        NotifyActionType action = (NotifyActionType)actionsIter.next();
            
            NotificationType notification = createNotification(subscription,
                matchedObjects, action);
            
            sendNotification(context, action, notification, ae);
        }
    }
    
    protected abstract void sendNotification(ServerRequestContext context, NotifyActionType action, 
        NotificationType notification, AuditableEventType ae) throws RegistryException;
    
    private NotificationType createNotification(SubscriptionType subscription,
        List matchedObjects,
        ActionType action) throws RegistryException {
        
        NotificationType notification = null;
        
        try {
        
            if (action instanceof NotifyActionType) {
                NotifyActionType notifyAction = (NotifyActionType)action;
                String notificationOption =  notifyAction.getNotificationOption();

                notification = BindingUtility.getInstance().rimFac.createNotification();
                RegistryObjectListType roList = BindingUtility.getInstance().rimFac.createRegistryObjectList();
                if (notificationOption.equals(BindingUtility.CANONICAL_NOTIFICATION_OPTION_TYPE_ID_Objects)) {
                    roList.getIdentifiable().addAll(matchedObjects);
                }
                else {
                    roList.getIdentifiable().addAll(BindingUtility.getInstance().getObjectRefsFromRegistryObjects(matchedObjects));
                }

                notification.setId(org.freebxml.omar.common.Utility.getInstance().createId());            
                notification.setObjectType(BindingUtility.CANONICAL_OBJECT_TYPE_ID_Notification);            
                notification.setRegistryObjectList(roList);
                notification.setSubscription(subscription.getId());
                //TODO: spec issue: Notification should have the eventType included.

            }
            else {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.onlyNotifyActionsSupported"));
            }
        }
        catch (JAXBException e) {
            throw new RegistryException(e);
        }
        catch (JAXRException e) {
            throw new RegistryException(e);
        }
        
        return notification;
    }
}
