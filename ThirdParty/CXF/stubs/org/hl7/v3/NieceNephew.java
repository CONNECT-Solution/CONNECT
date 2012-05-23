
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NieceNephew.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="NieceNephew">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="NIENEPH"/>
 *     &lt;enumeration value="NEPHEW"/>
 *     &lt;enumeration value="NIECE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "NieceNephew")
@XmlEnum
public enum NieceNephew {

    NIENEPH,
    NEPHEW,
    NIECE;

    public String value() {
        return name();
    }

    public static NieceNephew fromValue(String v) {
        return valueOf(v);
    }

}
