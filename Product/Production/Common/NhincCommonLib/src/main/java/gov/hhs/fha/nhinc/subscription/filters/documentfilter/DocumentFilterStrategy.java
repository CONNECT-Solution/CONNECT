/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.subscription.filters.documentfilter;

import gov.hhs.fha.nhinc.adapterdocumentregistry.proxy.AdapterDocumentRegistryProxy;
import gov.hhs.fha.nhinc.adapterdocumentregistry.proxy.AdapterDocumentRegistryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.AdhocQueryMarshaller;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.RetrieveDocumentSetRequestMarshaller;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.util.format.DocumentClassCodeParser;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import gov.hhs.fha.nhinc.xmlCommon.XpathHelper;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import java.util.List;
import javax.xml.xpath.XPathExpressionException;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.query._3.ResponseOptionType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author rayj
 */
public class DocumentFilterStrategy {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(DocumentFilterStrategy.class);

    public static boolean IsDocumentCentric(Node subscriptionTopicExpression) {
        //find better way to handle this, maybe just share code
        boolean result = false;
        String value = XmlUtility.getNodeValue(subscriptionTopicExpression);
        if (NullChecker.isNotNullish(value)) {
            if (value.contentEquals("nhinc:document")) {
                result = true;
            }
        }
        return result;
    }

    public boolean MeetsCriteria(Element subscribeElement, Element notificationMessageElement) {
        //todo: modify to only to the retrieve of metadata one time
        
        if (subscribeElement == null) {
            return false;
        }
        DocumentRequest documentIdentifier = extractDocumentIdentifiersAsObject(notificationMessageElement);
        ExtrinsicObjectType documentMetadata = null;
        try {
            documentMetadata = getDocumentMetadata(documentIdentifier);
        } catch (Exception ex) {
            log.error("Failed to find document metadata", ex);
        }

        boolean match ;
        if (documentMetadata != null) {
            //match=adhoc query and doc meta data are match
            AdhocQueryType adhocQuery = extractAdhocQueryAsObject(subscribeElement);
            match= meetsCriteria(documentMetadata, adhocQuery);
        } else {
            log.warn("Document meta data could not be accessed, so assumed to not be a match");
            match=false;
        }
        return match;
    }

    private boolean meetsCriteria(ExtrinsicObjectType documentMetadata, AdhocQueryType adhocQuery) {
        boolean match = true;

        for (SlotType1 slot : adhocQuery.getSlot()) {
            boolean slotMatch = true;
            log.debug("Checking if the slot [name='" + slot.getName() + "'] from the subscribe is meet by the document metadata from notify");
            String slotValue = null;
            if (slot.getValueList().getValue().size() == 0) {
                log.warn("there are no slot values - assume that this will not result in no match");
            } else if (slot.getValueList().getValue().size() > 1) {
                log.warn("there are multiple slot values, currently unsupported.  Will use the first value");
                slotValue = slot.getValueList().getValue().get(0);
            } else {
                slotValue = slot.getValueList().getValue().get(0);
            }

            if (NullChecker.isNotNullish(slotValue)) {
                log.debug("[slot name='" + slot.getName() + "'][slot value='" + slotValue + "']");

                if (slot.getName().contentEquals(Constants.PatientIdSlotName)) {
                    QualifiedSubjectIdentifierType patientId = DocumentMetadataHelper.getPatient(documentMetadata);
                    log.debug("extracted patient id from the metadata " + patientId.getSubjectIdentifier() + ";" + patientId.getAssigningAuthorityIdentifier());
                    slotMatch = patientId.getSubjectIdentifier().contentEquals(PatientIdFormatUtil.parsePatientId(slotValue)) && patientId.getAssigningAuthorityIdentifier().contentEquals(PatientIdFormatUtil.parseCommunityId(slotValue));
                } else if (slot.getName().contentEquals(Constants.DocumentClassCodeSlotName)) {
                    String metadataValue = DocumentMetadataHelper.getDocumentClassCode(documentMetadata);
                    log.debug("correspond value(s) from metadata=" + metadataValue);

                    log.debug("splitting slot value into individual parts");
                    List<String> slotValues = DocumentClassCodeParser.parseFormattedParameter(slotValue);
                    for (String value : slotValues) {
                        log.debug("checking document class code match. [" + value + "]==[" + metadataValue + "]");
                        slotMatch = slotMatch && value.contentEquals(metadataValue);
                        log.debug("slotMatch=" + slotMatch);
                    }
                } else {
                    log.warn("the current implementation of the document filter does not support this slot type - assume this does not affect the filtering");
                    slotMatch = true;
                }
                log.debug("slot match?=" + slotMatch + "[slot name='" + slot.getName() + "'][slot value='" + slotValue + "']");
            }
            match = match && slotMatch;
        }

        log.debug("match?=" + match);
        return match;
    }

    private Element extractAdhocQueryAsElement(Element element) {
        String xpathQuery = "//*[local-name()='AdhocQuery']";
        Element adhocQueryElement = null;
        try {
            adhocQueryElement = (Element) XpathHelper.performXpathQuery(element, xpathQuery);
        } catch (XPathExpressionException ex) {
            log.error("failed to extract adhoc query due to xpath exception");
            adhocQueryElement = null;
        }
        return adhocQueryElement;
    }

    private AdhocQueryType extractAdhocQueryAsObject(Element element) {
        log.info("extract adhoc query set from:" + XmlUtility.serializeElementIgnoreFaults(element));
        Element adhocQueryElement = extractAdhocQueryAsElement(element);
        log.info("extracted adhocQueryElement:" + XmlUtility.serializeElementIgnoreFaults(adhocQueryElement));

        AdhocQueryMarshaller marshaller = new AdhocQueryMarshaller();
        AdhocQueryType adhocQuery = marshaller.unmarshal(adhocQueryElement);
        return adhocQuery;
    }

    private Element extractRetrieveDocumentSetRequestAsElement(Element element) {
        String xpathQuery = "//*[local-name()='RetrieveDocumentSetRequest']";
        Element documentQueryElement = null;
        try {
            documentQueryElement = (Element) XpathHelper.performXpathQuery(element, xpathQuery);
        } catch (XPathExpressionException ex) {
            log.error("failed to extract document query due to xpath exception");
            documentQueryElement = null;
        }
        return documentQueryElement;
    }

    private DocumentRequest extractDocumentIdentifiersAsObject(Element element) {
        log.info("extract retrieve document set from:" + XmlUtility.serializeElementIgnoreFaults(element));
        Element retrieveDocumentSetRequestElement = extractRetrieveDocumentSetRequestAsElement(element);
        log.info("extracted retrieveDocumentSetRequestElement:" + XmlUtility.serializeElementIgnoreFaults(retrieveDocumentSetRequestElement));

        RetrieveDocumentSetRequestMarshaller marshaller = new RetrieveDocumentSetRequestMarshaller();
        RetrieveDocumentSetRequestType retrieveDocumentSetRequestType = marshaller.unmarshal(retrieveDocumentSetRequestElement);
        DocumentRequest documentIdentifier = retrieveDocumentSetRequestType.getDocumentRequest().get(0);
        return documentIdentifier;
    }

    private ExtrinsicObjectType getDocumentMetadata(DocumentRequest documentIdentifier) throws Exception {
        AdapterDocumentRegistryProxyObjectFactory factory = new gov.hhs.fha.nhinc.adapterdocumentregistry.proxy.AdapterDocumentRegistryProxyObjectFactory();
        AdapterDocumentRegistryProxy proxy = factory.getAdapterDocumentRegistryProxy();

        AssertionType assertion = null;
        NhinTargetSystemType target = null;
        AdhocQueryRequest adhocQuery = buildAdhocQueryToQueryByDocumentIdentifiers(documentIdentifier);
        AdhocQueryResponse response = proxy.queryForDocument(adhocQuery, assertion, target);

        //todo: a little defensive programming needed here
        ExtrinsicObjectType metadata = (ExtrinsicObjectType) response.getRegistryObjectList().getIdentifiable().get(0).getValue();
        return metadata;
    }

    private AdhocQueryRequest buildAdhocQueryToQueryByDocumentIdentifiers(DocumentRequest documentIdentifier) {
        AdhocQueryRequest request = new AdhocQueryRequest();
        ResponseOptionType responseOption = new ResponseOptionType();
        responseOption.setReturnType("LeafClass");
        responseOption.setReturnComposedObjects(true);
        request.setResponseOption(responseOption);

        AdhocQueryType adhocQuery = new AdhocQueryType();
        SlotType1 slot = new SlotType1();
        slot.setName("$XDSDocumentEntryUniqueId");
        ValueListType slotValueList = new ValueListType();
        String slotValue = "('" + documentIdentifier.getDocumentUniqueId() + "')";
        slotValueList.getValue().add(slotValue);
        slot.setValueList(slotValueList);
        adhocQuery.getSlot().add(slot);

        request.setAdhocQuery(adhocQuery);
        return request;
    }
}
