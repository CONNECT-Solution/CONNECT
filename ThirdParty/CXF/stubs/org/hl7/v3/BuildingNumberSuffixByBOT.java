
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BuildingNumberSuffixByBOT.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="BuildingNumberSuffixByBOT">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="BNS"/>
 *     &lt;enumeration value="POB"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "BuildingNumberSuffixByBOT")
@XmlEnum
public enum BuildingNumberSuffixByBOT {

    BNS,
    POB;

    public String value() {
        return name();
    }

    public static BuildingNumberSuffixByBOT fromValue(String v) {
        return valueOf(v);
    }

}
