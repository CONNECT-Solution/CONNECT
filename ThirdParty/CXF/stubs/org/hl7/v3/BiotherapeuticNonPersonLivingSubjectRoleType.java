
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BiotherapeuticNon-personLivingSubjectRoleType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="BiotherapeuticNon-personLivingSubjectRoleType">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="BIOTH"/>
 *     &lt;enumeration value="ANTIBIOT"/>
 *     &lt;enumeration value="DEBR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "BiotherapeuticNon-personLivingSubjectRoleType")
@XmlEnum
public enum BiotherapeuticNonPersonLivingSubjectRoleType {

    BIOTH,
    ANTIBIOT,
    DEBR;

    public String value() {
        return name();
    }

    public static BiotherapeuticNonPersonLivingSubjectRoleType fromValue(String v) {
        return valueOf(v);
    }

}
