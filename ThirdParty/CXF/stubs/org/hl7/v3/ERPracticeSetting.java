
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ERPracticeSetting.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ERPracticeSetting">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ER"/>
 *     &lt;enumeration value="ETU"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ERPracticeSetting")
@XmlEnum
public enum ERPracticeSetting {

    ER,
    ETU;

    public String value() {
        return name();
    }

    public static ERPracticeSetting fromValue(String v) {
        return valueOf(v);
    }

}
