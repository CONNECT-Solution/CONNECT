
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IntralesionalRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="IntralesionalRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ILESINJ"/>
 *     &lt;enumeration value="ILESIRR"/>
 *     &lt;enumeration value="ILTOP"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "IntralesionalRoute")
@XmlEnum
public enum IntralesionalRoute {

    ILESINJ,
    ILESIRR,
    ILTOP;

    public String value() {
        return name();
    }

    public static IntralesionalRoute fromValue(String v) {
        return valueOf(v);
    }

}
