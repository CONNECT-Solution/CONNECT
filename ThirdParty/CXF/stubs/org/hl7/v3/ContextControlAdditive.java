
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ContextControlAdditive.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ContextControlAdditive">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="AN"/>
 *     &lt;enumeration value="AP"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ContextControlAdditive")
@XmlEnum
public enum ContextControlAdditive {

    AN,
    AP;

    public String value() {
        return name();
    }

    public static ContextControlAdditive fromValue(String v) {
        return valueOf(v);
    }

}
