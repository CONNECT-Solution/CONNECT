
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IntrapleuralRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="IntrapleuralRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="IPLRINJ"/>
 *     &lt;enumeration value="CTINSTL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "IntrapleuralRoute")
@XmlEnum
public enum IntrapleuralRoute {

    IPLRINJ,
    CTINSTL;

    public String value() {
        return name();
    }

    public static IntrapleuralRoute fromValue(String v) {
        return valueOf(v);
    }

}
