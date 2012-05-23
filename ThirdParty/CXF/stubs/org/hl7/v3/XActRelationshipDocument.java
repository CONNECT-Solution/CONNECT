
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for x_ActRelationshipDocument.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="x_ActRelationshipDocument">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="APND"/>
 *     &lt;enumeration value="RPLC"/>
 *     &lt;enumeration value="XFRM"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "x_ActRelationshipDocument")
@XmlEnum
public enum XActRelationshipDocument {

    APND,
    RPLC,
    XFRM;

    public String value() {
        return name();
    }

    public static XActRelationshipDocument fromValue(String v) {
        return valueOf(v);
    }

}
