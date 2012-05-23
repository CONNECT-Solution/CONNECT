
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VerificationMethod.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="VerificationMethod">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="VDOC"/>
 *     &lt;enumeration value="VTOKEN"/>
 *     &lt;enumeration value="VREG"/>
 *     &lt;enumeration value="VVOICE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "VerificationMethod")
@XmlEnum
public enum VerificationMethod {

    VDOC,
    VTOKEN,
    VREG,
    VVOICE;

    public String value() {
        return name();
    }

    public static VerificationMethod fromValue(String v) {
        return valueOf(v);
    }

}
