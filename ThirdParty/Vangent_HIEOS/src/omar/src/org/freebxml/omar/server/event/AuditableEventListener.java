/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org. All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/event/AuditableEventListener.java,v 1.4 2005/12/12 17:14:32 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.event;

import org.freebxml.omar.server.common.ServerRequestContext;
import org.oasis.ebxml.registry.bindings.rim.AuditableEventType;

/**
 * Listens to AuditableEvents.
 *
 * @author Farrukh S. Najmi
 * @author Nikola Stojanovic
 */
public interface AuditableEventListener {
    
    public void onEvent(ServerRequestContext context, AuditableEventType ae);
}
