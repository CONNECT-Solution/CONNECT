
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AdditionalLocator.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AdditionalLocator">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ADL"/>
 *     &lt;enumeration value="UNIT"/>
 *     &lt;enumeration value="UNID"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AdditionalLocator")
@XmlEnum
public enum AdditionalLocator {

    ADL,
    UNIT,
    UNID;

    public String value() {
        return name();
    }

    public static AdditionalLocator fromValue(String v) {
        return valueOf(v);
    }

}
