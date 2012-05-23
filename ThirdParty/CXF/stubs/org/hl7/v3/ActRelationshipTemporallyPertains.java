
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActRelationshipTemporallyPertains.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActRelationshipTemporallyPertains">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="SAE"/>
 *     &lt;enumeration value="SAS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActRelationshipTemporallyPertains")
@XmlEnum
public enum ActRelationshipTemporallyPertains {

    SAE,
    SAS;

    public String value() {
        return name();
    }

    public static ActRelationshipTemporallyPertains fromValue(String v) {
        return valueOf(v);
    }

}
