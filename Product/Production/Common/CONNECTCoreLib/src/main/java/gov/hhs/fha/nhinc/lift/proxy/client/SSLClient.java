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
// FILE: SSLClient.java
//
// Copyright (C) 2010 Harris Corporation. All rights reserved.
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: SSLClient.java 
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
import gov.hhs.fha.nhinc.lift.proxy.util.DemoProtocol;
import gov.hhs.fha.nhinc.lift.proxy.util.ProtocolWrapper;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;


public class SSLClient extends Client {

    private SSLSocket socket;
    private ProtocolWrapper wrapper;

    public SSLClient(InetAddress address, int port, RequestToken token, ClientHandshaker handshaker)
            throws IOException {
        super(address, port, token, handshaker);
    }

    @Override
    protected boolean connect(InetAddress address, int port) throws IOException {
        SSLSocketFactory factory = getTLSSocketFactory();
        socket = (SSLSocket) factory.createSocket();

            // Restrict ciphers only for FIPS
            if ("NONE".equalsIgnoreCase(System.getProperty("javax.net.ssl.keyStore"))){
                String[] enabledCipherSuites = getEnabledCipherSuites();

                if (enabledCipherSuites != null){
                    socket.setEnabledCipherSuites(enabledCipherSuites);
                }else{
                    log.error("FIPS Mode: Cipher Suites are not available");
                    throw new IOException("FIPS Mode: Cipher suites are not available");
                }
            }


        socket.connect(new InetSocketAddress(address, port));
        log.debug("Create socket " + socket.getInetAddress() + ": " + socket.getPort());
        wrapper = new DemoProtocol(socket);

        return true;
    }

    @Override
    protected boolean performHandshake(ClientHandshaker handshaker) throws IOException {

        boolean success = handshaker.handshake(wrapper, this);
        // Return if was a good response or not.
        if (success) {
            return true;
        } else {
            log.error("Handshake with server failed. ");
            log.debug("Handshake failed close socket " + socket.getInetAddress() + ": " + socket.getPort());
            socket.close();
            throw new SSLHandshakeException(
                    "Failed handshaking process failed handshake with server.");
        }
    }

    private SSLSocketFactory getTLSSocketFactory() throws IOException {
        SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
        try {
            SSLContext ctx;
            KeyManagerFactory kmf;
            KeyStore ks;

            boolean isPKCSKeyStore = false;

            // Check whether the keystore is set to NONE
            if ("NONE".equalsIgnoreCase(System.getProperty("javax.net.ssl.keyStore"))){
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

            sf = ctx.getSocketFactory();
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
        return sf;
    }

    @Override
    public void sendLine(String mess) throws IOException {

        wrapper.sendLine(mess);
        log.debug("Sending message: " + mess);
    }

    @Override
    public String readLine() throws IOException {
        String mess = wrapper.readLine();
        log.debug("Reading message: " + mess);
        return mess;
    }

    @Override
    public void close() throws IOException {
        log.debug("Closing SSL Client socket " + socket.getInetAddress() + ": " + socket.getPort());
        socket.close();
    }

    @Override
    public InputStream getInStream() throws IOException {
        return socket.getInputStream();
    }

    @Override
    public OutputStream getOutStream() throws IOException {
        return socket.getOutputStream();
    }

    @Override
    public SSLSocket getSocket() {
        return socket;
    }

    private void setupProvider(){
        String configFile;

        try {
            configFile = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.LIFT_CLIENT_NSS_CONFIG);
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
