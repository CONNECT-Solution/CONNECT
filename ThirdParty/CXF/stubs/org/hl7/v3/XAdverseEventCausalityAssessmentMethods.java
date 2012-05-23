
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for x_AdverseEventCausalityAssessmentMethods.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="x_AdverseEventCausalityAssessmentMethods">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ALGM"/>
 *     &lt;enumeration value="BYCL"/>
 *     &lt;enumeration value="GINT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "x_AdverseEventCausalityAssessmentMethods")
@XmlEnum
public enum XAdverseEventCausalityAssessmentMethods {

    ALGM,
    BYCL,
    GINT;

    public String value() {
        return name();
    }

    public static XAdverseEventCausalityAssessmentMethods fromValue(String v) {
        return valueOf(v);
    }

}
