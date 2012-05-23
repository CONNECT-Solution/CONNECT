
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActContainerRegistrationCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActContainerRegistrationCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="X"/>
 *     &lt;enumeration value="ID"/>
 *     &lt;enumeration value="IP"/>
 *     &lt;enumeration value="O"/>
 *     &lt;enumeration value="L"/>
 *     &lt;enumeration value="M"/>
 *     &lt;enumeration value="R"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActContainerRegistrationCode")
@XmlEnum
public enum ActContainerRegistrationCode {

    X,
    ID,
    IP,
    O,
    L,
    M,
    R;

    public String value() {
        return name();
    }

    public static ActContainerRegistrationCode fromValue(String v) {
        return valueOf(v);
    }

}
