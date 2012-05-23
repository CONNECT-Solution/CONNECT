
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for x_ActRelationshipPatientTransport.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="x_ActRelationshipPatientTransport">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ARR"/>
 *     &lt;enumeration value="DEP"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "x_ActRelationshipPatientTransport")
@XmlEnum
public enum XActRelationshipPatientTransport {

    ARR,
    DEP;

    public String value() {
        return name();
    }

    public static XActRelationshipPatientTransport fromValue(String v) {
        return valueOf(v);
    }

}
