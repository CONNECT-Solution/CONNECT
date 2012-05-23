
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GenitourinaryRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="GenitourinaryRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="GUIRR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "GenitourinaryRoute")
@XmlEnum
public enum GenitourinaryRoute {

    GUIRR;

    public String value() {
        return name();
    }

    public static GenitourinaryRoute fromValue(String v) {
        return valueOf(v);
    }

}
