
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SwabDrugForm.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SwabDrugForm">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="SWAB"/>
 *     &lt;enumeration value="MEDSWAB"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SwabDrugForm")
@XmlEnum
public enum SwabDrugForm {

    SWAB,
    MEDSWAB;

    public String value() {
        return name();
    }

    public static SwabDrugForm fromValue(String v) {
        return valueOf(v);
    }

}
