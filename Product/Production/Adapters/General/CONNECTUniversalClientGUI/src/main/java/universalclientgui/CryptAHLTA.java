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

import javax.crypto.*;
import javax.crypto.spec.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.* ;
//import javax.swing.*;
//import java.awt.*;

public class CryptAHLTA{
	private static byte[] iv = new byte[8];
	private static byte[] keyData ={ (byte) 0xBF, (byte) 0x61, (byte) 0xD0, (byte) 0xED, (byte) 0xD2 };
	private static SecretKeySpec key;
	private static RC2ParameterSpec rc2Spec;
	private static Cipher cipher;
	private static String digits = "0123456789abcdef";


	public CryptAHLTA()
	/* default constructor
	 * it is important that these occur as part of the constructor...
	 * so that they only happen once.  These are very time expensive calls
	 *
	 */
	{
		// add the provider
		Provider secProvider = new BouncyCastleProvider();
		java.security.Security.addProvider(secProvider);
		try{
			//  Constructs a secret key from mykey using RC2 algorithm
			key = new SecretKeySpec(keyData, "RC2");
			// Constructs a parameter set for RC2 from the given effective key size
			// (in bits) and IV.
			rc2Spec = new RC2ParameterSpec(40, iv);
			// Generates a Cipher object that implements the specified transformation.
			cipher = Cipher.getInstance("RC2/CBC/PKCS5Padding");
		}
		catch (Exception e)
		{
			System.out.println("Error Initializing Ciphers: " + e);
		}
	}

	/*
	 * converts from a hex string to a byte array of unsigned values
	 */
	private static byte[] fromHex(String hexString)
	{
		byte[] bts = new byte[hexString.length() / 2];
		for (int i = 0; i < bts.length; i++)
		{
			bts[i] = (byte) Integer.parseInt(hexString.substring(2 * i, 2 * i + 2), 16);
		}
		return bts;
	}
	/*
	 * converts from an unsigned byte array to a hex string
	 */
	private static String toHex(byte[] data, int length)
	{
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i != length; i++)
		{
			int v = data[i] & 0xff;

			buf.append(digits.charAt(v >> 4));
			buf.append(digits.charAt(v & 0xf));
		}

		return buf.toString();
	}
/*
 * This method initializes the cipher object to be in decrypt mode
 * it then calls the dofinal method to decrypt the cipher
 */
	public String decrypt(String cipherHexString)
	{
		String plainString= "";
		byte[] cipherString = new byte[100]; ;
		byte[] bytedecipher ;
		// added to remove and leadig or trailing white space
		cipherHexString = cipherHexString.trim();
		cipherString = fromHex(cipherHexString);

		try{
			cipher.init(Cipher.DECRYPT_MODE, key, rc2Spec, null);
			bytedecipher = cipher.doFinal(cipherString);
			plainString = new String (bytedecipher) ;
		}
		catch (Exception e)
		{
			System.out.println("Error Decrypting : " + e);
		}
		return plainString;
	}


	/*
	 * This method initializes the cipher object to be in encrypt mode
	 * it then calls the dofinal method to decrypt the cipher
	 */
	public String encrypt(String plainText)
	{
		try
		{   plainText = plainText.trim();
			int len = plainText.length();
			for (int i=len;i<=8;i++)
			{
				plainText = plainText + " ";
			}
			byte[] plaintextArray = plainText.getBytes();
			cipher.init(Cipher.ENCRYPT_MODE, key, new RC2ParameterSpec(40, new byte[8]));
			byte[] cipherText = cipher.doFinal(plaintextArray);
			return toHex(cipherText, 16).toUpperCase();
		}
		catch (Exception ex)
		{
			return "";
		}
	}

/*	public static void main(String args[]) {

		CryptAHLTA cipherOBJ = new CryptAHLTA();
		String inString = "A319FFD99D52E4C12D27A6178BECEB5E";
		String outString = cipherOBJ.decrypt(inString);
		System.out.println(" instring =  " + inString);
		System.out.println(" outstring = " + outString);
		          //123456789
		inString = "01best";
		outString = cipherOBJ.encrypt(inString);
		System.out.println(" instring =  " + inString);
		System.out.println(" outstring = " + outString);

		inString = outString ;
		outString = cipherOBJ.decrypt(inString);
		System.out.println(" instring =  " + inString);
		System.out.println(" outstring = " + outString);


	}
*/
}