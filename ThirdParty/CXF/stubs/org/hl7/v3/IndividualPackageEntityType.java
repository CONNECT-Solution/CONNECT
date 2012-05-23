
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IndividualPackageEntityType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="IndividualPackageEntityType">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="AMP"/>
 *     &lt;enumeration value="MINIM"/>
 *     &lt;enumeration value="NEBAMP"/>
 *     &lt;enumeration value="OVUL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "IndividualPackageEntityType")
@XmlEnum
public enum IndividualPackageEntityType {

    AMP,
    MINIM,
    NEBAMP,
    OVUL;

    public String value() {
        return name();
    }

    public static IndividualPackageEntityType fromValue(String v) {
        return valueOf(v);
    }

}
