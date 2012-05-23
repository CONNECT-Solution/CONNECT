
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IntratympanicRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="IntratympanicRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ITYMPINJ"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "IntratympanicRoute")
@XmlEnum
public enum IntratympanicRoute {

    ITYMPINJ;

    public String value() {
        return name();
    }

    public static IntratympanicRoute fromValue(String v) {
        return valueOf(v);
    }

}
