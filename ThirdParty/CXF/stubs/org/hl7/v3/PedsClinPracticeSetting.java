
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PedsClinPracticeSetting.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PedsClinPracticeSetting">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="PEDC"/>
 *     &lt;enumeration value="PEDCARD"/>
 *     &lt;enumeration value="PEDE"/>
 *     &lt;enumeration value="PEDGI"/>
 *     &lt;enumeration value="PEDHEM"/>
 *     &lt;enumeration value="PEDID"/>
 *     &lt;enumeration value="PEDNEPH"/>
 *     &lt;enumeration value="PEDHO"/>
 *     &lt;enumeration value="PEDRHEUM"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PedsClinPracticeSetting")
@XmlEnum
public enum PedsClinPracticeSetting {

    PEDC,
    PEDCARD,
    PEDE,
    PEDGI,
    PEDHEM,
    PEDID,
    PEDNEPH,
    PEDHO,
    PEDRHEUM;

    public String value() {
        return name();
    }

    public static PedsClinPracticeSetting fromValue(String v) {
        return valueOf(v);
    }

}
