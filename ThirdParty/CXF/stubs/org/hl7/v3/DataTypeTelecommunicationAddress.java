
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DataTypeTelecommunicationAddress.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DataTypeTelecommunicationAddress">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="TEL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DataTypeTelecommunicationAddress")
@XmlEnum
public enum DataTypeTelecommunicationAddress {

    TEL;

    public String value() {
        return name();
    }

    public static DataTypeTelecommunicationAddress fromValue(String v) {
        return valueOf(v);
    }

}
