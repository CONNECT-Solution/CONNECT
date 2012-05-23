
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ReactionDetectedIssueCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ReactionDetectedIssueCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="REACT"/>
 *     &lt;enumeration value="ALGY"/>
 *     &lt;enumeration value="INT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ReactionDetectedIssueCode")
@XmlEnum
public enum ReactionDetectedIssueCode {

    REACT,
    ALGY,
    INT;

    public String value() {
        return name();
    }

    public static ReactionDetectedIssueCode fromValue(String v) {
        return valueOf(v);
    }

}
