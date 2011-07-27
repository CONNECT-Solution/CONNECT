/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.redactionengine.adapter;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.policyengine.adapter.pip.CDAConstants;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class DocQueryResponseProcessor
{
    private static final String EBXML_DOCENTRY_PATIENT_ID = "$XDSDocumentEntryPatientId";
    private static final String EBXML_RESPONSE_TYPECODE_CLASS_SCHEME = "urn:uuid:f0306f51-975f-434e-a61c-c59651d33983";
    private Log log = null;
    private String patientId;
    private String assigningAuthorityId;
    private String homeCommunityId;

    public DocQueryResponseProcessor()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    protected PatientConsentHelper getPatientConsentHelper()
    {
        return new PatientConsentHelper();
    }
    
    protected void setPatientId(String patientId)
    {
        this.patientId = patientId;
    }

    protected String getPatientId()
    {
        return patientId;
    }

    protected void setAssigningAuthorityId(String assigningAuthorityId)
    {
        this.assigningAuthorityId = assigningAuthorityId;
    }

    protected String getAssigningAuthorityId()
    {
        return assigningAuthorityId;
    }

    protected void setHomeCommunityId(String homeCommunityId)
    {
        this.homeCommunityId = homeCommunityId;
    }

    protected String getHomeCommunityId()
    {
        return homeCommunityId;
    }

    public AdhocQueryResponse filterAdhocQueryResults(AdhocQueryRequest adhocQueryRequest, AdhocQueryResponse adhocQueryResponse)
    {
        log.debug("Begin filterAdhocQueryResults");
        AdhocQueryResponse response = null;
        if(adhocQueryRequest == null)
        {
            log.warn("AdhocQueryRequest was null.");
        }
        else if(adhocQueryResponse == null)
        {
            log.warn("AdhocQueryResponse was null.");

        }
        else
        {
            extractIdentifiers(adhocQueryRequest);
            if((patientId != null) && (!patientId.isEmpty()))
            {
                PatientConsentHelper patientConsentHelper = getPatientConsentHelper();
                if(patientConsentHelper == null)
                {
                    log.warn("PatientConsentHelper was null.");
                }
                else
                {
                    PatientPreferencesType patientPreferences = patientConsentHelper.retrievePatientConsentbyPatientId(patientId, assigningAuthorityId);
                    if(patientPreferences == null)
                    {
                        log.warn("PatientPreferences was null.");
                    }
                    else
                    {
                        response = filterResults(adhocQueryResponse, patientPreferences);
                    }
                }
            }
            else
            {
                log.info("Not a patient-centric query.");
                response = filterResultsNonPatientCentric(adhocQueryResponse);
            }
        }
        log.debug("End filterAdhocQueryResults");
        return response;
    }

    protected void extractIdentifiers(AdhocQueryRequest adhocQueryRequest)
    {
        log.debug("Begin extractIdentifiers");
        if(adhocQueryRequest == null)
        {
            log.warn("AdhocQueryRequest was null.");
        }
        else
        {
            AdhocQueryType adhocQuery = adhocQueryRequest.getAdhocQuery();
            if(adhocQuery != null)
            {
                homeCommunityId = HomeCommunityMap.getCommunityIdForDeferredQDRequest(adhocQuery);

                List<SlotType1> slots = null;
                if(adhocQuery != null)
                {
                    slots = adhocQuery.getSlot();
                    List<String> slotValues = extractSlotValues(slots, EBXML_DOCENTRY_PATIENT_ID);
                    if((slotValues != null) && (!slotValues.isEmpty()))
                    {
                        String formattedPatientId = slotValues.get(0);
                        patientId = PatientIdFormatUtil.parsePatientId(formattedPatientId);
                        assigningAuthorityId = PatientIdFormatUtil.parseCommunityId(formattedPatientId);
                    }
                }
            }
        }

        log.debug("End extractIdentifiers");
    }

    protected List<String> extractSlotValues(List<SlotType1> slots, String slotName)
    {
        log.debug("Begin extractSlotValues");
        List<String> returnValues = null;
        if(slots != null)
        {
            for(SlotType1 slot : slots)
            {
                if ((slot.getName() != null) &&
                    (slot.getName().length() > 0) &&
                    (slot.getValueList() != null) &&
                    (slot.getValueList().getValue() != null) &&
                    (slot.getValueList().getValue().size() > 0))
                {

                    if(slot.getName().equals(slotName))
                    {
                        ValueListType valueListType = slot.getValueList();
                        List<String> slotValues = valueListType.getValue();
                        returnValues = new ArrayList<String>();
                        for(String slotValue : slotValues)
                        {
                            returnValues.add(slotValue);
                        }
                    }
                }

            }
        }
        log.debug("End extractSlotValues");
        return returnValues;
    }

    protected AdhocQueryResponse filterResultsNonPatientCentric(AdhocQueryResponse adhocQueryResponse)
    {
        log.debug("In filterResultsNonPatientCentric");
        return filterResults(adhocQueryResponse, null);
    }

    protected AdhocQueryResponse filterResults(AdhocQueryResponse adhocQueryResponse, PatientPreferencesType patientPreferences)
    {
        log.debug("Begin filterResults");
        AdhocQueryResponse response = null;
        if(adhocQueryResponse == null)
        {
            log.warn("AdhocQueryResponse was null.");
        }
        else
        {
            ObjectFactory rimObjectFactory = new ObjectFactory();
            response = new AdhocQueryResponse();
            response.setRegistryErrorList(adhocQueryResponse.getRegistryErrorList());
            response.setStatus(adhocQueryResponse.getStatus());
            RegistryObjectListType registryObjectList = null;
            long docCount = 0;

            RegistryObjectListType sourceRegistryObjectList = adhocQueryResponse.getRegistryObjectList();
            if(sourceRegistryObjectList != null)
            {
                List<JAXBElement<? extends IdentifiableType>> olRegObjs = sourceRegistryObjectList.getIdentifiable();
                for (JAXBElement<? extends IdentifiableType> oJAXBObj : olRegObjs)
                {
                    if ((oJAXBObj != null) &&
                            (oJAXBObj.getDeclaredType() != null) &&
                            (oJAXBObj.getDeclaredType().getCanonicalName() != null) &&
                            (oJAXBObj.getDeclaredType().getCanonicalName().equals("oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType")) &&
                            (oJAXBObj.getValue() != null))
                    {
                        ExtrinsicObjectType oExtObj = (ExtrinsicObjectType) oJAXBObj.getValue();
                        PatientPreferencesType workingPatientPreferences = null;
                        if(patientPreferences == null)
                        {
                            workingPatientPreferences = retrievePatientPreferencesForDocument(oExtObj);
                        }
                        else
                        {
                            workingPatientPreferences = patientPreferences;
                        }
                        if(documentAllowed(oExtObj, workingPatientPreferences))
                        {
                            log.debug("Adding document query response to the list.");
                            if(registryObjectList == null)
                            {
                                registryObjectList = new RegistryObjectListType();
                                response.setRegistryObjectList(registryObjectList);
                            }
                            registryObjectList.getIdentifiable().add(rimObjectFactory.createExtrinsicObject(oExtObj));
                            docCount++;
                        }
                        else
                        {
                            log.debug("Skipping document");
                        }
                    }
                }
                response.setTotalResultCount(BigInteger.valueOf(docCount));
            }
            else
            {
                log.info("RegistryObjectList was null.");
            }

        }
        log.debug("End filterResults");
        return response;
    }

    protected PatientPreferencesType retrievePatientPreferencesForDocument(ExtrinsicObjectType oExtObj)
    {
        PatientPreferencesType patientPreferences = null;
        if(oExtObj == null)
        {
            log.error("Extrinsic Object was null.");
        }
        else
        {
            String documentId = extractDocumentId(oExtObj);
            String repositoryId = extractRepositoryId(oExtObj);
            patientPreferences = getPatientConsentHelper().retrievePatientConsentbyDocumentId(homeCommunityId, repositoryId, documentId);
        }
        return patientPreferences;
    }
    
    protected String extractDocumentId(ExtrinsicObjectType oExtObj)
    {
        log.debug("Begin extractDocumentId");
        String documentId = null;
        if (!oExtObj.getExternalIdentifier().isEmpty())
        {
            List<ExternalIdentifierType> olExtId = oExtObj.getExternalIdentifier();
            for (ExternalIdentifierType oExtId : olExtId)
            {
                if ((oExtId.getIdentificationScheme() != null) &&
                        (oExtId.getIdentificationScheme().equals(CDAConstants.DOCUMENT_ID_IDENT_SCHEME)) &&
                        (oExtId.getValue() != null) &&
                        (oExtId.getValue().length() > 0))
                {
                    documentId = oExtId.getValue().trim();
                }
            }
        }
        log.debug("End extractDocumentId - returning: " + documentId);
        return documentId;
    }

    protected String extractRepositoryId(ExtrinsicObjectType oExtObj)
    {
        log.debug("Begin extractRepositoryId");
        String repositoryId = null;
        if (!oExtObj.getSlot().isEmpty())
        {
            List<SlotType1> slots = oExtObj.getSlot();
            for (SlotType1 slot : slots)
            {
                if ((slot != null) &&
                        (CDAConstants.SLOT_NAME_REPOSITORY_UNIQUE_ID.equals(slot.getName())) &&
                        (slot.getValueList() != null) &&
                        (!slot.getValueList().getValue().isEmpty()))
                {
                    repositoryId = slot.getValueList().getValue().get(0);
                    if(repositoryId != null)
                    {
                        repositoryId = repositoryId.trim();
                        break;
                    }
                }
            }
        }
        log.debug("End extractRepositoryId - returning: " + repositoryId);
        return repositoryId;
    }

    protected String extractDocumentType(ExtrinsicObjectType oExtObj)
    {
        log.debug("Begin extractDocumentType");
        String documentType = null;
        if (!oExtObj.getClassification().isEmpty())
        {
            List<ClassificationType> classifications = oExtObj.getClassification();
            for (ClassificationType classification : classifications) {
                if ((classification != null) &&
                        (EBXML_RESPONSE_TYPECODE_CLASS_SCHEME.equals(classification.getClassificationScheme())))
                {
                    documentType = classification.getNodeRepresentation();
                    break;
                }
            }
        }
        log.debug("End extractDocumentType - returning: " + documentType);
        return documentType;
    }

    protected boolean documentAllowed(ExtrinsicObjectType extObject, PatientPreferencesType patientPreferences)
    {
        log.debug("Begin documentAllowed");
        boolean allowed = false;
        String documentTypeCode = extractDocumentType(extObject);
        allowed = getPatientConsentHelper().documentSharingAllowed(documentTypeCode, patientPreferences);
        log.debug("End documentAllowed - response: " + allowed);
        return allowed;
    }

}
