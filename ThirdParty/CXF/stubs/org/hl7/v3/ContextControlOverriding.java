
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ContextControlOverriding.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ContextControlOverriding">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ON"/>
 *     &lt;enumeration value="OP"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ContextControlOverriding")
@XmlEnum
public enum ContextControlOverriding {

    ON,
    OP;

    public String value() {
        return name();
    }

    public static ContextControlOverriding fromValue(String v) {
        return valueOf(v);
    }

}
