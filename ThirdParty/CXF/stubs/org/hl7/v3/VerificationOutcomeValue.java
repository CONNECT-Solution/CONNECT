
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VerificationOutcomeValue.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="VerificationOutcomeValue">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ACTPEND"/>
 *     &lt;enumeration value="ACT"/>
 *     &lt;enumeration value="ELG"/>
 *     &lt;enumeration value="INACT"/>
 *     &lt;enumeration value="INPNDUPD"/>
 *     &lt;enumeration value="INPNDINV"/>
 *     &lt;enumeration value="NELG"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "VerificationOutcomeValue")
@XmlEnum
public enum VerificationOutcomeValue {

    ACTPEND,
    ACT,
    ELG,
    INACT,
    INPNDUPD,
    INPNDINV,
    NELG;

    public String value() {
        return name();
    }

    public static VerificationOutcomeValue fromValue(String v) {
        return valueOf(v);
    }

}
