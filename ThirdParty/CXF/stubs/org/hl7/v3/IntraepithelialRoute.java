
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IntraepithelialRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="IntraepithelialRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="IEPITHINJ"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "IntraepithelialRoute")
@XmlEnum
public enum IntraepithelialRoute {

    IEPITHINJ;

    public String value() {
        return name();
    }

    public static IntraepithelialRoute fromValue(String v) {
        return valueOf(v);
    }

}
