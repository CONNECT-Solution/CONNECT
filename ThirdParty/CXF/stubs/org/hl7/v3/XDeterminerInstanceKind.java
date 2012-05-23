
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for x_DeterminerInstanceKind.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="x_DeterminerInstanceKind">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="KIND"/>
 *     &lt;enumeration value="QUANTIFIED_KIND"/>
 *     &lt;enumeration value="INSTANCE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "x_DeterminerInstanceKind")
@XmlEnum
public enum XDeterminerInstanceKind {

    KIND,
    QUANTIFIED_KIND,
    INSTANCE;

    public String value() {
        return name();
    }

    public static XDeterminerInstanceKind fromValue(String v) {
        return valueOf(v);
    }

}
