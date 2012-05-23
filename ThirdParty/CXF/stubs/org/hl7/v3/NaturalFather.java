
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NaturalFather.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="NaturalFather">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="NFTH"/>
 *     &lt;enumeration value="NFTHF"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "NaturalFather")
@XmlEnum
public enum NaturalFather {

    NFTH,
    NFTHF;

    public String value() {
        return name();
    }

    public static NaturalFather fromValue(String v) {
        return valueOf(v);
    }

}
