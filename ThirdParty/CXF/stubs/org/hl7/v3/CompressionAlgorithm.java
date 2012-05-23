
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CompressionAlgorithm.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CompressionAlgorithm">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="Z"/>
 *     &lt;enumeration value="DF"/>
 *     &lt;enumeration value="GZ"/>
 *     &lt;enumeration value="ZL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CompressionAlgorithm")
@XmlEnum
public enum CompressionAlgorithm {

    Z,
    DF,
    GZ,
    ZL;

    public String value() {
        return name();
    }

    public static CompressionAlgorithm fromValue(String v) {
        return valueOf(v);
    }

}
