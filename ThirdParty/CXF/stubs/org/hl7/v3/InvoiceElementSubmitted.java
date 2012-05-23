
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InvoiceElementSubmitted.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="InvoiceElementSubmitted">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="SBBLELAT"/>
 *     &lt;enumeration value="SBBLELCT"/>
 *     &lt;enumeration value="SBNFELCT"/>
 *     &lt;enumeration value="SBNFELAT"/>
 *     &lt;enumeration value="SBPDELAT"/>
 *     &lt;enumeration value="SBPDELCT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "InvoiceElementSubmitted")
@XmlEnum
public enum InvoiceElementSubmitted {

    SBBLELAT,
    SBBLELCT,
    SBNFELCT,
    SBNFELAT,
    SBPDELAT,
    SBPDELCT;

    public String value() {
        return name();
    }

    public static InvoiceElementSubmitted fromValue(String v) {
        return valueOf(v);
    }

}
