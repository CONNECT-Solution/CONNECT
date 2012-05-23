
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RectalInstillation.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RectalInstillation">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="RECINSTL"/>
 *     &lt;enumeration value="RECTINSTL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "RectalInstillation")
@XmlEnum
public enum RectalInstillation {

    RECINSTL,
    RECTINSTL;

    public String value() {
        return name();
    }

    public static RectalInstillation fromValue(String v) {
        return valueOf(v);
    }

}
