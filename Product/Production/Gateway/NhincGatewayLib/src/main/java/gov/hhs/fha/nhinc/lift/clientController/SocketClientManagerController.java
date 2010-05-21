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

    @Override
    public void run() {
        /*
         * TODO Need to accept connections, read in data, and send data to the
         * ClientManager if possible.
         */

        while (true) {
            try {
                Socket socket = server.accept();

                while (socket == null) {
                    socket = server.accept();
                }

                InputStream in = socket.getInputStream();

                // TODO Read information from the stream.
                String message = InterProcessSocketProtocol.readData(in);
                log.info("SocketClientManagerController received message: " + message);
                if (message != null) {
                    LiftMessage m = (LiftMessage) JaxbUtil.unmarshalFromReader(new StringReader(message), LiftMessage.class);

                    // TODO Attempt to use the information to called the manager
                    manager.startClient(m);
                }
            } catch (IOException e) {
                // TODO Use a better logging scheme than just standard out.
                e.printStackTrace();
                log.error("CMC Error: " + e.getMessage());
            }
        }
    }
}
