
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActTaskOrderEntryCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActTaskOrderEntryCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="OE"/>
 *     &lt;enumeration value="LABOE"/>
 *     &lt;enumeration value="MEDOE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActTaskOrderEntryCode")
@XmlEnum
public enum ActTaskOrderEntryCode {

    OE,
    LABOE,
    MEDOE;

    public String value() {
        return name();
    }

    public static ActTaskOrderEntryCode fromValue(String v) {
        return valueOf(v);
    }

}
