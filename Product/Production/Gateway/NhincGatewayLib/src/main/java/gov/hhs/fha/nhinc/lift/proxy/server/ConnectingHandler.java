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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConnectingHandler extends HandlerPrototype {

    private Log log = null;
    private final ProducerProxyPropertiesFacade props;
    private final int bufferSize;
    private LiftConnectionRequestToken token;

    public ConnectingHandler(ProducerProxyPropertiesFacade props, int bufferSize) {
        this.props = props;
        this.bufferSize = bufferSize;
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    @Override
    protected HandlerPrototype createInstance(ProtocolWrapper wrapper) {
        HandlerPrototype hand = new ConnectingHandler(props, bufferSize);
        hand.setWrapper(wrapper);
        return hand;
    }

    @Override
    protected boolean performHandshake() throws IOException {

        boolean state;
        String str = this.readLine();
        log.debug("Performing the handshake: " + str);
        Object obj = ProxyUtil.unmarshalFromReader(new StringReader(str), LiftConnectionRequestToken.class);

        if (validChallenge(obj)) {
            LiftConnectionResponseToken resp = new LiftConnectionResponseToken("granted");
            String res = ProxyUtil.marshalToString(resp);

            this.sendLine(res);

            state = true;
        } else {
            LiftConnectionResponseToken resp = new LiftConnectionResponseToken("denied");
            String res = ProxyUtil.marshalToString(resp);

            this.sendLine(res);

            state = false;
        }
        return state;
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
            LiftConnectionRequestToken token = (LiftConnectionRequestToken) obj;
            isKnown = props.verifySecurityForRequest(token);
            log.debug("Validation against database return a known entry: " + isKnown);
            this.token = token;
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
        try {
            Thread toProxyThread, fromProxyThread;
            Socket socket = props.getSocketToServerForRequest(token);

            try {
                //Connection established, build Connectors to pass through messages.
                Connector toProxy = new Connector(socket.getInputStream(), this.getWrapper().getOutStream(), bufferSize);
                log.debug("Connector started TO proxy");
                Connector fromProxy = new Connector(this.getWrapper().getInStream(), socket.getOutputStream(), bufferSize);
                log.debug("Connector started FROM proxy");

                toProxyThread = new Thread(toProxy);
                fromProxyThread = new Thread(fromProxy);
                toProxyThread.start();
                fromProxyThread.start();

                fromProxyThread.join();
                log.debug("Connector FROM proxy complete");

//				System.out.println("HANDLER: Closing socket because from proxy connector joined.");
//				socket.close();
//				System.out.println("HANDLER: Closing connection between proxies.");
//				this.getWrapper().close();

                toProxyThread.join();
                log.debug("Connector TO proxy complete");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                log.debug("Closing connection between proxies (if not yet closed).");
                this.getWrapper().close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
