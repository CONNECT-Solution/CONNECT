
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IntravenousInjection.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="IntravenousInjection">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="IVINJ"/>
 *     &lt;enumeration value="IVINJBOL"/>
 *     &lt;enumeration value="IVPUSH"/>
 *     &lt;enumeration value="IVRPUSH"/>
 *     &lt;enumeration value="IVSPUSH"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "IntravenousInjection")
@XmlEnum
public enum IntravenousInjection {

    IVINJ,
    IVINJBOL,
    IVPUSH,
    IVRPUSH,
    IVSPUSH;

    public String value() {
        return name();
    }

    public static IntravenousInjection fromValue(String v) {
        return valueOf(v);
    }

}
