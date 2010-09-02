/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.util.hash;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *  This class is used to compute the SHA1 hash code of a string.
 * 
 * @author Les Westberg
 */
public class SHA1HashCode
{

    /**
     * This method takes a byte array and converts it to a String HEX representation
     * of the byte array.
     * 
     * @param data The byte array to be converted.
     * @return The HEX string representation of the byte array.
     */
    private static String convertToHex(byte[] data)
    {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++)
        {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do
            {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                {
                    buf.append((char) ('0' + halfbyte));
                } else
                {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    
    /**
     * This method calculates the SHA1 hash code for the given string.
     * 
     * @param text The string that is being used to compute the SHA-1 hash code.
     * @return The SHA-1 has code based on the given string.
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.io.UnsupportedEncodingException
     */
    public static String calculateSHA1(String text)
            throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        if (text != null)
        {
            MessageDigest md;
            md = MessageDigest.getInstance("SHA-1");
            byte[] sha1hash = new byte[40];
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
            sha1hash = md.digest();
            return convertToHex(sha1hash);
        }
        else
        {
            return "";
        }
    }
}
