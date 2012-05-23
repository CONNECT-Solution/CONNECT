
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FamilyMemberUncle.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FamilyMemberUncle">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="UNCLE"/>
 *     &lt;enumeration value="MUNCLE"/>
 *     &lt;enumeration value="PUNCLE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "FamilyMemberUncle")
@XmlEnum
public enum FamilyMemberUncle {

    UNCLE,
    MUNCLE,
    PUNCLE;

    public String value() {
        return name();
    }

    public static FamilyMemberUncle fromValue(String v) {
        return valueOf(v);
    }

}
