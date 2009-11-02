/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org. All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/event/Notifier.java,v 1.6 2006/04/26 06:21:17 doballve Exp $
 *
 * ====================================================================
 */
package org.freebxml.omar.server.event;

import java.util.List;

import javax.xml.registry.RegistryException;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.oasis.ebxml.registry.bindings.rim.AuditableEventType;
import org.oasis.ebxml.registry.bindings.rim.SubscriptionType;

/**
 * The interface implemented by all types of Notifiers.
 * @author <a href="mailto:Farrukh.Najmi@Sun.COM">Farrukh S. Najmi</a>
 */
public interface Notifier {
    /*
     * Perform all notification for the specified subscription and include the specified objects
     * or their references in the notifications.
     */
    public void sendNotifications(ServerRequestContext context, SubscriptionType subscription, 
        List matchedObjects, AuditableEventType ae) throws RegistryException;
}
