
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TableFrame.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TableFrame">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="above"/>
 *     &lt;enumeration value="below"/>
 *     &lt;enumeration value="border"/>
 *     &lt;enumeration value="box"/>
 *     &lt;enumeration value="hsides"/>
 *     &lt;enumeration value="lhs"/>
 *     &lt;enumeration value="rhs"/>
 *     &lt;enumeration value="void"/>
 *     &lt;enumeration value="vsides"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TableFrame")
@XmlEnum
public enum TableFrame {

    @XmlEnumValue("above")
    ABOVE("above"),
    @XmlEnumValue("below")
    BELOW("below"),
    @XmlEnumValue("border")
    BORDER("border"),
    @XmlEnumValue("box")
    BOX("box"),
    @XmlEnumValue("hsides")
    HSIDES("hsides"),
    @XmlEnumValue("lhs")
    LHS("lhs"),
    @XmlEnumValue("rhs")
    RHS("rhs"),
    @XmlEnumValue("void")
    VOID("void"),
    @XmlEnumValue("vsides")
    VSIDES("vsides");
    private final String value;

    TableFrame(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TableFrame fromValue(String v) {
        for (TableFrame c: TableFrame.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
