/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/exceptions/UnauthorizedRequestException.java,v 1.3 2005/04/19 19:47:59 joehw Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.exceptions;

import org.freebxml.omar.common.CommonResourceBundle;



/**
 * This Exception is thrown when a User attempt a secure operation for which they are not authorized.
 *
 * @author <a href="mailto:Farrukh.Najmi@Sun.COM">Farrukh S. Najmi</a>
 */
public class UnauthorizedRequestException extends AuthorizationException {
    protected static CommonResourceBundle resourceBundle = CommonResourceBundle.getInstance();

    private String id;
    private String userId;
    String action;


    public UnauthorizedRequestException(String objectId, String userId,
        String action, String msg) {
        this(resourceBundle.getString("message.unauthorizedRequestException", 
                new String[]{userId, action, objectId, msg}));
        id = objectId;
        this.userId = userId;
        this.action = action;
    }

    public UnauthorizedRequestException(String detail) {
        super(detail);
    }

    /**
     * Get the id of the object on which the user makes unauthorized request
     */
    public String getId() {
        return id;
    }

    /**
     * Get the id of the user who makes the unauthorized request
     */
    public String getUserId() {
        return userId;
    }
}
