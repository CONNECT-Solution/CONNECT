
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActCredentialedCareProvisionProgramCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActCredentialedCareProvisionProgramCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="AAMC"/>
 *     &lt;enumeration value="AALC"/>
 *     &lt;enumeration value="ABHC"/>
 *     &lt;enumeration value="ACAC"/>
 *     &lt;enumeration value="AHOC"/>
 *     &lt;enumeration value="ACHC"/>
 *     &lt;enumeration value="ALTC"/>
 *     &lt;enumeration value="AOSC"/>
 *     &lt;enumeration value="CACS"/>
 *     &lt;enumeration value="CAMI"/>
 *     &lt;enumeration value="CAST"/>
 *     &lt;enumeration value="CBAR"/>
 *     &lt;enumeration value="CCAR"/>
 *     &lt;enumeration value="COPD"/>
 *     &lt;enumeration value="CCAD"/>
 *     &lt;enumeration value="CDEP"/>
 *     &lt;enumeration value="CDIA"/>
 *     &lt;enumeration value="CDGD"/>
 *     &lt;enumeration value="CEPI"/>
 *     &lt;enumeration value="CFEL"/>
 *     &lt;enumeration value="CHFC"/>
 *     &lt;enumeration value="CHRO"/>
 *     &lt;enumeration value="CHYP"/>
 *     &lt;enumeration value="CMIH"/>
 *     &lt;enumeration value="CMSC"/>
 *     &lt;enumeration value="CONC"/>
 *     &lt;enumeration value="CORT"/>
 *     &lt;enumeration value="COJR"/>
 *     &lt;enumeration value="CPAD"/>
 *     &lt;enumeration value="CPND"/>
 *     &lt;enumeration value="CPST"/>
 *     &lt;enumeration value="CSIC"/>
 *     &lt;enumeration value="CSLD"/>
 *     &lt;enumeration value="CSPT"/>
 *     &lt;enumeration value="CSDM"/>
 *     &lt;enumeration value="CTBU"/>
 *     &lt;enumeration value="CVDC"/>
 *     &lt;enumeration value="CWOH"/>
 *     &lt;enumeration value="CWMA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActCredentialedCareProvisionProgramCode")
@XmlEnum
public enum ActCredentialedCareProvisionProgramCode {

    AAMC,
    AALC,
    ABHC,
    ACAC,
    AHOC,
    ACHC,
    ALTC,
    AOSC,
    CACS,
    CAMI,
    CAST,
    CBAR,
    CCAR,
    COPD,
    CCAD,
    CDEP,
    CDIA,
    CDGD,
    CEPI,
    CFEL,
    CHFC,
    CHRO,
    CHYP,
    CMIH,
    CMSC,
    CONC,
    CORT,
    COJR,
    CPAD,
    CPND,
    CPST,
    CSIC,
    CSLD,
    CSPT,
    CSDM,
    CTBU,
    CVDC,
    CWOH,
    CWMA;

    public String value() {
        return name();
    }

    public static ActCredentialedCareProvisionProgramCode fromValue(String v) {
        return valueOf(v);
    }

}
