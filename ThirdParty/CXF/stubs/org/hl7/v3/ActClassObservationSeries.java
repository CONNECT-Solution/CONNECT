
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActClassObservationSeries.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActClassObservationSeries">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="OBSSER"/>
 *     &lt;enumeration value="OBSCOR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActClassObservationSeries")
@XmlEnum
public enum ActClassObservationSeries {

    OBSSER,
    OBSCOR;

    public String value() {
        return name();
    }

    public static ActClassObservationSeries fromValue(String v) {
        return valueOf(v);
    }

}
