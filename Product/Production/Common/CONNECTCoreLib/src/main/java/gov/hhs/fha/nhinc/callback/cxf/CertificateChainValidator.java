/*
 * Copyright (c) 2009-2016, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.callback.cxf;




import org.apache.commons.codec.binary.Hex;

import gov.hhs.fha.nhinc.callback.openSAML.CertificateManager;
import gov.hhs.fha.nhinc.callback.openSAML.CertificateManagerImpl;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import org.apache.ws.security.components.crypto.DERDecoder;
import org.apache.ws.security.components.crypto.X509SubjectPublicKeyInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validate chain of trust
 * @author mpnguyen
 *
 */
public class CertificateChainValidator {
    private static final Logger LOG = LoggerFactory.getLogger(CertificateChainValidator.class);
    private final CertificateManager certificateManager;
    private PropertyAccessor accessor;

    /**
     *
     */
    CertificateChainValidator() {
        certificateManager = CertificateManagerImpl.getInstance();
        accessor = PropertyAccessor.getInstance();
    }


    /**
     * @param certificateManager
     */
    CertificateChainValidator(CertificateManager certificateManager) {
        this.certificateManager = certificateManager;
    }


    /**
     *
     * @param certToCheck
     * @return true if cert is part of chain of trust or self-sign cert
     */
    public boolean isPartChainOfTrust(X509Certificate certToCheck) {
        try {
            // Try to verify certificate signature with its own public key
            PublicKey key = certToCheck.getPublicKey();
            certToCheck.verify(key);
            return false;
        } catch (SignatureException | InvalidKeyException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException sigEx) {
            // LOG.warn(sigEx.getLocalizedMessage(),sigEx);
            return true;
        }

    }

    /**
     * Validate chain of tr
     * @param certToCheck
     * @return
     */
    public boolean validateCert(X509Certificate certToCheck) {
        //check if cert is chain of trust
        if(isPartChainOfTrust(certToCheck)){
            //extract intermediate/root out and compare with trust store
            //extract intermediate
            try {
                /*Crypto crypto = CryptoFactory.getInstance("signature.properties");
                LOG.debug(crypto.getCryptoProvider());
                String issuerString = certToCheck.getIssuerX500Principal().getName();
                BigInteger issuerSerial = certToCheck.getSerialNumber();
                LOG.debug("Intermediate cert {} and serial number {} ", issuerString,issuerSerial.toString(16));*/
                /* List<X509Certificate> chain = Arrays.asList(certToCheck);

                final X509CertSelector certSelector = new X509CertSelector();
                certSelector.setCertificate(certToCheck);

                final CertPathParameters certPathParameters = new PKIXBuilderParameters(certificateManager.getTrustStore(),certSelector);
                final CertPathBuilder certPathBuilder = CertPathBuilder.getInstance("PKIX");
                final PKIXCertPathBuilderResult certPathBuilderResult = (PKIXCertPathBuilderResult)certPathBuilder.build(certPathParameters);
                CertPath cp =  certPathBuilderResult.getCertPath();*/
                //---------

                byte[] signature = certToCheck.getSignature();
                LOG.debug("Algroithm {} and serial number {} and hex {}",certToCheck.getSigAlgName(),certToCheck.getSerialNumber().toString(16));
                String issuerString = certToCheck.getIssuerX500Principal().getName();
                LOG.debug("Issuer or intermediate {} ",issuerString);
                // String authorityKeyId = getExtensionValue(certToCheck,"2.5.29.35");
                byte[]authorityKeyIdentifier = certToCheck.getExtensionValue("2.5.29.35");//AuthorityKeyIdentifier
                /* byte[]authorityKeyIdentifier = certToCheck.getExtensionValue("2.5.29.35");//AuthorityKeyIdentifier
                ASN1OctetString akiOc = ASN1OctetString.getInstance(authorityKeyIdentifier);
                AuthorityKeyIdentifier aki = AuthorityKeyIdentifier.getInstance(akiOc.getOctets());*/
                byte[]subjetKey = certToCheck.getExtensionValue("2.5.29.14");//SubjectKey
                DERDecoder extVal = new DERDecoder(subjetKey);
                extVal.expect(DERDecoder.TYPE_OCTET_STRING);  // ExtensionValue OCTET STRING
                int Length = extVal.getLength();
                extVal.expect(DERDecoder.TYPE_OCTET_STRING);  // KeyIdentifier OCTET STRING
                int keyIDLen = extVal.getLength();
                byte[]hexValue = extVal.getBytes(keyIDLen);
                LOG.debug("Subject Key Value {}",Hex.encodeHexString(hexValue));

                DERDecoder extValA = new DERDecoder(authorityKeyIdentifier);
                extValA.skip(6);
                byte[]hexValueA = extValA.getBytes(20);
                LOG.debug("Value {}",Hex.encodeHexString(hexValueA));

                //String value = fromByteArray(Hex.encodeHex(authorityKeyIdentifier));


                List<X509Certificate>allCerts = getAllCertInKeyStore(certificateManager.getTrustStore());
                for (X509Certificate cert : allCerts){

                    /* PublicKey publicKey = cert.getPublicKey();
                    try{
                        Signature signateuTest = Signature.getInstance(certToCheck.getSigAlgName());
                        signateuTest.initVerify(cert.getPublicKey());
                        //signateuTest.update(certToCheck.getSignature());
                        boolean verify =signateuTest.verify(certToCheck.getSignature());
                        if (verify){
                            LOG.debug("Matching "+cert.getSerialNumber().toString(16));
                        }

                    }catch(Exception ex){
                        //LOG.warn(ex.getLocalizedMessage(),ex);
                        LOG.warn("Got exception for cert number: {}",cert.getSerialNumber().toString(16));
                    }*/
                    String name = cert.getSubjectX500Principal().getName();
                    if (issuerString.equals(name)){
                        LOG.debug("Check the cert serial number: {}",cert.getSerialNumber().toString(16));
                        X509SubjectPublicKeyInfo subjectPublicKey = new X509SubjectPublicKeyInfo(cert.getPublicKey());
                        byte[]subjectKeyAgain = subjectPublicKey.getSubjectPublicKey();
                        byte[]subjectKeyArray = cert.getExtensionValue("2.5.29.14");//Subject Key Identify
                        if (Arrays.equals(authorityKeyIdentifier, subjectKeyArray)){
                            LOG.debug("Found the matching {}",cert.getSerialNumber().toString(16));
                        }
                        /* byte[]subjectKey = cert.getExtensionValue("2.5.29.14");//Subject Key Identify


                        LOG.debug("Compare leaf {} vs {} ",new String(authorityKeyIdentifier),new String(subjectKey));
                        if (Arrays.equals(authorityKeyIdentifier, subjectKey)){
                            LOG.debug("Found the matching {}",cert.getSerialNumber().toString(16));
                        }*/
                        // String subjectKey = getExtensionValue(cert, "2.5.29.14");
                        // LOG.debug("Compare leaf {} vs {} ",authorityKeyId,subjectKey);
                        /*if (authorityKeyId.equals(subjectKey)){
                            LOG.debug("Found the matching {}",cert.getSerialNumber().toString(16));
                        }*/


                    }

                }
                //----------




                /* final CertPathBuilderResult certPathBuilderResult = certPathBuilder.build(certPathParameters);
                final CertPath certPath = certPathBuilderResult.getCertPath();*/
                /*CertStoreParameters intermediates = new CollectionCertStoreParameters(chain);
                certPathParameters.addCertStore(CertStore.getInstance("Collection", intermediates));*/


                /*CryptoType cryptoType = new CryptoType(CryptoType.TYPE.ISSUER_SERIAL);
                //cryptoType.setIssuerSerial(issuerString, issuerSerial);
                X509Certificate[] foundCerts = crypto.getX509Certificates(cryptoType);
                for (X509Certificate cert : foundCerts){
                    LOG.debug(cert.getIssuerDN().getName()+"-->"+cert.toString());
                }*/

                return true;
            } catch ( Exception e) {
                LOG.error(e.getLocalizedMessage(),e);
            }
            /*Merlin merlin = new Merlin();
            merlin.setKeyStore(certificateManager.getKeyStore());
            merlin.setTrustStore(certificateManager.getTrustStore());*/


        }
        return false;
    }
    private List<X509Certificate> getAllCertInKeyStore(KeyStore store){
        List<X509Certificate>cerList = new ArrayList<>();
        try {
            for (Enumeration<String> e = store.aliases(); e.hasMoreElements();) {
                String alias = e.nextElement();
                //LOG.debug("Alias {}",alias);
                X509Certificate cert = (X509Certificate)store.getCertificate(alias);
                Certificate[] certs = store.getCertificateChain(alias);
                cerList.add(cert);
            }
        } catch (KeyStoreException e) {
            LOG.error(e.getLocalizedMessage(),e);
        }
        return cerList;


    }
    public static String fromByteArray(byte[] bytes)
    {
        return new String(asCharArray(bytes));
    }

    /**
     * Do a simple conversion of an array of 8 bit characters into a string.
     *
     * @param bytes 8 bit characters.
     * @return resulting String.
     */
    public static char[] asCharArray(byte[] bytes)
    {
        char[] chars = new char[bytes.length];

        for (int i = 0; i != chars.length; i++)
        {
            chars[i] = (char)(bytes[i] & 0xff);
        }

        return chars;
    }
}
