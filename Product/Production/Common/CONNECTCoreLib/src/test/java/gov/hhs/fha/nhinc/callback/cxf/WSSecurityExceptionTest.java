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

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertPathValidatorException;
import org.apache.ws.security.WSSecurityException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test WSSecurityException which deals with several exceptions
 *
 * @author mpnguyen
 *
 */
public class WSSecurityExceptionTest {

    @Test
    public final void testCerPathException() {
        try {
            final boolean throwProvider = false;
            final boolean throwPath = true;
            final boolean throwAlg = false;
            generateWSSecurityException(throwProvider, throwPath, throwAlg);
        } catch (final WSSecurityException e) {
            final Throwable causes = e.getCause();
            final boolean status = causes instanceof CertPathValidatorException;
            Assert.assertTrue(status);
        }
    }

    @Test
    public final void testAlgorithmException() {
        try {
            final boolean throwProvider = false;
            final boolean throwPath = false;
            final boolean throwAlg = true;
            generateWSSecurityException(throwProvider, throwPath, throwAlg);
        } catch (final WSSecurityException e) {
            final Throwable causes = e.getCause();
            final boolean status = causes instanceof NoSuchAlgorithmException;
            Assert.assertTrue(status);
        }
    }

    @Test
    public final void testWrongException() {
        try {
            final boolean throwProvider = true;
            final boolean throwPath = false;
            final boolean throwAlg = false;
            generateWSSecurityException(throwProvider, throwPath, throwAlg);
        } catch (final WSSecurityException e) {
            final Throwable causes = e.getCause();
            final boolean status = causes instanceof NoSuchAlgorithmException;
            Assert.assertFalse(status);
        }
    }

    private void generateWSSecurityException(final boolean throwProvider, final boolean throwPath,
        final boolean throwAlg) throws WSSecurityException {

        try {
            generateProviderException(throwProvider);
            generateCertPathException(throwPath);
            generateAlgorithmException(throwAlg);

        } catch (final java.security.NoSuchProviderException e) {
            throw new WSSecurityException(WSSecurityException.FAILURE, "certpath", new Object[] { e.getMessage() }, e);
        } catch (final java.security.NoSuchAlgorithmException e) {
            throw new WSSecurityException(WSSecurityException.FAILURE, "certpath", new Object[] { e.getMessage() }, e);
        } catch (final java.security.cert.CertPathValidatorException e) {
            throw new WSSecurityException(WSSecurityException.FAILURE, "certpath", new Object[] { e.getMessage() }, e);
        }

    }

    private void generateCertPathException(final boolean shouldThrowException) throws CertPathValidatorException {
        if (shouldThrowException) {
            throw new CertPathValidatorException("signature check failed", new SignatureException(
                "Signature does not match"));
        }
    }

    private void generateProviderException(final boolean shouldThrowException) throws NoSuchProviderException {
        if (shouldThrowException) {
            throw new NoSuchProviderException("No provider");
        }
    }

    private void generateAlgorithmException(final boolean shouldThrowException) throws NoSuchAlgorithmException {
        if (shouldThrowException) {
            throw new NoSuchAlgorithmException("No Algorithm");
        }
    }

}
