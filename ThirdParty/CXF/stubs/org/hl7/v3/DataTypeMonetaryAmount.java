
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DataTypeMonetaryAmount.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DataTypeMonetaryAmount">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="MO"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DataTypeMonetaryAmount")
@XmlEnum
public enum DataTypeMonetaryAmount {

    MO;

    public String value() {
        return name();
    }

    public static DataTypeMonetaryAmount fromValue(String v) {
        return valueOf(v);
    }

}
