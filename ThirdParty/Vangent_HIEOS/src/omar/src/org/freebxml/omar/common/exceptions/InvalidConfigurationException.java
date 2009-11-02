/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/exceptions/InvalidConfigurationException.java,v 1.3 2005/04/19 19:47:57 joehw Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.exceptions;

import org.freebxml.omar.common.CommonResourceBundle;
import javax.xml.registry.RegistryException;

/**
 * This Exception signifies that the specified content was found to be
 * invalid.
 *
 */
public class InvalidConfigurationException extends RegistryException {
    protected static CommonResourceBundle resourceBundle = CommonResourceBundle.getInstance();

    public InvalidConfigurationException() {
        this(resourceBundle.getString("message.invalidConfigurationException"));
    }

    public InvalidConfigurationException(String reason) {
        super(reason);
    }

    public InvalidConfigurationException(String reason, Exception e) {
        super(reason, e);
    }

    public InvalidConfigurationException(Exception e) {
        super(e);
    }
}
