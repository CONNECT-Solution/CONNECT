
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActInvoicePaymentCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActInvoicePaymentCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="BONUS"/>
 *     &lt;enumeration value="CFWD"/>
 *     &lt;enumeration value="EPYMT"/>
 *     &lt;enumeration value="EDU"/>
 *     &lt;enumeration value="GARN"/>
 *     &lt;enumeration value="PINV"/>
 *     &lt;enumeration value="PPRD"/>
 *     &lt;enumeration value="PROA"/>
 *     &lt;enumeration value="RECOV"/>
 *     &lt;enumeration value="RETRO"/>
 *     &lt;enumeration value="INVOICE"/>
 *     &lt;enumeration value="TRAN"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActInvoicePaymentCode")
@XmlEnum
public enum ActInvoicePaymentCode {

    BONUS,
    CFWD,
    EPYMT,
    EDU,
    GARN,
    PINV,
    PPRD,
    PROA,
    RECOV,
    RETRO,
    INVOICE,
    TRAN;

    public String value() {
        return name();
    }

    public static ActInvoicePaymentCode fromValue(String v) {
        return valueOf(v);
    }

}
