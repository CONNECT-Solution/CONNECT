/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/CredentialInfo.java,v 1.7 2005/04/01 11:16:47 doballve Exp $
 * ====================================================================
 */
package org.freebxml.omar.common;

import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;


/**
 * Class to encapsulate Client Credential information.
 * 
 */
public class CredentialInfo {
    
    public String alias;
    public X509Certificate cert;
    public Certificate[] certChain;
    public PrivateKey privateKey;
    public String sessionId;

    public CredentialInfo() {
        
    }
    
    public CredentialInfo(
    String alias,
    X509Certificate cert,
    Certificate[] certChain,
    PrivateKey privateKey) {
        this.alias = alias;
        this.cert = cert;
        this.certChain = certChain;
        this.privateKey = privateKey;
        this.sessionId = sessionId;
    }
    
}
