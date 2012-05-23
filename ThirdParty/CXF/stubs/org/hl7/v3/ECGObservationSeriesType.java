
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ECGObservationSeriesType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ECGObservationSeriesType">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="REPRESENTATIVE_BEAT"/>
 *     &lt;enumeration value="RHYTHM"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ECGObservationSeriesType")
@XmlEnum
public enum ECGObservationSeriesType {

    REPRESENTATIVE_BEAT,
    RHYTHM;

    public String value() {
        return name();
    }

    public static ECGObservationSeriesType fromValue(String v) {
        return valueOf(v);
    }

}
