
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TransdermalPatch.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TransdermalPatch">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="TPATCH"/>
 *     &lt;enumeration value="TPATH16"/>
 *     &lt;enumeration value="TPATH24"/>
 *     &lt;enumeration value="TPATH72"/>
 *     &lt;enumeration value="TPATH2WK"/>
 *     &lt;enumeration value="TPATHWK"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TransdermalPatch")
@XmlEnum
public enum TransdermalPatch {

    TPATCH("TPATCH"),
    @XmlEnumValue("TPATH16")
    TPATH_16("TPATH16"),
    @XmlEnumValue("TPATH24")
    TPATH_24("TPATH24"),
    @XmlEnumValue("TPATH72")
    TPATH_72("TPATH72"),
    @XmlEnumValue("TPATH2WK")
    TPATH_2_WK("TPATH2WK"),
    TPATHWK("TPATHWK");
    private final String value;

    TransdermalPatch(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TransdermalPatch fromValue(String v) {
        for (TransdermalPatch c: TransdermalPatch.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
