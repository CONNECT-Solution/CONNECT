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
// FILE: SocketClientManagerController.java
//
// Copyright (C) 2010 Harris Corporation. All rights reserved.
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: SocketClientManagerController.java
//
// LIMITATIONS: None
//
// SOFTWARE HISTORY:
//
//
//Feb 24 2010 PTR xxx R.Robinson   Initial Coding.
//
//********************************************************************
package gov.hhs.fha.nhinc.lift.clientController;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;

import gov.hhs.fha.nhinc.lift.clientManager.client.ClientManager;
import gov.hhs.fha.nhinc.lift.common.util.LiftMessage;
import gov.hhs.fha.nhinc.lift.common.util.InterProcessSocketProtocol;
import gov.hhs.fha.nhinc.lift.common.util.JaxbUtil;
import gov.hhs.fha.nhinc.lift.proxy.GatewayLiftManagerProxy;
import gov.hhs.fha.nhinc.lift.proxy.GatewayLiftManagerProxyObjectFactory;
import gov.hhs.fha.nhinc.gateway.lift.CompleteLiftTransactionRequestType;
import gov.hhs.fha.nhinc.gateway.lift.CompleteLiftTransactionResponseType;
import gov.hhs.fha.nhinc.gateway.lift.FailedLiftTransactionRequestType;
import gov.hhs.fha.nhinc.gateway.lift.FailedLiftTransactionResponseType;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author rrobin20
 *
 */
public class SocketClientManagerController implements Runnable {

    private Log log = null;
    private final ServerSocket server;
    private final ClientManager manager;
    private Map<String, String> mesgState = new HashMap<String, String>();

    public SocketClientManagerController(ServerSocket server,
            ClientManager manager) {
        super();
        this.server = server;
        this.manager = manager;
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    public void reportFailure(String request, String errorMesg) {
        if (mesgState.containsKey(request)) {
                        System.out.println("Call back FAIL removing " + request);
            mesgState.remove(request);

            GatewayLiftManagerProxyObjectFactory factory = new GatewayLiftManagerProxyObjectFactory();
            GatewayLiftManagerProxy proxy = factory.getGatewayLiftManagerProxy();

            FailedLiftTransactionRequestType failedRequest = new FailedLiftTransactionRequestType();
            failedRequest.setRequestKeyGuid(request);
            failedRequest.setErrorMessage(errorMesg);

            FailedLiftTransactionResponseType response = proxy.failedLiftTransaction(failedRequest);
            log.debug("Reported failed file transfer status: " + response.getStatus());
        }
    }

    public void reportSuccess(String request, URI writtenFile) {
        if (mesgState.containsKey(request)) {
                        System.out.println("Call back SUCCESS removing " + request);
            mesgState.remove(request);

            GatewayLiftManagerProxyObjectFactory factory = new GatewayLiftManagerProxyObjectFactory();
            GatewayLiftManagerProxy proxy = factory.getGatewayLiftManagerProxy();

            CompleteLiftTransactionRequestType completeRequest = new CompleteLiftTransactionRequestType();
            completeRequest.setFileURI(writtenFile.toString());
            completeRequest.setRequestKeyGuid(request);

            CompleteLiftTransactionResponseType response = proxy.completeLiftTransaction(completeRequest);
            log.debug("Reported successful file transfer status: " + response.getStatus());
        }
    }

    @Override
    public void run() {

        while (true) {
            Socket socket = null;
            InputStream in = null;
            URI writtenFile = null;
            String errorMesg = null;
            LiftMessage mesg = null;
            try {

                socket = server.accept();
                while (socket == null) {
                    socket = server.accept();
                }
                in = socket.getInputStream();
                log.debug("Server " + server.getInetAddress() + " connecting to socket " + socket.getInetAddress() + ": " + socket.getPort());

                InterProcessSocketProtocol processSocket = new InterProcessSocketProtocol();
                String message = processSocket.readData(in);
                log.info("SocketClientManagerController received message: " + message);
                if (message != null) {
                    mesg = (LiftMessage) JaxbUtil.unmarshalFromReader(new StringReader(message), LiftMessage.class);
                    System.out.println("Setting " + mesg.getRequest().getRequest() + " to in progress");
                    mesgState.put(mesg.getRequest().getRequest(), "InProcess");
                    writtenFile = manager.startClient(mesg, this);
                }
            } catch (JAXBException ex) {
                errorMesg = "Client is unable to process LiftMessage " + ex.getMessage();
                log.error(errorMesg);
            } catch (IOException e) {
                errorMesg = "Client is unable to process incoming socket information " + e.getMessage();
                log.error(errorMesg);
            } finally {
                if (socket != null) {
                    try {
                        log.debug("Closing socket " + socket);
                        socket.close();
                    } catch (IOException ex) {
                        log.warn("Unable to close socket " + socket);
                    }
                }

                if (writtenFile != null) {
                    reportSuccess(mesg.getRequest().getRequest(), writtenFile);
                } else {
                    if(mesg != null && mesg.getRequest() != null && mesg.getRequest().getRequest() != null) {
                    reportFailure(mesg.getRequest().getRequest(), errorMesg);
                    } else {
                        reportFailure(null, errorMesg);
                    }

                }

            }
        }
    }
}
