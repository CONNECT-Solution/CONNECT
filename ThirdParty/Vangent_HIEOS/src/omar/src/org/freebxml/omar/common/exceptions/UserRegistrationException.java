/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/exceptions/UserRegistrationException.java,v 1.1 2005/02/23 16:04:30 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.exceptions;


/**
 * This Exception is thrown when User regstration fails for some reason.
 *
 * @author <a href="mailto:Farrukh.Najmi@Sun.COM">Farrukh S. Najmi</a>
 */
public class UserRegistrationException extends javax.xml.registry.RegistryException {
    public UserRegistrationException(String msg) {
        super(msg);
    }

    public UserRegistrationException(Exception e) {
        super(e);
    }

    public UserRegistrationException(String msg, Exception e) {
        super(msg, e);
    }
}
