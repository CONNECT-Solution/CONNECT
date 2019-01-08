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

import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringUtils;

/**
 * String utilities...
 *
 * @author Les Westberg
 */
public class StringUtil {

    public static final String UTF8_CHARSET = "UTF-8";

    /**
     * Extracts required string by removing the tokens given as input.
     *
     * @param tokenString
     * @param tokens
     * @return String
     */
    public static String extractStringFromTokens(final String tokenString, final String tokens) {
        String resultString;

        if (StringUtils.isNotEmpty(tokens)) {
            final StringTokenizer tk = new StringTokenizer(tokenString, tokens);
            final StringBuffer outString = new StringBuffer();
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
    public static String wrapCdata(final String sText) {
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
    public static String unwrapCdata(final String sText) {
        if (sText != null && sText.trim().startsWith("[CDATA[") && sText.trim().endsWith("]]")) {
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
    public static String convertToStringUTF8(final byte[] byteArray) throws UnsupportedEncodingException {
        return new String(byteArray, UTF8_CHARSET);
    }
}
