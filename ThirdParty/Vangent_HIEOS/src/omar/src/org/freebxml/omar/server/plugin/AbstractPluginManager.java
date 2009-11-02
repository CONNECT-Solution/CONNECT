/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/plugin/AbstractPluginManager.java,v 1.2 2005/12/18 09:47:31 dougb62 Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;
import javax.xml.registry.RegistryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freebxml.omar.common.BindingUtility;
import org.oasis.ebxml.registry.bindings.rim.Registry;
import org.oasis.ebxml.registry.bindings.rim.UserType;
import org.oasis.ebxml.registry.bindings.rs.RegistryRequestType;

/**
 * The abstract interface implemented by all PluginManagers.
 * A PluginManager manages registry server plugins.
 * A registry server plugin implements a registry spi interface.
 * 
 * @author Farrukh Najmi
 *
 */
public abstract class AbstractPluginManager implements PluginManager {
    protected static BindingUtility bu = BindingUtility.getInstance();
    private static final Log log = LogFactory.getLog(AbstractPluginManager.class);

    private static Registry registry = null;

    protected AbstractPluginManager() {
    }
        
    /**
    * Creates the instance of the pluginClass
    */
    protected Object createPluginInstance(String pluginClass)
        throws Exception {
        Object plugin = null;

        if (log.isDebugEnabled()) {
            log.debug("pluginClass = " + pluginClass);
        }

        if (pluginClass != null) {
            Class theClass = Class.forName(pluginClass);

            //try to invoke zero arg constructor using Reflection, 
            //if this fails then try invoking getInstance()
            try {
                Constructor constructor = theClass.getConstructor((java.lang.Class[])null);
                plugin = constructor.newInstance(new Object[0]);
            } catch (Exception e) {
                Method factory = theClass.getDeclaredMethod("getInstance", (java.lang.Class[])null);
                plugin = factory.invoke((java.lang.Class[])null, new Object[0]);
            }
        }
        return plugin;
    }
}
