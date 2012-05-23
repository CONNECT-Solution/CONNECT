
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OralSolution.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OralSolution">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ORALSOL"/>
 *     &lt;enumeration value="ELIXIR"/>
 *     &lt;enumeration value="RINSE"/>
 *     &lt;enumeration value="ORDROP"/>
 *     &lt;enumeration value="SYRUP"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OralSolution")
@XmlEnum
public enum OralSolution {

    ORALSOL,
    ELIXIR,
    RINSE,
    ORDROP,
    SYRUP;

    public String value() {
        return name();
    }

    public static OralSolution fromValue(String v) {
        return valueOf(v);
    }

}
