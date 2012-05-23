
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PartialCompletionScale.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PartialCompletionScale">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="G"/>
 *     &lt;enumeration value="LE"/>
 *     &lt;enumeration value="ME"/>
 *     &lt;enumeration value="MI"/>
 *     &lt;enumeration value="N"/>
 *     &lt;enumeration value="S"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PartialCompletionScale")
@XmlEnum
public enum PartialCompletionScale {

    G,
    LE,
    ME,
    MI,
    N,
    S;

    public String value() {
        return name();
    }

    public static PartialCompletionScale fromValue(String v) {
        return valueOf(v);
    }

}
