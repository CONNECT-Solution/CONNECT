
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ObservationDiagnosisTypes.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ObservationDiagnosisTypes">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="DX"/>
 *     &lt;enumeration value="ADMDX"/>
 *     &lt;enumeration value="DISDX"/>
 *     &lt;enumeration value="INTDX"/>
 *     &lt;enumeration value="NOI"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ObservationDiagnosisTypes")
@XmlEnum
public enum ObservationDiagnosisTypes {

    DX,
    ADMDX,
    DISDX,
    INTDX,
    NOI;

    public String value() {
        return name();
    }

    public static ObservationDiagnosisTypes fromValue(String v) {
        return valueOf(v);
    }

}
