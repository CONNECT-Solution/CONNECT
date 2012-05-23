
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActInsurancePolicyCodeDiseaseProgramByBOT.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActInsurancePolicyCodeDiseaseProgramByBOT">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="DISEASEPRG"/>
 *     &lt;enumeration value="HIVAIDS"/>
 *     &lt;enumeration value="CANPRG"/>
 *     &lt;enumeration value="ENDRENAL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActInsurancePolicyCodeDiseaseProgramByBOT")
@XmlEnum
public enum ActInsurancePolicyCodeDiseaseProgramByBOT {

    DISEASEPRG,
    HIVAIDS,
    CANPRG,
    ENDRENAL;

    public String value() {
        return name();
    }

    public static ActInsurancePolicyCodeDiseaseProgramByBOT fromValue(String v) {
        return valueOf(v);
    }

}
