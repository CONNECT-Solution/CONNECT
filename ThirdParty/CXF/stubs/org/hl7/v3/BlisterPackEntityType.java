
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BlisterPackEntityType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="BlisterPackEntityType">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="BLSTRPK"/>
 *     &lt;enumeration value="CARD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "BlisterPackEntityType")
@XmlEnum
public enum BlisterPackEntityType {

    BLSTRPK,
    CARD;

    public String value() {
        return name();
    }

    public static BlisterPackEntityType fromValue(String v) {
        return valueOf(v);
    }

}
