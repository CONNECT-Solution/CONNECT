/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/repository/RepositoryManagerFactory.java,v 1.13 2006/08/24 20:42:32 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.repository;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freebxml.omar.server.common.RegistryProperties;
import org.freebxml.omar.server.util.ServerResourceBundle;

/**
 *
 * @author  najmi
 */
public class RepositoryManagerFactory {
    private static RepositoryManagerFactory instance; //singleton instance
    public static final String REPOSITORY_MANAGER_CLASS_PROPERTY = "omar.server.repository.RepositoryManagerFactory.repositoryManagerClass";
    private RepositoryManager rm = null;
    private static final Log log = LogFactory.getLog(RepositoryManagerFactory.class);

    /** Creates a new instance of RepositoryManagerFactory */
    protected RepositoryManagerFactory() {
    }

    public synchronized static RepositoryManagerFactory getInstance() {
        if (instance == null) {
            instance = new RepositoryManagerFactory();
        }

        return instance;
    }

    public RepositoryManager getRepositoryManager() {
        if (rm == null) {
            synchronized (this) {
                if (rm == null) {
                    try {
                        String pluginClass = RegistryProperties.getInstance()
                                                               .getProperty(REPOSITORY_MANAGER_CLASS_PROPERTY);

                        rm = (RepositoryManager) createPluginInstance(pluginClass);
                    } catch (Exception e) {
                        String errmsg = ServerResourceBundle.getInstance().getString("message.CannotInstantiateRepositoryManagerPlugin",
						                                                   new Object[]{REPOSITORY_MANAGER_CLASS_PROPERTY});
                        log.error(errmsg, e);
                    }
                }
            }
        }

        return rm;
    }

    /**
    * Creates the instance of the pluginClass
    */
    private Object createPluginInstance(String pluginClass)
        throws Exception {
        Object plugin = null;

        if (log.isDebugEnabled()) {
            log.debug("pluginClass = " + pluginClass);
        }

        Class theClass = Class.forName(pluginClass);

        //try to invoke constructor using Reflection, 
        //if this fails then try invoking getInstance()
        try {
            Constructor constructor = theClass.getConstructor((java.lang.Class[])null);
            plugin = constructor.newInstance(new Object[0]);
        } catch (Exception e) {
            //log.trace(ServerResourceBundle.getInstance().getString("message.NoAccessibleConstructor"));

            Method factory = theClass.getDeclaredMethod("getInstance", (java.lang.Class[])null);
            plugin = factory.invoke(null, new Object[0]);
        }

        return plugin;
    }
}
