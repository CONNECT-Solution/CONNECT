
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IntradermalRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="IntradermalRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="IDIMPLNT"/>
 *     &lt;enumeration value="IDINJ"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "IntradermalRoute")
@XmlEnum
public enum IntradermalRoute {

    IDIMPLNT,
    IDINJ;

    public String value() {
        return name();
    }

    public static IntradermalRoute fromValue(String v) {
        return valueOf(v);
    }

}
