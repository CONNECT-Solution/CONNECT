/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2005 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/AbstractResourceBundle.java,v 1.9 2006/07/29 05:53:37 dougb62 Exp $
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
 * This class contains methods common to all XXResourceBundle classes
 *
 * @author  Paul Sterk / Sun Microsystems
 */
public abstract class AbstractResourceBundle extends ResourceBundle {

    private static final Log log = LogFactory.getLog(AbstractResourceBundle.class);

    public static final String LOCALE =
       "urn:oasis:names:tc:ebxml-regrep:rs:request:locale";

    /**
     * Gets 'key' from ResourceBundle and format mesage using 'args'.
     *
     * @param key String key for message.
     * @param args Array of arguments for message.
     * @return String formatted message.
     */
    public String getString(String key, Object args[]) {
        String pattern = getBundle().getString(key);
        return MessageFormat.format(pattern, args);
    }

    /**
     * Gets 'key' from ResourceBundle and format mesage using 'args'.
     *
     * @param key String key for message.
     * @param args Array of arguments for message.
     * @param locale Locale in which to perform key lookup.
     * @return String formatted message.
     */
    public String getString(String key, Object args[], Locale locale) {
        String pattern = null;
        if (locale == null) {
            pattern = getBundle().getString(key);
        } else {
            pattern = getBundle(locale).getString(key);
        }
        return MessageFormat.format(pattern, args);
    }

    /**
     * Parse a locale string, return corresponding Locale instance.
     *
     * @param localeString
     * Name for the locale of interest.  If null, use VM default locale.
     * @return New Locale instance.
     */
    public static Locale parseLocale(String localeString) {
        Locale locale = null;
        if (localeString == null) {
            log.trace(CommonResourceBundle.getInstance().getString("message.TheLocaleStringIsNullUsingDefaultLocale"));
            locale = Locale.getDefault();
        } else {
            try {
                String[] args = localeString.split("_");
                if (args.length == 1) {
                    locale = new Locale(args[0]);
                } else if (args.length == 2) {
                    locale = new Locale(args[0], args[1]);
                } else if (args.length == 3) {
                    locale = new Locale(args[0], args[1], args[2]);
                }
            } catch (Throwable t) {
                log.error(CommonResourceBundle.getInstance().getString("message.CouldNotCreateLocaleFromOmarcommonlocaleProperty"), t);
                log.error(CommonResourceBundle.getInstance().getString("message.UsingDefaultLocaleInstead", new Object[]{Locale.getDefault().toString()}));
                locale = Locale.getDefault();
            }
        }
        return locale;
    }

    /**
     * Subclasses of this class must implement this method so that the
     * correct resource bundle is passed to methods in this class
     *
     * @return
     *  A java.util.ResourceBundle from the subsclass. Methods in this class
     *  will use this reference.
     */
    public abstract ResourceBundle getBundle();

    /**
     * Subclasses of this class must implement this method so that the
     * correct resource bundle is passed to methods in this class
     *
     * @param locale
     *  The locale to use when getting the resource bundle
     * @return
     *  A java.util.ResourceBundle from the subsclass. Methods in this class
     *  will use this reference.
     */
    public abstract ResourceBundle getBundle(Locale locale);

    /**
     * Since we are changing the ResourceBundle extension point, must
     * implement handleGetObject() using delegate getBundle().  Uses
     * getObject() call to work around protected access to
     * ResourceBundle.handleGetObject().  Happily, this means parent tree
     * of delegate bundle is searched for a match.
     *
     * Implements java.util.ResourceBundle.handleGetObject; inherits that
     * javadoc information.
     *
     * @see java.util.ResourceBundle.handleGetObject
     */
    protected Object handleGetObject(String key) {
       return getBundle().getObject(key);
    }

    /**
     * Since we are changing the ResourceBundle extension point, must
     * implement getKeys() using delegate getBundle().
     *
     * Implements java.util.ResourceBundle.getKeys; inherits that javadoc
     * information.
     *
     * @see java.util.ResourceBundle.getKeys
     */
    public final Enumeration getKeys() {
       return getBundle().getKeys();
    }
}
