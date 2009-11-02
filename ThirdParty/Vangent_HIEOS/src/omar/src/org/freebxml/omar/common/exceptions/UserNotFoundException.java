/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/exceptions/UserNotFoundException.java,v 1.2 2005/04/19 19:48:00 joehw Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.exceptions;


import org.freebxml.omar.common.CommonResourceBundle;


/**
 * This Exception is thrown when searching a User by id fails to find a User.
 *
 * @author <a href="mailto:Farrukh.Najmi@Sun.COM">Farrukh S. Najmi</a>
 */
public class UserNotFoundException extends ObjectNotFoundException {
    protected static CommonResourceBundle resourceBundle = CommonResourceBundle.getInstance();

    private String userId;

    public UserNotFoundException(String userId) {
        super(resourceBundle.getString("message.userNotFoundException", new String[]{userId}));
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
