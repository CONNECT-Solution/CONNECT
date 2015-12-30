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

import gov.hhs.fha.nhinc.admingui.services.PasswordService;
import gov.hhs.fha.nhinc.admingui.services.exception.PasswordServiceException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import org.junit.Test;

/**
 * The Class SHA1PasswordServiceTest.
 */
/**
 * @author msw
 *
 */
public class SHA1PasswordServiceTest {

    /**
     * Test.
     *
     * @throws PasswordServiceException the password service exception
     * @throws NoSuchAlgorithmException the no such algorithm exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void test() throws PasswordServiceException, NoSuchAlgorithmException, IOException {
        byte[] candidatePassword = "candidatePassword".getBytes();
        byte[] salt = "ABCD".getBytes();
        byte[] passwordHash = "MhOFzcgMIkTaeHvIqXK/VkdJZHE=".getBytes();
        PasswordService service = getSHA1PasswordService();
        assert (service.checkPassword(passwordHash, candidatePassword, salt));
    }

    /**
     * Test that an incorrect password returns false.
     *
     * @throws PasswordServiceException the password service exception
     * @throws NoSuchAlgorithmException the no such algorithm exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testNegative() throws PasswordServiceException, NoSuchAlgorithmException, IOException {
        byte[] candidatePassword = "candidatePassword".getBytes();
        byte[] salt = "ABCD".getBytes();
        byte[] passwordHash = "nottherighthash".getBytes();
        PasswordService service = getSHA1PasswordService();
        assert (!service.checkPassword(passwordHash, candidatePassword, salt));
    }

    @Test
    public void generateHash() throws IOException, PasswordServiceException {
        String salt = "ABCD";// generateRandomSalt();
        String password = "password";
        String sha1 = new String(calculateHash(salt.getBytes(), password.getBytes()));
    }

    private String generateRandomSalt() {
        Random rng = new Random();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int length = 4;

        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }

    private byte[] calculateHash(byte[] salt, byte[] password) throws IOException, PasswordServiceException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(salt);
        outputStream.write(password);

        PasswordService service = getSHA1PasswordService();
        return service.calculateHash(outputStream.toByteArray());
    }

    /**
     * Gets the SHA1 password service.
     *
     * @return the SHA1 password service
     */
    private PasswordService getSHA1PasswordService() {
        return new SHA1PasswordService();
    }

}
