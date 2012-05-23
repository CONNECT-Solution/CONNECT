
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IntramedullaryRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="IntramedullaryRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="IMEDULINJ"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "IntramedullaryRoute")
@XmlEnum
public enum IntramedullaryRoute {

    IMEDULINJ;

    public String value() {
        return name();
    }

    public static IntramedullaryRoute fromValue(String v) {
        return valueOf(v);
    }

}
