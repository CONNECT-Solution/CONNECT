
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActClassGenomicObservation.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActClassGenomicObservation">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="GEN"/>
 *     &lt;enumeration value="SEQ"/>
 *     &lt;enumeration value="SEQVAR"/>
 *     &lt;enumeration value="DETPOL"/>
 *     &lt;enumeration value="EXP"/>
 *     &lt;enumeration value="LOC"/>
 *     &lt;enumeration value="PHN"/>
 *     &lt;enumeration value="POL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActClassGenomicObservation")
@XmlEnum
public enum ActClassGenomicObservation {

    GEN,
    SEQ,
    SEQVAR,
    DETPOL,
    EXP,
    LOC,
    PHN,
    POL;

    public String value() {
        return name();
    }

    public static ActClassGenomicObservation fromValue(String v) {
        return valueOf(v);
    }

}
