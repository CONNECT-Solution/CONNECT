
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActUncertainty.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActUncertainty">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="N"/>
 *     &lt;enumeration value="U"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActUncertainty")
@XmlEnum
public enum ActUncertainty {

    N,
    U;

    public String value() {
        return name();
    }

    public static ActUncertainty fromValue(String v) {
        return valueOf(v);
    }

}
