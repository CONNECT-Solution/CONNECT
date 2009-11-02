/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/spi/NotificationListener.java,v 1.2 2005/11/28 20:17:33 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.spi;

import javax.xml.registry.RegistryException;
import org.oasis.ebxml.registry.bindings.rim.NotificationType;

/**
 * The interface implemented by a NotificationListener plugin class 
 * 
 */
public interface NotificationListener {
    
    public void onNotification(RequestContext context, NotificationType notification) throws RegistryException;
    
}
