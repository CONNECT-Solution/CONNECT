/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2005 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/CommonResourceBundle.java,v 1.6 2006/07/29 05:53:37 dougb62 Exp $
 * ====================================================================
 */
package org.freebxml.omar.common;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is used to load localized strings
 *
 * @author  Paul Sterk / Sun Microsystems
 */
public class CommonResourceBundle extends AbstractResourceBundle {

    public static final String BASE_NAME =
        "org.freebxml.omar.common.ResourceBundle";
    private static CommonResourceBundle instance = null;
    private static Locale locale = null;
    private ResourceBundle bundle = null;
    private static final Log log = LogFactory.getLog(CommonResourceBundle.class);

    protected CommonResourceBundle() {
        // Load the resource bundle of default locale
        bundle = ResourceBundle.getBundle(BASE_NAME);
    }

    protected CommonResourceBundle(Locale locale) {
        // Load the resource bundle of specified locale
        bundle = ResourceBundle.getBundle(BASE_NAME, locale);
    }

    public synchronized static CommonResourceBundle getInstance() {
        if (instance == null) {
            instance = new CommonResourceBundle();
            String localeString = CommonProperties.getInstance()
                                                 .getProperty("omar.common.locale");
            locale = parseLocale(localeString);
        }

        return instance;
    }

    public synchronized static CommonResourceBundle getInstance(Locale locale) {
        if (instance == null) {
            instance = new CommonResourceBundle(locale);
        } else {
            if (CommonResourceBundle.locale != locale) {
                instance = new CommonResourceBundle(locale);
            }
	}
        return instance;
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public ResourceBundle getBundle(Locale locale) {
        return ResourceBundle.getBundle(BASE_NAME, locale);
    }
}
