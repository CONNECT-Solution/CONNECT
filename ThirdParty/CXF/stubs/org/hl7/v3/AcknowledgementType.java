
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AcknowledgementType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AcknowledgementType">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="CA"/>
 *     &lt;enumeration value="CE"/>
 *     &lt;enumeration value="CR"/>
 *     &lt;enumeration value="AA"/>
 *     &lt;enumeration value="AE"/>
 *     &lt;enumeration value="AR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AcknowledgementType")
@XmlEnum
public enum AcknowledgementType {

    CA,
    CE,
    CR,
    AA,
    AE,
    AR;

    public String value() {
        return name();
    }

    public static AcknowledgementType fromValue(String v) {
        return valueOf(v);
    }

}
