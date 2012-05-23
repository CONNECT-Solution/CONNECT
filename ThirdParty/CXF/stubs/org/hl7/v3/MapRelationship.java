
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MapRelationship.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MapRelationship">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="BT"/>
 *     &lt;enumeration value="E"/>
 *     &lt;enumeration value="NT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MapRelationship")
@XmlEnum
public enum MapRelationship {

    BT,
    E,
    NT;

    public String value() {
        return name();
    }

    public static MapRelationship fromValue(String v) {
        return valueOf(v);
    }

}
