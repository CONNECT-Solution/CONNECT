/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.audit;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import com.services.nhinc.schema.auditmessage.AuditMessageType.ActiveParticipant;
import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;
import com.services.nhinc.schema.auditmessage.CodedValueType;
import com.services.nhinc.schema.auditmessage.EventIdentificationType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import com.sun.corba.se.spi.ior.Identifiable;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.InternationalStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import org.hl7.v3.II;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType;
import java.io.ByteArrayOutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

/**
 *
 * @author dunnek
 */
public class XDRTransforms {
    private Log log = null;

    public XDRTransforms()
    {
        log = createLogger();
    }

    public LogEventRequestType transformRequestToAuditMsg(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion, String direction, String _interface)
    {
        LogEventRequestType result = null;
        AuditMessageType auditMsg = null;

        log.debug("Begin transformRequestToAuditMsg() -- NHIN");
        if(request == null)
        {
            log.error("Requst Object was null");
            return null;
        }
        if(assertion == null)
        {
            log.error("Assertion was null");
            return null;
        }

        //check to see that the required fields are not null
        boolean missingReqFields = areRequiredXDSfieldsNull(request, assertion);

        if (missingReqFields)
        {
            log.error("One or more required fields was missing");
            return null;
        }

        result = new LogEventRequestType();

        String patId = getIdentifiersFromRequest(request); //null values checked from the earlier call to areRequired201305fieldsNull() method
        auditMsg = new AuditMessageType();
        

        // Create EventIdentification
        CodedValueType eventID = getCodedValueTypeForXDR();

        EventIdentificationType oEventIdentificationType = getEventIdentificationType(eventID);
        auditMsg.setEventIdentification(oEventIdentificationType);


        ActiveParticipant participant = getActiveParticipant(assertion.getUserInfo());

        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = getParticipantObjectIdentificationType(patId);

        auditMsg.getActiveParticipant().add(participant);

        // Put the contents of the actual message into the Audit Log Message
        ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();

        marshalRequestMessage(baOutStrm, request);
        participantObject.setParticipantObjectQuery(baOutStrm.toByteArray());
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        /* Create the AuditSourceIdentifierType object */
        AuditSourceIdentificationType auditSource = getAuditSourceIdentificationType(assertion.getUserInfo());
        auditMsg.getAuditSourceIdentification().add(auditSource);

        result.setAuditMessage(auditMsg);
        result.setDirection(direction);
        result.setInterface(_interface);

        log.debug("end transformRequestToAuditMsg() -- NHIN");
        return result;
    }
    public LogEventRequestType transformRequestToAuditMsg(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion, String direction, String _interface)
    {
        LogEventRequestType result = null;
        AuditMessageType auditMsg = null;

        if(request == null)
        {
            log.error("Requst Object was null");
            return null;
        }
        if(assertion == null)
        {
            log.error("Assertion was null");
            return null;
        }

        //check to see that the required fields are not null
        boolean missingReqFields = areRequiredXDSfieldsNull(request.getProvideAndRegisterDocumentSetRequest(), assertion);

        if (missingReqFields)
        {
            log.error("One or more required fields was missing");
            return null;
        }

        result = new LogEventRequestType();

        String patId = getIdentifiersFromRequest(request.getProvideAndRegisterDocumentSetRequest()); //null values checked from the earlier call to areRequired201305fieldsNull() method
        auditMsg = new AuditMessageType();


        // Create EventIdentification
        CodedValueType eventID = getCodedValueTypeForXDRProxy();

        EventIdentificationType oEventIdentificationType = getEventIdentificationType(eventID);
        auditMsg.setEventIdentification(oEventIdentificationType);


        ActiveParticipant participant = getActiveParticipant(assertion.getUserInfo());

        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = getParticipantObjectIdentificationType(patId);

        auditMsg.getActiveParticipant().add(participant);

        // Put the contents of the actual message into the Audit Log Message
        ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();

        marshalRequestMessage(baOutStrm, request);
        participantObject.setParticipantObjectQuery(baOutStrm.toByteArray());
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        /* Create the AuditSourceIdentifierType object */
        AuditSourceIdentificationType auditSource = getAuditSourceIdentificationType(assertion.getUserInfo());
        auditMsg.getAuditSourceIdentification().add(auditSource);

        result.setAuditMessage(auditMsg);
        result.setDirection(direction);
        result.setInterface(_interface);

        return result;
    }
    public LogEventRequestType transformRequestToAuditMsg(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion, String direction, String _interface)
    {
        LogEventRequestType result = null;
        AuditMessageType auditMsg = null;

        log.debug("Begin transformRequestToAuditMsg() -- Entity");

        if(request == null)
        {
            log.error("Requst Object was null");
            return null;
        }
        if(assertion == null)
        {
            log.error("Assertion was null");
            return null;
        }

        //check to see that the required fields are not null
        boolean missingReqFields = areRequiredXDSfieldsNull(request.getProvideAndRegisterDocumentSetRequest(), assertion);

        if (missingReqFields)
        {
            log.error("One or more required fields was missing");
            return null;
        }

        result = new LogEventRequestType();

        String patId = getIdentifiersFromRequest(request.getProvideAndRegisterDocumentSetRequest()); //null values checked from the earlier call to areRequired201305fieldsNull() method
        auditMsg = new AuditMessageType();


        // Create EventIdentification
        CodedValueType eventID = getCodedValueTypeForXDREntity();

        EventIdentificationType oEventIdentificationType = getEventIdentificationType(eventID);
        auditMsg.setEventIdentification(oEventIdentificationType);


        ActiveParticipant participant = getActiveParticipant(assertion.getUserInfo());

        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = getParticipantObjectIdentificationType(patId);

        auditMsg.getActiveParticipant().add(participant);

        // Put the contents of the actual message into the Audit Log Message
        ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();

        marshalRequestMessage(baOutStrm, request);
        participantObject.setParticipantObjectQuery(baOutStrm.toByteArray());
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        /* Create the AuditSourceIdentifierType object */
        AuditSourceIdentificationType auditSource = getAuditSourceIdentificationType(assertion.getUserInfo());
        auditMsg.getAuditSourceIdentification().add(auditSource);

        result.setAuditMessage(auditMsg);
        result.setDirection(direction);
        result.setInterface(_interface);

        return result;
    }

    public LogEventRequestType transformRequestToAuditMsg(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request, AssertionType assertion, String direction, String _interface)
    {
        LogEventRequestType result = null;
        AuditMessageType auditMsg = null;

        if(request == null)
        {
            log.error("Requst Object was null");
            return null;
        }
        if(assertion == null)
        {
            log.error("Assertion was null");
            return null;
        }

        //check to see that the required fields are not null
        boolean missingReqFields = areRequiredResponseFieldsNull(request.getRegistryResponse(), assertion);

        if (missingReqFields)
        {
            log.error("One or more required fields was missing");
            return null;
        }

        result = new LogEventRequestType();

        auditMsg = new AuditMessageType();
        // Create EventIdentification
        CodedValueType eventID = getCodedValueTypeForXDRResponse();

        EventIdentificationType oEventIdentificationType = getEventIdentificationType(eventID);
        auditMsg.setEventIdentification(oEventIdentificationType);

        ActiveParticipant participant = getActiveParticipant(assertion.getUserInfo());

        auditMsg.getActiveParticipant().add(participant);
        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = getParticipantObjectIdentificationType("");

        // Put the contents of the actual message into the Audit Log Message
        ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();

        marshalRequestMessage(baOutStrm, request);
        participantObject.setParticipantObjectQuery(baOutStrm.toByteArray());
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        /* Create the AuditSourceIdentifierType object */
        AuditSourceIdentificationType auditSource = getAuditSourceIdentificationType(assertion.getUserInfo());
        auditMsg.getAuditSourceIdentification().add(auditSource);

        result.setAuditMessage(auditMsg);
        result.setDirection(direction);
        result.setInterface(_interface);

        return result;
    }

    public LogEventRequestType transformRequestToAuditMsg(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request, AssertionType assertion, String direction, String _interface)
    {
        LogEventRequestType result = null;
        AuditMessageType auditMsg = null;

        if(request == null)
        {
            log.error("Requst Object was null");
            return null;
        }
        if(assertion == null)
        {
            log.error("Assertion was null");
            return null;
        }

        //check to see that the required fields are not null
        boolean missingReqFields = areRequiredResponseFieldsNull(request.getRegistryResponse(), assertion);

        if (missingReqFields)
        {
            log.error("One or more required fields was missing");
            return null;
        }

        result = new LogEventRequestType();

        auditMsg = new AuditMessageType();
        // Create EventIdentification
        CodedValueType eventID = getCodedValueTypeForXDRResponse();

        EventIdentificationType oEventIdentificationType = getEventIdentificationType(eventID);
        auditMsg.setEventIdentification(oEventIdentificationType);

        ActiveParticipant participant = getActiveParticipant(assertion.getUserInfo());

        auditMsg.getActiveParticipant().add(participant);
        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = getParticipantObjectIdentificationType("");

        // Put the contents of the actual message into the Audit Log Message
        ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();

        marshalRequestMessage(baOutStrm, request);
        participantObject.setParticipantObjectQuery(baOutStrm.toByteArray());
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        /* Create the AuditSourceIdentifierType object */
        AuditSourceIdentificationType auditSource = getAuditSourceIdentificationType(assertion.getUserInfo());
        auditMsg.getAuditSourceIdentification().add(auditSource);

        result.setAuditMessage(auditMsg);
        result.setDirection(direction);
        result.setInterface(_interface);

        return result;
    }

    public LogEventRequestType transformResponseToAuditMsg(RegistryResponseType response, AssertionType assertion, String direction, String _interface)
    {
        LogEventRequestType result = null;
        AuditMessageType auditMsg = null;

        if(response == null)
        {
            log.error("Requst Object was null");
            return null;
        }
        if(assertion == null)
        {
            log.error("Assertion was null");
            return null;
        }

        //check to see that the required fields are not null
        boolean missingReqFields = areRequiredResponseFieldsNull(response, assertion);

        if (missingReqFields)
        {
            log.error("One or more required fields was missing");
            return null;
        }

        result = new LogEventRequestType();

        auditMsg = new AuditMessageType();
        // Create EventIdentification
        CodedValueType eventID = getCodedValueTypeForXDRResponse();

        EventIdentificationType oEventIdentificationType = getEventIdentificationType(eventID);
        auditMsg.setEventIdentification(oEventIdentificationType);

        ActiveParticipant participant = getActiveParticipant(assertion.getUserInfo());

        auditMsg.getActiveParticipant().add(participant);
        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = getParticipantObjectIdentificationType("");

        // Put the contents of the actual message into the Audit Log Message
        ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();

        marshalResponseMessage(baOutStrm, response);
        participantObject.setParticipantObjectQuery(baOutStrm.toByteArray());
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        /* Create the AuditSourceIdentifierType object */
        AuditSourceIdentificationType auditSource = getAuditSourceIdentificationType(assertion.getUserInfo());
        auditMsg.getAuditSourceIdentification().add(auditSource);

        result.setAuditMessage(auditMsg);
        result.setDirection(direction);
        result.setInterface(_interface);
        
        return result;

    }
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
    protected boolean areRequiredXDSfieldsNull(ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion)
    {
        try
        {
            
            if(assertion == null)
            {
                log.error("Assertion object is null");
                return true;
            }
            if(body == null)
            {
                log.error("ProvideAndRegisterDocumentSetRequestType object is null");
                return true;
            }

            if (areRequiredUserTypeFieldsNull(assertion))
            {
                log.error("One of more UserInfo fields from the Assertion object were null.");
                return true;
            }

            if(body.getSubmitObjectsRequest() == null)
            {
                log.error("No Registry Object");
                return true;
            }


            if (body.getSubmitObjectsRequest().getId().isEmpty())
            {
                log.error("SubmitObjectsRequest has no id");
                return true;
            }
            if(body.getSubmitObjectsRequest().getRegistryObjectList() == null)
            {
                log.error("No Registry Objects");
                return true;
            }
            if(body.getSubmitObjectsRequest().getRegistryObjectList().getIdentifiable().isEmpty())
            {
                log.error("No Identifiables on Registry Object");
                return true;
            }
            return false;
        }
        catch (Exception ex)
        {
            log.error("Encountered Error: " + ex.getMessage());
            return true;
        }
    }

    protected boolean areRequiredResponseFieldsNull(RegistryResponseType response, AssertionType assertion)
    {
         if(assertion == null)
        {
            log.error("Assertion object is null");
            return true;
        }
        if(response == null)
        {
            log.error("RegistryResponseType object is null");
            return true;
        }
        if (areRequiredUserTypeFieldsNull(assertion))
        {
            log.error("One of more UserInfo fields from the Assertion object were null.");
            return true;
        }
        if(response.getStatus() == null)
        {
            log.error("Response does not contain a status");
            return true;
        }
        if(response.getStatus().isEmpty())
        {
            log.error("Response does not contain a status");
            return true;
        }

         return false;
    }

    protected boolean areRequiredUserTypeFieldsNull(AssertionType oAssertion)
    {
        boolean bReturnVal = false;

        if ((oAssertion != null) &&
             (oAssertion.getUserInfo() != null))
        {
            if (oAssertion.getUserInfo().getUserName() != null)
            {
                log.debug("Incomming request.getAssertion.getUserInfo.getUserName: " + oAssertion.getUserInfo().getUserName());
            }
            else
            {
                log.error("Incomming request.getAssertion.getUserInfo.getUserName was null.");
                bReturnVal = true;
                return true;
            }

            if (oAssertion.getUserInfo().getOrg().getHomeCommunityId() != null)
            {
                log.debug("Incomming request.getAssertion.getUserInfo.getOrg().getHomeCommunityId(): " + oAssertion.getUserInfo().getOrg().getHomeCommunityId());
            }
            else
            {
                log.error("Incomming request.getAssertion.getUserInfo.getOrg().getHomeCommunityId() was null.");
                bReturnVal = true;
                return true;
            }

            if (oAssertion.getUserInfo().getOrg().getName() != null)
            {
                log.debug("Incomming request.getAssertion.getUserInfo.getOrg().getName() or Community Name: " + oAssertion.getUserInfo().getOrg().getName());
            }
            else
            {
                log.error("Incomming request.getAssertion.getUserInfo.getOrg().getName() or Community Name was null.");
                bReturnVal = true;
                return true;
            }
        }
        else
        {
            log.error("The UserType object or request assertion object containing the assertion user info was null.");
            bReturnVal = true;
            return true;
        } //else continue

        return bReturnVal;
    }
    private ParticipantObjectIdentificationType getParticipantObjectIdentificationType(String sPatientId) {
        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = AuditDataTransformHelper.createParticipantObjectIdentification(sPatientId);
        return participantObject;
    }
    private ActiveParticipant getActiveParticipant(UserType oUserInfo) {
        // Create Active Participant Section
        //create a method to call the AuditDataTransformHelper - one expectation
        AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper.createActiveParticipantFromUser(oUserInfo, true);
        return participant;
    }
    private String getIdentifiersFromRequest(ProvideAndRegisterDocumentSetRequestType request)
    {
        String result = "";

        if(request == null)
        {
            log.error(("Incoming ProvideAndRegisterDocumentSetRequestType was null"));
            return null;
        }

        if(request.getSubmitObjectsRequest() == null)
        
        {
            log.error(("Incoming ProvideAndRegisterDocumentSetRequestType metadata was null"));
            return null;
        }

        System.out.println(request.getSubmitObjectsRequest().getRegistryObjectList().getIdentifiable());
        RegistryObjectListType object = request.getSubmitObjectsRequest().getRegistryObjectList();

        for(int x= 0; x<object.getIdentifiable().size();x++)
        {
            System.out.println(object.getIdentifiable().get(x).getName());
            
            if(object.getIdentifiable().get(x).getDeclaredType().equals(RegistryPackageType.class))
            {
                RegistryPackageType registryPackage = (RegistryPackageType) object.getIdentifiable().get(x).getValue();

                System.out.println(registryPackage.getSlot().size());

                for(int y=0; y< registryPackage.getExternalIdentifier().size();y++)
                {                    
                    String test = registryPackage.getExternalIdentifier().get(y).getName().getLocalizedString().get(0).getValue();
                    if(test.equals("XDSSubmissionSet.patientId"))
                    {
                        result = registryPackage.getExternalIdentifier().get(y).getValue();
                    }

                    
                }

                
            }
        }
        

        return result;
    }
    private String getCompositePatientId(String sCommunityId, String sPatientId) {
        sPatientId = AuditDataTransformHelper.createCompositePatientId(sCommunityId, sPatientId);
        return sPatientId;
    }
    protected void marshalRequestMessage(ByteArrayOutputStream baOutStrm, ProvideAndRegisterDocumentSetRequestType request) throws RuntimeException {
        // Put the contents of the actual message into the Audit Log Message
        log.debug("Begin marshalRequestMessage() -- NHIN Interface");
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("ihe.iti.xds_b._2007");
            Marshaller marshaller = jc.createMarshaller();
            baOutStrm.reset();

            javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:ihe:iti:xds-b:2007", "ProvideAndRegisterDocumentSetRequest");
            JAXBElement<ProvideAndRegisterDocumentSetRequestType> element;
            
//            element = new JAXBElement<ProvideAndRegisterDocumentSetRequestType>(xmlqname, ProvideAndRegisterDocumentSetRequestType.class, request);

            ihe.iti.xds_b._2007.ObjectFactory factory = new ihe.iti.xds_b._2007.ObjectFactory();
            element = factory.createProvideAndRegisterDocumentSetRequest(request);


            marshaller.marshal(element, baOutStrm);
            log.debug("Done marshalling the ProvideAndRegisterDocumentSetRequestType  message.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
     protected void marshalRequestMessage(ByteArrayOutputStream baOutStrm, gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request) throws RuntimeException {
        // Put the contents of the actual message into the Audit Log Message
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommonproxy");
            Marshaller marshaller = jc.createMarshaller();
            baOutStrm.reset();

            javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:ihe:iti:xds-b:2007", "RespondingGatewayProvideAndRegisterDocumentSetSecuredRequest");
            JAXBElement<gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType> element;

            element = new JAXBElement<gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType>(xmlqname, gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType.class, request);


            marshaller.marshal(element, baOutStrm);
            log.debug("Done marshalling the message.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
     protected void marshalRequestMessage(ByteArrayOutputStream baOutStrm, gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request) throws RuntimeException {
        // Put the contents of the actual message into the Audit Log Message
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommonproxy");
            Marshaller marshaller = jc.createMarshaller();
            baOutStrm.reset();

            javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:gov:hhs:fha:nhinc:common:nhinccommonentity", "RespondingGatewayProvideAndRegisterDocumentSetSecuredRequest");
            JAXBElement<gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType> element;

            element = new JAXBElement<gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType>(xmlqname, gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType.class, request);


            marshaller.marshal(element, baOutStrm);
            log.debug("Done marshalling the message.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    protected void marshalRequestMessage(ByteArrayOutputStream baOutStrm, gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request) throws RuntimeException
    {
        // Put the contents of the actual message into the Audit Log Message
        try
        {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommonentity");
            Marshaller marshaller = jc.createMarshaller();
            baOutStrm.reset();

            javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:gov:hhs:fha:nhinc:common:nhinccommonentity", "RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType");
            JAXBElement<gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType> element;

            element = new JAXBElement<gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType>(xmlqname, gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType.class, request);

            marshaller.marshal(element, baOutStrm);
            log.debug("Done marshalling the RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType message.");
        } 
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    protected void marshalRequestMessage(ByteArrayOutputStream baOutStrm, gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request) throws RuntimeException
    {
        // Put the contents of the actual message into the Audit Log Message
        try
        {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommonproxy");
            Marshaller marshaller = jc.createMarshaller();
            baOutStrm.reset();

            javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:gov:hhs:fha:nhinc:common:nhinccommonproxy", "RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType");
            JAXBElement<gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType> element;

            element = new JAXBElement<gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType>(xmlqname, gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType.class, request);

            marshaller.marshal(element, baOutStrm);
            log.debug("Done marshalling the RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType message.");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    protected void marshalResponseMessage(ByteArrayOutputStream baOutStrm, RegistryResponseType response) throws RuntimeException {
        // Put the contents of the actual message into the Audit Log Message
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("oasis.names.tc.ebxml_regrep.xsd.rs._3");
            Marshaller marshaller = jc.createMarshaller();
            baOutStrm.reset();

            javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0", "RegistryResponse");
            JAXBElement<RegistryResponseType> element;

            element = new JAXBElement<RegistryResponseType>(xmlqname, RegistryResponseType.class, response);


            marshaller.marshal(element, baOutStrm);
            log.debug("Done marshalling the message.");
        } catch (Exception e) {
            log.error("Exception while marshalling Acknowledgement", e);
            throw new RuntimeException();
        }
    }

    protected void marshalAcknowledgement(ByteArrayOutputStream baOutStrm, ihe.iti.xdr._2007.AcknowledgementType acknowledgement) throws RuntimeException {
        // Put the contents of the actual message into the Audit Log Message
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("ihe.iti.xdr._2007");
            Marshaller marshaller = jc.createMarshaller();
            baOutStrm.reset();

            javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:ihe:iti:xdr:2007", "Acknowledgement");
            JAXBElement<ihe.iti.xdr._2007.AcknowledgementType> element;

            element = new JAXBElement<ihe.iti.xdr._2007.AcknowledgementType>(xmlqname, ihe.iti.xdr._2007.AcknowledgementType.class, acknowledgement);


            marshaller.marshal(element, baOutStrm);
            log.debug("Done marshalling the message.");
        } catch (Exception e) {
            log.error("Exception while marshalling Acknowledgement", e);
            throw new RuntimeException();
        }
    }

    private AuditSourceIdentificationType getAuditSourceIdentificationType(UserType userInfo)
    {
        AuditSourceIdentificationType result;

        String communityId = "";
        String communityName = "";
        if (userInfo != null && userInfo.getOrg() != null)
        {
            if (userInfo.getOrg().getHomeCommunityId() != null)
            {
                communityId = userInfo.getOrg().getHomeCommunityId();
            }
            if (userInfo.getOrg().getName() != null)
            {
                communityName = userInfo.getOrg().getName();
            }
        }
        result = AuditDataTransformHelper.createAuditSourceIdentification(communityId, communityName);
        return result;
    }
    private CodedValueType getCodedValueTypeForXDR() {
        // Create EventIdentification
        CodedValueType eventID = AuditDataTransformHelper
                .createEventId(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_XDR,
                               AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_XDR,
                               AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_XDR,
                               AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_XDR);
        return eventID;
    }
    private CodedValueType getCodedValueTypeForXDRProxy() {
        // Create EventIdentification
        CodedValueType eventID = AuditDataTransformHelper
                .createEventId(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_XDR,
                               AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_XDR_PROXY,
                               AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_XDR,
                               AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_XDR_PROXY);
        return eventID;
    }
    private CodedValueType getCodedValueTypeForXDREntity() {
        // Create EventIdentification
        CodedValueType eventID = AuditDataTransformHelper
                .createEventId(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_XDR,
                               AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_XDR_ENTITY,
                               AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_XDR,
                               AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_XDR_ENTITY);
        return eventID;
    }

    private CodedValueType getCodedValueTypeForXDRResponse() {
        // Create EventIdentification
        CodedValueType eventID = AuditDataTransformHelper
                .createEventId(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_XDR,
                               AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_XDRRESPONSE,
                               AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_XDR,
                               AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_XDRRESPONSE);
        return eventID;
    }

    private CodedValueType getCodedValueTypeForXDRRequestAcknowledgement() {
        // Create EventIdentification
        CodedValueType eventID = AuditDataTransformHelper
                .createEventId(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_XDRREQUEST,
                               AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_XDRREQUEST,
                               AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_XDRREQUEST,
                               AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_XDRREQUEST);
        return eventID;
    }

    private CodedValueType getCodedValueTypeForXDRResponseAcknowledgement() {
        // Create EventIdentification
        CodedValueType eventID = AuditDataTransformHelper
                .createEventId(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_XDRRESPONSE,
                               AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_XDRRESPONSE,
                               AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_XDRRESPONSE,
                               AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_XDRRESPONSE);
        return eventID;
    }

    private EventIdentificationType getEventIdentificationType(CodedValueType eventID) {
        EventIdentificationType oEventIdentificationType =
                AuditDataTransformHelper.createEventIdentification(AuditDataTransformConstants.EVENT_ACTION_CODE_READ,
                                                                   AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS,
                                                                   eventID);
        return oEventIdentificationType;
    }


    /**
     * 
     */
    public LogEventRequestType transformAcknowledgementToAuditMsg(ihe.iti.xdr._2007.AcknowledgementType acknowledgement, AssertionType assertion, String direction, String _interface, String action)
    {
        LogEventRequestType result = null;
        AuditMessageType auditMsg = null;

        if(acknowledgement == null)
        {
            log.error("Acknowledgement is null");
            return null;
        }
        
        if(assertion == null)
        {
            log.error("Assertion is null");
            return null;
        }

        //check to see that the required fields are not null
        boolean missingReqFields = areRequiredAcknowledgementFieldsNull(acknowledgement, assertion);

        if (missingReqFields)
        {
            log.error("One or more required fields was missing");
            return null;
        }

        result = new LogEventRequestType();

        auditMsg = new AuditMessageType();
        // Create EventIdentification
        CodedValueType eventID = null;

        if (NhincConstants.XDR_REQUEST_ACTION.equalsIgnoreCase(action)){
            eventID = getCodedValueTypeForXDRRequestAcknowledgement();
        }else if (NhincConstants.XDR_RESPONSE_ACTION.equalsIgnoreCase(action)){
            eventID = getCodedValueTypeForXDRResponseAcknowledgement();
        }

        EventIdentificationType oEventIdentificationType = getEventIdentificationType(eventID);
        auditMsg.setEventIdentification(oEventIdentificationType);

        ActiveParticipant participant = getActiveParticipant(assertion.getUserInfo());

        auditMsg.getActiveParticipant().add(participant);
        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = getParticipantObjectIdentificationType("");

        // Put the contents of the actual message into the Audit Log Message
        ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();

        marshalAcknowledgement(baOutStrm, acknowledgement);
        participantObject.setParticipantObjectQuery(baOutStrm.toByteArray());
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        /* Create the AuditSourceIdentifierType object */
        AuditSourceIdentificationType auditSource = getAuditSourceIdentificationType(assertion.getUserInfo());
        auditMsg.getAuditSourceIdentification().add(auditSource);

        result.setAuditMessage(auditMsg);
        result.setDirection(direction);
        result.setInterface(_interface);

        return result;

    }

    /**
     *
     * @param acknowledgement
     * @param assertion
     * @return
     */
    protected boolean areRequiredAcknowledgementFieldsNull(ihe.iti.xdr._2007.AcknowledgementType acknowledgement, AssertionType assertion)
    {
        if(assertion == null)
        {
            log.error("Assertion object is null");
            return true;
        }
        if(acknowledgement == null)
        {
            log.error("Acknowledge object is null");
            return true;
        }
        if (areRequiredUserTypeFieldsNull(assertion))
        {
            log.error("One of more UserInfo fields from the Assertion object were null.");
            return true;
        }
        if(acknowledgement.getMessage() == null || "".equalsIgnoreCase(acknowledgement.getMessage()))
        {
            log.error("Acknowledgement does not contain a message");
            return true;
        }

         return false;
    }

}
