
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HL7StandardVersionCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="HL7StandardVersionCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ActRelationshipExpectedSubset"/>
 *     &lt;enumeration value="ActRelationshipPastSubset"/>
 *     &lt;enumeration value="_ParticipationSubset"/>
 *     &lt;enumeration value="FUTURE"/>
 *     &lt;enumeration value="LAST"/>
 *     &lt;enumeration value="NEXT"/>
 *     &lt;enumeration value="FIRST"/>
 *     &lt;enumeration value="FUTSUM"/>
 *     &lt;enumeration value="MAX"/>
 *     &lt;enumeration value="MIN"/>
 *     &lt;enumeration value="RECENT"/>
 *     &lt;enumeration value="PAST"/>
 *     &lt;enumeration value="PREVSUM"/>
 *     &lt;enumeration value="SUM"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "HL7StandardVersionCode")
@XmlEnum
public enum HL7StandardVersionCode {

    @XmlEnumValue("ActRelationshipExpectedSubset")
    ACT_RELATIONSHIP_EXPECTED_SUBSET("ActRelationshipExpectedSubset"),
    @XmlEnumValue("ActRelationshipPastSubset")
    ACT_RELATIONSHIP_PAST_SUBSET("ActRelationshipPastSubset"),
    @XmlEnumValue("_ParticipationSubset")
    PARTICIPATION_SUBSET("_ParticipationSubset"),
    FUTURE("FUTURE"),
    LAST("LAST"),
    NEXT("NEXT"),
    FIRST("FIRST"),
    FUTSUM("FUTSUM"),
    MAX("MAX"),
    MIN("MIN"),
    RECENT("RECENT"),
    PAST("PAST"),
    PREVSUM("PREVSUM"),
    SUM("SUM");
    private final String value;

    HL7StandardVersionCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static HL7StandardVersionCode fromValue(String v) {
        for (HL7StandardVersionCode c: HL7StandardVersionCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
