
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SetOperator.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SetOperator">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="_ValueSetOperator"/>
 *     &lt;enumeration value="H"/>
 *     &lt;enumeration value="E"/>
 *     &lt;enumeration value="I"/>
 *     &lt;enumeration value="A"/>
 *     &lt;enumeration value="P"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SetOperator")
@XmlEnum
public enum SetOperator {

    @XmlEnumValue("_ValueSetOperator")
    VALUE_SET_OPERATOR("_ValueSetOperator"),
    H("H"),
    E("E"),
    I("I"),
    A("A"),
    P("P");
    private final String value;

    SetOperator(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SetOperator fromValue(String v) {
        for (SetOperator c: SetOperator.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
