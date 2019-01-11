/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
/*
Copyright (c) 2010, NHIN Direct Project
All rights reserved.

Authors:
   Umesh Madan     umeshma@microsoft.com
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

package gov.hhs.fha.nhinc.directconfig.entity.helpers;

import gov.hhs.fha.nhinc.directconfig.exception.CertificateException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

/**
 * NOTE: This is a copy of the original Thumbprint class from the agent code
 * base. Copied to this package as a quick fix to a visibility issue.
 *
 * An X509Certificate thumb print. Thumb print is essentially a SHA-1 digest of
 * the certificates der encoding.
 *
 * @author Greg Meyer
 */
public class Thumbprint {
    private final byte[] digest;
    private final String digestString;

    /**
     * Creates a thumbprint of an X509Certificate.
     *
     * @param cert
     *            The certificate to convert.
     * @return A thumbprint of the certificate.
     * @throws CertificateException
     */
    public static Thumbprint toThumbprint(X509Certificate cert) throws CertificateException {
        if (cert == null) {
            throw new IllegalArgumentException();
        }

        try {
            final Thumbprint retVal = new Thumbprint(cert);
            return retVal;
        } catch (NoSuchAlgorithmException nsae) {
            throw new CertificateException(nsae);
        } catch (CertificateEncodingException cee) {
            throw new CertificateException(cee);
        }
    }

    private Thumbprint (X509Certificate cert) throws NoSuchAlgorithmException, CertificateEncodingException {
        final MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] der = cert.getEncoded();

        md.update(der);
        digest = md.digest();

        digestString = createStringRep();
    }

    /**
     * Gets the raw byte digest of the certificate's der encoding.
     *
     * @return The certificates digest.
     */
    public byte[] getDigest() {
        return digest.clone();
    }

    private String createStringRep() {
        final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        final StringBuffer buf = new StringBuffer(digest.length * 2);

        for (byte bt : digest) {
            buf.append(hexDigits[(bt & 0xf0) >> 4]);
            buf.append(hexDigits[bt & 0x0f]);
        }

        return buf.toString();
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public String toString() {
        return digestString;
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Thumbprint)) {
            return false;
        }

        final Thumbprint compareTo = (Thumbprint)obj;

        // deep compare
        return Arrays.equals(compareTo.digest, digest);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Arrays.hashCode(this.digest);
        return hash;
    }
}
