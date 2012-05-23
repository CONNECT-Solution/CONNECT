
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for x_ActFinancialProductAcquisitionCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="x_ActFinancialProductAcquisitionCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="RENT"/>
 *     &lt;enumeration value="SALE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "x_ActFinancialProductAcquisitionCode")
@XmlEnum
public enum XActFinancialProductAcquisitionCode {

    RENT,
    SALE;

    public String value() {
        return name();
    }

    public static XActFinancialProductAcquisitionCode fromValue(String v) {
        return valueOf(v);
    }

}
