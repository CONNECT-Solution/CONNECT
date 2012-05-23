
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for x_PersonNamePartType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="x_PersonNamePartType">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="DEL"/>
 *     &lt;enumeration value="FAM"/>
 *     &lt;enumeration value="GIV"/>
 *     &lt;enumeration value="PFX"/>
 *     &lt;enumeration value="SFX"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "x_PersonNamePartType")
@XmlEnum
public enum XPersonNamePartType {

    DEL,
    FAM,
    GIV,
    PFX,
    SFX;

    public String value() {
        return name();
    }

    public static XPersonNamePartType fromValue(String v) {
        return valueOf(v);
    }

}
