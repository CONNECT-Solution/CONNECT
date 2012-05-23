
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActMedicationList.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActMedicationList">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="MEDLIST"/>
 *     &lt;enumeration value="CURMEDLIST"/>
 *     &lt;enumeration value="DISCMEDLIST"/>
 *     &lt;enumeration value="HISTMEDLIST"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActMedicationList")
@XmlEnum
public enum ActMedicationList {

    MEDLIST,
    CURMEDLIST,
    DISCMEDLIST,
    HISTMEDLIST;

    public String value() {
        return name();
    }

    public static ActMedicationList fromValue(String v) {
        return valueOf(v);
    }

}
