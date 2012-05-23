
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GenericUpdateReasonCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="GenericUpdateReasonCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="FIXDATA"/>
 *     &lt;enumeration value="CHGDATA"/>
 *     &lt;enumeration value="NEWDATA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "GenericUpdateReasonCode")
@XmlEnum
public enum GenericUpdateReasonCode {

    FIXDATA,
    CHGDATA,
    NEWDATA;

    public String value() {
        return name();
    }

    public static GenericUpdateReasonCode fromValue(String v) {
        return valueOf(v);
    }

}
