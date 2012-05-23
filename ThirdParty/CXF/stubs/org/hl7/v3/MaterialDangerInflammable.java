
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MaterialDangerInflammable.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MaterialDangerInflammable">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="IFL"/>
 *     &lt;enumeration value="EXP"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MaterialDangerInflammable")
@XmlEnum
public enum MaterialDangerInflammable {

    IFL,
    EXP;

    public String value() {
        return name();
    }

    public static MaterialDangerInflammable fromValue(String v) {
        return valueOf(v);
    }

}
