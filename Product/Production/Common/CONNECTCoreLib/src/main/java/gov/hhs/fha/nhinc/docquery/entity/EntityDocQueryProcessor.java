package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.gateway.executorservice.ExecutorServiceHelper;
import gov.hhs.fha.nhinc.orchestration.NhinResponseProcessor;
import gov.hhs.fha.nhinc.orchestration.EntityOrchestratableMessage;
import gov.hhs.fha.nhinc.orchestration.EntityOrchestratable;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

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
 * Class handles all processing of responses for DocQuery
 * for all the different spec combinations:
 * 1.  CumulativeResponse is spec a0 and an individual Response is spec a0
 * 2.  CumulativeResponse is spec a0 and an individual Response is spec a1
 * 3.  CumulativeResponse is spec a1 and an individual Response is spec a0
 * 4.  CumulativeResponse is spec a1 and an individual Response is spec a1
 *
 * @author paul.eftis
 */
public class EntityDocQueryProcessor implements NhinResponseProcessor{

    private static Log log = LogFactory.getLog(EntityDocQueryProcessor.class);

    private NhincConstants.GATEWAY_API_LEVEL cumulativeSpecLevel = null;
    private int count = 0;

    private static final QName ExtrinsicObjectQname = new QName(
            "urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0",
            "ExtrinsicObject");
    private static final String XDS_RESPONSE_STATUS_FAILURE =
            "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure";


    public EntityDocQueryProcessor(NhincConstants.GATEWAY_API_LEVEL level){
        cumulativeSpecLevel = level;
    }


    /**
     * Handles all processing and aggregation of individual responses
     * from TaskExecutor execution of a NhinDelegate
     * 
     * @param individual
     * @param cumulativeResponse
     */
    public EntityOrchestratableMessage processNhinResponse(
            EntityOrchestratableMessage individual,
            EntityOrchestratableMessage cumulativeResponse){

        count++;
        log.debug("EntityDocQueryProcessor::processNhinResponse count=" + count);

        EntityOrchestratableMessage response = null;

        if(cumulativeResponse == null){
            switch(cumulativeSpecLevel){
                case LEVEL_g0:
                {
                    log.debug("EntityDocQueryProcessor::processNhinResponse createNewCumulativeResponse_a0");
                    cumulativeResponse = EntityDocQueryProcessorHelper.createNewCumulativeResponse_a0(
                            (EntityDocQueryOrchestratable)individual);
                    break;
                }
                case LEVEL_g1:
                {
                    log.debug("EntityDocQueryProcessor::processNhinResponse createNewCumulativeResponse_a1");
                    cumulativeResponse = EntityDocQueryProcessorHelper.createNewCumulativeResponse_a1(
                            (EntityDocQueryOrchestratable)individual);
                    break;
                }
                default:
                {
                    log.debug("EntityDocQueryProcessor::processNhinResponse unknown cumulativeSpecLevel so createNewCumulativeResponse_a1");
                    cumulativeResponse = EntityDocQueryProcessorHelper.createNewCumulativeResponse_a1(
                            (EntityDocQueryOrchestratable)individual);
                    break;
                }
            }
        }

        if(individual == null){
            // can't get here as NhinCallableRequest will always return something
            // but if we ever do, log it and return cumulativeResponse passed in
            log.error("EntityDocQueryProcessor::handleNhinResponse individual received was null!!!");
            response = cumulativeResponse;
        }else{
            EntityOrchestratableMessage individualResponse = processResponse(individual);
            response = aggregateResponse((EntityDocQueryOrchestratable)individualResponse, cumulativeResponse);
        }

        return response;
    }


    /**
     * DQ response requires no processing
     * @param individualResponse
     */
    @SuppressWarnings("static-access")
    public EntityOrchestratableMessage processResponse(EntityOrchestratableMessage individualResponse){
        try{
            // DQ response requires no processing
            return individualResponse;
        }catch(Exception ex){
            ExecutorServiceHelper.getInstance().outputCompleteException(ex);
            EntityOrchestratableMessage response = processErrorResponse(individualResponse,
                        "Exception processing response.  Exception message=" + ex.getMessage());
            return response;
        }
    }


    /**
     * Aggregates an individual DQ response into the cumulative response
     * Note that all response aggregation exceptions are caught here and handled
     * by returning a DQ response with the error/exception and hcid for response
     * @param individual is individual DQ response
     * @param cumulative is current cumulative DQ response
     * @return cumulative response with individual added
     */
    @SuppressWarnings("static-access")
    public EntityOrchestratableMessage aggregateResponse(
            EntityDocQueryOrchestratable individual,
            EntityOrchestratableMessage cumulative){

        try{
            if(cumulative instanceof EntityDocQueryOrchestratable_a0){
                // cumulative is spec_a0
                EntityDocQueryOrchestratable_a0 cumulativeResponse =
                        (EntityDocQueryOrchestratable_a0)cumulative;
                if(individual instanceof EntityDocQueryOrchestratable_a0){
                    // individual is spec_a0 and cumulative is spec_a0, so just aggregate_a0
                    EntityDocQueryOrchestratable_a0 individualResponse =
                            (EntityDocQueryOrchestratable_a0)individual;
                    aggregateResponse_a0(individualResponse, cumulativeResponse);
                }else if(individual instanceof EntityDocQueryOrchestratable_a1){
                    // individual is spec_a1 and cumulative is spec_a0
                    // so transform individual to spec_a0 and then aggregate_a0
                    EntityDocQueryOrchestratable_a0 individualResponse =
                            EntityDocQueryProcessorHelper.transformResponse_ToA0(
                            (EntityDocQueryOrchestratable_a1)individual);
                    aggregateResponse_a0(individualResponse, cumulativeResponse);
                }else{
                    log.error("EntityDocQueryProcessor::aggregateResponse individualResponse received was unknown!!!");
                    throw new Exception("EntityDocQueryProcessor::aggregateResponse individualResponse received was unknown!!!");
                }

                EntityDocQueryOrchestratable_a0 response = new EntityDocQueryOrchestratable_a0(
                    null, null, null, null, cumulativeResponse.getAssertion(),
                    cumulativeResponse.getServiceName(), cumulativeResponse.getTarget(),
                    cumulativeResponse.getRequest());
                response.setCumulativeResponse(cumulativeResponse.getCumulativeResponse());
                return response;
            }else if(cumulative instanceof EntityDocQueryOrchestratable_a1){
                // cumulative is spec_a1
                EntityDocQueryOrchestratable_a1 cumulativeResponse =
                        (EntityDocQueryOrchestratable_a1)cumulative;
                if(individual instanceof EntityDocQueryOrchestratable_a0){
                    // individual is spec_a0 and cumulative is spec_a1
                    // so transform individual to spec_a1 and then aggregate_a1
                    EntityDocQueryOrchestratable_a1 individualResponse =
                            EntityDocQueryProcessorHelper.transformResponse_ToA1(
                            (EntityDocQueryOrchestratable_a0)individual);
                    aggregateResponse_a1(individualResponse, cumulativeResponse);
                }else if(individual instanceof EntityDocQueryOrchestratable_a1){
                    // individual is spec_a1 and cumulative is spec_a1, so just aggregate_a1
                    EntityDocQueryOrchestratable_a1 individualResponse =
                            (EntityDocQueryOrchestratable_a1)individual;
                    aggregateResponse_a1(individualResponse, cumulativeResponse);
                }else{
                    log.error("EntityDocQueryProcessor::aggregateResponse individualResponse received was unknown!!!");
                    throw new Exception("EntityDocQueryProcessor::aggregateResponse individualResponse received was unknown!!!");
                }

                EntityDocQueryOrchestratable_a1 response = new EntityDocQueryOrchestratable_a1(
                    null, null, null, null, cumulativeResponse.getAssertion(),
                    cumulativeResponse.getServiceName(), cumulativeResponse.getTarget(),
                    cumulativeResponse.getRequest());
                response.setCumulativeResponse(cumulativeResponse.getCumulativeResponse());
                return response;
            }else{
                log.error("EntityDocQueryProcessor::aggregateResponse cumulativeResponse received was unknown!!!");
                throw new Exception("EntityDocQueryProcessor::aggregateResponse cumulativeResponse received was unknown!!!");
            }
        }catch(Exception e){
            ExecutorServiceHelper.getInstance().outputCompleteException(e);
            // add error response for exception to cumulativeResponse
            RegistryError regErr = new RegistryError();
            regErr.setErrorCode("XDSRepositoryError");
            regErr.setCodeContext(e.getMessage());
            regErr.setValue("Exception aggregating response from target homeId="
                    + individual.getTarget().getHomeCommunity().getHomeCommunityId());
            regErr.setSeverity("Error");
            if(cumulative instanceof EntityDocQueryOrchestratable_a0){
                EntityDocQueryOrchestratable_a0 cumulativeResponse =
                        (EntityDocQueryOrchestratable_a0)cumulative;
                cumulativeResponse.getCumulativeResponse().getRegistryErrorList()
                        .getRegistryError().add(regErr);
                return cumulativeResponse;
            }else if(cumulative instanceof EntityDocQueryOrchestratable_a1){
                EntityDocQueryOrchestratable_a1 cumulativeResponse =
                        (EntityDocQueryOrchestratable_a1)cumulative;
                cumulativeResponse.getCumulativeResponse().getRegistryErrorList().
                        getRegistryError().add(regErr);
                return cumulativeResponse;
            }else{
                // can do nothing if we ever get here other than return what was passed in
                return cumulative;
            }
        }
    }


    /**
     * General error handler that calls appropriate error handler based on request
     * (i.e. if request is spec a0 will call processError_a0 and if request is
     * spec a1 will call processError_a1)
     * @param request is initial request
     * @param error is String with error message
     * @return
     */
    public EntityOrchestratableMessage processErrorResponse(EntityOrchestratableMessage request, String error){
        log.debug("EntityDocQueryProcessor::processErrorResponse error=" + error);
        if(request instanceof EntityDocQueryOrchestratable_a0){
            return processError_a0((EntityDocQueryOrchestratable)request, error);
        }else if(request instanceof EntityDocQueryOrchestratable_a1){
            return processError_a1((EntityDocQueryOrchestratable)request, error);
        }else{
            log.error("EntityDocQueryProcessor::processErrorResponse request was unknown!!!");
            return processError_a1((EntityDocQueryOrchestratable)request, error);
        }
    }


    /**
     * Generates an EntityDocQueryOrchestratable_a0 response with
     * an error response for spec a0 that contains target hcid that
     * produced error as well as error string passed in
     * @param request is initial request
     * @param error is String with error message
     * @return EntityDocQueryOrchestratable_a0 with error response
     */
    public EntityDocQueryOrchestratable_a0 processError_a0(EntityDocQueryOrchestratable request, String error){
        log.debug("EntityDocQueryProcessor::processError_a0 error=" + error);
        EntityDocQueryOrchestratable_a0 response = new EntityDocQueryOrchestratable_a0(
                null, request.getResponseProcessor(), null, null, request.getAssertion(),
                request.getServiceName(), request.getTarget(), request.getRequest());
        AdhocQueryResponse adhocresponse = new AdhocQueryResponse();
        RegistryErrorList regErrList = new RegistryErrorList();
        adhocresponse.setStatus(XDS_RESPONSE_STATUS_FAILURE);
        RegistryError regErr = new RegistryError();
        regErr.setErrorCode("XDSRepositoryError");
        regErr.setCodeContext(error);
        regErr.setValue("Error from target homeId=" + request.getTarget().getHomeCommunity().getHomeCommunityId());
        regErr.setSeverity("Error");
        regErrList.getRegistryError().add(regErr);
        adhocresponse.setRegistryErrorList(regErrList);
        response.setResponse(adhocresponse);
        return response;
    }


    /**
     * Generates an EntityDocQueryOrchestratable_a1 response with
     * an error response for spec a1 that contains target hcid that
     * produced error as well as error string passed in
     * @param request is initial request
     * @param error is String with error message
     * @return EntityDocQueryOrchestratable_a1 with error response
     */
    public EntityDocQueryOrchestratable_a1 processError_a1(EntityDocQueryOrchestratable request, String error){
        log.debug("EntityDocQueryProcessor::processError_a1 error=" + error);
        EntityDocQueryOrchestratable_a1 response = new EntityDocQueryOrchestratable_a1(
                null, request.getResponseProcessor(), null, null, request.getAssertion(),
                request.getServiceName(), request.getTarget(), request.getRequest());
        AdhocQueryResponse adhocresponse = new AdhocQueryResponse();
        RegistryErrorList regErrList = new RegistryErrorList();
        adhocresponse.setStatus(XDS_RESPONSE_STATUS_FAILURE);
        RegistryError regErr = new RegistryError();
        regErr.setErrorCode("XDSRepositoryError");
        regErr.setCodeContext(error);
        regErr.setValue("Error from target homeId=" + request.getTarget().getHomeCommunity().getHomeCommunityId());
        regErr.setSeverity("Error");
        regErrList.getRegistryError().add(regErr);
        adhocresponse.setRegistryErrorList(regErrList);
        response.setResponse(adhocresponse);
        return response;
    }


    /**
     * aggregates an a0 spec individualResponse into an a0 spec cumulativeResponse
     */
    @SuppressWarnings("static-access")
    private void aggregateResponse_a0(EntityDocQueryOrchestratable_a0 individual,
            EntityDocQueryOrchestratable_a0 cumulativeResponse){

        AdhocQueryResponse current = individual.getResponse();
        try{
            // add the responses from registry object list
            if(current.getRegistryObjectList() != null){
                List<JAXBElement<? extends IdentifiableType>> IdentifiableList = current.getRegistryObjectList().getIdentifiable();
                if(IdentifiableList != null){
                    for(JAXBElement<? extends IdentifiableType> identifiable : IdentifiableList){
                        ExtrinsicObjectType currentExtrinsicObject = cast(identifiable, ExtrinsicObjectType.class);
                        JAXBElement<ExtrinsicObjectType> identifiableObj = new JAXBElement<ExtrinsicObjectType>(
                                ExtrinsicObjectQname,
                                ExtrinsicObjectType.class,
                                currentExtrinsicObject);
                        cumulativeResponse.getCumulativeResponse().getRegistryObjectList().getIdentifiable().add(identifiableObj);
                    }
                }
            }

            // add any registry errors
            if(current.getRegistryErrorList() != null && current.getRegistryErrorList().getRegistryError() != null
                    && current.getRegistryErrorList().getRegistryError().size() > 0){
                for(RegistryError re : current.getRegistryErrorList().getRegistryError()){
                    cumulativeResponse.getCumulativeResponse().getRegistryErrorList().getRegistryError().add(re);
                }
            }

            // add any slotlist response data
            if(current.getResponseSlotList() != null && current.getResponseSlotList().getSlot() != null
                    && current.getResponseSlotList().getSlot().size() > 0){
                for(SlotType1 slot : current.getResponseSlotList().getSlot()){
                    cumulativeResponse.getCumulativeResponse().getResponseSlotList().getSlot().add(slot);
                }
            }
            cumulativeResponse.getCumulativeResponse().setTotalResultCount(
                    cumulativeResponse.getCumulativeResponse().getTotalResultCount().add(BigInteger.ONE));
            log.debug("EntityDocQueryProcessor::aggregateResponse_a0 combine next response done cumulativeResponse count="
                + cumulativeResponse.getCumulativeResponse().getTotalResultCount().toString());
        }catch(Exception ex){
            // exception will throw out and be caught by aggregateResponse
            ExecutorServiceHelper.getInstance().outputCompleteException(ex);
        }
    }

    
    /**
     * aggregates an a1 spec individualResponse into an a1 spec cumulativeResponse
     */
    @SuppressWarnings("static-access")
    private void aggregateResponse_a1(EntityDocQueryOrchestratable_a1 individual,
            EntityDocQueryOrchestratable_a1 cumulativeResponse){

        AdhocQueryResponse current = individual.getResponse();
        try{
            // add the responses from registry object list
            if(current.getRegistryObjectList() != null){
                List<JAXBElement<? extends IdentifiableType>> IdentifiableList = current.getRegistryObjectList().getIdentifiable();
                if(IdentifiableList != null){
                    for(JAXBElement<? extends IdentifiableType> identifiable : IdentifiableList){
                        ExtrinsicObjectType currentExtrinsicObject = cast(identifiable, ExtrinsicObjectType.class);
                        JAXBElement<ExtrinsicObjectType> identifiableObj = new JAXBElement<ExtrinsicObjectType>(
                                ExtrinsicObjectQname,
                                ExtrinsicObjectType.class,
                                currentExtrinsicObject);
                        cumulativeResponse.getCumulativeResponse().getRegistryObjectList().getIdentifiable().add(identifiableObj);
                    }
                }
            }

            // add any registry errors
            if(current.getRegistryErrorList() != null && current.getRegistryErrorList().getRegistryError() != null
                    && current.getRegistryErrorList().getRegistryError().size() > 0){
                for(RegistryError re : current.getRegistryErrorList().getRegistryError()){
                    cumulativeResponse.getCumulativeResponse().getRegistryErrorList().getRegistryError().add(re);
                }
            }

            // add any slotlist response data
            if(current.getResponseSlotList() != null && current.getResponseSlotList().getSlot() != null
                    && current.getResponseSlotList().getSlot().size() > 0){
                for(SlotType1 slot : current.getResponseSlotList().getSlot()){
                    cumulativeResponse.getCumulativeResponse().getResponseSlotList().getSlot().add(slot);
                }
            }
            cumulativeResponse.getCumulativeResponse().setTotalResultCount(
                    cumulativeResponse.getCumulativeResponse().getTotalResultCount().add(BigInteger.ONE));
            log.debug("EntityDocQueryProcessor::aggregateResponse_a1 combine next response done cumulativeResponse count="
                + cumulativeResponse.getCumulativeResponse().getTotalResultCount().toString());
        }catch(Exception ex){
            // exception will throw out and be caught by aggregateResponse
            ExecutorServiceHelper.getInstance().outputCompleteException(ex);
        }
    }


    private <T extends IdentifiableType> T cast(JAXBElement<? extends IdentifiableType> identifiable, Class<T> type) {
        if ((identifiable.getDeclaredType() == type) || identifiable.getValue().getClass() == type) {
            return type.cast(identifiable.getValue());
        }
        return null;
    }


    /**
     * NOT USED
     */
    public void aggregate(EntityOrchestratable individualResponse,
            EntityOrchestratable cumulativeResponse){
    }
}
