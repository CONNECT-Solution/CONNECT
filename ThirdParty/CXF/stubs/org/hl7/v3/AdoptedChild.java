
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AdoptedChild.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AdoptedChild">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="CHLDADOPT"/>
 *     &lt;enumeration value="DAUADOPT"/>
 *     &lt;enumeration value="SONADOPT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AdoptedChild")
@XmlEnum
public enum AdoptedChild {

    CHLDADOPT,
    DAUADOPT,
    SONADOPT;

    public String value() {
        return name();
    }

    public static AdoptedChild fromValue(String v) {
        return valueOf(v);
    }

}
