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
// FILE: ClientConnectorManager.javas
//
// Copyright (C) 2010 Harris Corporation. All rights reserved.
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: ClientConnectorManager.java 
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
import gov.hhs.fha.nhinc.lift.proxy.properties.interfaces.ConsumerProxyPropertiesFacade;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author rrobin20
 *
 */
public class ClientConnectorManager {

    private Log log = null;
    private static ClientConnectorManager instance;
    // should maintain references to currently active client connections it creates.
    private final Map<Integer, Thread> connectors;

    protected ClientConnectorManager() {
        connectors = new HashMap<Integer, Thread>();
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    /**
     * This is a singleton so this method is in place to return a single
     * instance of this class.
     * @return
     */
    public static ClientConnectorManager getInstance() {
        if (instance == null) {
            instance = new ClientConnectorManager();
        }

        return instance;
    }

    /**
     * This method will create a tunnel of a type defined by the properties
     * facade and will then bind a local temporary port for a client app to use
     * to communicate through the proxy tunnel.  Returns an address to the
     * local server a client can talk to.
     *
     * @param token
     * @param serverProxyAddress
     * @param serverProxyPort
     * @return
     * @throws IOException
     */
    public InetSocketAddress startConnector(RequestToken token,
            InetAddress serverProxyAddress, int serverProxyPort, int bufferSize,
            ConsumerProxyPropertiesFacade props) throws IOException {
        /*
         * Attempts to start up a connection with the desired server proxy.
         *
         * Which type of client to use should come from configuration somewhere.
         */
        log.debug("Creating Client instance to connect to server proxy: " + serverProxyAddress + ":" +serverProxyPort);
        Client client = props.getClientInstance(serverProxyAddress, serverProxyPort, token);

        /*
         * Start up a socket server bound to the local proxy hostname and to a
         * port unique to this request.
         */
        InetAddress localProxyAddress = props.getClientProxyAddress();
        log.debug("Local client proxy address set as: " + localProxyAddress);

        InetSocketAddress connectorAddress = new InetSocketAddress(localProxyAddress, 0);
        log.debug("Starting server socket for client to access on port: " + connectorAddress.getPort());

        ServerSocket server = new ServerSocket();
        server.bind(connectorAddress);
        log.debug("Server bound to port: " + server.getLocalPort());

        ClientConnector connector = new ClientConnector(server, client, bufferSize);
        Thread conn = new Thread(connector);
        connectors.put(connectorAddress.getPort(), conn);

        log.debug("Starting new Client Connector thread.");
        conn.start();

        return new InetSocketAddress(server.getInetAddress(), server.getLocalPort());
    }

    private ServerSocketFactory getTLSServerSocketFactory() {

        ServerSocketFactory ssf = ServerSocketFactory.getDefault();
        try {
            // set up key manager to do server authentication
            SSLContext ctx;
            KeyManagerFactory kmf;
            KeyStore ks;
            char[] passphrase = System.getProperty("javax.net.ssl.keyStorePassword").toCharArray();

            ctx = SSLContext.getInstance("TLS");
            kmf = KeyManagerFactory.getInstance("SunX509");
            ks = KeyStore.getInstance("JKS");

            ks.load(new FileInputStream(System.getProperty("javax.net.ssl.keyStore")), passphrase);
            kmf.init(ks, passphrase);
            ctx.init(kmf.getKeyManagers(), null, null);

            ssf = ctx.getServerSocketFactory();
            return ssf;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ssf;
    }

    /**
     * Returns true if a connector thread was stopped.  False if not.  Blocks
     * while waiting for the ClientConnector to halt.
     * @param address
     * @return
     */
    public boolean stopConnector(InetSocketAddress address) {
        Thread t = connectors.get(address.getPort());

        try {
            if (t != null) {
                t.interrupt();

                t.join();

                return true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }
}
