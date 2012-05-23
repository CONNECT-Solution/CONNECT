
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RelationshipConjunction.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RelationshipConjunction">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="AND"/>
 *     &lt;enumeration value="XOR"/>
 *     &lt;enumeration value="OR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "RelationshipConjunction")
@XmlEnum
public enum RelationshipConjunction {

    AND,
    XOR,
    OR;

    public String value() {
        return name();
    }

    public static RelationshipConjunction fromValue(String v) {
        return valueOf(v);
    }

}
