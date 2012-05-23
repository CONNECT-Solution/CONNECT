
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ApplicationMediaType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ApplicationMediaType">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="application/dicom"/>
 *     &lt;enumeration value="application/msword"/>
 *     &lt;enumeration value="application/pdf"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ApplicationMediaType")
@XmlEnum
public enum ApplicationMediaType {

    @XmlEnumValue("application/dicom")
    APPLICATION_DICOM("application/dicom"),
    @XmlEnumValue("application/msword")
    APPLICATION_MSWORD("application/msword"),
    @XmlEnumValue("application/pdf")
    APPLICATION_PDF("application/pdf");
    private final String value;

    ApplicationMediaType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ApplicationMediaType fromValue(String v) {
        for (ApplicationMediaType c: ApplicationMediaType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
