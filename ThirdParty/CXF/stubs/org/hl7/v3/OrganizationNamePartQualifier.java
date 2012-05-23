
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OrganizationNamePartQualifier.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OrganizationNamePartQualifier">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="LS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OrganizationNamePartQualifier")
@XmlEnum
public enum OrganizationNamePartQualifier {

    LS;

    public String value() {
        return name();
    }

    public static OrganizationNamePartQualifier fromValue(String v) {
        return valueOf(v);
    }

}
