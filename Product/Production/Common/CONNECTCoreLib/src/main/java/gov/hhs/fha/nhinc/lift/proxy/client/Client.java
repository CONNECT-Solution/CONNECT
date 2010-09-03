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
// FILE: Client.javas
//
// Copyright (C) 2010 Harris Corporation. All rights reserved.
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: Client.java 
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
package gov.hhs.fha.nhinc.lift.proxy.client;

import gov.hhs.fha.nhinc.lift.common.util.RequestToken;
import gov.hhs.fha.nhinc.lift.proxy.util.ClientHandshaker;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import javax.net.ssl.SSLSocket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class Client {

    protected Log log = null;
    private final RequestToken token;

    /**
     * Attempts to connect to a server at the provided address and calls the
     * perfomHandshake() method to trigger handshaking to occur after initial
     * connection is established.  How this handshaking is done is not defined
     * at this point, it is left to how subclasses are implemented to decided
     * how to do handshaking.
     *
     * NOTE: Subclasses may not call this constructor.  This means that perform
     * handshake may not happen.
     * @param address
     * @param token
     * @param handshaker
     * @throws IOException
     */
    public Client(InetAddress address, int port, RequestToken token, ClientHandshaker handshaker) throws IOException {

        log = createLogger();
        this.token = token;
        this.connect(address, port);

        if (performHandshake(handshaker)) {
            log.info("Handshake successful, connection available.");
        }
    }

    protected Log createLogger() {
        if (log == null) {
            log = LogFactory.getLog(getClass());
        }
        return log;
    }

    protected abstract boolean connect(InetAddress address, int port) throws IOException;

    /**
     * Implementation specific, must know how to handshake with the server.
     * This method has nothing to do with SSL handshaking, it is in place for
     * any additional handshaking between an proxy client and proxy server.
     * @return
     * @throws IOException
     */
    protected abstract boolean performHandshake(ClientHandshaker handshaker) throws IOException;

    /**
     * Helper method in place to send a message.
     * @param mess
     * @throws IOException
     */
    public abstract void sendLine(String mess) throws IOException;

    /**
     * Helper method in place to read line from the output stream of the
     * socket.
     * @return
     * @throws IOException
     */
    public abstract String readLine() throws IOException;

    public abstract OutputStream getOutStream() throws IOException;

    public abstract InputStream getInStream() throws IOException;

    public abstract SSLSocket getSocket();

    protected RequestToken getToken() {
        return token;
    }

    /**
     * Closes the socket.
     * @throws IOException
     */
    public abstract void close() throws IOException;
}
