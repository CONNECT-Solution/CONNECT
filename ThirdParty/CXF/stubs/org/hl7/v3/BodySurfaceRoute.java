
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BodySurfaceRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="BodySurfaceRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ELECTOSMOS"/>
 *     &lt;enumeration value="SOAK"/>
 *     &lt;enumeration value="TOPICAL"/>
 *     &lt;enumeration value="IONTO"/>
 *     &lt;enumeration value="DRESS"/>
 *     &lt;enumeration value="SWAB"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "BodySurfaceRoute")
@XmlEnum
public enum BodySurfaceRoute {

    ELECTOSMOS,
    SOAK,
    TOPICAL,
    IONTO,
    DRESS,
    SWAB;

    public String value() {
        return name();
    }

    public static BodySurfaceRoute fromValue(String v) {
        return valueOf(v);
    }

}
