
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SpecimenEntityType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SpecimenEntityType">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ABS"/>
 *     &lt;enumeration value="AMN"/>
 *     &lt;enumeration value="ASP"/>
 *     &lt;enumeration value="BPH"/>
 *     &lt;enumeration value="BIFL"/>
 *     &lt;enumeration value="BLDCO"/>
 *     &lt;enumeration value="BLDA"/>
 *     &lt;enumeration value="BBL"/>
 *     &lt;enumeration value="BLDC"/>
 *     &lt;enumeration value="BPU"/>
 *     &lt;enumeration value="BLDV"/>
 *     &lt;enumeration value="FLU"/>
 *     &lt;enumeration value="BON"/>
 *     &lt;enumeration value="MILK"/>
 *     &lt;enumeration value="BRO"/>
 *     &lt;enumeration value="BRN"/>
 *     &lt;enumeration value="CALC"/>
 *     &lt;enumeration value="STON"/>
 *     &lt;enumeration value="CNL"/>
 *     &lt;enumeration value="CDM"/>
 *     &lt;enumeration value="CTP"/>
 *     &lt;enumeration value="CSF"/>
 *     &lt;enumeration value="CVM"/>
 *     &lt;enumeration value="CVX"/>
 *     &lt;enumeration value="COL"/>
 *     &lt;enumeration value="CNJT"/>
 *     &lt;enumeration value="CRN"/>
 *     &lt;enumeration value="CUR"/>
 *     &lt;enumeration value="CYST"/>
 *     &lt;enumeration value="DIAF"/>
 *     &lt;enumeration value="DOSE"/>
 *     &lt;enumeration value="DRN"/>
 *     &lt;enumeration value="DUFL"/>
 *     &lt;enumeration value="EAR"/>
 *     &lt;enumeration value="EARW"/>
 *     &lt;enumeration value="ELT"/>
 *     &lt;enumeration value="ENDC"/>
 *     &lt;enumeration value="ENDM"/>
 *     &lt;enumeration value="EOS"/>
 *     &lt;enumeration value="RBC"/>
 *     &lt;enumeration value="BRTH"/>
 *     &lt;enumeration value="EXG"/>
 *     &lt;enumeration value="EYE"/>
 *     &lt;enumeration value="FIB"/>
 *     &lt;enumeration value="FLT"/>
 *     &lt;enumeration value="FIST"/>
 *     &lt;enumeration value="FOOD"/>
 *     &lt;enumeration value="GAS"/>
 *     &lt;enumeration value="GAST"/>
 *     &lt;enumeration value="GEN"/>
 *     &lt;enumeration value="GENC"/>
 *     &lt;enumeration value="GENF"/>
 *     &lt;enumeration value="GENL"/>
 *     &lt;enumeration value="GENV"/>
 *     &lt;enumeration value="HAR"/>
 *     &lt;enumeration value="IHG"/>
 *     &lt;enumeration value="IT"/>
 *     &lt;enumeration value="ISLT"/>
 *     &lt;enumeration value="LAM"/>
 *     &lt;enumeration value="WBC"/>
 *     &lt;enumeration value="LN"/>
 *     &lt;enumeration value="LNA"/>
 *     &lt;enumeration value="LNV"/>
 *     &lt;enumeration value="LIQ"/>
 *     &lt;enumeration value="LYM"/>
 *     &lt;enumeration value="MAC"/>
 *     &lt;enumeration value="MAR"/>
 *     &lt;enumeration value="MEC"/>
 *     &lt;enumeration value="MBLD"/>
 *     &lt;enumeration value="MLK"/>
 *     &lt;enumeration value="NAIL"/>
 *     &lt;enumeration value="NOS"/>
 *     &lt;enumeration value="PAFL"/>
 *     &lt;enumeration value="PAT"/>
 *     &lt;enumeration value="PRT"/>
 *     &lt;enumeration value="PLC"/>
 *     &lt;enumeration value="PLAS"/>
 *     &lt;enumeration value="PLB"/>
 *     &lt;enumeration value="PPP"/>
 *     &lt;enumeration value="PRP"/>
 *     &lt;enumeration value="PLR"/>
 *     &lt;enumeration value="PMN"/>
 *     &lt;enumeration value="PUS"/>
 *     &lt;enumeration value="SAL"/>
 *     &lt;enumeration value="SMN"/>
 *     &lt;enumeration value="SMPLS"/>
 *     &lt;enumeration value="SER"/>
 *     &lt;enumeration value="SKM"/>
 *     &lt;enumeration value="SKN"/>
 *     &lt;enumeration value="SPRM"/>
 *     &lt;enumeration value="SPT"/>
 *     &lt;enumeration value="SPTC"/>
 *     &lt;enumeration value="SPTT"/>
 *     &lt;enumeration value="STL"/>
 *     &lt;enumeration value="SWT"/>
 *     &lt;enumeration value="SNV"/>
 *     &lt;enumeration value="TEAR"/>
 *     &lt;enumeration value="THRT"/>
 *     &lt;enumeration value="THRB"/>
 *     &lt;enumeration value="TISG"/>
 *     &lt;enumeration value="TLGI"/>
 *     &lt;enumeration value="TLNG"/>
 *     &lt;enumeration value="TISPL"/>
 *     &lt;enumeration value="TSMI"/>
 *     &lt;enumeration value="TISU"/>
 *     &lt;enumeration value="TISS"/>
 *     &lt;enumeration value="TUB"/>
 *     &lt;enumeration value="ULC"/>
 *     &lt;enumeration value="UMB"/>
 *     &lt;enumeration value="UMED"/>
 *     &lt;enumeration value="USUB"/>
 *     &lt;enumeration value="URTH"/>
 *     &lt;enumeration value="UR"/>
 *     &lt;enumeration value="URT"/>
 *     &lt;enumeration value="URC"/>
 *     &lt;enumeration value="URNS"/>
 *     &lt;enumeration value="VOM"/>
 *     &lt;enumeration value="WAT"/>
 *     &lt;enumeration value="BLD"/>
 *     &lt;enumeration value="BDY"/>
 *     &lt;enumeration value="WICK"/>
 *     &lt;enumeration value="WND"/>
 *     &lt;enumeration value="WNDA"/>
 *     &lt;enumeration value="WNDD"/>
 *     &lt;enumeration value="WNDE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SpecimenEntityType")
@XmlEnum
public enum SpecimenEntityType {

    ABS,
    AMN,
    ASP,
    BPH,
    BIFL,
    BLDCO,
    BLDA,
    BBL,
    BLDC,
    BPU,
    BLDV,
    FLU,
    BON,
    MILK,
    BRO,
    BRN,
    CALC,
    STON,
    CNL,
    CDM,
    CTP,
    CSF,
    CVM,
    CVX,
    COL,
    CNJT,
    CRN,
    CUR,
    CYST,
    DIAF,
    DOSE,
    DRN,
    DUFL,
    EAR,
    EARW,
    ELT,
    ENDC,
    ENDM,
    EOS,
    RBC,
    BRTH,
    EXG,
    EYE,
    FIB,
    FLT,
    FIST,
    FOOD,
    GAS,
    GAST,
    GEN,
    GENC,
    GENF,
    GENL,
    GENV,
    HAR,
    IHG,
    IT,
    ISLT,
    LAM,
    WBC,
    LN,
    LNA,
    LNV,
    LIQ,
    LYM,
    MAC,
    MAR,
    MEC,
    MBLD,
    MLK,
    NAIL,
    NOS,
    PAFL,
    PAT,
    PRT,
    PLC,
    PLAS,
    PLB,
    PPP,
    PRP,
    PLR,
    PMN,
    PUS,
    SAL,
    SMN,
    SMPLS,
    SER,
    SKM,
    SKN,
    SPRM,
    SPT,
    SPTC,
    SPTT,
    STL,
    SWT,
    SNV,
    TEAR,
    THRT,
    THRB,
    TISG,
    TLGI,
    TLNG,
    TISPL,
    TSMI,
    TISU,
    TISS,
    TUB,
    ULC,
    UMB,
    UMED,
    USUB,
    URTH,
    UR,
    URT,
    URC,
    URNS,
    VOM,
    WAT,
    BLD,
    BDY,
    WICK,
    WND,
    WNDA,
    WNDD,
    WNDE;

    public String value() {
        return name();
    }

    public static SpecimenEntityType fromValue(String v) {
        return valueOf(v);
    }

}
