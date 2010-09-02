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
import java.security.Provider;
import java.security.Security;
import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

public class SSLServer extends Server {
    private static final String NONE = "NONE";

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

            boolean isPKCSKeyStore = false;
            
            // Check whether the keystore is set to NONE
            if (NONE.equalsIgnoreCase(System.getProperty("javax.net.ssl.keyStore"))){
                log.info("Set up SUNPKCS11 Provider");
                isPKCSKeyStore = true;
                setupProvider();
            }

            char[] passphrase = System.getProperty("javax.net.ssl.keyStorePassword").toCharArray();
            ctx = SSLContext.getInstance("TLS");
            kmf = KeyManagerFactory.getInstance("SunX509");

            if (isPKCSKeyStore){
                ks = KeyStore.getInstance("PKCS11");
                ks.load(null, passphrase);
            }else{
                ks = KeyStore.getInstance("JKS");
                ks.load(new FileInputStream(System.getProperty("javax.net.ssl.keyStore")), passphrase);
            }

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

            // Restrict ciphers only for FIPS
            if (NONE.equalsIgnoreCase(System.getProperty("javax.net.ssl.keyStore"))){
                String[] enabledCipherSuites = getEnabledCipherSuites();

                if (enabledCipherSuites != null){
                    socket.setEnabledCipherSuites(enabledCipherSuites);
                }else{
                    log.error("FIPS Mode: Cipher Suites are not available");
                    throw new IOException("FIPS Mode: Cipher suites are not available");
                }
            }
            
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

    private void setupProvider(){

        String configFile;

        try {
            configFile = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.LIFT_SERVER_NSS_CONFIG);    
        } catch (Exception e) {
            log.error("Unable to read location of NSS config file", e);
            throw new RuntimeException(e);
        }


        String providerName = "SunPKCS11-NSS";
        // Check whether the provider exists

        Provider sunpkcs11Provider = Security.getProvider(providerName);

        if (sunpkcs11Provider == null){
            sunpkcs11Provider = new sun.security.pkcs11.SunPKCS11(configFile);
            Security.insertProviderAt(sunpkcs11Provider, 1);
        }
    }

    /**
     *
     * @return
     */
    private String[] getEnabledCipherSuites(){
        
        String cipherSuites = null;
        
        try {
            // Retrieve
            cipherSuites = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.LIFT_CIPHER_SUITES);
            
        } catch (Exception e) {
            log.error("Unable to read Cipher Suites from gateway.properties file");
            return null;
        }
        
        return cipherSuites.split(",");
    }
}
