/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/spi/LifeCycleManagerFactory.java,v 1.3 2006/08/24 20:42:11 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.spi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.freebxml.omar.common.*;

/**
 *
 * @author  najmi
 */
public class LifeCycleManagerFactory {
    private static LifeCycleManagerFactory instance; //singleton instance
    public static final String LIFECYCLE_MANAGER_CLASS_PROPERTY = "omar.common.LifeCycleManagerFactory.lifeCycleManagerClass";
    private LifeCycleManager lcm = null;
    private static final Log log = LogFactory.getLog(LifeCycleManagerFactory.class);

    /** Creates a new instance of LifeCycleManager */
    protected LifeCycleManagerFactory() {
    }

    public synchronized static LifeCycleManagerFactory getInstance() {
        if (instance == null) {
            instance = new LifeCycleManagerFactory();
        }

        return instance;
    }

    public LifeCycleManager getLifeCycleManager() {
        if (lcm == null) {
            synchronized (this) {
                if (lcm == null) {
                    String pluginClass = null;
                    try {
                        pluginClass = CommonProperties.getInstance()
                                                               .getProperty(LIFECYCLE_MANAGER_CLASS_PROPERTY);

                        lcm = (LifeCycleManager) createPluginInstance(pluginClass);
                    } catch (Exception e) {
                        String errmsg = "[LifeCycleManager] Cannot instantiate " +
                            "LifeCycleManager plugin class named: '" + pluginClass + "'. Please check that " +
                            "property '" + LIFECYCLE_MANAGER_CLASS_PROPERTY +
                            "' is correctly set in omar-common.properties file.";
                        log.error(errmsg, e);
                    }
                }
            }
        }

        return lcm;
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
            plugin = factory.invoke((java.lang.Class[])null, new Object[0]);
        }

        return plugin;
    }
}
