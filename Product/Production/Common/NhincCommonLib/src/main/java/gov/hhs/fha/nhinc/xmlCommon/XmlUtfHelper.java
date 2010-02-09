/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.xmlCommon;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;

/**
 *
 * @author rayj
 */
public class XmlUtfHelper {

    private static final String XMLHEADER_UTF8 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static final String XMLHEADER_UTF16 = "<?xml version=\"1.0\" encoding=\"UTF-16\"?>";

    public static boolean isUtf8(String xml) {
        return isUtfVersion(xml, XMLHEADER_UTF8);
    }

    public static boolean isUtf16(String xml) {
        return isUtfVersion(xml, XMLHEADER_UTF16);
    }

    public static boolean isUtfVersion(String xml, String utf) {
        if (NullChecker.isNullish(xml)) {
            return false;
        }
        return xml.startsWith(utf);
    }

    public static String convertToUtf8(String xml) {
        return convertToUtf(xml, XMLHEADER_UTF8) ;
    }

    public static String convertToUtf16(String xml) {
        return convertToUtf(xml, XMLHEADER_UTF16) ;
    }

    public static String convertToUtf(String xml ,String utf) {
        return utf + xml.substring(xml.indexOf("?>") + 2);
    }
}
