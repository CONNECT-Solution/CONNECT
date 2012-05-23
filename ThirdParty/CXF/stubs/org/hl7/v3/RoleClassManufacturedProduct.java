
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RoleClassManufacturedProduct.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RoleClassManufacturedProduct">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="MANU"/>
 *     &lt;enumeration value="THER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "RoleClassManufacturedProduct")
@XmlEnum
public enum RoleClassManufacturedProduct {

    MANU,
    THER;

    public String value() {
        return name();
    }

    public static RoleClassManufacturedProduct fromValue(String v) {
        return valueOf(v);
    }

}
