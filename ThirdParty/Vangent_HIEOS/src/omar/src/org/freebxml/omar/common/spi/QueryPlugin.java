/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/spi/QueryPlugin.java,v 1.1 2006/03/24 00:03:25 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.spi;

import javax.xml.registry.RegistryException;


/**
 * The interface implemented by a QueryPlugin plugin class.
 * Implementations of this interface may be configured in registry server
 * to implement special parameterized queries that cannot be expressed easily within 
 * normal SQL queries. Examples are queries that need to make repeated
 * accesses to the database or queries that need to access external data sources.
 *
 * @see http://ebxmlrr.sourceforge.net/wiki/index.php/Dev/omar/design/QueryPlugin
 * 
 * @author Farrukh Najmi
 *
 */
public interface QueryPlugin extends RegistryPlugin {
    
    /** Process the AdhocQueryRequest in the RequestContext */
    public void processRequest(RequestContext context) throws RegistryException;        
}
