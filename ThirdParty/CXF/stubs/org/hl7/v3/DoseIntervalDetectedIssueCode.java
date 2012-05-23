
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DoseIntervalDetectedIssueCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DoseIntervalDetectedIssueCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="DOSEIVL"/>
 *     &lt;enumeration value="DOSEIVLIND"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DoseIntervalDetectedIssueCode")
@XmlEnum
public enum DoseIntervalDetectedIssueCode {

    DOSEIVL,
    DOSEIVLIND;

    public String value() {
        return name();
    }

    public static DoseIntervalDetectedIssueCode fromValue(String v) {
        return valueOf(v);
    }

}
