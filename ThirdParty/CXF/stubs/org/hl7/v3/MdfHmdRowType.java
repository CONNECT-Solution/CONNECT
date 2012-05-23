
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MdfHmdRowType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MdfHmdRowType">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="assoc"/>
 *     &lt;enumeration value="attr"/>
 *     &lt;enumeration value="item"/>
 *     &lt;enumeration value="hmd"/>
 *     &lt;enumeration value="class"/>
 *     &lt;enumeration value="stc"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MdfHmdRowType")
@XmlEnum
public enum MdfHmdRowType {

    @XmlEnumValue("assoc")
    ASSOC("assoc"),
    @XmlEnumValue("attr")
    ATTR("attr"),
    @XmlEnumValue("item")
    ITEM("item"),
    @XmlEnumValue("hmd")
    HMD("hmd"),
    @XmlEnumValue("class")
    CLASS("class"),
    @XmlEnumValue("stc")
    STC("stc");
    private final String value;

    MdfHmdRowType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MdfHmdRowType fromValue(String v) {
        for (MdfHmdRowType c: MdfHmdRowType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
