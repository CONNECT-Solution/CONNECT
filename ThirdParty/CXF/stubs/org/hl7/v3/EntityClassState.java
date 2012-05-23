
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EntityClassState.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EntityClassState">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="STATE"/>
 *     &lt;enumeration value="NAT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EntityClassState")
@XmlEnum
public enum EntityClassState {

    STATE,
    NAT;

    public String value() {
        return name();
    }

    public static EntityClassState fromValue(String v) {
        return valueOf(v);
    }

}
