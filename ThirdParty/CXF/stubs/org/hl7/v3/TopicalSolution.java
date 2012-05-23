
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TopicalSolution.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TopicalSolution">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="TOPSOL"/>
 *     &lt;enumeration value="LIN"/>
 *     &lt;enumeration value="MUCTOPSOL"/>
 *     &lt;enumeration value="TINC"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TopicalSolution")
@XmlEnum
public enum TopicalSolution {

    TOPSOL,
    LIN,
    MUCTOPSOL,
    TINC;

    public String value() {
        return name();
    }

    public static TopicalSolution fromValue(String v) {
        return valueOf(v);
    }

}
