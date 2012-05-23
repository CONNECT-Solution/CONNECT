
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TransplacentalRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TransplacentalRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="TRPLACINJ"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TransplacentalRoute")
@XmlEnum
public enum TransplacentalRoute {

    TRPLACINJ;

    public String value() {
        return name();
    }

    public static TransplacentalRoute fromValue(String v) {
        return valueOf(v);
    }

}
