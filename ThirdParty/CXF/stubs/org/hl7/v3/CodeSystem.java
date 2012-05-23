
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CodeSystem.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CodeSystem">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ABCcodes"/>
 *     &lt;enumeration value="AS4E"/>
 *     &lt;enumeration value="AS4"/>
 *     &lt;enumeration value="AcknowledgementDetailType"/>
 *     &lt;enumeration value="AcknowledgementCondition"/>
 *     &lt;enumeration value="AcknowledgementDetailCode"/>
 *     &lt;enumeration value="AcknowledgementType"/>
 *     &lt;enumeration value="ActClass"/>
 *     &lt;enumeration value="ActCode"/>
 *     &lt;enumeration value="ActExposureLevelCode"/>
 *     &lt;enumeration value="ActInvoiceElementModifier"/>
 *     &lt;enumeration value="ActMood"/>
 *     &lt;enumeration value="ActPriority"/>
 *     &lt;enumeration value="ActReason"/>
 *     &lt;enumeration value="ActRelationshipCheckpoint"/>
 *     &lt;enumeration value="ActRelationshipJoin"/>
 *     &lt;enumeration value="ActRelationshipSplit"/>
 *     &lt;enumeration value="ActRelationshipSubset"/>
 *     &lt;enumeration value="ActRelationshipType"/>
 *     &lt;enumeration value="ActSite"/>
 *     &lt;enumeration value="ActStatus"/>
 *     &lt;enumeration value="ActUncertainty"/>
 *     &lt;enumeration value="AddressPartType"/>
 *     &lt;enumeration value="AdministrativeGender"/>
 *     &lt;enumeration value="ACR"/>
 *     &lt;enumeration value="ATC"/>
 *     &lt;enumeration value="AmericanIndianAlaskaNativeLanguages"/>
 *     &lt;enumeration value="CAMNCVS"/>
 *     &lt;enumeration value="CSAID"/>
 *     &lt;enumeration value="CDCA"/>
 *     &lt;enumeration value="CDCM"/>
 *     &lt;enumeration value="CDS"/>
 *     &lt;enumeration value="CVX"/>
 *     &lt;enumeration value="MVX"/>
 *     &lt;enumeration value="CD2"/>
 *     &lt;enumeration value="CE"/>
 *     &lt;enumeration value="CLP"/>
 *     &lt;enumeration value="CST"/>
 *     &lt;enumeration value="C4"/>
 *     &lt;enumeration value="C5"/>
 *     &lt;enumeration value="Calendar"/>
 *     &lt;enumeration value="CalendarCycle"/>
 *     &lt;enumeration value="CalendarType"/>
 *     &lt;enumeration value="CCI"/>
 *     &lt;enumeration value="ICD-10-CA"/>
 *     &lt;enumeration value="Charset"/>
 *     &lt;enumeration value="CAS"/>
 *     &lt;enumeration value="CodeSystem"/>
 *     &lt;enumeration value="CodingRationale"/>
 *     &lt;enumeration value="CommunicationFunctionType"/>
 *     &lt;enumeration value="CompressionAlgorithm"/>
 *     &lt;enumeration value="ConceptGenerality"/>
 *     &lt;enumeration value="Confidentiality"/>
 *     &lt;enumeration value="ContainerCap"/>
 *     &lt;enumeration value="ContainerSeparator"/>
 *     &lt;enumeration value="ContentProcessingMode"/>
 *     &lt;enumeration value="ContextControl"/>
 *     &lt;enumeration value="Currency"/>
 *     &lt;enumeration value="DCL"/>
 *     &lt;enumeration value="DQL"/>
 *     &lt;enumeration value="DCM"/>
 *     &lt;enumeration value="DataType"/>
 *     &lt;enumeration value="Dentition"/>
 *     &lt;enumeration value="DeviceAlertLevel"/>
 *     &lt;enumeration value="DocumentCompletion"/>
 *     &lt;enumeration value="DocumentStorage"/>
 *     &lt;enumeration value="EPSG-GeodeticParameterDataset"/>
 *     &lt;enumeration value="E"/>
 *     &lt;enumeration value="EditStatus"/>
 *     &lt;enumeration value="EducationLevel"/>
 *     &lt;enumeration value="EmployeeJobClass"/>
 *     &lt;enumeration value="EncounterAcuity"/>
 *     &lt;enumeration value="EncounterAccident"/>
 *     &lt;enumeration value="EncounterAdmissionSource"/>
 *     &lt;enumeration value="EncounterReferralSource"/>
 *     &lt;enumeration value="EncounterSpecialCourtesy"/>
 *     &lt;enumeration value="EntityClass"/>
 *     &lt;enumeration value="EntityCode"/>
 *     &lt;enumeration value="EntityDeterminer"/>
 *     &lt;enumeration value="EntityHandling"/>
 *     &lt;enumeration value="EntityNamePartQualifier"/>
 *     &lt;enumeration value="EntityNamePartType"/>
 *     &lt;enumeration value="EntityNameUse"/>
 *     &lt;enumeration value="EntityRisk"/>
 *     &lt;enumeration value="EntityStatus"/>
 *     &lt;enumeration value="ENZC"/>
 *     &lt;enumeration value="EquipmentAlertLevel"/>
 *     &lt;enumeration value="Ethnicity"/>
 *     &lt;enumeration value="E5"/>
 *     &lt;enumeration value="E7"/>
 *     &lt;enumeration value="E6"/>
 *     &lt;enumeration value="ExposureMode"/>
 *     &lt;enumeration value="FDK"/>
 *     &lt;enumeration value="FDDX"/>
 *     &lt;enumeration value="FDDC"/>
 *     &lt;enumeration value="GTSAbbreviation"/>
 *     &lt;enumeration value="GenderStatus"/>
 *     &lt;enumeration value="HPC"/>
 *     &lt;enumeration value="HB"/>
 *     &lt;enumeration value="CodeSystemType"/>
 *     &lt;enumeration value="ConceptStatus"/>
 *     &lt;enumeration value="HL7ITSVersionCode"/>
 *     &lt;enumeration value="ConceptProperty"/>
 *     &lt;enumeration value="HL7CommitteeIDInRIM"/>
 *     &lt;enumeration value="HL7ConformanceInclusion"/>
 *     &lt;enumeration value="HL7DefinedRoseProperty"/>
 *     &lt;enumeration value="HL7StandardVersionCode"/>
 *     &lt;enumeration value="HL7UpdateMode"/>
 *     &lt;enumeration value="HI"/>
 *     &lt;enumeration value="HealthcareProviderTaxonomyHIPAA"/>
 *     &lt;enumeration value="HHC"/>
 *     &lt;enumeration value="HtmlLinkType"/>
 *     &lt;enumeration value="ICS"/>
 *     &lt;enumeration value="I10"/>
 *     &lt;enumeration value="I10P"/>
 *     &lt;enumeration value="I9C"/>
 *     &lt;enumeration value="I9"/>
 *     &lt;enumeration value="IC2"/>
 *     &lt;enumeration value="IETF1766"/>
 *     &lt;enumeration value="IBT"/>
 *     &lt;enumeration value="MDC"/>
 *     &lt;enumeration value="ISO3166-1"/>
 *     &lt;enumeration value="ISO3166-2"/>
 *     &lt;enumeration value="ISO3166-3"/>
 *     &lt;enumeration value="ISO4217"/>
 *     &lt;enumeration value="IUPC"/>
 *     &lt;enumeration value="IUPP"/>
 *     &lt;enumeration value="IntegrityCheckAlgorithm"/>
 *     &lt;enumeration value="ICDO"/>
 *     &lt;enumeration value="ICSD"/>
 *     &lt;enumeration value="JC8"/>
 *     &lt;enumeration value="LanguageAbilityMode"/>
 *     &lt;enumeration value="LanguageAbilityProficiency"/>
 *     &lt;enumeration value="LivingArrangement"/>
 *     &lt;enumeration value="LocalMarkupIgnore"/>
 *     &lt;enumeration value="LocalRemoteControlState"/>
 *     &lt;enumeration value="LN"/>
 *     &lt;enumeration value="MDFAttributeType"/>
 *     &lt;enumeration value="MDFSubjectAreaPrefix"/>
 *     &lt;enumeration value="UMD"/>
 *     &lt;enumeration value="MEDCIN"/>
 *     &lt;enumeration value="MIME"/>
 *     &lt;enumeration value="ManagedParticipationStatus"/>
 *     &lt;enumeration value="MapRelationship"/>
 *     &lt;enumeration value="MaritalStatus"/>
 *     &lt;enumeration value="MaterialType"/>
 *     &lt;enumeration value="MdfHmdMetSourceType"/>
 *     &lt;enumeration value="MdfHmdRowType"/>
 *     &lt;enumeration value="MdfRmimRowType"/>
 *     &lt;enumeration value="MediaType"/>
 *     &lt;enumeration value="MEDR"/>
 *     &lt;enumeration value="MEDX"/>
 *     &lt;enumeration value="MEDC"/>
 *     &lt;enumeration value="MDDX"/>
 *     &lt;enumeration value="MGPI"/>
 *     &lt;enumeration value="MessageWaitingPriority"/>
 *     &lt;enumeration value="MessageCondition"/>
 *     &lt;enumeration value="ModifyIndicator"/>
 *     &lt;enumeration value="MULTUM"/>
 *     &lt;enumeration value="NAACCR"/>
 *     &lt;enumeration value="NDA"/>
 *     &lt;enumeration value="NOC"/>
 *     &lt;enumeration value="NUCCProviderCodes"/>
 *     &lt;enumeration value="NUBC-UB92"/>
 *     &lt;enumeration value="NDC"/>
 *     &lt;enumeration value="NAICS"/>
 *     &lt;enumeration value="NullFlavor"/>
 *     &lt;enumeration value="NIC"/>
 *     &lt;enumeration value="NMMDS"/>
 *     &lt;enumeration value="ObservationInterpretation"/>
 *     &lt;enumeration value="ObservationMethod"/>
 *     &lt;enumeration value="ObservationValue"/>
 *     &lt;enumeration value="OHA"/>
 *     &lt;enumeration value="OrderableDrugForm"/>
 *     &lt;enumeration value="OrganizationNameType"/>
 *     &lt;enumeration value="POS"/>
 *     &lt;enumeration value="ParameterizedDataType"/>
 *     &lt;enumeration value="ParticipationFunction"/>
 *     &lt;enumeration value="ParticipationMode"/>
 *     &lt;enumeration value="ParticipationSignature"/>
 *     &lt;enumeration value="ParticipationType"/>
 *     &lt;enumeration value="PatientImportance"/>
 *     &lt;enumeration value="PaymentTerms"/>
 *     &lt;enumeration value="PeriodicIntervalOfTimeAbbreviation"/>
 *     &lt;enumeration value="PNDS"/>
 *     &lt;enumeration value="PersonDisabilityType"/>
 *     &lt;enumeration value="ConceptCodeRelationship"/>
 *     &lt;enumeration value="PostalAddressUse"/>
 *     &lt;enumeration value="ProbabilityDistributionType"/>
 *     &lt;enumeration value="ProcedureMethod"/>
 *     &lt;enumeration value="ProcessingID"/>
 *     &lt;enumeration value="ProcessingMode"/>
 *     &lt;enumeration value="QueryParameterValue"/>
 *     &lt;enumeration value="QueryPriority"/>
 *     &lt;enumeration value="QueryQuantityUnit"/>
 *     &lt;enumeration value="QueryRequestLimit"/>
 *     &lt;enumeration value="QueryResponse"/>
 *     &lt;enumeration value="QueryStatusCode"/>
 *     &lt;enumeration value="Race"/>
 *     &lt;enumeration value="RC"/>
 *     &lt;enumeration value="RelationalOperator"/>
 *     &lt;enumeration value="RelationshipConjunction"/>
 *     &lt;enumeration value="ReligiousAffiliation"/>
 *     &lt;enumeration value="ResponseLevel"/>
 *     &lt;enumeration value="ResponseModality"/>
 *     &lt;enumeration value="ResponseMode"/>
 *     &lt;enumeration value="RoleClass"/>
 *     &lt;enumeration value="RoleCode"/>
 *     &lt;enumeration value="RoleLinkType"/>
 *     &lt;enumeration value="RoleStatus"/>
 *     &lt;enumeration value="RouteOfAdministration"/>
 *     &lt;enumeration value="SCDHEC-GISSpatialAccuracyTiers"/>
 *     &lt;enumeration value="SNM3"/>
 *     &lt;enumeration value="SNT"/>
 *     &lt;enumeration value="SDM"/>
 *     &lt;enumeration value="Sequencing"/>
 *     &lt;enumeration value="SetOperator"/>
 *     &lt;enumeration value="SpecialArrangement"/>
 *     &lt;enumeration value="SpecimenType"/>
 *     &lt;enumeration value="StyleType"/>
 *     &lt;enumeration value="SubstanceAdminSubstitution"/>
 *     &lt;enumeration value="SubstitutionCondition"/>
 *     &lt;enumeration value="SNM"/>
 *     &lt;enumeration value="TableCellHorizontalAlign"/>
 *     &lt;enumeration value="TableCellScope"/>
 *     &lt;enumeration value="TableCellVerticalAlign"/>
 *     &lt;enumeration value="TableFrame"/>
 *     &lt;enumeration value="TableRules"/>
 *     &lt;enumeration value="IETF3066"/>
 *     &lt;enumeration value="TargetAwareness"/>
 *     &lt;enumeration value="TelecommunicationAddressUse"/>
 *     &lt;enumeration value="RCFB"/>
 *     &lt;enumeration value="RCV2"/>
 *     &lt;enumeration value="TimingEvent"/>
 *     &lt;enumeration value="TransmissionRelationshipTypeCode"/>
 *     &lt;enumeration value="TribalEntityUS"/>
 *     &lt;enumeration value="UC"/>
 *     &lt;enumeration value="UCUM"/>
 *     &lt;enumeration value="URLScheme"/>
 *     &lt;enumeration value="UML"/>
 *     &lt;enumeration value="UnitsOfMeasure"/>
 *     &lt;enumeration value="UPC"/>
 *     &lt;enumeration value="VaccineManufacturer"/>
 *     &lt;enumeration value="VaccineType"/>
 *     &lt;enumeration value="VocabularyDomainQualifier"/>
 *     &lt;enumeration value="WC"/>
 *     &lt;enumeration value="ART"/>
 *     &lt;enumeration value="W4"/>
 *     &lt;enumeration value="W1-W2"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CodeSystem")
@XmlEnum
public enum CodeSystem {

    @XmlEnumValue("ABCcodes")
    AB_CCODES("ABCcodes"),
    @XmlEnumValue("AS4E")
    AS_4_E("AS4E"),
    @XmlEnumValue("AS4")
    AS_4("AS4"),
    @XmlEnumValue("AcknowledgementDetailType")
    ACKNOWLEDGEMENT_DETAIL_TYPE("AcknowledgementDetailType"),
    @XmlEnumValue("AcknowledgementCondition")
    ACKNOWLEDGEMENT_CONDITION("AcknowledgementCondition"),
    @XmlEnumValue("AcknowledgementDetailCode")
    ACKNOWLEDGEMENT_DETAIL_CODE("AcknowledgementDetailCode"),
    @XmlEnumValue("AcknowledgementType")
    ACKNOWLEDGEMENT_TYPE("AcknowledgementType"),
    @XmlEnumValue("ActClass")
    ACT_CLASS("ActClass"),
    @XmlEnumValue("ActCode")
    ACT_CODE("ActCode"),
    @XmlEnumValue("ActExposureLevelCode")
    ACT_EXPOSURE_LEVEL_CODE("ActExposureLevelCode"),
    @XmlEnumValue("ActInvoiceElementModifier")
    ACT_INVOICE_ELEMENT_MODIFIER("ActInvoiceElementModifier"),
    @XmlEnumValue("ActMood")
    ACT_MOOD("ActMood"),
    @XmlEnumValue("ActPriority")
    ACT_PRIORITY("ActPriority"),
    @XmlEnumValue("ActReason")
    ACT_REASON("ActReason"),
    @XmlEnumValue("ActRelationshipCheckpoint")
    ACT_RELATIONSHIP_CHECKPOINT("ActRelationshipCheckpoint"),
    @XmlEnumValue("ActRelationshipJoin")
    ACT_RELATIONSHIP_JOIN("ActRelationshipJoin"),
    @XmlEnumValue("ActRelationshipSplit")
    ACT_RELATIONSHIP_SPLIT("ActRelationshipSplit"),
    @XmlEnumValue("ActRelationshipSubset")
    ACT_RELATIONSHIP_SUBSET("ActRelationshipSubset"),
    @XmlEnumValue("ActRelationshipType")
    ACT_RELATIONSHIP_TYPE("ActRelationshipType"),
    @XmlEnumValue("ActSite")
    ACT_SITE("ActSite"),
    @XmlEnumValue("ActStatus")
    ACT_STATUS("ActStatus"),
    @XmlEnumValue("ActUncertainty")
    ACT_UNCERTAINTY("ActUncertainty"),
    @XmlEnumValue("AddressPartType")
    ADDRESS_PART_TYPE("AddressPartType"),
    @XmlEnumValue("AdministrativeGender")
    ADMINISTRATIVE_GENDER("AdministrativeGender"),
    ACR("ACR"),
    ATC("ATC"),
    @XmlEnumValue("AmericanIndianAlaskaNativeLanguages")
    AMERICAN_INDIAN_ALASKA_NATIVE_LANGUAGES("AmericanIndianAlaskaNativeLanguages"),
    CAMNCVS("CAMNCVS"),
    CSAID("CSAID"),
    CDCA("CDCA"),
    CDCM("CDCM"),
    CDS("CDS"),
    CVX("CVX"),
    MVX("MVX"),
    @XmlEnumValue("CD2")
    CD_2("CD2"),
    CE("CE"),
    CLP("CLP"),
    CST("CST"),
    @XmlEnumValue("C4")
    C_4("C4"),
    @XmlEnumValue("C5")
    C_5("C5"),
    @XmlEnumValue("Calendar")
    CALENDAR("Calendar"),
    @XmlEnumValue("CalendarCycle")
    CALENDAR_CYCLE("CalendarCycle"),
    @XmlEnumValue("CalendarType")
    CALENDAR_TYPE("CalendarType"),
    CCI("CCI"),
    @XmlEnumValue("ICD-10-CA")
    ICD_10_CA("ICD-10-CA"),
    @XmlEnumValue("Charset")
    CHARSET("Charset"),
    CAS("CAS"),
    @XmlEnumValue("CodeSystem")
    CODE_SYSTEM("CodeSystem"),
    @XmlEnumValue("CodingRationale")
    CODING_RATIONALE("CodingRationale"),
    @XmlEnumValue("CommunicationFunctionType")
    COMMUNICATION_FUNCTION_TYPE("CommunicationFunctionType"),
    @XmlEnumValue("CompressionAlgorithm")
    COMPRESSION_ALGORITHM("CompressionAlgorithm"),
    @XmlEnumValue("ConceptGenerality")
    CONCEPT_GENERALITY("ConceptGenerality"),
    @XmlEnumValue("Confidentiality")
    CONFIDENTIALITY("Confidentiality"),
    @XmlEnumValue("ContainerCap")
    CONTAINER_CAP("ContainerCap"),
    @XmlEnumValue("ContainerSeparator")
    CONTAINER_SEPARATOR("ContainerSeparator"),
    @XmlEnumValue("ContentProcessingMode")
    CONTENT_PROCESSING_MODE("ContentProcessingMode"),
    @XmlEnumValue("ContextControl")
    CONTEXT_CONTROL("ContextControl"),
    @XmlEnumValue("Currency")
    CURRENCY("Currency"),
    DCL("DCL"),
    DQL("DQL"),
    DCM("DCM"),
    @XmlEnumValue("DataType")
    DATA_TYPE("DataType"),
    @XmlEnumValue("Dentition")
    DENTITION("Dentition"),
    @XmlEnumValue("DeviceAlertLevel")
    DEVICE_ALERT_LEVEL("DeviceAlertLevel"),
    @XmlEnumValue("DocumentCompletion")
    DOCUMENT_COMPLETION("DocumentCompletion"),
    @XmlEnumValue("DocumentStorage")
    DOCUMENT_STORAGE("DocumentStorage"),
    @XmlEnumValue("EPSG-GeodeticParameterDataset")
    EPSG_GEODETIC_PARAMETER_DATASET("EPSG-GeodeticParameterDataset"),
    E("E"),
    @XmlEnumValue("EditStatus")
    EDIT_STATUS("EditStatus"),
    @XmlEnumValue("EducationLevel")
    EDUCATION_LEVEL("EducationLevel"),
    @XmlEnumValue("EmployeeJobClass")
    EMPLOYEE_JOB_CLASS("EmployeeJobClass"),
    @XmlEnumValue("EncounterAcuity")
    ENCOUNTER_ACUITY("EncounterAcuity"),
    @XmlEnumValue("EncounterAccident")
    ENCOUNTER_ACCIDENT("EncounterAccident"),
    @XmlEnumValue("EncounterAdmissionSource")
    ENCOUNTER_ADMISSION_SOURCE("EncounterAdmissionSource"),
    @XmlEnumValue("EncounterReferralSource")
    ENCOUNTER_REFERRAL_SOURCE("EncounterReferralSource"),
    @XmlEnumValue("EncounterSpecialCourtesy")
    ENCOUNTER_SPECIAL_COURTESY("EncounterSpecialCourtesy"),
    @XmlEnumValue("EntityClass")
    ENTITY_CLASS("EntityClass"),
    @XmlEnumValue("EntityCode")
    ENTITY_CODE("EntityCode"),
    @XmlEnumValue("EntityDeterminer")
    ENTITY_DETERMINER("EntityDeterminer"),
    @XmlEnumValue("EntityHandling")
    ENTITY_HANDLING("EntityHandling"),
    @XmlEnumValue("EntityNamePartQualifier")
    ENTITY_NAME_PART_QUALIFIER("EntityNamePartQualifier"),
    @XmlEnumValue("EntityNamePartType")
    ENTITY_NAME_PART_TYPE("EntityNamePartType"),
    @XmlEnumValue("EntityNameUse")
    ENTITY_NAME_USE("EntityNameUse"),
    @XmlEnumValue("EntityRisk")
    ENTITY_RISK("EntityRisk"),
    @XmlEnumValue("EntityStatus")
    ENTITY_STATUS("EntityStatus"),
    ENZC("ENZC"),
    @XmlEnumValue("EquipmentAlertLevel")
    EQUIPMENT_ALERT_LEVEL("EquipmentAlertLevel"),
    @XmlEnumValue("Ethnicity")
    ETHNICITY("Ethnicity"),
    @XmlEnumValue("E5")
    E_5("E5"),
    @XmlEnumValue("E7")
    E_7("E7"),
    @XmlEnumValue("E6")
    E_6("E6"),
    @XmlEnumValue("ExposureMode")
    EXPOSURE_MODE("ExposureMode"),
    FDK("FDK"),
    FDDX("FDDX"),
    FDDC("FDDC"),
    @XmlEnumValue("GTSAbbreviation")
    GTS_ABBREVIATION("GTSAbbreviation"),
    @XmlEnumValue("GenderStatus")
    GENDER_STATUS("GenderStatus"),
    HPC("HPC"),
    HB("HB"),
    @XmlEnumValue("CodeSystemType")
    CODE_SYSTEM_TYPE("CodeSystemType"),
    @XmlEnumValue("ConceptStatus")
    CONCEPT_STATUS("ConceptStatus"),
    @XmlEnumValue("HL7ITSVersionCode")
    HL_7_ITS_VERSION_CODE("HL7ITSVersionCode"),
    @XmlEnumValue("ConceptProperty")
    CONCEPT_PROPERTY("ConceptProperty"),
    @XmlEnumValue("HL7CommitteeIDInRIM")
    HL_7_COMMITTEE_ID_IN_RIM("HL7CommitteeIDInRIM"),
    @XmlEnumValue("HL7ConformanceInclusion")
    HL_7_CONFORMANCE_INCLUSION("HL7ConformanceInclusion"),
    @XmlEnumValue("HL7DefinedRoseProperty")
    HL_7_DEFINED_ROSE_PROPERTY("HL7DefinedRoseProperty"),
    @XmlEnumValue("HL7StandardVersionCode")
    HL_7_STANDARD_VERSION_CODE("HL7StandardVersionCode"),
    @XmlEnumValue("HL7UpdateMode")
    HL_7_UPDATE_MODE("HL7UpdateMode"),
    HI("HI"),
    @XmlEnumValue("HealthcareProviderTaxonomyHIPAA")
    HEALTHCARE_PROVIDER_TAXONOMY_HIPAA("HealthcareProviderTaxonomyHIPAA"),
    HHC("HHC"),
    @XmlEnumValue("HtmlLinkType")
    HTML_LINK_TYPE("HtmlLinkType"),
    ICS("ICS"),
    @XmlEnumValue("I10")
    I_10("I10"),
    @XmlEnumValue("I10P")
    I_10_P("I10P"),
    @XmlEnumValue("I9C")
    I_9_C("I9C"),
    @XmlEnumValue("I9")
    I_9("I9"),
    @XmlEnumValue("IC2")
    IC_2("IC2"),
    @XmlEnumValue("IETF1766")
    IETF_1766("IETF1766"),
    IBT("IBT"),
    MDC("MDC"),
    @XmlEnumValue("ISO3166-1")
    ISO_3166_1("ISO3166-1"),
    @XmlEnumValue("ISO3166-2")
    ISO_3166_2("ISO3166-2"),
    @XmlEnumValue("ISO3166-3")
    ISO_3166_3("ISO3166-3"),
    @XmlEnumValue("ISO4217")
    ISO_4217("ISO4217"),
    IUPC("IUPC"),
    IUPP("IUPP"),
    @XmlEnumValue("IntegrityCheckAlgorithm")
    INTEGRITY_CHECK_ALGORITHM("IntegrityCheckAlgorithm"),
    ICDO("ICDO"),
    ICSD("ICSD"),
    @XmlEnumValue("JC8")
    JC_8("JC8"),
    @XmlEnumValue("LanguageAbilityMode")
    LANGUAGE_ABILITY_MODE("LanguageAbilityMode"),
    @XmlEnumValue("LanguageAbilityProficiency")
    LANGUAGE_ABILITY_PROFICIENCY("LanguageAbilityProficiency"),
    @XmlEnumValue("LivingArrangement")
    LIVING_ARRANGEMENT("LivingArrangement"),
    @XmlEnumValue("LocalMarkupIgnore")
    LOCAL_MARKUP_IGNORE("LocalMarkupIgnore"),
    @XmlEnumValue("LocalRemoteControlState")
    LOCAL_REMOTE_CONTROL_STATE("LocalRemoteControlState"),
    LN("LN"),
    @XmlEnumValue("MDFAttributeType")
    MDF_ATTRIBUTE_TYPE("MDFAttributeType"),
    @XmlEnumValue("MDFSubjectAreaPrefix")
    MDF_SUBJECT_AREA_PREFIX("MDFSubjectAreaPrefix"),
    UMD("UMD"),
    MEDCIN("MEDCIN"),
    MIME("MIME"),
    @XmlEnumValue("ManagedParticipationStatus")
    MANAGED_PARTICIPATION_STATUS("ManagedParticipationStatus"),
    @XmlEnumValue("MapRelationship")
    MAP_RELATIONSHIP("MapRelationship"),
    @XmlEnumValue("MaritalStatus")
    MARITAL_STATUS("MaritalStatus"),
    @XmlEnumValue("MaterialType")
    MATERIAL_TYPE("MaterialType"),
    @XmlEnumValue("MdfHmdMetSourceType")
    MDF_HMD_MET_SOURCE_TYPE("MdfHmdMetSourceType"),
    @XmlEnumValue("MdfHmdRowType")
    MDF_HMD_ROW_TYPE("MdfHmdRowType"),
    @XmlEnumValue("MdfRmimRowType")
    MDF_RMIM_ROW_TYPE("MdfRmimRowType"),
    @XmlEnumValue("MediaType")
    MEDIA_TYPE("MediaType"),
    MEDR("MEDR"),
    MEDX("MEDX"),
    MEDC("MEDC"),
    MDDX("MDDX"),
    MGPI("MGPI"),
    @XmlEnumValue("MessageWaitingPriority")
    MESSAGE_WAITING_PRIORITY("MessageWaitingPriority"),
    @XmlEnumValue("MessageCondition")
    MESSAGE_CONDITION("MessageCondition"),
    @XmlEnumValue("ModifyIndicator")
    MODIFY_INDICATOR("ModifyIndicator"),
    MULTUM("MULTUM"),
    NAACCR("NAACCR"),
    NDA("NDA"),
    NOC("NOC"),
    @XmlEnumValue("NUCCProviderCodes")
    NUCC_PROVIDER_CODES("NUCCProviderCodes"),
    @XmlEnumValue("NUBC-UB92")
    NUBC_UB_92("NUBC-UB92"),
    NDC("NDC"),
    NAICS("NAICS"),
    @XmlEnumValue("NullFlavor")
    NULL_FLAVOR("NullFlavor"),
    NIC("NIC"),
    NMMDS("NMMDS"),
    @XmlEnumValue("ObservationInterpretation")
    OBSERVATION_INTERPRETATION("ObservationInterpretation"),
    @XmlEnumValue("ObservationMethod")
    OBSERVATION_METHOD("ObservationMethod"),
    @XmlEnumValue("ObservationValue")
    OBSERVATION_VALUE("ObservationValue"),
    OHA("OHA"),
    @XmlEnumValue("OrderableDrugForm")
    ORDERABLE_DRUG_FORM("OrderableDrugForm"),
    @XmlEnumValue("OrganizationNameType")
    ORGANIZATION_NAME_TYPE("OrganizationNameType"),
    POS("POS"),
    @XmlEnumValue("ParameterizedDataType")
    PARAMETERIZED_DATA_TYPE("ParameterizedDataType"),
    @XmlEnumValue("ParticipationFunction")
    PARTICIPATION_FUNCTION("ParticipationFunction"),
    @XmlEnumValue("ParticipationMode")
    PARTICIPATION_MODE("ParticipationMode"),
    @XmlEnumValue("ParticipationSignature")
    PARTICIPATION_SIGNATURE("ParticipationSignature"),
    @XmlEnumValue("ParticipationType")
    PARTICIPATION_TYPE("ParticipationType"),
    @XmlEnumValue("PatientImportance")
    PATIENT_IMPORTANCE("PatientImportance"),
    @XmlEnumValue("PaymentTerms")
    PAYMENT_TERMS("PaymentTerms"),
    @XmlEnumValue("PeriodicIntervalOfTimeAbbreviation")
    PERIODIC_INTERVAL_OF_TIME_ABBREVIATION("PeriodicIntervalOfTimeAbbreviation"),
    PNDS("PNDS"),
    @XmlEnumValue("PersonDisabilityType")
    PERSON_DISABILITY_TYPE("PersonDisabilityType"),
    @XmlEnumValue("ConceptCodeRelationship")
    CONCEPT_CODE_RELATIONSHIP("ConceptCodeRelationship"),
    @XmlEnumValue("PostalAddressUse")
    POSTAL_ADDRESS_USE("PostalAddressUse"),
    @XmlEnumValue("ProbabilityDistributionType")
    PROBABILITY_DISTRIBUTION_TYPE("ProbabilityDistributionType"),
    @XmlEnumValue("ProcedureMethod")
    PROCEDURE_METHOD("ProcedureMethod"),
    @XmlEnumValue("ProcessingID")
    PROCESSING_ID("ProcessingID"),
    @XmlEnumValue("ProcessingMode")
    PROCESSING_MODE("ProcessingMode"),
    @XmlEnumValue("QueryParameterValue")
    QUERY_PARAMETER_VALUE("QueryParameterValue"),
    @XmlEnumValue("QueryPriority")
    QUERY_PRIORITY("QueryPriority"),
    @XmlEnumValue("QueryQuantityUnit")
    QUERY_QUANTITY_UNIT("QueryQuantityUnit"),
    @XmlEnumValue("QueryRequestLimit")
    QUERY_REQUEST_LIMIT("QueryRequestLimit"),
    @XmlEnumValue("QueryResponse")
    QUERY_RESPONSE("QueryResponse"),
    @XmlEnumValue("QueryStatusCode")
    QUERY_STATUS_CODE("QueryStatusCode"),
    @XmlEnumValue("Race")
    RACE("Race"),
    RC("RC"),
    @XmlEnumValue("RelationalOperator")
    RELATIONAL_OPERATOR("RelationalOperator"),
    @XmlEnumValue("RelationshipConjunction")
    RELATIONSHIP_CONJUNCTION("RelationshipConjunction"),
    @XmlEnumValue("ReligiousAffiliation")
    RELIGIOUS_AFFILIATION("ReligiousAffiliation"),
    @XmlEnumValue("ResponseLevel")
    RESPONSE_LEVEL("ResponseLevel"),
    @XmlEnumValue("ResponseModality")
    RESPONSE_MODALITY("ResponseModality"),
    @XmlEnumValue("ResponseMode")
    RESPONSE_MODE("ResponseMode"),
    @XmlEnumValue("RoleClass")
    ROLE_CLASS("RoleClass"),
    @XmlEnumValue("RoleCode")
    ROLE_CODE("RoleCode"),
    @XmlEnumValue("RoleLinkType")
    ROLE_LINK_TYPE("RoleLinkType"),
    @XmlEnumValue("RoleStatus")
    ROLE_STATUS("RoleStatus"),
    @XmlEnumValue("RouteOfAdministration")
    ROUTE_OF_ADMINISTRATION("RouteOfAdministration"),
    @XmlEnumValue("SCDHEC-GISSpatialAccuracyTiers")
    SCDHEC_GIS_SPATIAL_ACCURACY_TIERS("SCDHEC-GISSpatialAccuracyTiers"),
    @XmlEnumValue("SNM3")
    SNM_3("SNM3"),
    SNT("SNT"),
    SDM("SDM"),
    @XmlEnumValue("Sequencing")
    SEQUENCING("Sequencing"),
    @XmlEnumValue("SetOperator")
    SET_OPERATOR("SetOperator"),
    @XmlEnumValue("SpecialArrangement")
    SPECIAL_ARRANGEMENT("SpecialArrangement"),
    @XmlEnumValue("SpecimenType")
    SPECIMEN_TYPE("SpecimenType"),
    @XmlEnumValue("StyleType")
    STYLE_TYPE("StyleType"),
    @XmlEnumValue("SubstanceAdminSubstitution")
    SUBSTANCE_ADMIN_SUBSTITUTION("SubstanceAdminSubstitution"),
    @XmlEnumValue("SubstitutionCondition")
    SUBSTITUTION_CONDITION("SubstitutionCondition"),
    SNM("SNM"),
    @XmlEnumValue("TableCellHorizontalAlign")
    TABLE_CELL_HORIZONTAL_ALIGN("TableCellHorizontalAlign"),
    @XmlEnumValue("TableCellScope")
    TABLE_CELL_SCOPE("TableCellScope"),
    @XmlEnumValue("TableCellVerticalAlign")
    TABLE_CELL_VERTICAL_ALIGN("TableCellVerticalAlign"),
    @XmlEnumValue("TableFrame")
    TABLE_FRAME("TableFrame"),
    @XmlEnumValue("TableRules")
    TABLE_RULES("TableRules"),
    @XmlEnumValue("IETF3066")
    IETF_3066("IETF3066"),
    @XmlEnumValue("TargetAwareness")
    TARGET_AWARENESS("TargetAwareness"),
    @XmlEnumValue("TelecommunicationAddressUse")
    TELECOMMUNICATION_ADDRESS_USE("TelecommunicationAddressUse"),
    RCFB("RCFB"),
    @XmlEnumValue("RCV2")
    RCV_2("RCV2"),
    @XmlEnumValue("TimingEvent")
    TIMING_EVENT("TimingEvent"),
    @XmlEnumValue("TransmissionRelationshipTypeCode")
    TRANSMISSION_RELATIONSHIP_TYPE_CODE("TransmissionRelationshipTypeCode"),
    @XmlEnumValue("TribalEntityUS")
    TRIBAL_ENTITY_US("TribalEntityUS"),
    UC("UC"),
    UCUM("UCUM"),
    @XmlEnumValue("URLScheme")
    URL_SCHEME("URLScheme"),
    UML("UML"),
    @XmlEnumValue("UnitsOfMeasure")
    UNITS_OF_MEASURE("UnitsOfMeasure"),
    UPC("UPC"),
    @XmlEnumValue("VaccineManufacturer")
    VACCINE_MANUFACTURER("VaccineManufacturer"),
    @XmlEnumValue("VaccineType")
    VACCINE_TYPE("VaccineType"),
    @XmlEnumValue("VocabularyDomainQualifier")
    VOCABULARY_DOMAIN_QUALIFIER("VocabularyDomainQualifier"),
    WC("WC"),
    ART("ART"),
    @XmlEnumValue("W4")
    W_4("W4"),
    @XmlEnumValue("W1-W2")
    W_1_W_2("W1-W2");
    private final String value;

    CodeSystem(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CodeSystem fromValue(String v) {
        for (CodeSystem c: CodeSystem.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
