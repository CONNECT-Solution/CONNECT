
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for x_PhoneOrEmailURLScheme.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="x_PhoneOrEmailURLScheme">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="fax"/>
 *     &lt;enumeration value="mailto"/>
 *     &lt;enumeration value="tel"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "x_PhoneOrEmailURLScheme")
@XmlEnum
public enum XPhoneOrEmailURLScheme {

    @XmlEnumValue("fax")
    FAX("fax"),
    @XmlEnumValue("mailto")
    MAILTO("mailto"),
    @XmlEnumValue("tel")
    TEL("tel");
    private final String value;

    XPhoneOrEmailURLScheme(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static XPhoneOrEmailURLScheme fromValue(String v) {
        for (XPhoneOrEmailURLScheme c: XPhoneOrEmailURLScheme.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
