
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Insertion.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Insertion">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="CERVINS"/>
 *     &lt;enumeration value="IOSURGINS"/>
 *     &lt;enumeration value="IU"/>
 *     &lt;enumeration value="LPINS"/>
 *     &lt;enumeration value="PR"/>
 *     &lt;enumeration value="SQSURGINS"/>
 *     &lt;enumeration value="URETHINS"/>
 *     &lt;enumeration value="VAGINSI"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Insertion")
@XmlEnum
public enum Insertion {

    CERVINS,
    IOSURGINS,
    IU,
    LPINS,
    PR,
    SQSURGINS,
    URETHINS,
    VAGINSI;

    public String value() {
        return name();
    }

    public static Insertion fromValue(String v) {
        return valueOf(v);
    }

}
