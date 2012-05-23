
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for x_RoleClassCoverageInvoice.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="x_RoleClassCoverageInvoice">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="PAYOR"/>
 *     &lt;enumeration value="PAYEE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "x_RoleClassCoverageInvoice")
@XmlEnum
public enum XRoleClassCoverageInvoice {

    PAYOR,
    PAYEE;

    public String value() {
        return name();
    }

    public static XRoleClassCoverageInvoice fromValue(String v) {
        return valueOf(v);
    }

}
