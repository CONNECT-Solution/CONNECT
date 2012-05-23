
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Currency.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Currency">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ARS"/>
 *     &lt;enumeration value="AUD"/>
 *     &lt;enumeration value="THB"/>
 *     &lt;enumeration value="BRL"/>
 *     &lt;enumeration value="CAD"/>
 *     &lt;enumeration value="DEM"/>
 *     &lt;enumeration value="EUR"/>
 *     &lt;enumeration value="FRF"/>
 *     &lt;enumeration value="INR"/>
 *     &lt;enumeration value="TRL"/>
 *     &lt;enumeration value="FIM"/>
 *     &lt;enumeration value="MXN"/>
 *     &lt;enumeration value="NLG"/>
 *     &lt;enumeration value="NZD"/>
 *     &lt;enumeration value="PHP"/>
 *     &lt;enumeration value="GBP"/>
 *     &lt;enumeration value="ZAR"/>
 *     &lt;enumeration value="RUR"/>
 *     &lt;enumeration value="ILS"/>
 *     &lt;enumeration value="ESP"/>
 *     &lt;enumeration value="CHF"/>
 *     &lt;enumeration value="TWD"/>
 *     &lt;enumeration value="USD"/>
 *     &lt;enumeration value="CLF"/>
 *     &lt;enumeration value="KRW"/>
 *     &lt;enumeration value="JPY"/>
 *     &lt;enumeration value="CNY"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Currency")
@XmlEnum
public enum Currency {

    ARS,
    AUD,
    THB,
    BRL,
    CAD,
    DEM,
    EUR,
    FRF,
    INR,
    TRL,
    FIM,
    MXN,
    NLG,
    NZD,
    PHP,
    GBP,
    ZAR,
    RUR,
    ILS,
    ESP,
    CHF,
    TWD,
    USD,
    CLF,
    KRW,
    JPY,
    CNY;

    public String value() {
        return name();
    }

    public static Currency fromValue(String v) {
        return valueOf(v);
    }

}
