
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Charset.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Charset">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="EBCDIC"/>
 *     &lt;enumeration value="ISO-10646-UCS-2"/>
 *     &lt;enumeration value="ISO-10646-UCS-4"/>
 *     &lt;enumeration value="ISO-8859-1"/>
 *     &lt;enumeration value="ISO-8859-2"/>
 *     &lt;enumeration value="ISO-8859-5"/>
 *     &lt;enumeration value="JIS-2022-JP"/>
 *     &lt;enumeration value="US-ASCII"/>
 *     &lt;enumeration value="UTF-7"/>
 *     &lt;enumeration value="UTF-8"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Charset")
@XmlEnum
public enum Charset {

    EBCDIC("EBCDIC"),
    @XmlEnumValue("ISO-10646-UCS-2")
    ISO_10646_UCS_2("ISO-10646-UCS-2"),
    @XmlEnumValue("ISO-10646-UCS-4")
    ISO_10646_UCS_4("ISO-10646-UCS-4"),
    @XmlEnumValue("ISO-8859-1")
    ISO_8859_1("ISO-8859-1"),
    @XmlEnumValue("ISO-8859-2")
    ISO_8859_2("ISO-8859-2"),
    @XmlEnumValue("ISO-8859-5")
    ISO_8859_5("ISO-8859-5"),
    @XmlEnumValue("JIS-2022-JP")
    JIS_2022_JP("JIS-2022-JP"),
    @XmlEnumValue("US-ASCII")
    US_ASCII("US-ASCII"),
    @XmlEnumValue("UTF-7")
    UTF_7("UTF-7"),
    @XmlEnumValue("UTF-8")
    UTF_8("UTF-8");
    private final String value;

    Charset(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Charset fromValue(String v) {
        for (Charset c: Charset.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
