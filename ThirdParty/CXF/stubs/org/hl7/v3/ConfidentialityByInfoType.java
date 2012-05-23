
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ConfidentialityByInfoType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ConfidentialityByInfoType">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="HIV"/>
 *     &lt;enumeration value="PSY"/>
 *     &lt;enumeration value="SDV"/>
 *     &lt;enumeration value="ETH"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ConfidentialityByInfoType")
@XmlEnum
public enum ConfidentialityByInfoType {

    HIV,
    PSY,
    SDV,
    ETH;

    public String value() {
        return name();
    }

    public static ConfidentialityByInfoType fromValue(String v) {
        return valueOf(v);
    }

}
