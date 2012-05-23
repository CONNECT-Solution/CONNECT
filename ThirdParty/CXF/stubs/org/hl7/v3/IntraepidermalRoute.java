
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IntraepidermalRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="IntraepidermalRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="IEPIDINJ"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "IntraepidermalRoute")
@XmlEnum
public enum IntraepidermalRoute {

    IEPIDINJ;

    public String value() {
        return name();
    }

    public static IntraepidermalRoute fromValue(String v) {
        return valueOf(v);
    }

}
