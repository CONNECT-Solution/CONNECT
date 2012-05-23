
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AutomobileInsurancePolicy.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AutomobileInsurancePolicy">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="AUTOPOL"/>
 *     &lt;enumeration value="COL"/>
 *     &lt;enumeration value="UNINSMOT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AutomobileInsurancePolicy")
@XmlEnum
public enum AutomobileInsurancePolicy {

    AUTOPOL,
    COL,
    UNINSMOT;

    public String value() {
        return name();
    }

    public static AutomobileInsurancePolicy fromValue(String v) {
        return valueOf(v);
    }

}
