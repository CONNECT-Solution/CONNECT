
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HL7DefinedRoseProperty.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="HL7DefinedRoseProperty">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ApplicationRoleI"/>
 *     &lt;enumeration value="Cardinality"/>
 *     &lt;enumeration value="MayRepeat"/>
 *     &lt;enumeration value="InstancedDTsymbo"/>
 *     &lt;enumeration value="DTsymbol"/>
 *     &lt;enumeration value="DevelopingCommit"/>
 *     &lt;enumeration value="Organization"/>
 *     &lt;enumeration value="EndState"/>
 *     &lt;enumeration value="HMD"/>
 *     &lt;enumeration value="zhxID"/>
 *     &lt;enumeration value="ID"/>
 *     &lt;enumeration value="DeleteFromMIM"/>
 *     &lt;enumeration value="MIM_id"/>
 *     &lt;enumeration value="MandatoryInclusi"/>
 *     &lt;enumeration value="MsgID"/>
 *     &lt;enumeration value="ModelDate"/>
 *     &lt;enumeration value="ModelDescription"/>
 *     &lt;enumeration value="ModelID"/>
 *     &lt;enumeration value="ModelName"/>
 *     &lt;enumeration value="ModelVersion"/>
 *     &lt;enumeration value="IsPrimitiveDT"/>
 *     &lt;enumeration value="RcvResp"/>
 *     &lt;enumeration value="IsReferenceDT"/>
 *     &lt;enumeration value="RespComm_id"/>
 *     &lt;enumeration value="StartState"/>
 *     &lt;enumeration value="StateAttribute"/>
 *     &lt;enumeration value="StateTransition"/>
 *     &lt;enumeration value="IsSubjectClass"/>
 *     &lt;enumeration value="V23_Fields"/>
 *     &lt;enumeration value="V23_Datatype"/>
 *     &lt;enumeration value="Vocab_domain"/>
 *     &lt;enumeration value="Vocab_strength"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "HL7DefinedRoseProperty")
@XmlEnum
public enum HL7DefinedRoseProperty {

    @XmlEnumValue("ApplicationRoleI")
    APPLICATION_ROLE_I("ApplicationRoleI"),
    @XmlEnumValue("Cardinality")
    CARDINALITY("Cardinality"),
    @XmlEnumValue("MayRepeat")
    MAY_REPEAT("MayRepeat"),
    @XmlEnumValue("InstancedDTsymbo")
    INSTANCED_D_TSYMBO("InstancedDTsymbo"),
    @XmlEnumValue("DTsymbol")
    D_TSYMBOL("DTsymbol"),
    @XmlEnumValue("DevelopingCommit")
    DEVELOPING_COMMIT("DevelopingCommit"),
    @XmlEnumValue("Organization")
    ORGANIZATION("Organization"),
    @XmlEnumValue("EndState")
    END_STATE("EndState"),
    HMD("HMD"),
    @XmlEnumValue("zhxID")
    ZHX_ID("zhxID"),
    ID("ID"),
    @XmlEnumValue("DeleteFromMIM")
    DELETE_FROM_MIM("DeleteFromMIM"),
    @XmlEnumValue("MIM_id")
    MIM_ID("MIM_id"),
    @XmlEnumValue("MandatoryInclusi")
    MANDATORY_INCLUSI("MandatoryInclusi"),
    @XmlEnumValue("MsgID")
    MSG_ID("MsgID"),
    @XmlEnumValue("ModelDate")
    MODEL_DATE("ModelDate"),
    @XmlEnumValue("ModelDescription")
    MODEL_DESCRIPTION("ModelDescription"),
    @XmlEnumValue("ModelID")
    MODEL_ID("ModelID"),
    @XmlEnumValue("ModelName")
    MODEL_NAME("ModelName"),
    @XmlEnumValue("ModelVersion")
    MODEL_VERSION("ModelVersion"),
    @XmlEnumValue("IsPrimitiveDT")
    IS_PRIMITIVE_DT("IsPrimitiveDT"),
    @XmlEnumValue("RcvResp")
    RCV_RESP("RcvResp"),
    @XmlEnumValue("IsReferenceDT")
    IS_REFERENCE_DT("IsReferenceDT"),
    @XmlEnumValue("RespComm_id")
    RESP_COMM_ID("RespComm_id"),
    @XmlEnumValue("StartState")
    START_STATE("StartState"),
    @XmlEnumValue("StateAttribute")
    STATE_ATTRIBUTE("StateAttribute"),
    @XmlEnumValue("StateTransition")
    STATE_TRANSITION("StateTransition"),
    @XmlEnumValue("IsSubjectClass")
    IS_SUBJECT_CLASS("IsSubjectClass"),
    @XmlEnumValue("V23_Fields")
    V_23_FIELDS("V23_Fields"),
    @XmlEnumValue("V23_Datatype")
    V_23_DATATYPE("V23_Datatype"),
    @XmlEnumValue("Vocab_domain")
    VOCAB_DOMAIN("Vocab_domain"),
    @XmlEnumValue("Vocab_strength")
    VOCAB_STRENGTH("Vocab_strength");
    private final String value;

    HL7DefinedRoseProperty(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static HL7DefinedRoseProperty fromValue(String v) {
        for (HL7DefinedRoseProperty c: HL7DefinedRoseProperty.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
