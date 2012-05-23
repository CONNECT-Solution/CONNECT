
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OrganizationNameType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OrganizationNameType">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="A"/>
 *     &lt;enumeration value="L"/>
 *     &lt;enumeration value="ST"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OrganizationNameType")
@XmlEnum
public enum OrganizationNameType {

    A,
    L,
    ST;

    public String value() {
        return name();
    }

    public static OrganizationNameType fromValue(String v) {
        return valueOf(v);
    }

}
