package gov.hhs.fha.nhinc.deferredresults.impl;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docrepository.adapter.dao.DeferredResponseOptionDao;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocQueryDeferredResponseMetadata;
import gov.hhs.fha.nhinc.document.DocumentConstants;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

public class AdapterResponseHelper {

    private AdapterResponseHelper() {
    }

    public static RegistryResponseType createFailureWithMessage(String... values ) {
        RegistryResponseType response = new RegistryResponseType();
        response.setStatus(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE);

        RegistryErrorList errorList = new RegistryErrorList();
        List<RegistryError> list = errorList.getRegistryError();

        for (String value : values) {
            RegistryError error = new RegistryError();
            error.setValue(value);
            list.add(error);
        }

        response.setRegistryErrorList(errorList);
        return response;
    }

    public static AdhocQueryResponse createSuccessResponse() {
        AdhocQueryResponse response = new AdhocQueryResponse();
        response.setRegistryObjectList(new RegistryObjectListType());
        response.setStatus(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_SUCCESS);
        return response;
    }


    public static RegistryResponseType createSuccessACK() {
        return createACK(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_SUCCESS);
    }

    private static RegistryResponseType createACK(String status) {
        RegistryResponseType response = new RegistryResponseType();
        response.setStatus(status);
        return response;
    }

    public static RegistryResponseType createRegistryResponseTypeWithXdsQuerySuccess() {
        return createRegistryResponseTypeWithXdsQueryFailure();
    }

    public static RegistryResponseType createRegistryResponseTypeWithXdsQueryFailure(String... messsages) {
        RegistryResponseType response = new RegistryResponseType();
        response.setStatus(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_SUCCESS);
        if (ArrayUtils.isNotEmpty(messsages)) {
            response.setStatus(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE);

            RegistryErrorList errorList = new RegistryErrorList();
            List<RegistryError> list = errorList.getRegistryError();

            for (String value : messsages) {
                RegistryError error = new RegistryError();
                error.setValue(value);
                list.add(error);
            }
            response.setRegistryErrorList(errorList);
        }
        return response;
    }

    public static AdhocQueryResponse createAdhocQueryResponseWith(String status) {
        AdhocQueryResponse response = new AdhocQueryResponse();
        response.setRegistryObjectList(new RegistryObjectListType());
        response.setStatus(status);
        return response;
    }

    public static DocQueryDeferredResponseMetadata getDocQueryDeferredResponseMetadata(String requestId,
        String responseEndpoint) {
        DocQueryDeferredResponseMetadata meta = null;

        if (StringUtils.isNotBlank(requestId) && StringUtils.isNotBlank(responseEndpoint)) {
            meta = new DocQueryDeferredResponseMetadata();
            meta.setRequestId(requestId);
            meta.setResponseEndpoint(responseEndpoint);
        }
        return meta;
    }

    public static RegistryResponseType processAdapterDeferredResponseOption(DeferredResponseOptionDao dao,
        AdhocQueryRequest request, AssertionType assertion) {
        if (null != request && null != assertion) {
            DocQueryDeferredResponseMetadata meta = getDocQueryDeferredResponseMetadata(extractAdhocQueryId(request),
                assertion.getDeferredResponseEndpoint());
            if (null != meta && dao.save(meta)) {
                return createRegistryResponseTypeWithXdsQuerySuccess();
            }
        }

        return createRegistryResponseTypeWithXdsQueryFailure("requestId and responseEndpoint are required.");
    }

    public static AdhocQueryResponse convertAdhocQueryResponse(RegistryResponseType response) {
        if (null != response) {
            AdhocQueryResponse adhoc = new AdhocQueryResponse();
            adhoc.setStatus(response.getStatus());
            adhoc.setRegistryErrorList(response.getRegistryErrorList());
            return adhoc;
        }
        return null;
    }

    private static String extractAdhocQueryId(AdhocQueryRequest request) {
        if (null != request.getAdhocQuery()) {
            return request.getAdhocQuery().getId();
        }
        return null;
    }
}
