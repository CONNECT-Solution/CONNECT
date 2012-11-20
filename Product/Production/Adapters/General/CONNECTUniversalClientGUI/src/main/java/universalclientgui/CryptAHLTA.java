/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 *
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 *
 * All other rights reserved.
 *
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
package universalclientgui;

import java.security.Provider;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.RC2ParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.security.InvalidKeyException;
import java.security.InvalidAlgorithmParameterException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

/**
 *
 * @author Administrator
 */
public class CryptAHLTA {

    /**
     *
     */
    private static final int INITIAL_ARRAY_SIZE = 8;
    /**
     * 
     */
    private static final int RC2SPEC_ARRAY_VAL = 40;
    private static final int CYPHER_ARRAY_VAL = 100;
    private static final int HEX_VAL = 16;
    private static final int TO_HEX_VAL = 4;
    private static final int ENCRYPT_LENGTH = 8;
    private static byte[] iv = new byte[INITIAL_ARRAY_SIZE];
    private static byte[] keyData = {(byte) 0xBF, (byte) 0x61, (byte) 0xD0, (byte) 0xED, (byte) 0xD2};
    private static SecretKeySpec key;
    private static RC2ParameterSpec rc2Spec;
    private static Cipher cipher;
    private static String digits = "0123456789abcdef";

    private static final Log LOG = LogFactory.getLog(CryptAHLTA.class);

    /**
     *
     */
    public CryptAHLTA() /* default constructor
     * it is important that these occur as part of the constructor...
     * so that they only happen once.  These are very time expensive calls
     *
     */ {
        // add the provider
        Provider secProvider = new BouncyCastleProvider();
        java.security.Security.addProvider(secProvider);
        try {
            //  Constructs a secret key from mykey using RC2 algorithm
            key = new SecretKeySpec(keyData, "RC2");
            // Constructs a parameter set for RC2 from the given effective key size
            // (in bits) and IV.
            rc2Spec = new RC2ParameterSpec(RC2SPEC_ARRAY_VAL, iv);
            // Generates a Cipher object that implements the specified transformation.
            cipher = Cipher.getInstance("RC2/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException nsae) {
            LOG.error("No Such Algorithm Error Initializing Ciphers: ", nsae);
        }
        catch (NoSuchPaddingException bpe)
        {
            LOG.error("No Such Padding Error Initializing Ciphers: ", bpe);
        }

    }

    /*
     * converts from a hex string to a byte array of unsigned values
     */
    private static byte[] fromHex(String hexString) {
        byte[] bts = new byte[hexString.length() / 2];
        for (int i = 0; i < bts.length; i++) {
            bts[i] = (byte) Integer.parseInt(hexString.substring(2 * i, 2 * i + 2), HEX_VAL);
        }
        return bts;
    }
    /*
     * converts from an unsigned byte array to a hex string
     */

    private static String toHex(byte[] data, int length) {
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i != length; i++) {
            int v = data[i] & 0xff;

            buf.append(digits.charAt(v >> TO_HEX_VAL));
            buf.append(digits.charAt(v & 0xf));
        }

        return buf.toString();
    }
    /*
     * This method initializes the cipher object to be in decrypt mode
     * it then calls the dofinal method to decrypt the cipher
     */

    /**
     *
     * @param cipherHexString String
     * @return String
     */
    @SuppressWarnings("empty-statement")
    public String decrypt(String cipherHexString) {
        String plainString = "";
        String trimmedCipherHexString;
        byte[] cipherString = new byte[CYPHER_ARRAY_VAL];
        byte[] bytedecipher;
        // added to remove and leadig or trailing white space
        trimmedCipherHexString = cipherHexString.trim();
        cipherString = fromHex(trimmedCipherHexString);

        try {
            cipher.init(Cipher.DECRYPT_MODE, key, rc2Spec, null);
            bytedecipher = cipher.doFinal(cipherString);
            plainString = new String(bytedecipher);
        } catch (InvalidKeyException ike) {
            LOG.error("Invalid Key Error Decrypting : ", ike);
        } catch (IllegalBlockSizeException ibse) {
            LOG.error("Invalid Block Size Exception Decrypting : ", ibse);
        } catch (BadPaddingException ibse) {
            LOG.error("Bad Padding Exception Decrypting : ", ibse);
        }catch (InvalidAlgorithmParameterException iape) {
            LOG.error("Invalid Algorithm Exception Decrypting : ", iape);
        }

        return plainString;
    }


    /*
     * This method initializes the cipher object to be in encrypt mode
     * it then calls the dofinal method to decrypt the cipher
     */
    /**
     *
     * @param plainText String
     * @return String
     */
    public String encrypt(String plainText) {
        
        StringBuffer trimmedPlainText = new StringBuffer();

        try {
            trimmedPlainText.append(plainText.trim());
            int len = trimmedPlainText.length();
            for (int i = len; i <= ENCRYPT_LENGTH; i++) {
                trimmedPlainText.append(' ');
            }
            byte[] plaintextArray = trimmedPlainText.toString().getBytes();
            cipher.init(Cipher.ENCRYPT_MODE, key, new RC2ParameterSpec(RC2SPEC_ARRAY_VAL,
                new byte[INITIAL_ARRAY_SIZE]));
            byte[] cipherText = cipher.doFinal(plaintextArray);
            return toHex(cipherText, HEX_VAL).toUpperCase();
        } catch (InvalidKeyException ike) {
            LOG.error("Invalid Key Exception while encrypting value:", ike);
            return "";
        } catch (InvalidAlgorithmParameterException iape) {
            LOG.error("Invalid Algorithm Parameter Exception while encrypting value:", iape);
            return "";
        } catch (IllegalBlockSizeException ibse) {
            LOG.error("Illegal Block Size Exception while encrypting value:", ibse);
            return "";
        }catch (BadPaddingException bpe) {
            LOG.error("Bad Padding Exception while encrypting value:", bpe);
            return "";
        }
    }
    
}
