
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActClassFinancialContract.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActClassFinancialContract">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="FCNTRCT"/>
 *     &lt;enumeration value="COV"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActClassFinancialContract")
@XmlEnum
public enum ActClassFinancialContract {

    FCNTRCT,
    COV;

    public String value() {
        return name();
    }

    public static ActClassFinancialContract fromValue(String v) {
        return valueOf(v);
    }

}
