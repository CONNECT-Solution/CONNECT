
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActRelationshipJoin.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActRelationshipJoin">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="D"/>
 *     &lt;enumeration value="X"/>
 *     &lt;enumeration value="K"/>
 *     &lt;enumeration value="W"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActRelationshipJoin")
@XmlEnum
public enum ActRelationshipJoin {

    D,
    X,
    K,
    W;

    public String value() {
        return name();
    }

    public static ActRelationshipJoin fromValue(String v) {
        return valueOf(v);
    }

}
