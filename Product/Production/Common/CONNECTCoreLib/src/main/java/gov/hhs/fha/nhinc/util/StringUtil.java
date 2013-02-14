/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
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

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;

/**
 * String utilities...
 *
 * @author Les Westberg
 */
public class StringUtil {

    public static final String UTF8_CHARSET = "UTF-8";
    /**
     * This method reads the entire contents of a text file and returns the contents in a string variable.
     *
     * @param sFileName The path and location of the text file.
     * @return The contents that was read in.
     */
    public static String readTextFile(String sFileName) throws UtilException {
        String sText = "";
        InputStreamReader frTextFile = null;

        try {
            
            frTextFile = new InputStreamReader(new FileInputStream(sFileName),StringUtil.UTF8_CHARSET);
            char caBuf[] = new char[1024];
            int iLen = 0;
            StringBuffer sbText = new StringBuffer();
            while ((iLen = frTextFile.read(caBuf, 0, 1024)) != -1) {
                sbText.append(caBuf, 0, iLen);
            }

            sText = sbText.toString();
        } catch (Exception e) {
            String sErrorMessage = "Failed to read text file: " + sFileName + ". Error: " + e.getMessage();
            throw new UtilException(sErrorMessage, e);
        } finally {
            if (frTextFile != null) {
                try {
                    frTextFile.close();
                } catch (Exception e) {
                }
            }
        }

        return sText;
    }

    /**
     * Extracts required string by removing the tokens given as input.
     *
     * @param tokenString
     * @param tokens
     * @return String
     */
    public static String extractStringFromTokens(String tokenString, String tokens) {
        String resultString = "";
        if (tokens != null && !tokens.equals("")) {
            StringTokenizer tk = new StringTokenizer(tokenString, tokens);
            StringBuffer outString = new StringBuffer();
            while (tk.hasMoreTokens()) {
                outString.append(tk.nextToken());
            }
            resultString = outString.toString();
        } else {
            resultString = tokenString;
        }
        return resultString;
    }

    /**
     * This method is used to add CDATA tags around a string.
     *
     * @param sText The text to be wrapped in a CDATA tag.
     * @return The wrapped text.
     */
    public static String wrapCdata(String sText) {
        if (sText != null) {
            return "[CDATA[" + sText + "]]";
        } else {
            return "[CDATA[]]";
        }

    }

    /**
     * If the text is wrapped with a "[CDATA[ ]]" tag then it is removed and the text inside is returned.
     *
     * @param sText The text containing a CDATA wrapper.
     * @return The text without the CDATA wrapper. If it does not contain a CDATA wrapper, it simply returns the same
     *         text it was passed.
     */
    public static String unwrapCdata(String sText) {
        if ((sText != null) && (sText.trim().startsWith("[CDATA[")) && (sText.trim().endsWith("]]"))) {
            return sText.trim().substring(7, sText.trim().length() - 2);
        } else {
            return sText;
        }

    }
    /**
     * Converts a byte array into a Eight-bit Unicode Transformation Format string.
     *
     * @param byteArray byte array
     * @return UTF-8 format string
     * 
     */
    public static String convertToStringUTF8(byte[] byteArray) throws UnsupportedEncodingException {
             return new String(byteArray,UTF8_CHARSET);
    }
}
