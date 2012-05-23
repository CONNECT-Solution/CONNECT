
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HeightSurfaceAreaAlert.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="HeightSurfaceAreaAlert">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="DOSEHINDSA"/>
 *     &lt;enumeration value="DOSELINDSA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "HeightSurfaceAreaAlert")
@XmlEnum
public enum HeightSurfaceAreaAlert {

    DOSEHINDSA,
    DOSELINDSA;

    public String value() {
        return name();
    }

    public static HeightSurfaceAreaAlert fromValue(String v) {
        return valueOf(v);
    }

}
