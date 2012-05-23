
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActClassControlAct.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActClassControlAct">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="CACT"/>
 *     &lt;enumeration value="ACTN"/>
 *     &lt;enumeration value="INFO"/>
 *     &lt;enumeration value="STC"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActClassControlAct")
@XmlEnum
public enum ActClassControlAct {

    CACT,
    ACTN,
    INFO,
    STC;

    public String value() {
        return name();
    }

    public static ActClassControlAct fromValue(String v) {
        return valueOf(v);
    }

}
