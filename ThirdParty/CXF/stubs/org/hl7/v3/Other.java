
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Other.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Other">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="OTH"/>
 *     &lt;enumeration value="NINF"/>
 *     &lt;enumeration value="PINF"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Other")
@XmlEnum
public enum Other {

    OTH,
    NINF,
    PINF;

    public String value() {
        return name();
    }

    public static Other fromValue(String v) {
        return valueOf(v);
    }

}
