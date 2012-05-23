
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OrthoClinPracticeSetting.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OrthoClinPracticeSetting">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ORTHO"/>
 *     &lt;enumeration value="HAND"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OrthoClinPracticeSetting")
@XmlEnum
public enum OrthoClinPracticeSetting {

    ORTHO,
    HAND;

    public String value() {
        return name();
    }

    public static OrthoClinPracticeSetting fromValue(String v) {
        return valueOf(v);
    }

}
