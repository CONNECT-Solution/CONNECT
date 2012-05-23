
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BiliaryRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="BiliaryRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="BILINJ"/>
 *     &lt;enumeration value="CHOLINJ"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "BiliaryRoute")
@XmlEnum
public enum BiliaryRoute {

    BILINJ,
    CHOLINJ;

    public String value() {
        return name();
    }

    public static BiliaryRoute fromValue(String v) {
        return valueOf(v);
    }

}
