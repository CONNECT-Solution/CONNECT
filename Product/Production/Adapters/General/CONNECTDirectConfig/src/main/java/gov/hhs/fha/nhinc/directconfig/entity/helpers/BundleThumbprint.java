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

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer
in the documentation and/or other materials provided with the distribution.
3. Neither the name of the The NHIN Direct Project (nhindirect.org) nor the names of its contributors may be used to endorse or promote
products derived from this software without specific prior written permission.

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
import java.util.Arrays;

/**
 * An bundle thumb print. Thumb print is essentially a SHA-1 digest of
 * the raw binary bundle file.
 * @author Greg Meyer
 * @since 1.2
 */
public class BundleThumbprint {
    private final byte[] digest;
    private final String digestString;

    /**
     * Creates a thumbprint of a byte array.
     *
     * @param bytes
     *            The byte array to convert to a thumbprint.
     * @return A thumbprint of the byte array.
     * @throws CertificateException
     */
    public static BundleThumbprint toThumbprint(byte[] bytes) throws NoSuchAlgorithmException {
        if (bytes == null) {
            throw new IllegalArgumentException();
        }

        final BundleThumbprint retVal = new BundleThumbprint(bytes);

        return retVal;
    }

    private BundleThumbprint (byte[] bytes) throws NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance("SHA-1");

        md.update(bytes);
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

    @Override
    /**
     * {@inheritDoc}
     */
    public String toString() {
        return digestString;
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public boolean equals(Object obj) {
        boolean isEqual = false;

        if (obj instanceof BundleThumbprint) {
            final BundleThumbprint compareTo = (BundleThumbprint) obj;

            // deep compare
            isEqual = Arrays.equals(compareTo.digest, digest);
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Arrays.hashCode(this.digest);
        return hash;
    }
}
