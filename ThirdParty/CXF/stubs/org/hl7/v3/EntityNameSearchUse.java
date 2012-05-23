
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EntityNameSearchUse.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EntityNameSearchUse">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="SRCH"/>
 *     &lt;enumeration value="SNDX"/>
 *     &lt;enumeration value="PHON"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EntityNameSearchUse")
@XmlEnum
public enum EntityNameSearchUse {

    SRCH,
    SNDX,
    PHON;

    public String value() {
        return name();
    }

    public static EntityNameSearchUse fromValue(String v) {
        return valueOf(v);
    }

}
