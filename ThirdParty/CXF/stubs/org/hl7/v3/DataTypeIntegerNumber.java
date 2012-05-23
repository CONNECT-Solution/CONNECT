
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DataTypeIntegerNumber.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DataTypeIntegerNumber">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="INT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DataTypeIntegerNumber")
@XmlEnum
public enum DataTypeIntegerNumber {

    INT;

    public String value() {
        return name();
    }

    public static DataTypeIntegerNumber fromValue(String v) {
        return valueOf(v);
    }

}
