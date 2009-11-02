/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/RepositoryItem.java,v 1.7 2005/02/23 22:56:47 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.common;

import java.io.IOException;

import javax.activation.DataHandler;
import javax.mail.internet.MimeMultipart;
import javax.xml.registry.RegistryException;

import org.w3c.dom.Element;


/**
* Repository item interface
*
* @author Adrian Chong
* @author Peter Burgess
*
* @version $Version: $ [$Date: 2005/02/23 22:56:47 $]
*/
public interface RepositoryItem {    
    
    /**
     * Return the size of of the repository item in bytes.
     */
    public int getSize() throws IOException;
    
    public String getId();
    
    public void setId(String id);
    
    public Element getSignatureElement();
    
    public DataHandler getDataHandler();
            
    public MimeMultipart getMimeMultipart() throws RegistryException;
    
    public Object clone() throws CloneNotSupportedException;
}
