
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActInvoiceElementModifier.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActInvoiceElementModifier">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="EFORM"/>
 *     &lt;enumeration value="FAX"/>
 *     &lt;enumeration value="LINV"/>
 *     &lt;enumeration value="PAPER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActInvoiceElementModifier")
@XmlEnum
public enum ActInvoiceElementModifier {

    EFORM,
    FAX,
    LINV,
    PAPER;

    public String value() {
        return name();
    }

    public static ActInvoiceElementModifier fromValue(String v) {
        return valueOf(v);
    }

}
