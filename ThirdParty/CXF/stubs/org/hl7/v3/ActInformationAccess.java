
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActInformationAccess.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActInformationAccess">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="INFA"/>
 *     &lt;enumeration value="INFASO"/>
 *     &lt;enumeration value="INFAO"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActInformationAccess")
@XmlEnum
public enum ActInformationAccess {

    INFA,
    INFASO,
    INFAO;

    public String value() {
        return name();
    }

    public static ActInformationAccess fromValue(String v) {
        return valueOf(v);
    }

}
