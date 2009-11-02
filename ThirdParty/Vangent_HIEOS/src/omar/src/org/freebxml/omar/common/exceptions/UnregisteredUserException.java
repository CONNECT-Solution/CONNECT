/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/exceptions/UnregisteredUserException.java,v 1.7 2006/05/31 01:18:44 dougb62 Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.exceptions;

import org.freebxml.omar.common.CommonResourceBundle;
import java.security.cert.X509Certificate;


/**
 * This Exception is thrown when an UnregisteredUser attempts a secure operation.
 *
 * @author <a href="mailto:Farrukh.Najmi@Sun.COM">Farrukh S. Najmi</a>
 */
public class UnregisteredUserException extends UnauthorizedRequestException {
    protected static CommonResourceBundle resourceBundle = CommonResourceBundle.getInstance();

    public UnregisteredUserException(X509Certificate cert) {
        this(resourceBundle.getString("message.unregisteredUserException",
				      new String[]{cert.getSubjectDN().
						   getName()}));
    }
    
    public UnregisteredUserException(String msg) {
        super(msg);
    }
}
