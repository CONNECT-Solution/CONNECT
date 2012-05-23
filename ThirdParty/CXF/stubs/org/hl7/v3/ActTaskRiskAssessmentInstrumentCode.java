
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActTaskRiskAssessmentInstrumentCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActTaskRiskAssessmentInstrumentCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="RISKASSESS"/>
 *     &lt;enumeration value="FALLRISK"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActTaskRiskAssessmentInstrumentCode")
@XmlEnum
public enum ActTaskRiskAssessmentInstrumentCode {

    RISKASSESS,
    FALLRISK;

    public String value() {
        return name();
    }

    public static ActTaskRiskAssessmentInstrumentCode fromValue(String v) {
        return valueOf(v);
    }

}
