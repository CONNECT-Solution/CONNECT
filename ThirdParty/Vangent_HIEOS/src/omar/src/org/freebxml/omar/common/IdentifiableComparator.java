/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/IdentifiableComparator.java,v 1.2 2005/04/19 19:47:56 joehw Exp $
 * ====================================================================
 */
package org.freebxml.omar.common;

import org.oasis.ebxml.registry.bindings.rim.IdentifiableType;

import java.util.Comparator;

/**
 * Compares two instances of <code>IdentifiableType</code>.
 */
public class IdentifiableComparator implements Comparator {
    protected static CommonResourceBundle resourceBundle = CommonResourceBundle.getInstance();

    public int compare(Object o1,
		       Object o2) throws ClassCastException {

	if (!(o1 instanceof IdentifiableType)) {
	    throw new ClassCastException(resourceBundle.getString("message.objectNotIdentifiableType", new String[]{o1.toString()}));
	}

	if (!(o2 instanceof IdentifiableType)) {
	    throw new ClassCastException(resourceBundle.getString("message.objectNotIdentifiableType", new String[]{o2.toString()}));
	}

	IdentifiableType it1 = (IdentifiableType) o1;
	IdentifiableType it2 = (IdentifiableType) o2;

	return it1.getId().compareTo(it2.getId());
    }
}
