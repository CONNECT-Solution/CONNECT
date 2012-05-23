
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ObservationLivingDependencyValue.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ObservationLivingDependencyValue">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="LIVDEP"/>
 *     &lt;enumeration value="RELDEP"/>
 *     &lt;enumeration value="SPSDEP"/>
 *     &lt;enumeration value="URELDEP"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ObservationLivingDependencyValue")
@XmlEnum
public enum ObservationLivingDependencyValue {

    LIVDEP,
    RELDEP,
    SPSDEP,
    URELDEP;

    public String value() {
        return name();
    }

    public static ObservationLivingDependencyValue fromValue(String v) {
        return valueOf(v);
    }

}
