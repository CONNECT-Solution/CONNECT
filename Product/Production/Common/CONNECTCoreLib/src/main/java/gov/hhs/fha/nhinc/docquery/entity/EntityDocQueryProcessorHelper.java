package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;

import java.math.BigInteger;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Helper methods for DQ Processing to create a new cumulativeResponse object
 * for a particular spec level and to transform an individualResponse object
 * from one spec to another
 * @author paul.eftis
 */
public class EntityDocQueryProcessorHelper{

    private static Log log = LogFactory.getLog(EntityDocQueryProcessorHelper.class);

    private static final String XDS_RESPONSE_STATUS_SUCCESS =
            "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success";


    /**
     * constructs a new EntityDocQueryOrchestratable_a0 object with
     * associated new cumulativeResponse
     * @param request
     * @return EntityDocQueryOrchestratable_a0
     */
    public static EntityDocQueryOrchestratable_a0 createNewCumulativeResponse_a0(
            EntityDocQueryOrchestratable request){

        EntityDocQueryOrchestratable_a0 cumulativeResponse = new EntityDocQueryOrchestratable_a0(
            null, null, null, null, request.getAssertion(),
            request.getServiceName(), request.getTarget(),
            request.getRequest());

        // create new cumulativeResponse object
        AdhocQueryResponse newResponse = new AdhocQueryResponse();
        newResponse.setStatus(XDS_RESPONSE_STATUS_SUCCESS);
        newResponse.setStartIndex(BigInteger.ZERO);
        newResponse.setTotalResultCount(BigInteger.ZERO);

        RegistryObjectListType regObj = new RegistryObjectListType();
        newResponse.setRegistryObjectList(regObj);
        SlotListType slots = new SlotListType();
        newResponse.setResponseSlotList(slots);
        RegistryErrorList regErr = new RegistryErrorList();
        newResponse.setRegistryErrorList(regErr);
        cumulativeResponse.setCumulativeResponse(newResponse);
        log.debug("EntityDocQueryProcessorHelper constructed initial a0 cumulativeResponse");
        return cumulativeResponse;
    }


    /**
     * constructs a new EntityDocQueryOrchestratable_a1 object with
     * associated new cumulativeResponse
     * @param request
     * @return EntityDocQueryOrchestratable_a1
     */
    public static EntityDocQueryOrchestratable_a1 createNewCumulativeResponse_a1(
            EntityDocQueryOrchestratable request){

        EntityDocQueryOrchestratable_a1 cumulativeResponse = new EntityDocQueryOrchestratable_a1(
            null, null, null, null, request.getAssertion(),
            request.getServiceName(), request.getTarget(),
            request.getRequest());

        // create new cumulativeResponse object
        AdhocQueryResponse newResponse = new AdhocQueryResponse();
        newResponse.setStatus(XDS_RESPONSE_STATUS_SUCCESS);
        newResponse.setStartIndex(BigInteger.ZERO);
        newResponse.setTotalResultCount(BigInteger.ZERO);

        RegistryObjectListType regObj = new RegistryObjectListType();
        newResponse.setRegistryObjectList(regObj);
        SlotListType slots = new SlotListType();
        newResponse.setResponseSlotList(slots);
        RegistryErrorList regErr = new RegistryErrorList();
        newResponse.setRegistryErrorList(regErr);
        cumulativeResponse.setCumulativeResponse(newResponse);
        log.debug("EntityDocQueryProcessorHelper constructed initial a1 cumulativeResponse");
        return cumulativeResponse;
    }


    /**
     * takes a response spec a1 and converts to response spec a0
     * @param original is spec a1
     * @return EntityDocQueryOrchestratable_a0 with transformed a0 response
     */
    public static EntityDocQueryOrchestratable_a0 transformResponse_ToA0(
            EntityDocQueryOrchestratable original){

        EntityDocQueryOrchestratable_a0 response_a0 = new EntityDocQueryOrchestratable_a0();
        // currently a0 is same as a1
        EntityDocQueryOrchestratable_a1 original_a1 = (EntityDocQueryOrchestratable_a1)original;
        response_a0.setResponse(original_a1.getResponse());
        return response_a0;
    }

    
    /**
     * takes a response spec a0 and converts to response spec a1
     * @param original is spec a0
     * @return EntityDocQueryOrchestratable_a1 with transformed a1 response
     */
    public static EntityDocQueryOrchestratable_a1 transformResponse_ToA1(
            EntityDocQueryOrchestratable original){

        EntityDocQueryOrchestratable_a1 response_a1 = new EntityDocQueryOrchestratable_a1();
        // currently a0 is same as a1
        EntityDocQueryOrchestratable_a0 original_a0 = (EntityDocQueryOrchestratable_a0)original;
        response_a1.setResponse(original_a0.getResponse());
        return response_a1;
    }
}
