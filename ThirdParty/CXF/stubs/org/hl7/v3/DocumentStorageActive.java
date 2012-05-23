
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DocumentStorageActive.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DocumentStorageActive">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="AC"/>
 *     &lt;enumeration value="AA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DocumentStorageActive")
@XmlEnum
public enum DocumentStorageActive {

    AC,
    AA;

    public String value() {
        return name();
    }

    public static DocumentStorageActive fromValue(String v) {
        return valueOf(v);
    }

}
