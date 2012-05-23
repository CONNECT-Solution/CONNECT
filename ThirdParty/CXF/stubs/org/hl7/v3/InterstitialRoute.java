
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InterstitialRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="InterstitialRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="INTERSTITINJ"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "InterstitialRoute")
@XmlEnum
public enum InterstitialRoute {

    INTERSTITINJ;

    public String value() {
        return name();
    }

    public static InterstitialRoute fromValue(String v) {
        return valueOf(v);
    }

}
