/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/AbstractQueryPlugin.java,v 1.1 2006/03/24 00:03:30 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query;

import java.util.Set;
import org.freebxml.omar.common.BindingUtility;
import org.freebxml.omar.common.spi.QueryPlugin;

/**
 * The asbtract base class for all QueryPlugin implementation classes.
 * 
 * @author Farrukh Najmi
 *
 */
public abstract class AbstractQueryPlugin implements QueryPlugin {
    
    protected BindingUtility bu = BindingUtility.getInstance();
    
    public Set getRoles() {
        return null;
    }

    public Set getActions() {
        return null;
    }
}
