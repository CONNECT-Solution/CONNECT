/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/sql/PrunePredicateException.java,v 1.2 2005/02/23 22:56:51 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query.sql;

import javax.xml.registry.RegistryException;


/**
 * The Exception that should be thrown when the need to prune a predicate is detected is SQLParser.
 *
 * @author Farrukh S. Najmi
*/
public class PrunePredicateException extends RegistryException {

    public PrunePredicateException() {
    }


}
