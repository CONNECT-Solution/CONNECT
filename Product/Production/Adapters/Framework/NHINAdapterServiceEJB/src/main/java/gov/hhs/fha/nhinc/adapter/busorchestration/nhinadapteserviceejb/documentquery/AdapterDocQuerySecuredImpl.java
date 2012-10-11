/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 * All other rights reserved.
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
package gov.hhs.fha.nhinc.adapter.busorchestration.nhinadapteserviceejb.documentquery;

/**
 *
 * @author mflynn02
 */
import gov.hhs.fha.nhinc.adapter.busorchestration.nhinadapteserviceejb.AdapterConstants;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.util.StringUtil;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.StringTokenizer;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceContext;


import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AssociationType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;

/**
 * This Class is the core orchestration piece to Handle the dynamic creation of documents. This class is intended to
 * recieve incomming document query requests and insure that dyanmic documents are created and stashed in the dynamic
 * document archive to insure that they can be retrieved. In order to accomplish this the this class coordinates calls
 * to the document assembler and insures that dyanamic documents are sent to the document manager for storage if they
 * are unique. At this point all specific document type requests are passed to the document assembler in a unary manner,
 * although a query might request mulitple document types. The current implementation will support multiple request
 * types assuming they requested types are supported by the document assembler. This class works in cooperation with the
 * AdapterDocRetrieve service to insure at dynamic documents will be flagged to be retained once they have been servered
 * to a remote host. This implementation only supports Dynamic Documents - It is envisioned that a future version may
 * server both static and dynamic content.
 * 
 * Note: If note if not specific document type is request then a C32 is considered the default.
 * 
 * @author Jerry Goodnough
 */
public class AdapterDocQuerySecuredImpl {

    /** The Singleton */
    private static AdapterDocQuerySecuredImpl singlton = null;
    /** Local Log */
    private static Log log = LogFactory.getLog(
        AdapterDocQuerySecuredImpl.class);
    /** Sucess code */
    private static String statusSuccess =
        "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success";
    //TODO: Validate the next element is correct.
    /** Query request Id */
    private static String queryRequestId =
        "urn:oasis:names:tc:ebeml-regrep:xsd:query:3.0";
    /*property file key*/
    private static final String PROPERTY_FILE_NAME_REPOSITORY = "repository";
    private static final String PROPERTY_FILE_REPOSITORY_ID = "dynamicDocumentRepositoryId";
    /** Qname for the extrinsice */
    private static final QName _ExtrinsicObject_QNAME = new QName(
        "urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "ExtrinsicObject");

    static {
        singlton = new AdapterDocQuerySecuredImpl();
    }

    private AdapterDocQuerySecuredImpl() {
    }

    /**
     * Provide access to the singleton implementation
     *
     * @return
     */
    public static AdapterDocQuerySecuredImpl getInstance() {
        return singlton;
    }

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse respondingGatewayCrossGatewayQuery(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest body, WebServiceContext context) {
        RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequest =
            new RespondingGatewayCrossGatewayQueryRequestType();
        oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse response;
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
        respondingGatewayCrossGatewayQueryRequest.setAdhocQueryRequest(body);
        respondingGatewayCrossGatewayQueryRequest.setAssertion(assertion);
        response = respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest);
        return response;
    }

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse respondingGatewayCrossGatewayQuery(
        gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequest) {

        log.debug("Entering Adapter respondingGatewayCrossGatewayQuery for Dynamic Assembly");

        oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse dqResponse = initializeAdhocQueryResponse();

        //Grab the homecommunity id - Not sure if we need it.
        String sHomeCommunityId = null;

        try {
            sHomeCommunityId = PropertyAccessor.getInstance().getProperty(
                NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (PropertyAccessException e) {
            log.error("Failed to read " +
                NhincConstants.HOME_COMMUNITY_ID_PROPERTY +
                " property from the " + NhincConstants.GATEWAY_PROPERTY_FILE +
                ".properties  file.  Error: " + e.getMessage(), e);
        }

        log.debug("Get the Assembler Endpoint");

        //Grab the Document Assembler End Point for the Service Endpoint service
        String assemblerEndpoint = getAssemblerEndpoint();

        log.debug("Get the Doc Manager Endpoint");

        //Grab the Document
        String docMgrEndpoint = getDocMgrEndpoint();

        //This entire section loops for each requested document code type
        //In effect we will loop each type and allow it to add to the output.
        //This will require modifing the input to the dynamic assember to only have one type
        //Check the ad-hoc query request;
        AdhocQueryRequest adhocQuery =
            respondingGatewayCrossGatewayQueryRequest.getAdhocQueryRequest();

        //respondingGatewayCrossGatewayQueryRequest.getAdhocQueryRequest().
        AdhocQueryType ahqType = adhocQuery.getAdhocQuery();

        //Examine the slots of data for the query - First we transfer them to a hash map
        HashMap<String, ValueListType> querySlotHashMap = createSlotMap(
            ahqType.getSlot());

        try {
            //The following creates a default of a C32 request if none was provided
            if (!querySlotHashMap.containsKey(AdapterConstants.XDSDocumentEntryClassCode)) {
                //Patch up the request for just this type of document
                SlotType1 newSlot = new SlotType1();
                newSlot.setName(AdapterConstants.XDSDocumentEntryClassCode);
                ValueListType vaList = new ValueListType();
                vaList.getValue().add(AdapterConstants.C32_DOCUMENT);
                vaList.getValue().add(AdapterConstants.ED_DISCHARGE_SUMMARY);
                vaList.getValue().add(AdapterConstants.RADIOLOGY_REPORT);
                newSlot.setValueList(vaList);
                replaceOrAddSlotValue(ahqType.getSlot(), newSlot);
                querySlotHashMap.put(AdapterConstants.XDSDocumentEntryClassCode, vaList);
                log.debug("Query did not specify a document type - Adding C32, Radiology Report & ED Discharge Summary");
            }

            //If the caller has specified a class code - required for us.
            //Then we loop each type individually to send out for assembly
            if (querySlotHashMap.containsKey(AdapterConstants.XDSDocumentEntryClassCode)) {
                gov.hhs.fha.nhinc.documentassembly.DocumentAssemblyPortType port = null;

                try // Call Web Service Operation
                {
                    gov.hhs.fha.nhinc.documentassembly.DocumentAssembly service =
                        new gov.hhs.fha.nhinc.documentassembly.DocumentAssembly();
                    port = service.getDocumentAssemblyPortSoap();
                    if ((assemblerEndpoint != null) &&
                        !assemblerEndpoint.isEmpty()) {
                        //Use the BOS Endpoint
                        ((BindingProvider) port).getRequestContext().put(
                            BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                            assemblerEndpoint);
                    }
                } catch (Exception ex) {
                    log.error("Exception during dynamic assembly", ex);
                    throw ex;
                }

                ValueListType vlClassCode = querySlotHashMap.get(
                    AdapterConstants.XDSDocumentEntryClassCode);

                List<String> documentTypeList = getDocumentTypes(vlClassCode);

                ListIterator<String> itr = documentTypeList.listIterator();

                while (itr.hasNext()) {

                    //Grab the document type from the list
                    String docType = itr.next();

                    //Normalize the document type for assembler
                    if (docType.contains("^^")) {
                        docType = docType.substring(0, docType.indexOf("^")).trim();
                    }

                    //if not a class code (docType) that Conemaugh supports, don't assemble it
                    if (docType.equals("34133-9") || docType.equals("18842-5") || docType.equals("18726-0")) {
                        //known class code - assemble document
                        log.debug("Supported class code of " + docType + " - Assemble Document");

                        docType = "('" + docType + "')";

                        log.debug("Calling Dynamic Assembly for type: " + docType);
                        //Patch up the request for just this type of document
                        SlotType1 newSlot = new SlotType1();
                        newSlot.setName(AdapterConstants.XDSDocumentEntryClassCode);

                        ValueListType vaList = new ValueListType();
                        vaList.getValue().add(docType);
                        newSlot.setValueList(vaList);
                        replaceOrAddSlotValue(ahqType.getSlot(), newSlot);

                        //Loop the request and scan the requested type codes
                        gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestRequestType dynResult =
                            null;

                        //Get the document from the Assembler
                        try // Call Web Service Operation
                        {
                            dynResult = port.dynamicAssemblyQuery(
                                respondingGatewayCrossGatewayQueryRequest);
                        } catch (Exception ex) {
                            log.error("Exception during dynamic assembly", ex);
                            throw ex;
                        }

                        if (dynResult != null) {
                            //Query the Dynamic Document Archive
                            oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse archiveResult =
                                null;

                            try {
                                // Call Web Service Operation
                                ihe.iti.xds_b._2007.DocumentManagerService service =
                                    new ihe.iti.xds_b._2007.DocumentManagerService();
                                ihe.iti.xds_b._2007.DocumentManagerPortType docManagerPort =
                                    service.getDocumentManagerPortSoap();

                                if ((docMgrEndpoint != null) &&
                                    !docMgrEndpoint.isEmpty()) {
                                    //Use the BOS Endpoint
                                    ((BindingProvider) docManagerPort).getRequestContext().put(
                                        BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                                        docMgrEndpoint);
                                }
                                log.debug("Calling to Document Manager for match");

                                //set the repository type as a dynamic document (1=dynamic doc, 2=inbound doc, 3 = policy doc)
                                AdhocQueryRequest aqr = respondingGatewayCrossGatewayQueryRequest.getAdhocQueryRequest();

                                SlotType1 slot = new SlotType1();
                                String slotValue = PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME_REPOSITORY, PROPERTY_FILE_REPOSITORY_ID);
                                ValueListType vList = new ValueListType();

                                slot.setName("$XDSRepositoryUniqueId");
                                vList.getValue().add(slotValue);
                                slot.setValueList(vList);
                                aqr.getAdhocQuery().getSlot().add(slot);

                                //invoke DocumentManagerService
                                //archiveResult = docManagerPort.documentManagerQueryDynamicDocumentArchive(aqr);
                                archiveResult = docManagerPort.documentManagerQueryForDocument(aqr);
                                log.debug("Calling to Document Manager done");
                            } catch (Exception ex) {
                                log.error("Exception during docment archive query", ex);
                                throw ex;
                            }

                            // Create objects appropriate for EACH Document and then process them accordingly
                            // The variable dynResult should not appear anywhere below the start of the for loop!!
                            SubmitObjectsRequest sor = dynResult.getProvideAndRegisterDocumentSetRequest().getSubmitObjectsRequest();
                            int counter = dynResult.getProvideAndRegisterDocumentSetRequest().getDocument().size();
                            for (int j = 0; j < counter; j++) {
                                Document currentDocument = dynResult.getProvideAndRegisterDocumentSetRequest().getDocument().get(j);
                                RegistryObjectListType rol = getRegistryObjectListForDocument(sor, j);

                                ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType currentPandRSet =
                                    new ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType();

                                ExtrinsicObjectType currentExtrinsic = (ExtrinsicObjectType) rol.getIdentifiable().get(0).getValue();
                                SubmitObjectsRequest newsor = new SubmitObjectsRequest();
                                newsor.setRegistryObjectList(rol);

                                currentPandRSet.getDocument().add(currentDocument);
                                currentPandRSet.setSubmitObjectsRequest(newsor);

                                //Grab the clinically unique hash
                                String dynamicHash = getClinicallyUniqueHash(currentExtrinsic);//, i);
                                log.debug("Dynamic Document Clinically Unique Hash = " + dynamicHash);

                                log.info("Checking for match in Archive");
                                //Compare results looking for a Match - Should a match be found

                                ExtrinsicObjectType matchFound = checkForMatchInArchive(archiveResult, dynamicHash, dqResponse);

                                if (matchFound == null) {
                                    log.info("Match not found in Archive");

                                    //The Assembler provides a unique id - So if we save it we us it
                                    //We should grab the repositoryId from the properties file
                                    //We now need to save it
                                    try // Call Web Service Operation
                                    {
                                        ihe.iti.xds_b._2007.DocumentManagerService service =
                                            new ihe.iti.xds_b._2007.DocumentManagerService();
                                        ihe.iti.xds_b._2007.DocumentManagerPortType docManagerPort =
                                            service.getDocumentManagerPortSoap();

                                        if ((docMgrEndpoint != null) &&
                                            !docMgrEndpoint.isEmpty()) {
                                            //Use the BOS Endpoint
                                            ((BindingProvider) docManagerPort).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, docMgrEndpoint);
                                        }

                                        //set the repository type as a dynamic document (1=dynamic doc, 2=inbound doc, 3 = policy doc)
                                        ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType pardsrt =
                                            currentPandRSet;
                                        //         pardsrt.setRepositoryUniqueType("1");
                                        //Now we format the return data to deal with results
                                        formatOutputForNewDynDoc(dqResponse, currentExtrinsic);

                                        //store dynamic document
                                        oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType result =
                                            //docManagerPort.documentManagerStoreDynamicDocument(pardsrt);
                                            docManagerPort.documentManagerStoreDocument(pardsrt);
                                        log.debug("Result of storing document " +
                                            result.getStatus());

                                        //Check success - Verify this is the right status code
                                        if (result.getStatus().compareTo(statusSuccess) != 0) {
                                            // OK we have a fault filing
                                            log.error(
                                                "Error storing dynamic document - Success code = " +
                                                result.getStatus());

                                            // Log the error and throw an exception
                                            throw new Exception(
                                                "Error storing dynamic document - Success code = " +
                                                result.getStatus());
                                        }
                                    } catch (Exception ex) {
                                        log.error("Exception saving dynamic document", ex);
                                        throw ex;
                                    }

                                } else {
                                    log.info("Match found in archive");
                                    JAXBElement<ExtrinsicObjectType> metadata =
                                        new JAXBElement<ExtrinsicObjectType>(
                                        _ExtrinsicObject_QNAME,
                                        ExtrinsicObjectType.class, matchFound);

                                    //Given a match add it to the output
                                    dqResponse.getRegistryObjectList().getIdentifiable().add(
                                        metadata);

                                    //The output was updated by the search so we really don't need to do anything
                                    log.info("Request fullfilled by existing document");
                                }
                            } //end foreach document in set
                        } else {
                            log.info("No documents returned...");
                        }
                    } else {
                        //unknown class code - do NOT asemble document
                        log.warn("Unsupported class code of " + docType + " - Do NOT Assemble Document");
                    }
                }  //end of iterator loop
            } else {
                log.info("No document types found");
            }
        } catch (Exception ex) {
            log.error("Exception for dynamic document query - Details above",
                ex);
            //build registry error
            dqResponse.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
            RegistryErrorList _regerrorlist = new RegistryErrorList();
            RegistryError _regerror = new RegistryError();
            _regerror.setCodeContext("Internal Document Query Processing");
            _regerror.setErrorCode("XDSRegistryError");
            _regerror.setSeverity("Error");
            _regerrorlist.getRegistryError().add(_regerror);

            dqResponse.setRegistryErrorList(_regerrorlist);
        }

        return dqResponse;

    }

    /**
     * Add slot to a a slot list
     *
     * @param  registry  - submission object
     * @param  name      - slot name
     * @param  values    - slot values
     */
    private static void addSlot(List<SlotType1> slots, String name,
        String[] values) {

        SlotType1 slot = new SlotType1();
        slot.setName(name);

        ValueListType valList = new ValueListType();

        for (String value : values) {
            valList.getValue().add(value);
        }

        slot.setValueList(valList);
        slots.add(slot);
    }

    /**
     * Search of a matching document already in the archive
     *
     * @param   archiveResult          The Results of the archicve
     * @param   sClinicallyUniqueHash  The Clinically Unique Hash of the Dynamic
     *                                 Document
     * @param   out                    The Exterinsic Object that the document
     *                                 of null
     *
     * @return
     */
    private static ExtrinsicObjectType checkForMatchInArchive(
        oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse archiveResult,
        String sClinicallyUnqiueHash,
        oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse out) {

        //For a moment pretend there is no match.

        ExtrinsicObjectType match = null;


        //Pull out submit objects
        List<JAXBElement<? extends IdentifiableType>> objectList =
            archiveResult.getRegistryObjectList().getIdentifiable();

        ExtrinsicObjectType docExtrinsic = null;

        log.debug("Archive query result size = " + objectList.size());
        //Find extrinsic object
        for (JAXBElement<? extends IdentifiableType> object : objectList) {
            IdentifiableType identifiableType = object.getValue();

            if (identifiableType instanceof ExtrinsicObjectType) {

                docExtrinsic = (ExtrinsicObjectType) identifiableType;

                SlotType1 slClinicalHash = findSlot(
                    AdapterConstants.XDSClinicallyUniqueHash,
                    docExtrinsic.getSlot());

                if (slClinicalHash != null) {
                    String sChkHash = slClinicalHash.getValueList().getValue().get(0);

                    log.debug("Check hash = " + sChkHash);

                    if (sChkHash.compareTo(sClinicallyUnqiueHash) == 0) {

                        //Ok weh have a Match !
                        match = docExtrinsic;
                        log.debug("Match found");
                        break;
                    }
                } else {
                    log.debug("Element did not have clinically unique hash value");
                }
            }
        }

        return match;
    }

    /**
     * Helper Function to map the requests slots into an Hashmap fot quick
     * usage.
     *
     * @param   slots  The List of Slots to Map
     *
     * @return  A Hash Map of the slots mapped by the slot names
     */
    private static HashMap<String, ValueListType> createSlotMap(
        List<SlotType1> slots) {
        HashMap<String, ValueListType> hm =
            new HashMap<String, ValueListType>();
        ListIterator<SlotType1> itr = slots.listIterator();

        while (itr.hasNext()) {
            SlotType1 slot = itr.next();
            hm.put(slot.getName(), slot.getValueList());
        }

        return hm;
    }

    /**
     * Helper function to find a Slot given it's name
     *
     * @param   slotName  The Name of the Slot to serach for
     * @param   slots     The Lost of slots to search
     *
     * @return  The slot or null ir not found
     */
    private static SlotType1 findSlot(String slotName, List<SlotType1> slots) {
        SlotType1 out = null;
        ListIterator<SlotType1> itr = slots.listIterator();

        while (itr.hasNext()) {
            SlotType1 slot = itr.next();

            if (slotName.compareTo(slot.getName()) == 0) {
                out = slot;

                break;
            }
        }

        return out;
    }

    /**
     * Fixup the repository id int the extrinsic - Id is added it needed
     *
     * @param  extrinsic     Document Exterinsic
     * @param  repositoryId  Repository Id
     */
    private static void fixRepositoryId(ExtrinsicObjectType extrinsic,
        String repositoryId) {
        SlotType1 repositoryIdSlot = null;

        //Find repositoryl id (if present)
        for (SlotType1 slot : extrinsic.getSlot()) {

            if (AdapterConstants.XDS_REPOSITORY_ID.equals(slot.getName())) {
                repositoryIdSlot = slot;

                break;
            }
        }

        //Create repository ID if not found
        if (repositoryIdSlot == null) {
            repositoryIdSlot = new SlotType1();
            addSlot(extrinsic.getSlot(), AdapterConstants.XDS_REPOSITORY_ID,
                new String[]{repositoryId});

            return;
        }

        //Ensure repository ID is correct
        ValueListType valList = new ValueListType();
        valList.getValue().add(repositoryId);
        repositoryIdSlot.setValueList(valList);
    }

    /*
     * Helper Function getRegistryObjectListForDocument
     *
     * Retrieves from the StoredObjectRequest the components for the Registtry
     * List for a document
     */
    RegistryObjectListType getRegistryObjectListForDocument(SubmitObjectsRequest sor, int k) {
        List<JAXBElement<? extends IdentifiableType>> objectList =
            sor.getRegistryObjectList().getIdentifiable();
        int objectListSize = objectList.size();

        oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory factory =
            new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();

        RegistryObjectListType registryObjectList = new RegistryObjectListType();

        int listIndex = k * 4;
        if (listIndex + 3 < objectListSize) {
            registryObjectList.getIdentifiable().add(factory.createExtrinsicObject((ExtrinsicObjectType) objectList.get(listIndex).getValue()));
            registryObjectList.getIdentifiable().add(factory.createRegistryPackage((RegistryPackageType) objectList.get(++listIndex).getValue()));
            registryObjectList.getIdentifiable().add(factory.createAssociation((AssociationType1) objectList.get(++listIndex).getValue()));
            registryObjectList.getIdentifiable().add(factory.createClassification((ClassificationType) objectList.get(++listIndex).getValue()));
            return registryObjectList;
        }
        return null;
    }

    /**
     * Helper function to Find the Extrinsic Object for the Dynamic Document.
     *
     * @param   dynamicDocument
     *
     * @return  The Extrinsic Object or Null
     */
    private static ExtrinsicObjectType findExtrinsicObject(SubmitObjectsRequest sor, int matchCount) {
        List<JAXBElement<? extends IdentifiableType>> objectList =
            sor.getRegistryObjectList().getIdentifiable();

        ExtrinsicObjectType matchingExtrinsic = null;

        matchCount++;
        int extrinsicNumber = 0;
        //Find extrinsic object
        for (JAXBElement<? extends IdentifiableType> object : objectList) {
            IdentifiableType identifiableType = object.getValue();

            if (identifiableType instanceof ExtrinsicObjectType) {
                extrinsicNumber++;
                if (matchCount == extrinsicNumber) {
                    matchingExtrinsic = (ExtrinsicObjectType) identifiableType;
                    break;
                }

            }
        }

        if (matchingExtrinsic == null) {
            log.warn(
                "Warning failed to find extrinsic object the dynamic document");
        }

        return matchingExtrinsic;
    }

    /**
     * Helper function to Find the Extrinsic Object for the Dynamic Document.
     *
     * @param   dynamicDocument
     *
     * @return  The Extrinsic Object or Null
     */
    private static ExtrinsicObjectType fndDynamicDocumentExtrinsicObject(
        gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestRequestType dynamicDocument) {
        List<JAXBElement<? extends IdentifiableType>> objectList =
            dynamicDocument.getProvideAndRegisterDocumentSetRequest().getSubmitObjectsRequest().getRegistryObjectList().getIdentifiable();

        ExtrinsicObjectType dynExtrinsic = null;

        //Find extrinsic object
        for (JAXBElement<? extends IdentifiableType> object : objectList) {
            IdentifiableType identifiableType = object.getValue();

            if (identifiableType instanceof ExtrinsicObjectType) {

                dynExtrinsic = (ExtrinsicObjectType) identifiableType;

                break;

            }
        }

        if (dynExtrinsic == null) {
            log.warn(
                "Warning failed to find extrinsic object the dynamic document");
        }

        return dynExtrinsic;
    }

    /**
     * Used to format the output when a new dynamic document has been created
     */
    private static void formatOutputForNewDynDoc(
        oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse out,
        ExtrinsicObjectType dynExtrinsic) {

        //TODO: Validate document assembler return data

        // We might need to Fixup the extrinsic - At this point I don't think so but who knows
        // In specific there are there elements I'm unsure of
        //     Is the Document Hash comming back correct from the Dynamic Assembler?
        //            Kim said see was correcting this
        //     Do the id fields in the contexts need to be fixed up

        //     Fix the RepositoryId
        String repositoryId = "";

        try {
            repositoryId = PropertyAccessor.getInstance().getProperty(
                AdapterConstants.REPOSITORY_PROPERTY_FILE,
                AdapterConstants.DYNAMIC_DOCUMENT_REPOSITORY_ID_PROP);
        } catch (PropertyAccessException e) {
            log.error("Error accessing property:" +
                AdapterConstants.DYNAMIC_DOCUMENT_REPOSITORY_ID_PROP +
                " in file:" + AdapterConstants.REPOSITORY_PROPERTY_FILE + ".",
                e);
        }

        log.debug("Got repository id value = " + repositoryId);

        fixRepositoryId(dynExtrinsic, repositoryId);

        log.debug("Fixed repository id");

        // Add the extrinsic to the output
        JAXBElement<ExtrinsicObjectType> metadata =
            new JAXBElement<ExtrinsicObjectType>(_ExtrinsicObject_QNAME,
            ExtrinsicObjectType.class, dynExtrinsic);

        //Given a match add it to the output
        out.getRegistryObjectList().getIdentifiable().add(metadata);

        //NOTE: A Very ungly work arround would be requery the document archive and find the new document information
        // This would entail a requery and searching for the match new document
    }

    /**
     * Helper function to get the Document Assembler Endpoint
     *
     * @return  The Document Assembler Endpoint or null if not mapped.
     */
    private static String getAssemblerEndpoint() {

        String adapterDocumentEndpoint = "";

        adapterDocumentEndpoint = AdapterConstants.ADAPTER_DOCUMENT_ASSEMBLY_ENDPOINT;

        return adapterDocumentEndpoint;
    }

    /**
     * Helper function to the Clinically unique hash from the extrinsic object
     * metadata
     *
     * @param   dynExtrinsic
     *
     * @return  The Hash as a String
     */
    private static String getClinicallyUniqueHash(
        ExtrinsicObjectType dynExtrinsic) {

        String sClinicallyUniqueHash = null;
        SlotType1 slClinicalHash = findSlot(
            AdapterConstants.XDSClinicallyUniqueHash,
            dynExtrinsic.getSlot());

        if (slClinicalHash != null) {
            sClinicallyUniqueHash = slClinicalHash.getValueList().getValue().get(0);
        } else {
            log.error(
                "DynamicDocument did not contatain a Clinically unique hash");
            throw new WebServiceException(
                "Unable to find Clinically unique Hash in the Dynamic Doument");

        }

        return sClinicallyUniqueHash;
    }

    /**
     * Helper function the gee the Document Manager Endpoint
     *
     * @return  The DocumentManager Endpoint or null if not mapped
     */
    private static String getDocMgrEndpoint() {

        String docMgrEndpoint = "";

        docMgrEndpoint = AdapterConstants.DOCUMENT_MANAGER_ENDPOINT;

        return docMgrEndpoint;
    }

    /**
     * Helper function to break a ValueListType into a List<String>
     *
     * @param   vlTypes  ValueListType to copy
     *
     * @return  The ValueListType as a List<String>
     */
    private static List<String> getDocumentTypes(ValueListType vlTypes) {

        LinkedList<String> types = new LinkedList<String>();

        Iterator<String> itr = vlTypes.getValue().iterator();

        while (itr.hasNext()) {
            String classCodes = itr.next();

            String normTypes = StringUtil.extractStringFromTokens(classCodes,
                "'()");

            if (normTypes.contains(",")) {
                //Ok We have a List of Types to break out

                //Spilt out list
                StringTokenizer st = new StringTokenizer(normTypes, ",");

                while (st.hasMoreTokens()) {
                    types.add(st.nextToken());
                }


            } else {

                //Just a simple entry add it to the Output
                types.add(normTypes);
            }
        }
        //We will normalize the string to a simple form - Latter it be reformatted for use in the document assembler
        return types;
    }

    /**
     * Helper function to serach a slot list and replace a
     *
     * @param  slots    The Slot list to search
     * @param  newSlot  The Replacement/Additional Slot
     */
    private static void replaceOrAddSlotValue(List<SlotType1> slots,
        SlotType1 newSlot) {
        boolean fnd = false;
        ListIterator<SlotType1> itr = slots.listIterator();

        while (itr.hasNext()) {
            SlotType1 slot = itr.next();

            if (slot.getName().compareTo(newSlot.getName()) == 0) {
                log.debug("Replacing ValueList for " + slot.getName());
                slot.setValueList(newSlot.getValueList());
                fnd = true;

                break;
            }
        }

        if (!fnd) {
            //We did not find the requested slot so we add to the list

            slots.add(newSlot);
        }
    }

    /**
     * Helper function to setup the output structure
     *
     * @return  Basic Response Shell
     */
    private static oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse initializeAdhocQueryResponse() {
        oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse out =
            new oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse();
        out.setStatus(statusSuccess);
        out.setRequestId(queryRequestId);

        RegistryObjectListType rolOut = new RegistryObjectListType();

        //rolOut.getIdentifiable().add(e);
        out.setRegistryObjectList(rolOut);

        return out;

    }

    /**
     * Function used to only keep the clinical hash and service times of the document that is being processed.
     */
    private static void updateAdditionalMetadata(gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestRequestType dynamicDocument, int index) {
        List<JAXBElement<? extends IdentifiableType>> objectList =
            dynamicDocument.getProvideAndRegisterDocumentSetRequest().getSubmitObjectsRequest().getRegistryObjectList().getIdentifiable();

        ExtrinsicObjectType dynExtrinsic = null;

        // Get the extrinsic object
        for (JAXBElement<? extends IdentifiableType> object : objectList) {
            IdentifiableType identifiableType = object.getValue();

            if (identifiableType instanceof ExtrinsicObjectType) {
                dynExtrinsic = (ExtrinsicObjectType) identifiableType;

                if (dynExtrinsic != null) {
                    SlotType1 s1ClinicalHash = findSlot(AdapterConstants.XDSClinicallyUniqueHash, dynExtrinsic.getSlot());
                    SlotType1 s1StartTime = findSlot(AdapterConstants.XDSStartTime, dynExtrinsic.getSlot());
                    SlotType1 s1StopTime = findSlot(AdapterConstants.XDSStopTime, dynExtrinsic.getSlot());

                    List<String> hashList = new ArrayList<String>();
                    List<String> startList = new ArrayList<String>();
                    List<String> stopList = new ArrayList<String>();

                    for (int i = 0; i < s1ClinicalHash.getValueList().getValue().size(); i++) {
                        hashList.add(s1ClinicalHash.getValueList().getValue().get(i));
                    }

                    s1ClinicalHash.getValueList().getValue().clear();
                    s1ClinicalHash.getValueList().getValue().add(hashList.get(index));

                    if (s1StartTime != null && s1StartTime.getValueList().getValue().size() > 0) {
                        for (int i = 0; i < s1StartTime.getValueList().getValue().size(); i++) {
                            startList.add(s1StartTime.getValueList().getValue().get(i));
                        }

                        s1StartTime.getValueList().getValue().clear();
                        s1StartTime.getValueList().getValue().add(startList.get(index));
                    }
                    if (s1StopTime != null && s1StopTime.getValueList().getValue().size() > 0) {
                        for (int i = 0; i < s1StopTime.getValueList().getValue().size(); i++) {
                            stopList.add(s1StopTime.getValueList().getValue().get(i));
                        }

                        s1StopTime.getValueList().getValue().clear();
                        s1StopTime.getValueList().getValue().add(stopList.get(index));
                    }
                }
                break;
            }
        }
    }
}
