
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MDFSubjectAreaPrefix.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MDFSubjectAreaPrefix">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="COI"/>
 *     &lt;enumeration value="DIM"/>
 *     &lt;enumeration value="RIM"/>
 *     &lt;enumeration value="STW"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MDFSubjectAreaPrefix")
@XmlEnum
public enum MDFSubjectAreaPrefix {

    COI,
    DIM,
    RIM,
    STW;

    public String value() {
        return name();
    }

    public static MDFSubjectAreaPrefix fromValue(String v) {
        return valueOf(v);
    }

}
