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
}
