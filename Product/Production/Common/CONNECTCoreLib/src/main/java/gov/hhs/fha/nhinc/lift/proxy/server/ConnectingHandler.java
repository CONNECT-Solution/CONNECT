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
// FILE: ConnectingHandler.java
//
// Copyright (C) 2010 Harris Corporation. All rights reserved.
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: ConnectingHandler.java 
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

import gov.hhs.fha.nhinc.lift.common.util.LiftConnectionRequestToken;
import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;

import gov.hhs.fha.nhinc.lift.proxy.properties.interfaces.ProducerProxyPropertiesFacade;
import gov.hhs.fha.nhinc.lift.proxy.util.Connector;
import gov.hhs.fha.nhinc.lift.proxy.util.ProtocolWrapper;
import gov.hhs.fha.nhinc.lift.proxy.util.ProxyUtil;
import gov.hhs.fha.nhinc.lift.proxy.util.LiftConnectionResponseToken;
import javax.xml.bind.JAXBException;


public class ConnectingHandler extends HandlerPrototype {

    private final ProducerProxyPropertiesFacade props;
    private final int bufferSize;
    private LiftConnectionRequestToken token;

    public ConnectingHandler(ProducerProxyPropertiesFacade props, int bufferSize) {
        this.props = props;
        this.bufferSize = bufferSize;
    }

    @Override
    protected HandlerPrototype createInstance(ProtocolWrapper wrapper) {
        HandlerPrototype hand = new ConnectingHandler(props, bufferSize);
        hand.setWrapper(wrapper);
        return hand;
    }

    @Override
    protected boolean performHandshake() throws IOException {
        try {
            boolean state;
            String str = this.readLine();
            log.debug("Performing the handshake: " + str);
            Object obj = ProxyUtil.unmarshalFromReader(new StringReader(str), LiftConnectionRequestToken.class);
            if (validChallenge(obj)) {
                LiftConnectionResponseToken resp = new LiftConnectionResponseToken(LiftConnectionResponseToken.ALLOW);
                String res = ProxyUtil.marshalToString(resp);
                this.sendLine(res);
                state = true;
            } else {
                LiftConnectionResponseToken resp = new LiftConnectionResponseToken(LiftConnectionResponseToken.DENY);
                String res = ProxyUtil.marshalToString(resp);
                this.sendLine(res);
                state = false;
            }
            return state;
        } catch (JAXBException ex) {
            log.error("JAXB exception in handshake: " + ex.getMessage());
            throw new IOException ("JAXB exception in handshake: " + ex.getMessage());
        }
    }

    /**
     * Need to decide if the given token proves the caller is allowed to
     * establish a connection.
     * @param obj
     * @return
     */
    protected boolean validChallenge(Object obj) {
        boolean isKnown = false;
        if (obj instanceof LiftConnectionRequestToken) {
            token = (LiftConnectionRequestToken) obj;
            isKnown = props.verifySecurityForRequest(token);
            log.debug("Validation against database return a known entry: " + isKnown);
        } else {
            log.error("Handshake expects a LiftConnectionRequestToken: " + obj);
        }
        return isKnown;
    }

    @Override
    public void run() {
        /*
         * Try to connect to the server for this request.  May want to check if
         * a connection already exists which would then mean that this request
         * should be denied.
         */
        Socket socket = null;
        try {
            Thread toProxyThread;
            Thread fromProxyThread;

            socket = props.getSocketToServerForRequest(token);
            log.debug("Using socket " + socket.getInetAddress()  + ": " + socket.getPort() + " for this request " + token.getRequestGUID());
            Connector toProxy = new Connector(socket.getInputStream(), this.getWrapper().getOutStream(), bufferSize);
            Connector fromProxy = new Connector(this.getWrapper().getInStream(), socket.getOutputStream(), bufferSize);
           
            toProxyThread = new Thread(toProxy,"Request Server To Proxy");
            fromProxyThread = new Thread(fromProxy, " Request Server From Proxy");
            toProxyThread.start();
            fromProxyThread.start();

            log.debug("Waiting to finish " + toProxyThread.getName());
            toProxyThread.join();
            log.debug("Waiting to finish " + fromProxyThread.getName());
            fromProxyThread.join();
            log.debug("Both Request Server communications complete");

            // Access to file is completed
            props.completeProcessingForRequest(token);

        } catch (IOException e) {
            log.error("Problem in creating client to proxy connectors: " + e.getMessage());
            props.terminateProcessingForRequest(token);
        } catch (InterruptedException e) {
            log.error("Client to proxy communication thread interrupted: " + e.getMessage());
            props.terminateProcessingForRequest(token);
        } finally {
            if (socket != null) {
                try {
                    // Also closes associated streams
                    log.debug("Closing request server socket " + socket.getInetAddress()  + ": " + socket.getPort());
                    socket.close();
                } catch (IOException ex) {
                    log.warn("Unable to close request server socket: " + ex.getMessage());
                }
            }
            if (this.getWrapper() != null) {
                try {
                    log.debug("Closing proxy connection ");
                    this.getWrapper().close();
                } catch (IOException ex) {
                    log.warn("Unable to close proxy connection: " + ex.getMessage());
                }
            }
        }
    }
}
