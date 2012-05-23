
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Transfer.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Transfer">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="TRANSFER"/>
 *     &lt;enumeration value="SALE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Transfer")
@XmlEnum
public enum Transfer {

    TRANSFER,
    SALE;

    public String value() {
        return name();
    }

    public static Transfer fromValue(String v) {
        return valueOf(v);
    }

}
