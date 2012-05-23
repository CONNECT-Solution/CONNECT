
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AgeDetectedIssueCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AgeDetectedIssueCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="AGE"/>
 *     &lt;enumeration value="DOSEHINDA"/>
 *     &lt;enumeration value="DOSELINDA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AgeDetectedIssueCode")
@XmlEnum
public enum AgeDetectedIssueCode {

    AGE,
    DOSEHINDA,
    DOSELINDA;

    public String value() {
        return name();
    }

    public static AgeDetectedIssueCode fromValue(String v) {
        return valueOf(v);
    }

}
