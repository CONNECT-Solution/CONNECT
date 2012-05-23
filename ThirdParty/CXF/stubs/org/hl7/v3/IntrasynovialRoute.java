
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IntrasynovialRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="IntrasynovialRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ISYNINJ"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "IntrasynovialRoute")
@XmlEnum
public enum IntrasynovialRoute {

    ISYNINJ;

    public String value() {
        return name();
    }

    public static IntrasynovialRoute fromValue(String v) {
        return valueOf(v);
    }

}
