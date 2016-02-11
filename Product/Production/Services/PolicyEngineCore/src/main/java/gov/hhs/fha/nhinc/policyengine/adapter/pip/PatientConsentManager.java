/*
 * Copyright (c) 2009-2016, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.policyengine.adapter.pip;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.BinaryDocumentPolicyCriteriaType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.BinaryDocumentPolicyCriterionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.BinaryDocumentStoreActionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FineGrainedPolicyMetadataType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.docregistry.adapter.proxy.AdapterComponentDocRegistryProxy;
import gov.hhs.fha.nhinc.docregistry.adapter.proxy.AdapterComponentDocRegistryProxyObjectFactory;
import gov.hhs.fha.nhinc.docrepository.adapter.proxy.AdapterComponentDocRepositoryProxy;
import gov.hhs.fha.nhinc.docrepository.adapter.proxy.AdapterComponentDocRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.largefile.LargeFileUtils;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import oasis.names.tc.xacml._2_0.policy.schema.os.PolicyType;
import org.hl7.v3.II;
import org.hl7.v3.POCDMT000040ClinicalDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class manages the patient consent form. It stores or retrieves the patient consent document from the repository.
 *
 * @author Les Westberg
 */
public class PatientConsentManager {

    private static final Logger LOG = LoggerFactory.getLogger(PatientConsentManager.class);
    private static final String XACML_MIME_TYPE = "text/xml";
    private static final String PDF_MIME_TYPE = "application/pdf";

    /**
     * This method saves the patient consent information to the document repository. It will overwrite anything that is
     * currently there.
     *
     * @param oPtPref The patient consent preferences.
     * @throws gov.hhs.fha.nhinc.policyengine.adapter.pip.AdapterPIPException This exception is thrown if there is an
     * error storing.
     * @throws gov.hhs.fha.nhinc.properties.PropertyAccessException
     */
    public void storePatientConsent(PatientPreferencesType oPtPref) throws AdapterPIPException, PropertyAccessException {

        LOG.info("------ Begin PatientConsentManager.storePatientConsent() ------");
        // PatientPreferences contains a FineGrainedPolicyCriteria and possibly
        // multiple BinaryDocumentPolicyCriteria.
        // ---------------------------------------------------------------------
        if (oPtPref != null) {

            // Create the document
            // This also takes care of the case where there is only the
            // OptIn\OptOut setting and no fine grained policy
            PolicyType oConsentXACML;
            XACMLCreator oCreator = new XACMLCreator();
            oConsentXACML = oCreator.createConsentXACMLDoc(oPtPref);
            LOG.info("Created XACML Doc with policy OID: " + oConsentXACML.getPolicyId());

            String sDocOID = null;
            if (oPtPref.getAssigningAuthority() != null && oPtPref.getPatientId() != null) {
                sDocOID = retrievePatientConsentDocumentIdByPatientId(oPtPref.getPatientId(),
                    oPtPref.getAssigningAuthority());
                LOG.info("storePatientConsent - sDocOID: " + sDocOID);
            }

            if (sDocOID == null) {
                sDocOID = oConsentXACML.getPolicyId();
            }

            XACMLSerializer oSerializer = new XACMLSerializer();
            String sConsentXACML = oSerializer.serializeConsentXACMLDoc(oConsentXACML);

            // Uses Repository Services
            storeCPPToRepositoryUsingXDSb(oPtPref, sConsentXACML, sDocOID, XACML_MIME_TYPE);

            // Next handle the storage of the BinaryDocumentPolicyCriteria docs
            // if any have a store action set to add or update
            if (oPtPref.getBinaryDocumentPolicyCriteria() != null
                && oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion() != null
                && !oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion().isEmpty()) {

                List<String> olDocIds2Store = new ArrayList<>();
                List<BinaryDocumentPolicyCriterionType> olBinPolicyCriteria = oPtPref.getBinaryDocumentPolicyCriteria()
                    .getBinaryDocumentPolicyCriterion();
                for (BinaryDocumentPolicyCriterionType oBinPolicyCriterion : olBinPolicyCriteria) {
                    if (oBinPolicyCriterion != null && oBinPolicyCriterion.getStoreAction() != null) {
                        if (oBinPolicyCriterion.getStoreAction().compareTo(BinaryDocumentStoreActionType.ADD) == 0
                            || oBinPolicyCriterion.getStoreAction().compareTo(BinaryDocumentStoreActionType.UPDATE) == 0) {
                            LOG.info(oBinPolicyCriterion.getStoreAction() + " requested for document: "
                                + oBinPolicyCriterion.getDocumentUniqueId());
                            olDocIds2Store.add(oBinPolicyCriterion.getDocumentUniqueId());
                        }
                        // TODO how do we handle the delete request ?
                    }
                }

                if (!olDocIds2Store.isEmpty()) {
                    CdaPdfCreator oPdfCreator = new CdaPdfCreator();
                    CdaPdfSerializer oPdfSerializer = new CdaPdfSerializer();
                    List<POCDMT000040ClinicalDocument> olPdfs = oPdfCreator.createCDA(oPtPref);
                    if (olPdfs != null && !olPdfs.isEmpty()) {
                        for (POCDMT000040ClinicalDocument oCda : olPdfs) {
                            if (oCda != null && oCda.getId() != null && oCda.getId().getExtension() != null
                                && !oCda.getId().getExtension().isEmpty()) {
                                // The CDA ID will reflect the document unique id in the extension part of the II
                                // This can be used to determine which documents to store
                                String sDocUniqueID = oCda.getId().getExtension();
                                if (olDocIds2Store.contains(sDocUniqueID)) {
                                    String sCda = oPdfSerializer.serialize(oCda);
                                    storeCPPToRepositoryUsingXDSb(oPtPref, sCda, sDocUniqueID, PDF_MIME_TYPE);
                                }
                            }
                        }
                    }
                } else {
                    LOG.info("No Binary Policy Documents required updating or adding.");
                }
            } else {
                LOG.info("No Binary Policy Documents are contained in the patient preferences.");
            }
        } else {
            String sErrorMessage = "failed to store patient consent.  The patient preference was null.";
            LOG.error(sErrorMessage);
            throw new AdapterPIPException(sErrorMessage);
        }
        LOG.info("------ End PatientConsentManager.storePatientConsent() ------");
    }

    protected AdhocQueryResponse invokeDocRegistryStoredQuery(AdhocQueryRequest request) {
        AdapterComponentDocRegistryProxyObjectFactory factory = new AdapterComponentDocRegistryProxyObjectFactory();
        AdapterComponentDocRegistryProxy proxy = factory.getAdapterComponentDocRegistryProxy();

        return proxy.registryStoredQuery(request, null);
    }

    protected RegistryResponseType invokeDocRepositoryProvideAndRegisterDocumentSetB(
        ProvideAndRegisterDocumentSetRequestType request) {
        AdapterComponentDocRepositoryProxyObjectFactory factory = new AdapterComponentDocRepositoryProxyObjectFactory();
        AdapterComponentDocRepositoryProxy proxy = factory.getAdapterDocumentRepositoryProxy();

        return proxy.provideAndRegisterDocumentSet(request, null);
    }

    protected RetrieveDocumentSetResponseType invokeDocRepositoryRetrieveDocumentSet(
        RetrieveDocumentSetRequestType request) {
        AdapterComponentDocRepositoryProxyObjectFactory factory = new AdapterComponentDocRepositoryProxyObjectFactory();
        AdapterComponentDocRepositoryProxy proxy = factory.getAdapterDocumentRepositoryProxy();

        return proxy.retrieveDocument(request, null);
    }

    /**
     * This method stores the patient preference document to the repository.
     *
     * @param oPtPref The patient preference information.
     * @param sPrefDoc The consent document.
     * @throws gov.hhs.fha.nhinc.policyengine.adapter.pip.AdapterPIPException This exception is thrown if there is an
     * error.
     */
    private void storeCPPToRepositoryUsingXDSb(PatientPreferencesType oPtPref, String sPrefDoc,
        String sUniqueDocumentId, String sMimeType) throws AdapterPIPException, PropertyAccessException {
        LOG.info("------ Begin PatientConsentManager.storeCPPToRepositoryUsingXDSb() ------");
        LOG.info("Request to store Document: " + sUniqueDocumentId);
        if ((oPtPref == null) || (oPtPref.getPatientId() == null) || (oPtPref.getPatientId().trim().length() <= 0)) {
            String sErrorMessage = "failed to store patient consent.  The patient ID was null or blank.";
            LOG.error(sErrorMessage);
            throw new AdapterPIPException(sErrorMessage);
        } else {
            saveCPPDoc(sPrefDoc, oPtPref, sUniqueDocumentId, sMimeType);
        }

        LOG.info("------ End PatientConsentManager.storeCPPToRepositoryUsingXDSb() ------");
    }

    /**
     *
     * @param sPrefDoc
     * @param oPtPref
     * @return boolean
     * @throws gov.hhs.fha.nhinc.policyengine.adapter.pip.AdapterPIPException
     */
    private void saveCPPDoc(String sPrefDoc, PatientPreferencesType oPtPref, String sUniqueDocumentId, String sMimeType)
        throws AdapterPIPException, PropertyAccessException {
        LOG.info("------ Begin PatientConsentManager.saveCPPDoc ------");

        if (oPtPref != null) {
            // Create a raw data document to be stored
            ProvideAndRegisterDocumentSetRequestType oRequest = new ProvideAndRegisterDocumentSetRequestType();
            ProvideAndRegisterDocumentSetRequestType.Document oDoc = createDocumentRawData(sPrefDoc, sUniqueDocumentId);
            LOG.info("Raw document created with ID: " + sUniqueDocumentId);
            oRequest.getDocument().add(oDoc);

            if (oDoc != null && oDoc.getValue() != null) {
                if (oPtPref.getFineGrainedPolicyMetadata() == null) {
                    oPtPref.setFineGrainedPolicyMetadata(new FineGrainedPolicyMetadataType());
                }

                oPtPref.getFineGrainedPolicyMetadata().setSize(String.valueOf(sPrefDoc.getBytes().length));
            }

            // If a matching document id is found the target will not be empty
            String sTargetObject = checkCPPMetaFromRepositoryUsingXDSb(oPtPref.getPatientId(),
                oPtPref.getAssigningAuthority(), sUniqueDocumentId, sMimeType);
            String sHomeCommunityId = PropertyAccessor.getInstance().getProperty("gateway", "localHomeCommunityId");
            PatientConsentDocumentBuilderHelper oPatConsentDocBuilderHelper = new PatientConsentDocumentBuilderHelper();
            SubmitObjectsRequest oSubmitObjectRequest = oPatConsentDocBuilderHelper.createSubmitObjectRequest(
                sTargetObject, sHomeCommunityId, sUniqueDocumentId, sMimeType, oPtPref);
            oRequest.setSubmitObjectsRequest(oSubmitObjectRequest);

            RegistryResponseType oRegistryResponse = invokeDocRepositoryProvideAndRegisterDocumentSetB(oRequest);

            if (oRegistryResponse != null) {
                if (oRegistryResponse.getStatus() != null && !oRegistryResponse.getStatus().isEmpty()) {
                    LOG.info("Patient Consent Document saved to repository Successfully");
                } else {
                    throw new AdapterPIPException("Unable to save Patient Consent Document"
                        + oRegistryResponse.getRegistryErrorList().getRegistryError().get(0).getValue());
                }
            } else {
                throw new AdapterPIPException("Did not receive a Registry Response.");
            }
        } else {
            throw new AdapterPIPException("Patient Preferences was null.");
        }

        LOG.info("------ End PatientConsentManager.saveCPPDoc ------");
    }

    /**
     * This method is used internal by saveCPPDoc creates Document with rawData and document unique id
     *
     * @param sPrefDoc
     * @param sDocUniqueId
     * @return ProvideAndRegisterDocumentSetRequestType.Document
     * @throws AdapterPIPException
     */
    private ProvideAndRegisterDocumentSetRequestType.Document createDocumentRawData(String sPrefDoc, String sDocUniqueId)
        throws AdapterPIPException {
        LOG.info("------ Begin PatientConsentManager.createDocumentRawData ------");
        try {
            ProvideAndRegisterDocumentSetRequestType.Document oDoc = new ProvideAndRegisterDocumentSetRequestType.Document();
            oDoc.setId(sDocUniqueId);
            oDoc.setValue(LargeFileUtils.getInstance().convertToDataHandler(sPrefDoc));
            LOG.info("------ End PatientConsentManager.createDocumentRawData ------");
            return oDoc;
        } catch (Exception e) {
            throw new AdapterPIPException("Failed to process document.", e);
        }
    }

    /**
     * To verify a XAML Document already persists in database
     *
     * @param sPatientId The Patient identifier
     * @param sAssigningAuthority The assigning authority
     * @return boolean
     * @throws gov.hhs.fha.nhinc.policyengine.adapter.pip.AdapterPIPException
     */
    private String checkCPPMetaFromRepositoryUsingXDSb(String sPatientId, String sAssigningAuthority,
        String sUniqueDocumentId, String sMimeType) throws AdapterPIPException {
        LOG.info("--------------- Begin checkCPPMetaFromRepositoryUsingXDSb ---------------");
        String sTargetObject = "";

        AdhocQueryRequest oRequest = new QueryUtil().createAdhocQueryRequest(sPatientId, sAssigningAuthority);
        AdhocQueryResponse oResponse = invokeDocRegistryStoredQuery(oRequest);

        if (oResponse != null && oResponse.getRegistryObjectList() != null
            && oResponse.getRegistryObjectList().getIdentifiable() != null
            && oResponse.getRegistryObjectList().getIdentifiable().size() > 0) {
            List<JAXBElement<? extends IdentifiableType>> olRegObjs = oResponse.getRegistryObjectList()
                .getIdentifiable();
            String sFoundXACMLDoc = "";
            foundLabel:
                for (JAXBElement<? extends IdentifiableType> oJAXBObj : olRegObjs) {
                    if ((oJAXBObj != null)
                        && (oJAXBObj.getDeclaredType() != null)
                        && (oJAXBObj.getDeclaredType().getCanonicalName() != null)
                        && (oJAXBObj.getDeclaredType().getCanonicalName()
                        .equals("oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType"))
                        && (oJAXBObj.getValue() != null)) {
                        ExtrinsicObjectType oExtObj = (ExtrinsicObjectType) oJAXBObj.getValue();
                        String sRepoObjMimeType = oExtObj.getMimeType();
                        LOG.info("Evaluate id: " + oExtObj.getId() + " of type: " + sRepoObjMimeType);

                    // If we are looking for a XACML type and this is of that type
                        // save it as a possiblity
                        if (XACML_MIME_TYPE.equals(sRepoObjMimeType) && XACML_MIME_TYPE.equals(sMimeType)) {
                            sFoundXACMLDoc = oExtObj.getId();
                        }

                    // Get found Document Unique ID
                        // -----------------------------
                        if ((oExtObj.getExternalIdentifier() != null) && (oExtObj.getExternalIdentifier().size() > 0)) {
                            List<ExternalIdentifierType> olExtId = oExtObj.getExternalIdentifier();
                            for (ExternalIdentifierType oExtId : olExtId) {
                                if ((oExtId.getIdentificationScheme() != null)
                                    && (oExtId.getIdentificationScheme().equals(CDAConstants.DOCUMENT_ID_IDENT_SCHEME))
                                    && (oExtId.getValue() != null) && (oExtId.getValue().length() > 0)) {
                                    String sDocumentId = oExtId.getValue().trim();

                                // If this matches the document id that we are
                                    // looking for save the target id and exit
                                    // ---------------------------------------------
                                    if (sDocumentId.equals(sUniqueDocumentId)) {
                                        sTargetObject = oExtObj.getId();
                                        LOG.info("Matching document id: " + sDocumentId + " sets target: " + sTargetObject);
                                        break foundLabel;
                                    } else {
                                        LOG.info("Non-matching document id: " + sDocumentId);
                                    }
                                }
                            } // for (ExternalIdentifierType oExtid : olExtId)
                        } // if ((oExtObj.getExternalIdentifier() != null) &&
                    }
                }

            if (sTargetObject.isEmpty() && !sFoundXACMLDoc.isEmpty()) {
                // XACML documents may be created with a generated UID
                // It may still replace a stored document if one is found of that type
                sTargetObject = sFoundXACMLDoc;
            }
        }
        LOG.info("--------------- End checkCPPMetaFromRepositoryUsingXDSb ---------------");
        return sTargetObject;
    }

    /**
     *
     * @param sHomeCommunityId
     * @param sRepositoryId
     * @param sDocumentUniqueId
     * @return The patient consent preferences.
     * @throws gov.hhs.fha.nhinc.policyengine.adapter.pip.AdapterPIPException This error is thrown if there are any
     * issues retrieving the document.
     */
    public PatientPreferencesType retrievePatientConsentByDocId(String sHomeCommunityId, String sRepositoryId,
        String sDocumentUniqueId) throws AdapterPIPException {
        LOG.info("--------------- Begin retrievePatientConsentByDocId ---------------");
        PatientPreferencesType oPtPref = new PatientPreferencesType();

        if ((sDocumentUniqueId == null) || (sDocumentUniqueId.trim().length() <= 0)) {
            String sErrorMessage = "Failed to retrieve patient consent.  The document unique ID was either null or an empty string.";
            LOG.error(sErrorMessage);
            throw new AdapterPIPException(sErrorMessage);
        }

        try {
            // Patient from the document is now the fully qualified Unique Patient Id; "PID^^^&AAID&ISO"
            String sPatientId = retrievePtIdFromDocumentId(sDocumentUniqueId, sRepositoryId);
            LOG.info("Given DocId: " + sDocumentUniqueId + " in Repository: " + sRepositoryId
                + " patientId retrieved is: " + sPatientId);

            // Extract the patient and assigning authority id into an II
            II patII = extractUniquePatientIdToII(sPatientId);

            if (patII != null && patII.getExtension() != null && !patII.getExtension().isEmpty()
                && patII.getRoot() != null && !patII.getRoot().isEmpty()) {
                oPtPref = retrievePatientConsentByPatientId(patII.getExtension(), patII.getRoot());
            }
        } catch (Exception e) {
            LOG.error("Exception processing retrievePatientConsentByPatientId: ", e);
        }

        LOG.info("--------------- End retrievePatientConsentByDocId ---------------");
        return oPtPref;
    }

    /**
     * This method takes the given Document ID and does a query against the repository to find out the patient ID for
     * this patient. It returns that patient ID.
     *
     * @param sDocumentUniqueId The document ID of an existing document in the repository.
     * @param sRepositoryId The repository ID of the repository where the document is stored.
     * @return The patient ID of the patient.
     * @throws AdapterPIPException - This is thrown if there is an error.
     */
    private String retrievePtIdFromDocumentId(String sDocumentUniqueId, String sRepositoryId)
        throws AdapterPIPException {
        LOG.debug("Begin PatientConsentManager.retrievePtIdFromDocumentId()..");
        String sPatientId;

        AdhocQueryRequest oRequest = new QueryUtil().createPatientIdQuery(sDocumentUniqueId, sRepositoryId);
        AdhocQueryResponse oResponse = invokeDocRegistryStoredQuery(oRequest);

        sPatientId = new QueryUtil().extractPatientId(oResponse);

        LOG.debug("retrievePtIdFromDocumentId() - PatientId:" + sPatientId);

        return sPatientId;
    }

    private II extractUniquePatientIdToII(String uniquePatientId) {
        LOG.debug("Begin extractUniquePatientIdToII - uniquePatientId is " + uniquePatientId);

        II patII = new II();

        String patientId = "";
        String aaId = "";

        if (uniquePatientId != null && !uniquePatientId.isEmpty()) {
            if (uniquePatientId.startsWith("'")) {
                uniquePatientId = uniquePatientId.substring(1);
            }
            int pos;
            String[] tokens = uniquePatientId.split("\\&");
            LOG.debug("extractUniquePatientIdToII - tokens length is " + tokens.length);
            if (tokens.length > 0) {
                for (String token : tokens) {
                    LOG.debug("token: " + token);
                }
            }

            if (tokens.length == 3) {
                patientId = tokens[0];
                pos = patientId.indexOf("^");
                if (pos > 0) {
                    patientId = patientId.substring(0, pos);
                }
                aaId = tokens[1];
            }

            if (patientId != null && !patientId.isEmpty()) {
                patII.setExtension(patientId);
                LOG.debug("extracted patient id is " + patientId);
            }
            if (aaId != null && !aaId.isEmpty()) {
                patII.setRoot(aaId);
                LOG.debug("extracted aa id is " + aaId);
            }
        }

        return patII;
    }

    /**
     * This method retrieves the patient consent information from the repository based on patient Id.
     *
     * @param sPatientId The ID of the patient.
     * @param sAssigningAuthority The assigning authority associated with the patient ID. Currently it is not really
     * used, but here if it is needed.
     * @return The patient consent preferences.
     * @throws gov.hhs.fha.nhinc.policyengine.adapter.pip.AdapterPIPException This error is thrown if there are any
     * issues retrieving the document.
     */
    public PatientPreferencesType retrievePatientConsentByPatientId(String sPatientId, String sAssigningAuthority)
        throws AdapterPIPException {
        LOG.info("--------------- Begin retrievePatientConsentByPatientId ---------------");
        PatientPreferencesType oPtPref = new PatientPreferencesType();

        if ((sPatientId == null) || (sPatientId.trim().length() <= 0)) {
            String sErrorMessage = "Failed to retrieve patient consent.  The patient ID was either null or an empty string.";
            LOG.error(sErrorMessage);
            throw new AdapterPIPException(sErrorMessage);
        }

        List<CPPDocumentInfo> olDocInfo = retrieveCPPFromRepositoryUsingXDSb(sPatientId, sAssigningAuthority);

        if (olDocInfo != null && !olDocInfo.isEmpty()) {
            LOG.info(olDocInfo.size() + " CPP documents were retrieved from the repository");
            oPtPref = populateConsentInfo(olDocInfo);
        }
        LOG.info("--------------- End retrievePatientConsentByPatientId ---------------");
        return oPtPref;
    }

    public String retrievePatientConsentDocumentIdByPatientId(String sPatientId, String sAssigningAuthority)
        throws AdapterPIPException {
        LOG.info("--------------- Begin retrievePatientConsentDocumentIdByPatientId ---------------");
        String patientConsentDocumentId = null;

        if ((sPatientId == null) || (sPatientId.trim().length() <= 0)) {
            String sErrorMessage = "Failed to retrieve patient consent.  The patient ID was either null or an empty string.";
            LOG.error(sErrorMessage);
            throw new AdapterPIPException(sErrorMessage);
        }

        List<CPPDocumentInfo> olDocInfo = retrieveCPPFromRepositoryUsingXDSb(sPatientId, sAssigningAuthority);

        if (olDocInfo != null && (olDocInfo.size() > 0)) {
            LOG.info(olDocInfo.size() + " CPP documents were retrieved from the repository");
            patientConsentDocumentId = olDocInfo.get(0).sDocumentUniqueId;
        }

        LOG.info("retrievePatientConsentDocumentIdByPatientId - patientConsentDocumentId: " + patientConsentDocumentId);
        LOG.info("--------------- End retrievePatientConsentDocumentIdByPatientId ---------------");
        return patientConsentDocumentId;
    }

    /**
     * This operation retrieves the currently stored patient consent information from the repository.
     *
     * @param sPatientId The ID of the patient.
     * @param sAssigningAuthority The Assigning authority
     * @return The document information.
     * @throws gov.hhs.fha.nhinc.policyengine.adapter.pip.AdapterPIPException The error if one occurs.
     */
    private List<CPPDocumentInfo> retrieveCPPFromRepositoryUsingXDSb(String sPatientId, String sAssigningAuthority)
        throws AdapterPIPException {

        List<CPPDocumentInfo> olCPPDocInfo = new ArrayList<>();

        List<DocumentRequest> olDocReq = retrieveCPPDocIdentifiers(sPatientId, sAssigningAuthority);
        for (DocumentRequest oDocRequest : olDocReq) {
            if (oDocRequest != null) {
                CPPDocumentInfo oCPPDocInfo = new CPPDocumentInfo();
                if (oDocRequest.getHomeCommunityId() != null) {
                    oCPPDocInfo.sHomeCommunityId = oDocRequest.getHomeCommunityId();
                }

                if (oDocRequest.getRepositoryUniqueId() != null) {
                    oCPPDocInfo.sRepositoryId = oDocRequest.getRepositoryUniqueId();
                }

                if (oDocRequest.getDocumentUniqueId() != null) {
                    oCPPDocInfo.sDocumentUniqueId = oDocRequest.getDocumentUniqueId();
                }
                LOG.info("Document Request has community: " + oCPPDocInfo.sHomeCommunityId + " repository: "
                    + oCPPDocInfo.sRepositoryId + " docId: " + oCPPDocInfo.sDocumentUniqueId);
                retrieveCPPDoc(oDocRequest, oCPPDocInfo);
                olCPPDocInfo.add(oCPPDocInfo);
            }

        }

        return olCPPDocInfo;
    }

    /**
     * This method retrieves the document identifier information from the repository for the Consumer Preferences
     * document for this patient.
     *
     * @param sPatientId The patient ID of the patient.
     * @param sAssigningAuthority The assigning authority
     * @return The document identifiers for the CPP document
     * @throws gov.hhs.fha.nhinc.policyengine.adapter.pip.AdapterPIPException This error is thrown if any problem occurs
     * getting the data.
     */
    private List<DocumentRequest> retrieveCPPDocIdentifiers(String sPatientId, String sAssigningAuthority)
        throws AdapterPIPException {
        try {
            QueryUtil queryUtil = new QueryUtil();

            AdhocQueryRequest oRequest = queryUtil.createAdhocQueryRequest(sPatientId, sAssigningAuthority);
            AdhocQueryResponse oResponse = invokeDocRegistryStoredQuery(oRequest);

            List<DocumentRequest> olDocReq = queryUtil.createDocumentRequest(oResponse);

            return olDocReq;
        } catch (Exception ex) {
            String message = "Error occurred calling PatientConsentManager.retrieveCPPDocIdentifiers.  Error: "
                + ex.getMessage();
            LOG.error(message, ex);
            throw new AdapterPIPException(message, ex);
        }
    }

    /**
     * This method takes the document identifiers for the CPP document and retrieves the document from the repository.
     *
     * @param oDocRequest The document identifiers.
     * @param oCPPDocInfo Document object to hold results
     * @return void.
     * @throws gov.hhs.fha.nhinc.policyengine.adapter.pip.AdapterPIPException This is thrown if there are any errors.
     */
    private void retrieveCPPDoc(DocumentRequest oDocRequest, CPPDocumentInfo oCPPDocInfo) throws AdapterPIPException {
        String sPrefDoc;

        RetrieveDocumentSetRequestType oRequest = new RetrieveDocumentSetRequestType();
        oRequest.getDocumentRequest().add(oDocRequest);

        RetrieveDocumentSetResponseType oResponse = invokeDocRepositoryRetrieveDocumentSet(oRequest);

        if (oResponse != null) {
            sPrefDoc = extractFineGrainedPrefDoc(oDocRequest, oResponse);
            if (NullChecker.isNotNullish(sPrefDoc)) {
                oCPPDocInfo.sConsentXACML = sPrefDoc;
            }

            List<String> olBinPrefDoc = extractBinPrefDoc(oDocRequest, oResponse);
            if (olBinPrefDoc != null && !olBinPrefDoc.isEmpty()) {
                oCPPDocInfo.olConsentPdf = olBinPrefDoc;
            }

        }
    }

    /**
     * This operation returns the Patient CPP document in XML form that it extracts from the Response.
     *
     * @param oResponse The response that was received from the repository.
     * @return The XML form of the patient CPP.
     * @throws gov.hhs.fha.nhinc.policyengine.adapter.pip.AdapterPIPException This is thrown if there are any errors.
     */
    private String extractFineGrainedPrefDoc(DocumentRequest oDocRequest, RetrieveDocumentSetResponseType oResponse)
        throws AdapterPIPException {
        String sPrefDoc = "";

        try {
            if ((oResponse != null) && (oResponse.getDocumentResponse() != null)
                && (oResponse.getDocumentResponse().size() > 0)) {
                List<DocumentResponse> olDocResponse = oResponse.getDocumentResponse();
                for (DocumentResponse oDocResponse : olDocResponse) {
                    LOG.info("Doc: " + oDocResponse.getDocumentUniqueId() + " Mime type: " + oDocResponse.getMimeType());
                    if (oDocRequest.getDocumentUniqueId().equals(oDocResponse.getDocumentUniqueId())) {
                        if (XACML_MIME_TYPE.equals(oDocResponse.getMimeType())) {
                            if (oDocResponse.getDocument() != null) {
                                LOG.info("Matching XACML document found");

                                byte[] rawData = LargeFileUtils.getInstance()
                                    .convertToBytes(oDocResponse.getDocument());
                                sPrefDoc = new String(rawData);
                                break;

                            }
                        }
                    }
                }
            }
        } catch (IOException ioe) {
            throw new AdapterPIPException("Failed to retrieve document.", ioe);
        }

        return sPrefDoc;
    }

    private List<String> extractBinPrefDoc(DocumentRequest oDocRequest, RetrieveDocumentSetResponseType oResponse) {

        LOG.info("--------------- Begin extractBinPrefDoc ---------------");
        List<String> olBinPrefDoc = new ArrayList<>();

        if ((oResponse != null) && (oResponse.getDocumentResponse() != null)
            && (oResponse.getDocumentResponse().size() > 0)) {
            List<DocumentResponse> olDocResponse = oResponse.getDocumentResponse();
            LOG.info(olDocResponse.size() + " documents have been found");
            for (DocumentResponse oDocResponse : olDocResponse) {
                LOG.info("Doc: " + oDocResponse.getDocumentUniqueId() + " Mime type: " + oDocResponse.getMimeType());
                if (oDocRequest.getDocumentUniqueId().equals(oDocResponse.getDocumentUniqueId())) {
                    if (PDF_MIME_TYPE.equals(oDocResponse.getMimeType())) {
                        if ((oDocResponse.getDocument() != null)) {
                            try {
                                byte[] rawData = LargeFileUtils.getInstance()
                                    .convertToBytes(oDocResponse.getDocument());

                                String sPrefDoc = new String(rawData);
                                LOG.info("Matching PDF document found");
                                olBinPrefDoc.add(sPrefDoc);
                            } catch (IOException ioe) {
                                LOG.error("Failed to retrieve document: " + oDocResponse.getDocumentUniqueId(), ioe);
                            }
                        }
                    }
                }
            }
        }
        LOG.info("--------------- End extractBinPrefDoc ---------------");
        return olBinPrefDoc;
    }

    /**
     * This method takes the XML Patient preference CDA documents and it populates the PtPref with the information it
     * finds in it.
     *
     * @param olDocInfo The list of patient preference documents
     * @return The patient preferences from the document.
     * @throws gov.hhs.fha.nhinc.policyengine.adapter.pip.AdapterPIPException
     */
    private PatientPreferencesType populateConsentInfo(List<CPPDocumentInfo> olDocInfo) throws AdapterPIPException {
        LOG.info("--------------- Begin populateConsentInfo ---------------");
        PatientPreferencesType oPtPref = new PatientPreferencesType();

        // Default to Opt-Out, but will be set by state found in XACML document
        oPtPref.setOptIn(false);

        boolean bHasFineGrained = false;
        for (CPPDocumentInfo oDocInfo : olDocInfo) {
            if (oDocInfo != null && oDocInfo.sConsentXACML != null && !oDocInfo.sConsentXACML.isEmpty()) {
                bHasFineGrained = true;

                String sConsentXACML = oDocInfo.sConsentXACML;
                XACMLSerializer oSerializer = new XACMLSerializer();
                PolicyType oConsentXACML = oSerializer.deserializeConsentXACMLDoc(sConsentXACML);
                if (oConsentXACML != null) {
                    XACMLExtractor oExtractor = new XACMLExtractor();
                    oPtPref = oExtractor.extractPatientPreferences(oConsentXACML);
                    LOG.info("Extracted XACML document has patient opt-in as: " + oPtPref.isOptIn());
                }
                break;
            }

        }
        // patient is required to have a fine-grained policy to continue with binary document
        if (bHasFineGrained) {
            LOG.info("Begin extraction for Binary documents");
            BinaryDocumentPolicyCriteriaType oBinDocPolicyCriteriaType = new BinaryDocumentPolicyCriteriaType();
            List<BinaryDocumentPolicyCriterionType> olBinDocPolicyCriteria = oBinDocPolicyCriteriaType
                .getBinaryDocumentPolicyCriterion();

            // Add Binary extraction for each document that contains a pdf
            for (CPPDocumentInfo oDocInfo : olDocInfo) {
                if (oDocInfo.olConsentPdf != null && !oDocInfo.olConsentPdf.isEmpty()) {
                    LOG.info(oDocInfo.lDocumentId + " has " + oDocInfo.olConsentPdf.size() + " pdf");

                    List<String> olConsentPdf = oDocInfo.olConsentPdf;
                    for (String sConsentPdf : olConsentPdf) {
                        CdaPdfSerializer oSerializer = new CdaPdfSerializer();
                        LOG.info("Deserialize document");
                        POCDMT000040ClinicalDocument oCda = oSerializer.deserialize(sConsentPdf);
                        LOG.info("Returned CDA: " + oCda);
                        if (oCda != null) {
                            CdaPdfExtractor oExtractor = new CdaPdfExtractor();
                            BinaryDocumentPolicyCriterionType oBinDocPolicyCriterion = oExtractor
                                .extractBinaryDocumentPolicyCriterion(oCda);
                            LOG.info("Returned Binary Criterion: " + oCda);
                            if (oBinDocPolicyCriterion != null) {
                                olBinDocPolicyCriteria.add(oBinDocPolicyCriterion);
                                LOG.info("Extracted Binary document has id: "
                                    + oBinDocPolicyCriterion.getDocumentUniqueId());
                            }
                        }
                    }
                }
            }
            if (olBinDocPolicyCriteria != null && !olBinDocPolicyCriteria.isEmpty()) {
                oPtPref.setBinaryDocumentPolicyCriteria(oBinDocPolicyCriteriaType);
            } else {
                LOG.info("No Binary documents are processed");
            }
        } else {
            LOG.info("Fine Grained Policy Criterion is not present.");
        }

        LOG.info("--------------- End populateConsentInfo ---------------");
        return oPtPref;
    }

    /**
     * This class is an inner class used to hold the document along with its set of identifiers. For internal purposes.
     */
    private class CPPDocumentInfo {

        long lDocumentId = 0; // note this is only used by our internal document repository service
        String sHomeCommunityId = "";
        String sRepositoryId = "";
        String sDocumentUniqueId = "";
        String sConsentXACML = "";
        List<String> olConsentPdf = new ArrayList<>();
    }
}
