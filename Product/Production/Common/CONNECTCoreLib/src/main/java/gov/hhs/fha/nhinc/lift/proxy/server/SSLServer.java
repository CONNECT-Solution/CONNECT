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
//###################################################################
//********************************************************************
// FILE: SSLServer.java
//
// Copyright (C) 2010 Harris Corporation. All rights reserved.
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: SSLServer.java 
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
import java.net.SocketAddress;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;

import gov.hhs.fha.nhinc.lift.proxy.util.DemoProtocol;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.security.KeyStore;
import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

public class SSLServer extends Server {

    private final SSLServerSocket server;

    /**
     * Set up a SSL server socket on the provided address.  Makes and runs
     * clones of the provided HandlerPrototype to handle client connections
     * to the Server.
     * @param address
     * @param prototype
     * @throws IOException
     */
    public SSLServer(SocketAddress address, HandlerPrototype prototype) throws IOException {
        super(prototype);

        InetSocketAddress addr = (InetSocketAddress) address;
        ServerSocketFactory factory = getTLSServerSocketFactory();
        ServerSocket createdSocket = factory.createServerSocket(addr.getPort());
        server = (SSLServerSocket) createdSocket;
        server.setNeedClientAuth(true);
        log.info("Creating 2-way TLS server bound: " + server.getInetAddress() + ": " + server.getLocalPort());
    }

    public SSLServerSocket getServer() {
        return server;
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

        } catch (KeyManagementException ex) {
            log.warn("Problem in setting TLS Server: " + ex.getMessage() + " Using default");
        } catch (UnrecoverableKeyException ex) {
            log.warn("Problem in setting TLS Server: " + ex.getMessage() + " Using default");
        } catch (IOException ex) {
            log.warn("Problem in setting TLS Server: " + ex.getMessage() + " Using default");
        } catch (CertificateException ex) {
            log.warn("Problem in setting TLS Server: " + ex.getMessage() + " Using default");
        } catch (KeyStoreException ex) {
            log.warn("Problem in setting TLS Server: " + ex.getMessage() + " Using default");
        } catch (NoSuchAlgorithmException ex) {
            log.warn("Problem in setting TLS Server: " + ex.getMessage() + " Using default");
        }
        return ssf;
    }

    @Override
    protected void acceptConnection() throws IOException {
        SSLSocket socket = null;
        try {
            log.debug("Waiting for incoming connection.");
            socket = (SSLSocket) server.accept();
            log.debug("Accepted a new connection " + socket.getInetAddress()  + ": " + socket.getPort());
            /*
             * Spawn off and start a new Handler thread to process
             * the new connection.
             */
            HandlerPrototype hand = this.getPrototype().clone(new DemoProtocol(socket));
            if (hand != null) {
                log.debug("Passing connection " + socket.getInetAddress()  + ": " + socket.getPort() + " to new handler thread ");
                Thread handlerThread = new Thread(hand);
                handlerThread.start();

            } else {
                log.debug("Connection failed .");
                throw new IOException("Connection failed.");
            }

        } catch (IOException ex) {
            log.error("Problem in setting up new connection: " + ex.getMessage());
            throw new IOException("Problem in setting up new connection: " + ex.getMessage());
        }
    }
}
