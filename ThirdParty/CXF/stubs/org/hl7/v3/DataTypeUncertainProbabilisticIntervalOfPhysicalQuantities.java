
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DataTypeUncertainProbabilisticIntervalOfPhysicalQuantities.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DataTypeUncertainProbabilisticIntervalOfPhysicalQuantities">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="UVP&lt;IVL&lt;PQ>>"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DataTypeUncertainProbabilisticIntervalOfPhysicalQuantities")
@XmlEnum
public enum DataTypeUncertainProbabilisticIntervalOfPhysicalQuantities {

    @XmlEnumValue("UVP<IVL<PQ>>")
    UVP_IVL_PQ("UVP<IVL<PQ>>");
    private final String value;

    DataTypeUncertainProbabilisticIntervalOfPhysicalQuantities(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DataTypeUncertainProbabilisticIntervalOfPhysicalQuantities fromValue(String v) {
        for (DataTypeUncertainProbabilisticIntervalOfPhysicalQuantities c: DataTypeUncertainProbabilisticIntervalOfPhysicalQuantities.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
