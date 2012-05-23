
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ConrolActNullificationReasonCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ConrolActNullificationReasonCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ALTD"/>
 *     &lt;enumeration value="EIE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ConrolActNullificationReasonCode")
@XmlEnum
public enum ConrolActNullificationReasonCode {

    ALTD,
    EIE;

    public String value() {
        return name();
    }

    public static ConrolActNullificationReasonCode fromValue(String v) {
        return valueOf(v);
    }

}
