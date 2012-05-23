
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActRelationshipSplit.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActRelationshipSplit">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="E1"/>
 *     &lt;enumeration value="EW"/>
 *     &lt;enumeration value="I1"/>
 *     &lt;enumeration value="IW"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActRelationshipSplit")
@XmlEnum
public enum ActRelationshipSplit {

    @XmlEnumValue("E1")
    E_1("E1"),
    EW("EW"),
    @XmlEnumValue("I1")
    I_1("I1"),
    IW("IW");
    private final String value;

    ActRelationshipSplit(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ActRelationshipSplit fromValue(String v) {
        for (ActRelationshipSplit c: ActRelationshipSplit.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
