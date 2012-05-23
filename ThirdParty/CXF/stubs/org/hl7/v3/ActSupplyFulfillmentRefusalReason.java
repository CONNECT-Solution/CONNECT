
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActSupplyFulfillmentRefusalReason.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActSupplyFulfillmentRefusalReason">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="FRR05"/>
 *     &lt;enumeration value="FRR03"/>
 *     &lt;enumeration value="FRR01"/>
 *     &lt;enumeration value="FRR04"/>
 *     &lt;enumeration value="FRR02"/>
 *     &lt;enumeration value="FRR06"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActSupplyFulfillmentRefusalReason")
@XmlEnum
public enum ActSupplyFulfillmentRefusalReason {

    @XmlEnumValue("FRR05")
    FRR_05("FRR05"),
    @XmlEnumValue("FRR03")
    FRR_03("FRR03"),
    @XmlEnumValue("FRR01")
    FRR_01("FRR01"),
    @XmlEnumValue("FRR04")
    FRR_04("FRR04"),
    @XmlEnumValue("FRR02")
    FRR_02("FRR02"),
    @XmlEnumValue("FRR06")
    FRR_06("FRR06");
    private final String value;

    ActSupplyFulfillmentRefusalReason(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ActSupplyFulfillmentRefusalReason fromValue(String v) {
        for (ActSupplyFulfillmentRefusalReason c: ActSupplyFulfillmentRefusalReason.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
