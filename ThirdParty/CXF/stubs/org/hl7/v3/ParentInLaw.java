
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParentInLaw.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ParentInLaw">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="PRNINLAW"/>
 *     &lt;enumeration value="FTHINLAW"/>
 *     &lt;enumeration value="MTHINLAW"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ParentInLaw")
@XmlEnum
public enum ParentInLaw {

    PRNINLAW,
    FTHINLAW,
    MTHINLAW;

    public String value() {
        return name();
    }

    public static ParentInLaw fromValue(String v) {
        return valueOf(v);
    }

}
