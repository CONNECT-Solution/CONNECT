
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EpiduralRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EpiduralRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="EPI"/>
 *     &lt;enumeration value="EPIDURINJ"/>
 *     &lt;enumeration value="EPIINJ"/>
 *     &lt;enumeration value="EPINJSP"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EpiduralRoute")
@XmlEnum
public enum EpiduralRoute {

    EPI,
    EPIDURINJ,
    EPIINJ,
    EPINJSP;

    public String value() {
        return name();
    }

    public static EpiduralRoute fromValue(String v) {
        return valueOf(v);
    }

}
