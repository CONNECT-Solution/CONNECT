
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AdministrativeContactRoleType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AdministrativeContactRoleType">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="BILL"/>
 *     &lt;enumeration value="PAYOR"/>
 *     &lt;enumeration value="ORG"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AdministrativeContactRoleType")
@XmlEnum
public enum AdministrativeContactRoleType {

    BILL,
    PAYOR,
    ORG;

    public String value() {
        return name();
    }

    public static AdministrativeContactRoleType fromValue(String v) {
        return valueOf(v);
    }

}
