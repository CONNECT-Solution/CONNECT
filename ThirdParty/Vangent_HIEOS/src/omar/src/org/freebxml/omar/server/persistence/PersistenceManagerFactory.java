/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/PersistenceManagerFactory.java,v 1.10 2006/08/24 20:42:27 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.persistence;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freebxml.omar.server.common.RegistryProperties;


/**
 *
 * @author  najmi
 */
public class PersistenceManagerFactory {
    private static PersistenceManagerFactory instance; //singleton instance
    public static final String PERSISTENCE_MANAGER_CLASS_PROPERTY = "omar.server.persistence.PersistenceManagerFactory.persistenceManagerClass";
    private PersistenceManager pm = null;
    private static final Log log = LogFactory.getLog(PersistenceManagerFactory.class);

    /** Creates a new instance of PersistenceManager */
    protected PersistenceManagerFactory() {
    }

    public synchronized static PersistenceManagerFactory getInstance() {
        if (instance == null) {
            instance = new PersistenceManagerFactory();
        }

        return instance;
    }

    public PersistenceManager getPersistenceManager() {
        if (pm == null) {
            synchronized (this) {
                if (pm == null) {
                    try {
                        String pluginClass = RegistryProperties.getInstance()
                                                               .getProperty(PERSISTENCE_MANAGER_CLASS_PROPERTY);

                        pm = (PersistenceManager) createPluginInstance(pluginClass);
                    } catch (Exception e) {
                        Throwable cause = e.getCause();                    
                        e.printStackTrace();
                        if (cause != null) {
                            System.err.println("Caused by.....");
                            cause.printStackTrace();
                        }

                        String errmsg = "[PersistenceManager] Cannot instantiate " +
                            "PersistenceManager plugin. Please check that " +
                            "property '" + PERSISTENCE_MANAGER_CLASS_PROPERTY +
                            "' is correctly set in omar.properties file.";
                        System.err.println(errmsg);
                    }
                }
            }
        }

        return pm;
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
            //log.warn("No accessible constructor. " +
            //    "invoking getInstance() instead.");

            Method factory = theClass.getDeclaredMethod("getInstance", (java.lang.Class[])null);
            plugin = factory.invoke(null, new Object[0]);
        }

        return plugin;
    }
    
    //Main method only to test the class
    public static void main(String[] args) {
        PersistenceManager pm = PersistenceManagerFactory.getInstance().getPersistenceManager();
        System.err.println(pm);
    }
}
