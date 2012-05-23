
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IntraocularRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="IntraocularRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="IOINJ"/>
 *     &lt;enumeration value="IOSURGINS"/>
 *     &lt;enumeration value="IOINSTL"/>
 *     &lt;enumeration value="IOIRR"/>
 *     &lt;enumeration value="IOTOP"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "IntraocularRoute")
@XmlEnum
public enum IntraocularRoute {

    IOINJ,
    IOSURGINS,
    IOINSTL,
    IOIRR,
    IOTOP;

    public String value() {
        return name();
    }

    public static IntraocularRoute fromValue(String v) {
        return valueOf(v);
    }

}
