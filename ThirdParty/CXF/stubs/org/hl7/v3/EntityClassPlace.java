
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EntityClassPlace.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EntityClassPlace">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="PLC"/>
 *     &lt;enumeration value="CITY"/>
 *     &lt;enumeration value="COUNTRY"/>
 *     &lt;enumeration value="COUNTY"/>
 *     &lt;enumeration value="PROVINCE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EntityClassPlace")
@XmlEnum
public enum EntityClassPlace {

    PLC,
    CITY,
    COUNTRY,
    COUNTY,
    PROVINCE;

    public String value() {
        return name();
    }

    public static EntityClassPlace fromValue(String v) {
        return valueOf(v);
    }

}
