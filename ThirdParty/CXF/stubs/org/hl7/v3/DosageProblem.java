
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DosageProblem.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DosageProblem">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="DOSE"/>
 *     &lt;enumeration value="DOSEDUR"/>
 *     &lt;enumeration value="DOSEIVL"/>
 *     &lt;enumeration value="DOSEH"/>
 *     &lt;enumeration value="DOSEL"/>
 *     &lt;enumeration value="DOSECOND"/>
 *     &lt;enumeration value="MDOSE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DosageProblem")
@XmlEnum
public enum DosageProblem {

    DOSE,
    DOSEDUR,
    DOSEIVL,
    DOSEH,
    DOSEL,
    DOSECOND,
    MDOSE;

    public String value() {
        return name();
    }

    public static DosageProblem fromValue(String v) {
        return valueOf(v);
    }

}
