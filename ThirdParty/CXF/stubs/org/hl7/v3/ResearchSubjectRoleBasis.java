
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResearchSubjectRoleBasis.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ResearchSubjectRoleBasis">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ERL"/>
 *     &lt;enumeration value="SCN"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ResearchSubjectRoleBasis")
@XmlEnum
public enum ResearchSubjectRoleBasis {

    ERL,
    SCN;

    public String value() {
        return name();
    }

    public static ResearchSubjectRoleBasis fromValue(String v) {
        return valueOf(v);
    }

}
