
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EntericCoatedCapsule.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EntericCoatedCapsule">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ENTCAP"/>
 *     &lt;enumeration value="ERENTCAP"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EntericCoatedCapsule")
@XmlEnum
public enum EntericCoatedCapsule {

    ENTCAP,
    ERENTCAP;

    public String value() {
        return name();
    }

    public static EntericCoatedCapsule fromValue(String v) {
        return valueOf(v);
    }

}
