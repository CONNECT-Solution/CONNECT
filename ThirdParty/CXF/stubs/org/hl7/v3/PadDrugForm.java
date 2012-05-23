
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PadDrugForm.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PadDrugForm">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="PAD"/>
 *     &lt;enumeration value="MEDPAD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PadDrugForm")
@XmlEnum
public enum PadDrugForm {

    PAD,
    MEDPAD;

    public String value() {
        return name();
    }

    public static PadDrugForm fromValue(String v) {
        return valueOf(v);
    }

}
