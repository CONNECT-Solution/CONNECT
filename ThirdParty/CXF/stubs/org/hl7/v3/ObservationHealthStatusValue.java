
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ObservationHealthStatusValue.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ObservationHealthStatusValue">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="HLSTAT"/>
 *     &lt;enumeration value="IVDRG"/>
 *     &lt;enumeration value="DISABLE"/>
 *     &lt;enumeration value="DRUG"/>
 *     &lt;enumeration value="PGNT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ObservationHealthStatusValue")
@XmlEnum
public enum ObservationHealthStatusValue {

    HLSTAT,
    IVDRG,
    DISABLE,
    DRUG,
    PGNT;

    public String value() {
        return name();
    }

    public static ObservationHealthStatusValue fromValue(String v) {
        return valueOf(v);
    }

}
