/*******************************************************************************
 * Copyright ?? 2013 The California Health and Human Services Agency (CHHS). All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"), you may not use this file except in compliance with the License. You may obtain a copy of the License at: http://www.apache.org/licenses/LICENSE-2.0.
 * Unless required by applicable law or agreed to in writing, content (including but not limited to software, documentation, information, and all other works distributed under the License) is distributed on an "AS IS" BASIS, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE CONTENT OR THE USE OR OTHER DEALINGS IN THE CONTENT. IN NO EVENT SHALL CHHS HAVE ANY OBLIGATION TO PROVIDE SUPPORT, UPDATES, MODIFICATIONS, AND/OR UPGRADES FOR CONTENT. See the License for the specific language governing permissions and limitations under the License.
 * This publication/product was made possible by Award Number 90HT0029 from Office of the National Coordinator for Health Information Technology (ONC), U.S. Department of Health and Human Services. Its contents are solely the responsibility of the authors and do not necessarily represent the official views of ONC or the State of California.
 ******************************************************************************/
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

        System.out.println("salt: ".concat(salt));
        System.out.println("password: ".concat(password));
        System.out.println("sha1: ".concat(sha1));
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
