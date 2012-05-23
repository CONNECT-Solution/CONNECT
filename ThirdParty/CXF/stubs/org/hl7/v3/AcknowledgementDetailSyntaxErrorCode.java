
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AcknowledgementDetailSyntaxErrorCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AcknowledgementDetailSyntaxErrorCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="SYN"/>
 *     &lt;enumeration value="SYN102"/>
 *     &lt;enumeration value="SYN104"/>
 *     &lt;enumeration value="SYN110"/>
 *     &lt;enumeration value="SYN112"/>
 *     &lt;enumeration value="SYN100"/>
 *     &lt;enumeration value="SYN101"/>
 *     &lt;enumeration value="SYN103"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AcknowledgementDetailSyntaxErrorCode")
@XmlEnum
public enum AcknowledgementDetailSyntaxErrorCode {

    SYN("SYN"),
    @XmlEnumValue("SYN102")
    SYN_102("SYN102"),
    @XmlEnumValue("SYN104")
    SYN_104("SYN104"),
    @XmlEnumValue("SYN110")
    SYN_110("SYN110"),
    @XmlEnumValue("SYN112")
    SYN_112("SYN112"),
    @XmlEnumValue("SYN100")
    SYN_100("SYN100"),
    @XmlEnumValue("SYN101")
    SYN_101("SYN101"),
    @XmlEnumValue("SYN103")
    SYN_103("SYN103");
    private final String value;

    AcknowledgementDetailSyntaxErrorCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AcknowledgementDetailSyntaxErrorCode fromValue(String v) {
        for (AcknowledgementDetailSyntaxErrorCode c: AcknowledgementDetailSyntaxErrorCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
