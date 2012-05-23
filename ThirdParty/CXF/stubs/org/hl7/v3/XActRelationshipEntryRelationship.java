
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for x_ActRelationshipEntryRelationship.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="x_ActRelationshipEntryRelationship">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="XCRPT"/>
 *     &lt;enumeration value="GEVL"/>
 *     &lt;enumeration value="COMP"/>
 *     &lt;enumeration value="RSON"/>
 *     &lt;enumeration value="SUBJ"/>
 *     &lt;enumeration value="SPRT"/>
 *     &lt;enumeration value="CAUS"/>
 *     &lt;enumeration value="MFST"/>
 *     &lt;enumeration value="REFR"/>
 *     &lt;enumeration value="SAS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "x_ActRelationshipEntryRelationship")
@XmlEnum
public enum XActRelationshipEntryRelationship {

    XCRPT,
    GEVL,
    COMP,
    RSON,
    SUBJ,
    SPRT,
    CAUS,
    MFST,
    REFR,
    SAS;

    public String value() {
        return name();
    }

    public static XActRelationshipEntryRelationship fromValue(String v) {
        return valueOf(v);
    }

}
