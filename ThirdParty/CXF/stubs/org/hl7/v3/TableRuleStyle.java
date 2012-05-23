
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TableRuleStyle.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TableRuleStyle">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="Botrule"/>
 *     &lt;enumeration value="Lrule"/>
 *     &lt;enumeration value="Rrule"/>
 *     &lt;enumeration value="Toprule"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TableRuleStyle")
@XmlEnum
public enum TableRuleStyle {

    @XmlEnumValue("Botrule")
    BOTRULE("Botrule"),
    @XmlEnumValue("Lrule")
    LRULE("Lrule"),
    @XmlEnumValue("Rrule")
    RRULE("Rrule"),
    @XmlEnumValue("Toprule")
    TOPRULE("Toprule");
    private final String value;

    TableRuleStyle(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TableRuleStyle fromValue(String v) {
        for (TableRuleStyle c: TableRuleStyle.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
