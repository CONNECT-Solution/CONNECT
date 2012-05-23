
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DataTypeBagOfPhysicalQuantities.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DataTypeBagOfPhysicalQuantities">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="BAG&lt;PQ>"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DataTypeBagOfPhysicalQuantities")
@XmlEnum
public enum DataTypeBagOfPhysicalQuantities {

    @XmlEnumValue("BAG<PQ>")
    BAG_PQ("BAG<PQ>");
    private final String value;

    DataTypeBagOfPhysicalQuantities(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DataTypeBagOfPhysicalQuantities fromValue(String v) {
        for (DataTypeBagOfPhysicalQuantities c: DataTypeBagOfPhysicalQuantities.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
