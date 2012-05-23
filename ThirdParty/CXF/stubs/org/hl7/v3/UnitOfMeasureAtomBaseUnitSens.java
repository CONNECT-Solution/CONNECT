
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UnitOfMeasureAtomBaseUnitSens.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="UnitOfMeasureAtomBaseUnitSens">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="C"/>
 *     &lt;enumeration value="K"/>
 *     &lt;enumeration value="cd"/>
 *     &lt;enumeration value="g"/>
 *     &lt;enumeration value="m"/>
 *     &lt;enumeration value="rad"/>
 *     &lt;enumeration value="s"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "UnitOfMeasureAtomBaseUnitSens")
@XmlEnum
public enum UnitOfMeasureAtomBaseUnitSens {

    C("C"),
    K("K"),
    @XmlEnumValue("cd")
    CD("cd"),
    @XmlEnumValue("g")
    G("g"),
    @XmlEnumValue("m")
    M("m"),
    @XmlEnumValue("rad")
    RAD("rad"),
    @XmlEnumValue("s")
    S("s");
    private final String value;

    UnitOfMeasureAtomBaseUnitSens(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static UnitOfMeasureAtomBaseUnitSens fromValue(String v) {
        for (UnitOfMeasureAtomBaseUnitSens c: UnitOfMeasureAtomBaseUnitSens.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
