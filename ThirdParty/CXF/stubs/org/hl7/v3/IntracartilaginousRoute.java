
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IntracartilaginousRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="IntracartilaginousRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ICARTINJ"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "IntracartilaginousRoute")
@XmlEnum
public enum IntracartilaginousRoute {

    ICARTINJ;

    public String value() {
        return name();
    }

    public static IntracartilaginousRoute fromValue(String v) {
        return valueOf(v);
    }

}
