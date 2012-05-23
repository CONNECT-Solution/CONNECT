
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for x_VeryBasicConfidentialityKind.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="x_VeryBasicConfidentialityKind">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="N"/>
 *     &lt;enumeration value="R"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "x_VeryBasicConfidentialityKind")
@XmlEnum
public enum XVeryBasicConfidentialityKind {

    N,
    R;

    public String value() {
        return name();
    }

    public static XVeryBasicConfidentialityKind fromValue(String v) {
        return valueOf(v);
    }

}
