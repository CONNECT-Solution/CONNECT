/*
Copyright (c) 2010, NHIN Direct Project
All rights reserved.

Authors:
   Greg Meyer      gm2552@cerner.com

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer
in the documentation and/or other materials provided with the distribution.  Neither the name of the The NHIN Direct Project (nhindirect.org).
nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
THE POSSIBILITY OF SUCH DAMAGE.
*/

package gov.hhs.fha.nhinc.directconfig.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.x500.X500Principal;

import org.apache.commons.io.FileUtils;
import gov.hhs.fha.nhinc.directconfig.exceptions.CertificateConversionException;

/**
 * Certificate utility methods.
 * @author Greg Meyer
 * @since 1.0
 */
///CLOVER:OFF
public class CertUtils
{
    private static final int RFC822Name_TYPE = 1; // name type constant for Subject Alternative name email address
    private static final int DNSName_TYPE = 2; // name type constant for Subject Alternative name domain name

    private static final String DEFAULT_JCE_PROVIDER_STRING = "BC";
    private static final String JCE_PROVIDER_STRING_SYS_PARAM = "org.nhindirect.config.JCEProviderName";

    static
    {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    /**
     * Gets the configured JCE crypto provider string for crypto operations.  This is configured using the
     * -Dorg.nhindirect.config.JCEProviderName JVM parameters.  If the parameter is not set or is empty,
     * then the default string "BC" (BouncyCastle provider) is returned.  By default the agent installs the BouncyCastle provider.
     * @return The name of the JCE provider string.
     */
    public static String getJCEProviderName()
    {
        String retVal = System.getProperty(JCE_PROVIDER_STRING_SYS_PARAM);

        if (retVal == null || retVal.isEmpty())
            retVal = DEFAULT_JCE_PROVIDER_STRING;

        return retVal;
    }

    /**
     * Overrides the configured JCE crypto provider string.  If the name is empty or null, the default string "BC" (BouncyCastle provider)
     * is used.
     * @param name The name of the JCE provider.
     */
    public static void setJCEProviderName(String name)
    {
        if (name == null || name.isEmpty())
            System.setProperty(JCE_PROVIDER_STRING_SYS_PARAM, DEFAULT_JCE_PROVIDER_STRING);
        else
            System.setProperty(JCE_PROVIDER_STRING_SYS_PARAM, name);
    }

    /**
     * Gets the owner of the certificate with is the email address of domain bound to the certificate.
     * The subject alt name is checked first, then the legacy email field, and lastsly the common name field.
     * @param certificate The certificate of the to get the owner of.
     * @return The owner of the certificate
     */
    public static String getOwner(X509Certificate certificate)
    {
        String address = "";
        // check alternative names first
        Collection<List<?>> altNames = null;
        try
        {
            altNames = certificate.getSubjectAlternativeNames();
        }
        catch (CertificateParsingException ex)
        {
            /* no -op */
        }

        if (altNames != null)
        {
            for (List<?> entries : altNames)
            {
                if (entries.size() >= 2) // should always be the case according the altNames spec, but checking to be defensive
                {

                    Integer nameType = (Integer)entries.get(0);
                    // prefer email over over domain?
                    if (nameType == RFC822Name_TYPE)
                        address = (String)entries.get(1);
                    else if (nameType == DNSName_TYPE && address.isEmpty())
                        address = (String)entries.get(1);
                }
            }
        }

        if (!address.isEmpty())
            return address;

        // can't find subject address in alt names... try the principal
        X500Principal issuerPrin = certificate.getSubjectX500Principal();

        // get the domain name
        Map<String, String> oidMap = new HashMap<String, String>();
        oidMap.put("1.2.840.113549.1.9.1", "EMAILADDRESS");  // OID for email address
        String prinName = issuerPrin.getName(X500Principal.RFC1779, oidMap);

        // see if there is an email address first in the DN
        String searchString = "EMAILADDRESS=";
        int index = prinName.indexOf(searchString);
        if (index == -1)
        {
            searchString = "CN=";
            // no Email.. check the CN
            index = prinName.indexOf(searchString);
            if (index == -1)
                return ""; // no CN... nothing else that can be done from here
        }

        // look for a "," to find the end of this attribute
        int endIndex = prinName.indexOf(",", index);
        if (endIndex > -1)
            address = prinName.substring(index + searchString.length(), endIndex);
        else
            address= prinName.substring(index + searchString.length());

        return address;
    }

    /**
     * Takes a PKCS12 byte stream and returns a PKCS12 byte stream with the pass phrase protection and encryption removed.
     * @param bytes The PKCS12 byte stream that will be stripped.
     * @param passphrase The pass phrase of the PKCS12 byte stream.  This is used to decrypt the PKCS12 stream.
     * @return A PKCS12 byte stream representation of the original PKCS12 stream with the pass phrase protection and encryption removed.
     */
    public static byte[] pkcs12ToStrippedPkcs12(byte[] bytes, String passphrase)
    {
        return changePkcs12Protection(bytes, passphrase.toCharArray(), passphrase.toCharArray(), "".toCharArray(), "".toCharArray());
    }


    /**
     * Modifies the keystore and private key protection on a PKCS12 keystore.
     * @param bytes The PKCS12 encoded as byte array that will be modified.
     * @param oldKeyStorePassPhrase The current pass phrase protecting the keystore file.
     * @param oldPrivateKeyPassPhrase The current pass phrase protecting the private key.
     * @param newKeystorePassPhrase The new pass phrase protecting the keystore file.
     * @param newPrivateKeyPassPhrase The new pass phrase protecting the private key.
     * @return The modified PKCS12 key store encoded as a byte array/
     */
    public static byte[] changePkcs12Protection(byte[] bytes, char[] oldKeyStorePassPhrase,
            char[] oldPrivateKeyPassPhrase, char[] newKeystorePassPhrase, char[] newPrivateKeyPassPhrase)
    {
        if (bytes == null || bytes.length == 0)
            throw new IllegalArgumentException("Pkcs byte stream cannot be null or empty.");

        byte[] retVal = null;
        final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        final ByteArrayOutputStream outStr = new ByteArrayOutputStream();
        // lets try this a as a PKCS12 data stream first
        try
        {
            final KeyStore localKeyStore = KeyStore.getInstance("PKCS12", getJCEProviderName());

            localKeyStore.load(bais, oldKeyStorePassPhrase);
            final Enumeration<String> aliases = localKeyStore.aliases();



            // we are really expecting only one alias
            if (aliases.hasMoreElements())
            {
                final String alias = aliases.nextElement();
                X509Certificate cert = (X509Certificate)localKeyStore.getCertificate(alias);

                // check if there is private key
                final Key key = localKeyStore.getKey(alias, oldPrivateKeyPassPhrase);
                if (key != null && key instanceof PrivateKey)
                {
                    // now convert to a pcks12 format without the new passphrase

                    localKeyStore.setKeyEntry("privCert", key, newPrivateKeyPassPhrase,  new java.security.cert.Certificate[] {cert});

                    localKeyStore.store(outStr, newKeystorePassPhrase);

                    retVal = outStr.toByteArray();

                }
            }
        }
        catch (Exception e)
        {
            throw new CertificateConversionException("Failed to strip encryption for PKCS stream.", e);
        }
        finally
        {
            try {bais.close(); }
            catch (Exception e) {/* no-op */}

            try {outStr.close(); }
            catch (Exception e) {/* no-op */}
        }

        return retVal;
    }

    /**
     * Converts a byte stream to an X509Certificate.  The byte stream can either be an encoded X509Certificate or a PKCS12 byte stream.
     * <p>
     * If the stream is a PKCS12 representation, then an empty ("") pass phrase is used to decrypt the stream.  In addition the resulting X509Certificate
     * implementation will contain the private key.
     * @param data  The byte stream representation to convert.
     * @return An X509Certificate representation of the byte stream.
     */
    public static X509Certificate toX509Certificate(byte[] data)
    {
        return toX509Certificate(data, "");
    }

    /**
     * Converts a byte stream to an X509Certificate.  The byte stream can either be an encoded X509Certificate or a PKCS12 byte stream.
     * <p>
     * If the stream is a PKCS12 representation, then the pass phrase is used to decrypt the stream.  In addition the resulting X509Certificate
     * implementation will contain the private key.
     * @param data The byte stream representation to convert.
     * @param passPhrase  If the byte stream is a PKCS12 representation, then the then the pass phrase is used to decrypt the stream.  Can be
     * null if the stream is an encoded X509Certificate and not a PKCS12 byte stream.
     * @return  An X509Certificate representation of the byte stream.
     */
    public static X509Certificate toX509Certificate(byte[] data, String passPhrase)
    {
        if (data == null || data.length == 0)
            throw new IllegalArgumentException("Byte stream cannot be null or empty.");

        // do not use a null pass phrase
        if (passPhrase == null)
            passPhrase = "";

        X509Certificate retVal = null;
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        try
        {

            // lets try this a as a PKCS12 data stream first
            try
            {
                KeyStore localKeyStore = KeyStore.getInstance("PKCS12", getJCEProviderName());

                localKeyStore.load(bais, passPhrase.toCharArray());
                Enumeration<String> aliases = localKeyStore.aliases();


                // we are really expecting only one alias
                if (aliases.hasMoreElements())
                {
                    String alias = aliases.nextElement();
                    X509Certificate cert = (X509Certificate)localKeyStore.getCertificate(alias);

                    // check if there is private key
                    Key key = localKeyStore.getKey(alias, passPhrase.toCharArray());
                    if (key != null && key instanceof PrivateKey)
                    {
                        retVal = cert;
                    }
                }
            }
            catch (Exception e)
            {
                // must not be a PKCS12 stream, try next step
            }

            if (retVal == null)
            {
                //try X509 certificate factory next
                bais.reset();
                bais = new ByteArrayInputStream(data);

                retVal = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(bais);
            }
        }
        catch (Exception e)
        {
            throw new CertificateConversionException("Failed to convert byte stream to a certificate.", e);
        }
        finally
        {
            try {bais.close();} catch (IOException ex) {}
        }

        return retVal;
    }

    /**
     * Creates an X509Certificate object from an existing file.  The file should be a DER encoded representation of the certificate.
     * @param certFile The file to load into a certificate object.
     * @return An X509Certificate loaded from the file.
     */
    public X509Certificate certFromFile(String certFile)
    {
        final File theCertFile = new File(certFile);
        try
        {
            return toX509Certificate(FileUtils.readFileToByteArray(theCertFile));
        }
        catch (Exception e)
        {
            // this is used as a factory method, so just return null if the certificate could not be loaded
            // instead of throwing an exception, but make sure the error is logged
            return null;
        }
    }

    public static CertContainer toCertContainer(byte[] data) throws CertificateConversionException
    {
        return toCertContainer(data, "".toCharArray(), "".toCharArray());
    }

    /**
     * Creates a certificate container that consists of the X509 certificate and its private key (if it exists).
     * @param data A DER encoded representation of either an X509 certificate or an unencrypted PKCS12 container.
     * @return A container object with the X509 certificate and private key (it it exists).
     * @throws CertificateConversionException
     */
    public static CertContainer toCertContainer(byte[] data, char[] keyStorePassPhrase, char[] privateKeyPassPhrase) throws CertificateConversionException
    {
        CertContainer certContainer = null;
        try
        {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);

            // lets try this a as a PKCS12 data stream first
            try
            {
                KeyStore localKeyStore = KeyStore.getInstance("PKCS12", getJCEProviderName());

                localKeyStore.load(bais, keyStorePassPhrase);
                Enumeration<String> aliases = localKeyStore.aliases();


                // we are really expecting only one alias
                if (aliases.hasMoreElements())
                {
                    String alias = aliases.nextElement();
                    X509Certificate cert = (X509Certificate)localKeyStore.getCertificate(alias);

                    // check if there is private key
                    Key key = localKeyStore.getKey(alias, privateKeyPassPhrase);
                    if (key != null && key instanceof PrivateKey)
                    {
                        certContainer = new CertContainer(cert, key);

                    }
                }
            }
            catch (Exception e)
            {
                // must not be a PKCS12 stream, go on to next step
            }

            if (certContainer == null)
            {
                //try X509 certificate factory next
                bais.reset();
                bais = new ByteArrayInputStream(data);

                X509Certificate cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(bais);
                certContainer = new CertContainer(cert, null);
            }
            bais.close();
        }
        catch (Exception e)
        {
            throw new CertificateConversionException("Data cannot be converted to a valid X.509 Certificate", e);
        }

        return certContainer;
    }

    public static class CertContainer
    {
        private final X509Certificate cert;
        private final Key key;

        public CertContainer(X509Certificate cert, Key key)
        {
            this.cert = cert;
            this.key = key;
        }

        public X509Certificate getCert()
        {
            return cert;
        }

        public Key getKey()
        {
            return key;
        }
    }


}
///CLOVER:ON