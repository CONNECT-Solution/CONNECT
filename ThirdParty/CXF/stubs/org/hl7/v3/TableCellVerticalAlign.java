
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TableCellVerticalAlign.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TableCellVerticalAlign">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="baseline"/>
 *     &lt;enumeration value="bottom"/>
 *     &lt;enumeration value="middle"/>
 *     &lt;enumeration value="top"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TableCellVerticalAlign")
@XmlEnum
public enum TableCellVerticalAlign {

    @XmlEnumValue("baseline")
    BASELINE("baseline"),
    @XmlEnumValue("bottom")
    BOTTOM("bottom"),
    @XmlEnumValue("middle")
    MIDDLE("middle"),
    @XmlEnumValue("top")
    TOP("top");
    private final String value;

    TableCellVerticalAlign(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TableCellVerticalAlign fromValue(String v) {
        for (TableCellVerticalAlign c: TableCellVerticalAlign.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
