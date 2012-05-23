
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PastSubset.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PastSubset">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="PAST"/>
 *     &lt;enumeration value="FIRST"/>
 *     &lt;enumeration value="RECENT"/>
 *     &lt;enumeration value="PREVSUM"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PastSubset")
@XmlEnum
public enum PastSubset {

    PAST,
    FIRST,
    RECENT,
    PREVSUM;

    public String value() {
        return name();
    }

    public static PastSubset fromValue(String v) {
        return valueOf(v);
    }

}
