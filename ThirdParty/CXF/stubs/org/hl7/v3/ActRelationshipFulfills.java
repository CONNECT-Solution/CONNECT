
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActRelationshipFulfills.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActRelationshipFulfills">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="FLFS"/>
 *     &lt;enumeration value="OCCR"/>
 *     &lt;enumeration value="OREF"/>
 *     &lt;enumeration value="SCH"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActRelationshipFulfills")
@XmlEnum
public enum ActRelationshipFulfills {

    FLFS,
    OCCR,
    OREF,
    SCH;

    public String value() {
        return name();
    }

    public static ActRelationshipFulfills fromValue(String v) {
        return valueOf(v);
    }

}
