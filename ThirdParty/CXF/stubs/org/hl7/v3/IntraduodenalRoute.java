
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IntraduodenalRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="IntraduodenalRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="IDUODINSTIL"/>
 *     &lt;enumeration value="IDOUDMAB"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "IntraduodenalRoute")
@XmlEnum
public enum IntraduodenalRoute {

    IDUODINSTIL,
    IDOUDMAB;

    public String value() {
        return name();
    }

    public static IntraduodenalRoute fromValue(String v) {
        return valueOf(v);
    }

}
