
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for x_ActRelationshipPertinentInfo.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="x_ActRelationshipPertinentInfo">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="SUBJ"/>
 *     &lt;enumeration value="SPRT"/>
 *     &lt;enumeration value="CAUS"/>
 *     &lt;enumeration value="MFST"/>
 *     &lt;enumeration value="REFR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "x_ActRelationshipPertinentInfo")
@XmlEnum
public enum XActRelationshipPertinentInfo {

    SUBJ,
    SPRT,
    CAUS,
    MFST,
    REFR;

    public String value() {
        return name();
    }

    public static XActRelationshipPertinentInfo fromValue(String v) {
        return valueOf(v);
    }

}
