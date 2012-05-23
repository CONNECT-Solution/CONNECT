
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ObservationEligibilityIndicatorValue.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ObservationEligibilityIndicatorValue">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ELSTAT"/>
 *     &lt;enumeration value="ADOPT"/>
 *     &lt;enumeration value="BTHCERT"/>
 *     &lt;enumeration value="CCOC"/>
 *     &lt;enumeration value="DRLIC"/>
 *     &lt;enumeration value="FOSTER"/>
 *     &lt;enumeration value="MRGCERT"/>
 *     &lt;enumeration value="MIL"/>
 *     &lt;enumeration value="PASSPORT"/>
 *     &lt;enumeration value="MEMBER"/>
 *     &lt;enumeration value="STUDENRL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ObservationEligibilityIndicatorValue")
@XmlEnum
public enum ObservationEligibilityIndicatorValue {

    ELSTAT,
    ADOPT,
    BTHCERT,
    CCOC,
    DRLIC,
    FOSTER,
    MRGCERT,
    MIL,
    PASSPORT,
    MEMBER,
    STUDENRL;

    public String value() {
        return name();
    }

    public static ObservationEligibilityIndicatorValue fromValue(String v) {
        return valueOf(v);
    }

}
