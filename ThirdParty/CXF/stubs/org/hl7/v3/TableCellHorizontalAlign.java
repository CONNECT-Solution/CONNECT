
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TableCellHorizontalAlign.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TableCellHorizontalAlign">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="center"/>
 *     &lt;enumeration value="char"/>
 *     &lt;enumeration value="justify"/>
 *     &lt;enumeration value="left"/>
 *     &lt;enumeration value="right"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TableCellHorizontalAlign")
@XmlEnum
public enum TableCellHorizontalAlign {

    @XmlEnumValue("center")
    CENTER("center"),
    @XmlEnumValue("char")
    CHAR("char"),
    @XmlEnumValue("justify")
    JUSTIFY("justify"),
    @XmlEnumValue("left")
    LEFT("left"),
    @XmlEnumValue("right")
    RIGHT("right");
    private final String value;

    TableCellHorizontalAlign(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TableCellHorizontalAlign fromValue(String v) {
        for (TableCellHorizontalAlign c: TableCellHorizontalAlign.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
