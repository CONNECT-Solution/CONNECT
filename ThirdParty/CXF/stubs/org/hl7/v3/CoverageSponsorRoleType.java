
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CoverageSponsorRoleType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CoverageSponsorRoleType">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="FULLINS"/>
 *     &lt;enumeration value="SELFINS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CoverageSponsorRoleType")
@XmlEnum
public enum CoverageSponsorRoleType {

    FULLINS,
    SELFINS;

    public String value() {
        return name();
    }

    public static CoverageSponsorRoleType fromValue(String v) {
        return valueOf(v);
    }

}
