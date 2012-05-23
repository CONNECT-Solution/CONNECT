
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EntityClassNonPersonLivingSubject.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EntityClassNonPersonLivingSubject">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="NLIV"/>
 *     &lt;enumeration value="ANM"/>
 *     &lt;enumeration value="MIC"/>
 *     &lt;enumeration value="PLNT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EntityClassNonPersonLivingSubject")
@XmlEnum
public enum EntityClassNonPersonLivingSubject {

    NLIV,
    ANM,
    MIC,
    PLNT;

    public String value() {
        return name();
    }

    public static EntityClassNonPersonLivingSubject fromValue(String v) {
        return valueOf(v);
    }

}
