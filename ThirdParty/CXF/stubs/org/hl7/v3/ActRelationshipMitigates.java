
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActRelationshipMitigates.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActRelationshipMitigates">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="MITGT"/>
 *     &lt;enumeration value="RCVY"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActRelationshipMitigates")
@XmlEnum
public enum ActRelationshipMitigates {

    MITGT,
    RCVY;

    public String value() {
        return name();
    }

    public static ActRelationshipMitigates fromValue(String v) {
        return valueOf(v);
    }

}
