
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TimingDetectedIssueCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TimingDetectedIssueCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="TIME"/>
 *     &lt;enumeration value="ENDLATE"/>
 *     &lt;enumeration value="STRTLATE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TimingDetectedIssueCode")
@XmlEnum
public enum TimingDetectedIssueCode {

    TIME,
    ENDLATE,
    STRTLATE;

    public String value() {
        return name();
    }

    public static TimingDetectedIssueCode fromValue(String v) {
        return valueOf(v);
    }

}
