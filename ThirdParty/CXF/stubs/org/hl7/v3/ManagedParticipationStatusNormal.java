
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ManagedParticipationStatusNormal.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ManagedParticipationStatusNormal">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="normal"/>
 *     &lt;enumeration value="active"/>
 *     &lt;enumeration value="cancelled"/>
 *     &lt;enumeration value="completed"/>
 *     &lt;enumeration value="pending"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ManagedParticipationStatusNormal")
@XmlEnum
public enum ManagedParticipationStatusNormal {

    @XmlEnumValue("normal")
    NORMAL("normal"),
    @XmlEnumValue("active")
    ACTIVE("active"),
    @XmlEnumValue("cancelled")
    CANCELLED("cancelled"),
    @XmlEnumValue("completed")
    COMPLETED("completed"),
    @XmlEnumValue("pending")
    PENDING("pending");
    private final String value;

    ManagedParticipationStatusNormal(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ManagedParticipationStatusNormal fromValue(String v) {
        for (ManagedParticipationStatusNormal c: ManagedParticipationStatusNormal.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
