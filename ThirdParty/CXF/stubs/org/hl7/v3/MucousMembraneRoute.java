
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MucousMembraneRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MucousMembraneRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="MUC"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MucousMembraneRoute")
@XmlEnum
public enum MucousMembraneRoute {

    MUC;

    public String value() {
        return name();
    }

    public static MucousMembraneRoute fromValue(String v) {
        return valueOf(v);
    }

}
