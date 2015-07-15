/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.admingui.services.impl;
import gov.hhs.fha.nhinc.admingui.services.exception.PasswordServiceException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.apache.commons.codec.binary.Base64;

/**
 * The Class SHA1PasswordService.
 *
 * @author msw
 */
public class SHA1PasswordService extends AbstractBase64EncodedPasswordService {

    public static final String HASH_ALGORITHM = "SHA-1";

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     *
     */
    public SHA1PasswordService() {
        super();
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.services.PasswordService#calculateHash(byte[])
     */
    @Override
    public byte[] calculateHash(byte[] input) throws PasswordServiceException {
        byte[] digest;

        try {
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            digest = md.digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new PasswordServiceException("Unable to calculate digest for password.", e);
        }

        return encode(digest);
    }

     /**
     *
     * @return the salt value
     */
    @Override
    public byte[] generateRandomSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return Base64.encodeBase64(salt);
    }

    /**
     *
     * @param salt the salt value
     * @param password the password
     * @return calculated hash as byte array
     * @throws IOException
     * @throws PasswordServiceException
     */
    @Override
    public byte[] calculateHash(byte[] salt, byte[] password) throws IOException, PasswordServiceException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        outputStream.write(salt);
        outputStream.write(password);

        return calculateHash(outputStream.toByteArray());
    }
}
