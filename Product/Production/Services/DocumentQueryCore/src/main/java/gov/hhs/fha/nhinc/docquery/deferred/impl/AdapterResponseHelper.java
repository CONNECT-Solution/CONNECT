
package gov.hhs.fha.nhinc.docquery.deferred.impl;

import gov.hhs.fha.nhinc.document.DocumentConstants;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

public class AdapterResponseHelper {

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

}
