
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ObservationAssetValue.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ObservationAssetValue">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ASSET"/>
 *     &lt;enumeration value="ANNUITY"/>
 *     &lt;enumeration value="PROP"/>
 *     &lt;enumeration value="RETACCT"/>
 *     &lt;enumeration value="TRUST"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ObservationAssetValue")
@XmlEnum
public enum ObservationAssetValue {

    ASSET,
    ANNUITY,
    PROP,
    RETACCT,
    TRUST;

    public String value() {
        return name();
    }

    public static ObservationAssetValue fromValue(String v) {
        return valueOf(v);
    }

}
