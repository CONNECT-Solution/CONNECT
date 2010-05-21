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
// FILE: ClientApp.java
//
// Copyright (C) 2010 Harris Corporation. All rights reserved.
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: ClientManagerControllerApp.java
//
// LIMITATIONS: None
//
// SOFTWARE HISTORY:
//
//
//Feb 24 2010 PTR xxx R.Robinson   Initial Coding.
//
//********************************************************************
package gov.hhs.fha.nhinc.lift;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.lift.proxy.properties.interfaces.ConsumerProxyPropertiesFacade;
import gov.hhs.fha.nhinc.lift.proxy.properties.imp.ConsumerProxyPropertiesFacadeRI;
import gov.hhs.fha.nhinc.lift.clientManager.client.properties.interfaces.ClientPropertiesFacade;
import gov.hhs.fha.nhinc.lift.clientManager.client.properties.imp.ClientPropertiesService;
import gov.hhs.fha.nhinc.lift.clientManager.client.LSTClientManager;
import gov.hhs.fha.nhinc.lift.clientController.SocketClientManagerController;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author vvickers
 */
public class ClientApp {

    private static Log log = LogFactory.getLog(ClientApp.class);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {

            String clientIP = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.LIFT_CLIENT_IP);
            String clientPort = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.LIFT_CLIENT_PORT);
            SocketAddress saddr = new InetSocketAddress(clientIP, Integer.parseInt(clientPort));
            ServerSocket server = new ServerSocket();
            server.bind(saddr);

            ClientPropertiesFacade props = new ClientPropertiesService();
            ConsumerProxyPropertiesFacade proxyProps = new ConsumerProxyPropertiesFacadeRI();
            proxyProps.setTrustStore();

            LSTClientManager manager = new LSTClientManager(props, proxyProps);
            SocketClientManagerController con = new SocketClientManagerController(server, manager);

            (new Thread(con)).start();
            System.out.println("ClientApp started: " + server);

        } catch (PropertyAccessException ex) {
            log.error(ex.getMessage());
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }
}
