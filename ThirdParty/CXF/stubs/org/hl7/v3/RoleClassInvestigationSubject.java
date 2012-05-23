
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RoleClassInvestigationSubject.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RoleClassInvestigationSubject">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="INVSBJ"/>
 *     &lt;enumeration value="CASEBJ"/>
 *     &lt;enumeration value="RESBJ"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "RoleClassInvestigationSubject")
@XmlEnum
public enum RoleClassInvestigationSubject {

    INVSBJ,
    CASEBJ,
    RESBJ;

    public String value() {
        return name();
    }

    public static RoleClassInvestigationSubject fromValue(String v) {
        return valueOf(v);
    }

}
