
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DataTypePostalAndResidentialAddress.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DataTypePostalAndResidentialAddress">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="AD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DataTypePostalAndResidentialAddress")
@XmlEnum
public enum DataTypePostalAndResidentialAddress {

    AD;

    public String value() {
        return name();
    }

    public static DataTypePostalAndResidentialAddress fromValue(String v) {
        return valueOf(v);
    }

}
