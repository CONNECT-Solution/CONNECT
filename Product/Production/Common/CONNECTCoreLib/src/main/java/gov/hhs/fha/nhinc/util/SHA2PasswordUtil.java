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
package gov.hhs.fha.nhinc.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Poornima Venkatakrishnan
 *
 */
public class SHA2PasswordUtil {
    private static final String HASH_ALGORITHM = "SHA-512";
    private static final Logger LOG = LoggerFactory.getLogger(SHA2PasswordUtil.class);

    private SHA2PasswordUtil() {
    }

    public static boolean checkPassword(byte[] passwordHash, byte[] candidatePassword, byte[] salt) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] candidateHash;

        try {
            outputStream.write(salt);
            outputStream.write(candidatePassword);
            candidateHash = calculateHash(outputStream.toByteArray());
        } catch (NoSuchAlgorithmException | IOException e) {
            LOG.error("Failed to check password hash token: {}", e.getLocalizedMessage(), e);
            return false;
        }
        return Arrays.equals(passwordHash, candidateHash);
    }

    public static byte[] calculateHash(byte[] salt, byte[] password) throws UtilException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] hash = null;

        try {
            outputStream.write(salt);
            outputStream.write(password);
            hash = calculateHash(outputStream.toByteArray());
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new UtilException("Failed to calculate hash token", e);
        }
        return hash;
    }

    private static byte[] calculateHash(byte[] input) throws NoSuchAlgorithmException {
        byte[] digest;

        MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
        digest = md.digest(input);

        return Base64.encodeBase64(digest);
    }

}
