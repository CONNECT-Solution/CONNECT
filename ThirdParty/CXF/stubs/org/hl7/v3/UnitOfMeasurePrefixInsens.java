
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UnitOfMeasurePrefixInsens.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="UnitOfMeasurePrefixInsens">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="A"/>
 *     &lt;enumeration value="C"/>
 *     &lt;enumeration value="D"/>
 *     &lt;enumeration value="DA"/>
 *     &lt;enumeration value="EX"/>
 *     &lt;enumeration value="F"/>
 *     &lt;enumeration value="GIB"/>
 *     &lt;enumeration value="GA"/>
 *     &lt;enumeration value="H"/>
 *     &lt;enumeration value="KIB"/>
 *     &lt;enumeration value="K"/>
 *     &lt;enumeration value="MIB"/>
 *     &lt;enumeration value="MA"/>
 *     &lt;enumeration value="U"/>
 *     &lt;enumeration value="M"/>
 *     &lt;enumeration value="N"/>
 *     &lt;enumeration value="PT"/>
 *     &lt;enumeration value="P"/>
 *     &lt;enumeration value="TIB"/>
 *     &lt;enumeration value="TR"/>
 *     &lt;enumeration value="YO"/>
 *     &lt;enumeration value="YA"/>
 *     &lt;enumeration value="ZO"/>
 *     &lt;enumeration value="ZA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "UnitOfMeasurePrefixInsens")
@XmlEnum
public enum UnitOfMeasurePrefixInsens {

    A,
    C,
    D,
    DA,
    EX,
    F,
    GIB,
    GA,
    H,
    KIB,
    K,
    MIB,
    MA,
    U,
    M,
    N,
    PT,
    P,
    TIB,
    TR,
    YO,
    YA,
    ZO,
    ZA;

    public String value() {
        return name();
    }

    public static UnitOfMeasurePrefixInsens fromValue(String v) {
        return valueOf(v);
    }

}
