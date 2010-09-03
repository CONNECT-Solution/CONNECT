/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.util;

import java.io.FileReader;
import java.util.StringTokenizer;

/**
 * String utilities...
 * 
 * @author Les Westberg
 */
public class StringUtil {

    /**
     * This method reads the entire contents of a text file and returns the contents in 
     * a string variable.
     * 
     * @param sFileName The path and location of the text file.
     * @return The contents that was read in.
     */
    public static String readTextFile(String sFileName)
            throws UtilException {
        String sText = "";
        FileReader frTextFile = null;

        try {
            frTextFile = new FileReader(sFileName);
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
     * Extracts required string by removing the tokens given as input
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
        }
        else {
            return "[CDATA[]]";
        }

    }

    /**
     * If the text is wrapped with a "[CDATA[ ]]" tag then it is removed and
     * the text inside is returned.
     * 
     * @param sText The text containing a CDATA wrapper.
     * @return The text without the CDATA wrapper.  If it does not contain a 
     *         CDATA wrapper, it simply returns the same text it was passed.
     */
    public static String unwrapCdata (String sText) {
        if ((sText != null) &&
            (sText.trim().startsWith("[CDATA[")) &&
            (sText.trim().endsWith("]]")))   {
            return sText.trim().substring(7, sText.trim().length()-2);
        }
        else {
            return sText;
        }

    }
}
