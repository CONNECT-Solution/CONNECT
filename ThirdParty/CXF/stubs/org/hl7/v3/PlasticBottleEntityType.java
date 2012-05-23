
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PlasticBottleEntityType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PlasticBottleEntityType">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="BOTP"/>
 *     &lt;enumeration value="BOTPLY"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PlasticBottleEntityType")
@XmlEnum
public enum PlasticBottleEntityType {

    BOTP,
    BOTPLY;

    public String value() {
        return name();
    }

    public static PlasticBottleEntityType fromValue(String v) {
        return valueOf(v);
    }

}
