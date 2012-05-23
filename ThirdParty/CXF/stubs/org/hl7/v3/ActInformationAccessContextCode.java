
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActInformationAccessContextCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActInformationAccessContextCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="INFCON"/>
 *     &lt;enumeration value="INFDNG"/>
 *     &lt;enumeration value="INFPWR"/>
 *     &lt;enumeration value="INFEMER"/>
 *     &lt;enumeration value="INFCRT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActInformationAccessContextCode")
@XmlEnum
public enum ActInformationAccessContextCode {

    INFCON,
    INFDNG,
    INFPWR,
    INFEMER,
    INFCRT;

    public String value() {
        return name();
    }

    public static ActInformationAccessContextCode fromValue(String v) {
        return valueOf(v);
    }

}
