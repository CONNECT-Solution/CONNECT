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
// FILE: ClientConnector.java
//
// Copyright (C) 2010 Harris Corporation. All rights reserved.
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: ClientConnector.java 
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

import gov.hhs.fha.nhinc.lift.clientController.SocketClientManagerController;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import gov.hhs.fha.nhinc.lift.proxy.util.Connector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ClientConnector implements Runnable {

    private Log log = null;
    private final ServerSocket server;
    private final Client proxyConnection;
    private final int bufferSize;
    SocketClientManagerController controller;

    public ClientConnector(ServerSocket server, Client proxyConnection, int bufferSize, SocketClientManagerController controller) {
        this.server = server;
        this.proxyConnection = proxyConnection;
        this.bufferSize = bufferSize;
        this.controller = controller;
        log = createLogger();
    }

    protected Log createLogger() {
        if (log == null) {
            log = LogFactory.getLog(getClass());
        }
        return log;
    }

    @Override
    public void run() {
        /*
         * Accept a connection and tunnel messages through the proxy system.
         */
        Socket socket = null;
        try {
            Thread toProxyThread;
            Thread fromProxyThread;

            socket = server.accept();
            log.debug("Server accepting to socket " + socket.getInetAddress());
            Connector toProxy = new Connector(socket.getInputStream(), proxyConnection.getOutStream(), bufferSize);
            Connector fromProxy = new Connector(proxyConnection.getInStream(), socket.getOutputStream(), bufferSize);

            toProxyThread = new Thread(toProxy, "Client To Proxy");
            fromProxyThread = new Thread(fromProxy, "Client From Proxy");

            toProxyThread.start();
            fromProxyThread.start();

            log.debug("Waiting to finish " + toProxyThread.getName());
            toProxyThread.join();

        } catch (IOException e) {
            String errorMsg = "Problem in creating client to proxy connectors: " + e.getMessage();
            log.error(errorMsg);
            controller.reportFailure(proxyConnection.getToken().getRequest(), errorMsg);
        } catch (InterruptedException e) {
            String errorMsg = "Client to proxy communication thread interrupted: " + e.getMessage();
            log.error(errorMsg);
            controller.reportFailure(proxyConnection.getToken().getRequest(), errorMsg);
        } finally {
            if (socket != null) {
                try {
                    log.debug("Closing socket " + socket.getInetAddress() + ": " + socket.getPort());
                    // Also closes associated streams
                    socket.close();
                } catch (IOException ex) {
                    log.warn("Unable to close client to proxy socket: " + ex.getMessage());
                }
            }
            if (proxyConnection != null) {
                try {
                    log.debug("Closing proxy connection " + proxyConnection.getSocket().getInetAddress() + ": " + proxyConnection.getSocket().getPort());
                    proxyConnection.close();
                } catch (IOException ex) {
                    log.warn("Unable to close proxy connection: " + ex.getMessage());
                }
            }
            if (server != null) {
                try {
                    log.debug("Closing client connection server" + server.getInetAddress() + ": " + server.getLocalPort());
                    server.close();
                } catch (IOException ex) {
                    log.warn("Unable to close proxy connection: " + ex.getMessage());
                }
            }
        }
    }
}

