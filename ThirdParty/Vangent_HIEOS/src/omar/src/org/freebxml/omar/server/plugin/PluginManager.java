/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/plugin/PluginManager.java,v 1.1 2005/11/28 20:17:37 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.plugin;

import java.util.Map;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.server.common.ServerRequestContext;

/**
 * The abstract base class for all PluginManagers.
 * A PluginManager manages registry server plugins.
 * A registry server plugin implements a registry spi interface.
 *
 * Current examples of plugins are: RequestInterceptor, NotificationListener, Validator, Cataloger
 * 
 * @author Farrukh Najmi
 * @author Diego Ballve
 *
 */
public interface PluginManager {
}
