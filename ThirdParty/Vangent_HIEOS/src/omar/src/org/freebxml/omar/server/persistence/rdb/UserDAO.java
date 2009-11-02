/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/UserDAO.java,v 1.21 2005/11/21 04:27:52 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.persistence.rdb;

import javax.xml.bind.JAXBException;

import org.freebxml.omar.server.common.ServerRequestContext;
import org.oasis.ebxml.registry.bindings.rim.User;


/**
 *
 * @author Farrukh S. Najmi
 * @version
 */
class UserDAO extends PersonDAO {

    /**
     * Use this constructor only.
     */
    UserDAO(ServerRequestContext context) {
        super(context);
    }
    
    public static String getTableNameStatic() {
        return "User_";
    }

    public String getTableName() {
        return getTableNameStatic();
    }
    
    /**
     * Creates an unitialized binding object for the type supported by this DAO.
     */
    Object createObject() throws JAXBException {
        User obj = bu.rimFac.createUser();
        
        return obj;
    }
}
