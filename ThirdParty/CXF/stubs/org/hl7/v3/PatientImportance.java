
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PatientImportance.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PatientImportance">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="BM"/>
 *     &lt;enumeration value="FD"/>
 *     &lt;enumeration value="FOR"/>
 *     &lt;enumeration value="GOVT"/>
 *     &lt;enumeration value="DFM"/>
 *     &lt;enumeration value="SFM"/>
 *     &lt;enumeration value="STF"/>
 *     &lt;enumeration value="DR"/>
 *     &lt;enumeration value="VIP"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PatientImportance")
@XmlEnum
public enum PatientImportance {

    BM,
    FD,
    FOR,
    GOVT,
    DFM,
    SFM,
    STF,
    DR,
    VIP;

    public String value() {
        return name();
    }

    public static PatientImportance fromValue(String v) {
        return valueOf(v);
    }

}
