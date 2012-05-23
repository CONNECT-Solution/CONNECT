
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActInvoiceRootGroupCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActInvoiceRootGroupCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="RXCINV"/>
 *     &lt;enumeration value="RXDINV"/>
 *     &lt;enumeration value="CPINV"/>
 *     &lt;enumeration value="CSPINV"/>
 *     &lt;enumeration value="CSINV"/>
 *     &lt;enumeration value="FININV"/>
 *     &lt;enumeration value="OHSINV"/>
 *     &lt;enumeration value="PAINV"/>
 *     &lt;enumeration value="SBFINV"/>
 *     &lt;enumeration value="VRXINV"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActInvoiceRootGroupCode")
@XmlEnum
public enum ActInvoiceRootGroupCode {

    RXCINV,
    RXDINV,
    CPINV,
    CSPINV,
    CSINV,
    FININV,
    OHSINV,
    PAINV,
    SBFINV,
    VRXINV;

    public String value() {
        return name();
    }

    public static ActInvoiceRootGroupCode fromValue(String v) {
        return valueOf(v);
    }

}
