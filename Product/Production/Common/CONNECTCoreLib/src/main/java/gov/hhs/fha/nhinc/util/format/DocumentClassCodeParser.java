/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.util.format;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rayj
 */
public class DocumentClassCodeParser {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(DocumentClassCodeParser.class);

    public static List<String> parseFormattedParameter(List<String> rawList) {
        List<String> normalizedList = new ArrayList<String>();
        for (String item : rawList) {
            normalizedList = parseFormattedParameter(item, normalizedList);
        }
        return normalizedList;
    }

    public static List<String> parseFormattedParameter(String paramFormattedString) {
        return parseFormattedParameter(paramFormattedString, null);
    }

    public static List<String> parseFormattedParameter(String paramFormattedString, List<String> resultCollection) {
        if (resultCollection == null) {
            resultCollection = new ArrayList<String>();
        }

        if ((NullChecker.isNotNullish(paramFormattedString)) && (resultCollection != null)) {
            if (paramFormattedString.startsWith("(")) {
                String working = paramFormattedString.substring(1);
                int endIndex = working.indexOf(")");
                if (endIndex != -1) {
                    working = working.substring(0, endIndex);
                }
                String[] multiValueString = working.split(",");
                if (multiValueString != null) {
                    for (int i = 0; i < multiValueString.length; i++) {
                        String singleValue = multiValueString[i];
                        if (singleValue != null) {
                            singleValue = singleValue.trim();
                        }
                        if (singleValue.startsWith("'")) {
                            singleValue = singleValue.substring(1);
                        }
                        if (singleValue.endsWith("'")) {
                            int endTickIndex = singleValue.indexOf("'");
                            if (endTickIndex != -1) {
                                singleValue = singleValue.substring(0, endTickIndex);
                            }
                        }
                        resultCollection.add(singleValue);
                        if (log.isDebugEnabled()) {
                            log.debug("Added single value: " + singleValue + " to query parameters");
                        }
                    }
                }
            } else {
                resultCollection.add(paramFormattedString);
                if (log.isDebugEnabled()) {
                    log.debug("No wrapper on status - adding status: " + paramFormattedString + " to query parameters");
                }
            }
        }

        return resultCollection;
    }

    public static String buildDocumentClassCodeItem(List<String> documentClassCodeList) {
        String buffer = "";

        if ((documentClassCodeList != null) && (documentClassCodeList.size() > 0)) {
            buffer = "(";
            for (String documentClassCode : documentClassCodeList) {
                documentClassCode = documentClassCode.trim();
                if (NullChecker.isNotNullish(buffer)) {
                    buffer = buffer + "'" + documentClassCode + "'" + ",";
                }
            }
            if (buffer.endsWith(",")) {
                buffer = buffer.substring(0, buffer.length() - 1);
            }
            buffer = buffer + ")";
        }
        if (buffer.contentEquals("()")) {
            buffer = "";
        }
        return buffer;
    }
}
