
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StepChild.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="StepChild">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="STPCHLD"/>
 *     &lt;enumeration value="STPDAU"/>
 *     &lt;enumeration value="STPSON"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "StepChild")
@XmlEnum
public enum StepChild {

    STPCHLD,
    STPDAU,
    STPSON;

    public String value() {
        return name();
    }

    public static StepChild fromValue(String v) {
        return valueOf(v);
    }

}
