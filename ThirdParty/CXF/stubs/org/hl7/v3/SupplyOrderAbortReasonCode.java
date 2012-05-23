
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SupplyOrderAbortReasonCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SupplyOrderAbortReasonCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="IMPROV"/>
 *     &lt;enumeration value="INTOL"/>
 *     &lt;enumeration value="NEWSTR"/>
 *     &lt;enumeration value="NEWTHER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SupplyOrderAbortReasonCode")
@XmlEnum
public enum SupplyOrderAbortReasonCode {

    IMPROV,
    INTOL,
    NEWSTR,
    NEWTHER;

    public String value() {
        return name();
    }

    public static SupplyOrderAbortReasonCode fromValue(String v) {
        return valueOf(v);
    }

}
