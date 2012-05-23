
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DeliveryAddressLine.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DeliveryAddressLine">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="DAL"/>
 *     &lt;enumeration value="DINSTA"/>
 *     &lt;enumeration value="DINSTQ"/>
 *     &lt;enumeration value="DINST"/>
 *     &lt;enumeration value="DMOD"/>
 *     &lt;enumeration value="DMODID"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DeliveryAddressLine")
@XmlEnum
public enum DeliveryAddressLine {

    DAL,
    DINSTA,
    DINSTQ,
    DINST,
    DMOD,
    DMODID;

    public String value() {
        return name();
    }

    public static DeliveryAddressLine fromValue(String v) {
        return valueOf(v);
    }

}
