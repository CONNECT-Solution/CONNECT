
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PrivateResidence.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PrivateResidence">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="PR"/>
 *     &lt;enumeration value="H"/>
 *     &lt;enumeration value="R"/>
 *     &lt;enumeration value="SL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PrivateResidence")
@XmlEnum
public enum PrivateResidence {

    PR,
    H,
    R,
    SL;

    public String value() {
        return name();
    }

    public static PrivateResidence fromValue(String v) {
        return valueOf(v);
    }

}
