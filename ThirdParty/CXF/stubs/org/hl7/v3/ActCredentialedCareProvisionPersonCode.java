
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActCredentialedCareProvisionPersonCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActCredentialedCareProvisionPersonCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="CPGC"/>
 *     &lt;enumeration value="CAMC"/>
 *     &lt;enumeration value="CAIC"/>
 *     &lt;enumeration value="CACC"/>
 *     &lt;enumeration value="CAPC"/>
 *     &lt;enumeration value="CANC"/>
 *     &lt;enumeration value="CBGC"/>
 *     &lt;enumeration value="CCCC"/>
 *     &lt;enumeration value="CCGC"/>
 *     &lt;enumeration value="CMGC"/>
 *     &lt;enumeration value="CCPC"/>
 *     &lt;enumeration value="CCSC"/>
 *     &lt;enumeration value="CDEC"/>
 *     &lt;enumeration value="CDRC"/>
 *     &lt;enumeration value="CEMC"/>
 *     &lt;enumeration value="CFPC"/>
 *     &lt;enumeration value="CIMC"/>
 *     &lt;enumeration value="CNSC"/>
 *     &lt;enumeration value="CNEC"/>
 *     &lt;enumeration value="CNQC"/>
 *     &lt;enumeration value="CNMC"/>
 *     &lt;enumeration value="COGC"/>
 *     &lt;enumeration value="COMC"/>
 *     &lt;enumeration value="COPC"/>
 *     &lt;enumeration value="COSC"/>
 *     &lt;enumeration value="COTC"/>
 *     &lt;enumeration value="CPEC"/>
 *     &lt;enumeration value="CPRC"/>
 *     &lt;enumeration value="CPSC"/>
 *     &lt;enumeration value="CPYC"/>
 *     &lt;enumeration value="CPHC"/>
 *     &lt;enumeration value="CROC"/>
 *     &lt;enumeration value="CRPC"/>
 *     &lt;enumeration value="CSUC"/>
 *     &lt;enumeration value="CTSC"/>
 *     &lt;enumeration value="CURC"/>
 *     &lt;enumeration value="CVSC"/>
 *     &lt;enumeration value="LGPC"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActCredentialedCareProvisionPersonCode")
@XmlEnum
public enum ActCredentialedCareProvisionPersonCode {

    CPGC,
    CAMC,
    CAIC,
    CACC,
    CAPC,
    CANC,
    CBGC,
    CCCC,
    CCGC,
    CMGC,
    CCPC,
    CCSC,
    CDEC,
    CDRC,
    CEMC,
    CFPC,
    CIMC,
    CNSC,
    CNEC,
    CNQC,
    CNMC,
    COGC,
    COMC,
    COPC,
    COSC,
    COTC,
    CPEC,
    CPRC,
    CPSC,
    CPYC,
    CPHC,
    CROC,
    CRPC,
    CSUC,
    CTSC,
    CURC,
    CVSC,
    LGPC;

    public String value() {
        return name();
    }

    public static ActCredentialedCareProvisionPersonCode fromValue(String v) {
        return valueOf(v);
    }

}
