
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MDFAttributeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MDFAttributeType">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ADDR"/>
 *     &lt;enumeration value="CD"/>
 *     &lt;enumeration value="COM"/>
 *     &lt;enumeration value="DTTM"/>
 *     &lt;enumeration value="DESC"/>
 *     &lt;enumeration value="EXPR"/>
 *     &lt;enumeration value="FRC"/>
 *     &lt;enumeration value="TIME"/>
 *     &lt;enumeration value="ID"/>
 *     &lt;enumeration value="IND"/>
 *     &lt;enumeration value="NM"/>
 *     &lt;enumeration value="NBR"/>
 *     &lt;enumeration value="PHON"/>
 *     &lt;enumeration value="QTY"/>
 *     &lt;enumeration value="TXT"/>
 *     &lt;enumeration value="TMR"/>
 *     &lt;enumeration value="VALUE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MDFAttributeType")
@XmlEnum
public enum MDFAttributeType {

    ADDR,
    CD,
    COM,
    DTTM,
    DESC,
    EXPR,
    FRC,
    TIME,
    ID,
    IND,
    NM,
    NBR,
    PHON,
    QTY,
    TXT,
    TMR,
    VALUE;

    public String value() {
        return name();
    }

    public static MDFAttributeType fromValue(String v) {
        return valueOf(v);
    }

}
