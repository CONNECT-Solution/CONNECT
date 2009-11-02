/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/plugin/RequestInterceptorManager.java,v 1.6 2006/02/24 16:39:50 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.plugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.JAXBException;
import javax.xml.registry.JAXRException;
import javax.xml.registry.RegistryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freebxml.omar.common.exceptions.ObjectNotFoundException;
import org.freebxml.omar.common.spi.RequestContext;
import org.freebxml.omar.common.spi.RequestInterceptor;
import org.freebxml.omar.server.cache.ServerCache;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.oasis.ebxml.registry.bindings.rim.RegistryType;
import org.oasis.ebxml.registry.bindings.rim.UserType;
import org.oasis.ebxml.registry.bindings.rs.RegistryRequestType;

/**
 * The class manages and invokes RequestInterceptor plugins.
 * 
 * @author Farrukh Najmi
 * @author Diego Ballve
 *
 */
public class RequestInterceptorManager extends AbstractPluginManager {
    private static final Log log = LogFactory.getLog(RequestInterceptorManager.class);
    private List interceptors = null;
    
    private static RequestInterceptorManager instance = null;            
    
    protected RequestInterceptorManager() throws RegistryException {
    }
            
    public synchronized static RequestInterceptorManager getInstance() throws RegistryException {
        if (instance == null) {
            instance = new org.freebxml.omar.server.plugin.RequestInterceptorManager();
        }

        return instance;
    }

    public void postProcessRequest(RequestContext context) throws RegistryException {
        List applicablePlugins = getApplicablePlugins((ServerRequestContext)context);
        
        Iterator iter = applicablePlugins.iterator();
        while (iter.hasNext()) {
            RequestInterceptor interceptor = (RequestInterceptor)iter.next();
            interceptor.postProcessRequest(context);
        }
    }

    public void preProcessRequest(RequestContext context) throws RegistryException {
        List applicablePlugins = getApplicablePlugins((ServerRequestContext)context);
        
        Iterator iter = applicablePlugins.iterator();
        while (iter.hasNext()) {
            RequestInterceptor interceptor = (RequestInterceptor)iter.next();
            interceptor.preProcessRequest(context);
        }
    }
    
    private List getApplicablePlugins(ServerRequestContext context) throws RegistryException {
        List applicablePlugins = new ArrayList();
        
        try {
            if (getInterceptors().size() > 0) {
                String requestAction = bu.getActionFromRequest(context.getCurrentRegistryRequest());

                //Get roles associated with user associated with this RequestContext
                Set subjectRoles = ServerCache.getInstance().getRoles(context);

                //Now get those RequestInterceptors whose roles are a proper subset of subjectRoles
                Iterator iter = getInterceptors().iterator();
                while (iter.hasNext()) {
                    RequestInterceptor interceptor = (RequestInterceptor)iter.next();
                    Set interceptorRoles = interceptor.getRoles();
                    Set interceptorActions = interceptor.getActions();
                    if ((subjectRoles.containsAll(interceptorRoles))) {
                        applicablePlugins.add(interceptor);
                    }
                }
            }
        } catch (JAXRException e) {
            throw new RegistryException(e);
        }
        
        return applicablePlugins;
    }

    public List getInterceptors() throws RegistryException {
	ServerRequestContext context = null;
        try {
            if (interceptors == null) {
                ArrayList _interceptors = new ArrayList();
                context =
		    new ServerRequestContext("RequestInterceptorManager:RequestInterceptorManager",
					     null);
                RegistryType registry = null;
                try {
                    registry = (RegistryType)ServerCache.getInstance().getRegistry(context);
                } catch (ObjectNotFoundException e) {
                    //Registry instance not loaded yet
                    //Must be in db build sequence.
                    //Return empty list but dont set the interceptors var until registry instance is read
                    return new ArrayList();
                }

                Map slotsMap = bu.getSlotsFromRegistryObject(registry);
                //??Need to fix getSlotsFromRegistryObject and getSlotsFromRegistryRequest to always return slot values as List
                //Otherwise a List slot with single value gets returned as String and we get ClassCastException.
                Object o = slotsMap.get(bu.FREEBXML_REGISTRY_REGISTRY_INTERCEPTORS);
                List interceptorClasses = null;
                if (o != null) {
                    if (o instanceof List) {
                        interceptorClasses = (List)o;
                    } else {
                        interceptorClasses = new ArrayList();
                        interceptorClasses.add((String)o);
                    }

                    //Instantiate and cache Interceptor classes
                    if (interceptorClasses != null) {
                        Iterator iter = interceptorClasses.iterator();
                        while (iter.hasNext()) {
                            String className = (String)iter.next();
                            RequestInterceptor interceptor = (RequestInterceptor)createPluginInstance(className);
                            _interceptors.add(interceptor);
                        }
                    }
                    interceptors = _interceptors;
                } else {
                    //NO interceptors configured.
                    interceptors = new ArrayList();
                }
            }
        } catch (Throwable e) {
            log.error(e);
            throw new RegistryException(e);
        } finally {
	    if (null != context) {
		context.rollback();
	    }
	}
        return interceptors;
    }
    
}
