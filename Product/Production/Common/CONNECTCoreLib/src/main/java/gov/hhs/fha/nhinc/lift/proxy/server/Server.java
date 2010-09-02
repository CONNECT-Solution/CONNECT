/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
//
// Non-Export Controlled Information
//
//####################################################################
//## The MIT License
//##
//## Copyright (c) 2010 Harris Corporation
//##
//## Permission is hereby granted, free of charge, to any person
//## obtaining a copy of this software and associated documentation
//## files (the "Software"), to deal in the Software without
//## restriction, including without limitation the rights to use,
//## copy, modify, merge, publish, distribute, sublicense, and/or sell
//## copies of the Software, and to permit persons to whom the
//## Software is furnished to do so, subject to the following conditions:
//##
//## The above copyright notice and this permission notice shall be
//## included in all copies or substantial portions of the Software.
//##
//## THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
//## EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
//## OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
//## NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
//## HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
//## WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//## FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
//## OTHER DEALINGS IN THE SOFTWARE.
//##
//####################################################################
//********************************************************************
// FILE: Server.java
//
// Copyright (C) 2010 Harris Corporation. All rights reserved.
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: Server.java 
//              
//
// LIMITATIONS: None
//
// SOFTWARE HISTORY:
//
//> Feb 24, 2010 PTR#  - R. Robinson
// Initial Coding.
//<
//********************************************************************
package gov.hhs.fha.nhinc.lift.proxy.server;

import java.io.IOException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class Server implements Runnable, HostnameVerifier {

    protected Log log = null;
    private HandlerPrototype prototype;

    /**
     * Set up a SSL server socket on the provided address.  Makes and runs
     * clones of the provided HandlerPrototype to handle client connections
     * to the Server.
     * @param address
     * @param prototype
     * @throws IOException
     */
    public Server(HandlerPrototype prototype) throws IOException {
        log = createLogger();
        this.setPrototype(prototype);
    }

    @Override
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected HandlerPrototype getPrototype() {
        return prototype;
    }

    /**
     * Lets the user change, at run time, what kind of handler will be used.
     * @param prototype
     */
    public void setPrototype(HandlerPrototype prototype) {
        this.prototype = prototype;
    }

    @Override
    public void run() {
        log.debug("LiFT Server started.");

        int num = 0;

        /*
         * Arbitrary number of failures allowed, should use a more scientific
         * way of determining how many/what kind of failures should halt the
         * server.  Perhaps load this from a properties file.
         */
        while (num < 3) {
            try {
                acceptConnection();
            } catch (IOException e) {
                log.error(e.getMessage());
                System.out.println(e.getMessage());
                num++;
            }
        }
    }

    /**
     * This method should block until a new connection has been processed.  It
     * is called continuously in the run block.  Implementer of this method
     * have access to the prototype stored in this object to create handlers
     * for the accepted requests.
     * @throws IOException
     */
    protected abstract void acceptConnection() throws IOException;
}
