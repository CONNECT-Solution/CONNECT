/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/spi/RequestInterceptor.java,v 1.3 2006/06/02 14:51:23 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.spi;

import javax.xml.registry.RegistryException;


/**
 * The interface implemented by a RequestInterceptor plugin class
 * Implementations of this interface may be configured in registry server
 * to intercept requests for specific roles and perform request/response modification
 * for applying complex validation or enforcing other governance policies
 * of the deployment.
 *
 * @see http://ebxmlrr.sourceforge.net/wiki/index.php/Dev/omar/design/serverPlugin
 * 
 * @author Farrukh Najmi
 * @author Diego Ballve
 *
 */
public interface RequestInterceptor extends RegistryPlugin {
    
    /** Pre-process the RegistryRequest in the RequestContext */
    public void preProcessRequest(RequestContext context) throws RegistryException;
    
    /** Post-process the RegistryRequest in the RequestContext */
    public void postProcessRequest(RequestContext context) throws RegistryException;
    
}
