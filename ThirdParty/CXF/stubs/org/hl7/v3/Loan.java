
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Loan.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Loan">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="LOAN"/>
 *     &lt;enumeration value="RENT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Loan")
@XmlEnum
public enum Loan {

    LOAN,
    RENT;

    public String value() {
        return name();
    }

    public static Loan fromValue(String v) {
        return valueOf(v);
    }

}
