/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org. All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/event/PluginNotifier.java,v 1.6 2006/04/26 06:21:17 doballve Exp $
 *
 * ====================================================================
 */
package org.freebxml.omar.server.event;

import javax.xml.registry.RegistryException;
import org.freebxml.omar.common.CommonResourceBundle;
import org.freebxml.omar.common.spi.NotificationListener;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.oasis.ebxml.registry.bindings.rim.AuditableEventType;
import org.oasis.ebxml.registry.bindings.rim.NotificationType;
import org.oasis.ebxml.registry.bindings.rim.NotifyActionType;

/**
 * Notifier used to send notifications to a SOAP-based web service when its Subscription matches a registry event.
 * @author <a href="mailto:Farrukh.Najmi@Sun.COM">Farrukh S. Najmi</a>
 */
public class PluginNotifier extends AbstractNotifier {
            
    protected void sendNotification(ServerRequestContext context, NotifyActionType notifyAction, 
        NotificationType notification, AuditableEventType ae) throws RegistryException {

        try {
            //Get the ServiceBinding id that represents the endPoint
            String endPoint =  notifyAction.getEndPoint();            
            Class clazz = Class.forName(endPoint);
            Object obj = clazz.newInstance();
            
            if (!(obj instanceof NotificationListener)) {
                throw new RegistryException(CommonResourceBundle.getInstance().getString("message.unexpectedObjectType",new Object[] {"org.freebxml.omar.common.NotificationListener"}));
            }
            
            NotificationListener listener = (NotificationListener)obj;
            listener.onNotification(context, notification);
        } catch (Exception e) {
            throw new RegistryException(e);
        }
    }    
}
