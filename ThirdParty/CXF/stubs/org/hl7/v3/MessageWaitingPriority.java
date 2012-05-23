
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MessageWaitingPriority.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MessageWaitingPriority">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="H"/>
 *     &lt;enumeration value="L"/>
 *     &lt;enumeration value="M"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MessageWaitingPriority")
@XmlEnum
public enum MessageWaitingPriority {

    H,
    L,
    M;

    public String value() {
        return name();
    }

    public static MessageWaitingPriority fromValue(String v) {
        return valueOf(v);
    }

}
