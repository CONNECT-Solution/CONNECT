
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IntraesophagealRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="IntraesophagealRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="IESOPHINSTIL"/>
 *     &lt;enumeration value="IESOPHTA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "IntraesophagealRoute")
@XmlEnum
public enum IntraesophagealRoute {

    IESOPHINSTIL,
    IESOPHTA;

    public String value() {
        return name();
    }

    public static IntraesophagealRoute fromValue(String v) {
        return valueOf(v);
    }

}
