
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IontophoresisRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="IontophoresisRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="IONTO"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "IontophoresisRoute")
@XmlEnum
public enum IontophoresisRoute {

    IONTO;

    public String value() {
        return name();
    }

    public static IontophoresisRoute fromValue(String v) {
        return valueOf(v);
    }

}
