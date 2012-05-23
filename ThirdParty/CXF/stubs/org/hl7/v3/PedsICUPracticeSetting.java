
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PedsICUPracticeSetting.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PedsICUPracticeSetting">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="PEDICU"/>
 *     &lt;enumeration value="PEDNICU"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PedsICUPracticeSetting")
@XmlEnum
public enum PedsICUPracticeSetting {

    PEDICU,
    PEDNICU;

    public String value() {
        return name();
    }

    public static PedsICUPracticeSetting fromValue(String v) {
        return valueOf(v);
    }

}
