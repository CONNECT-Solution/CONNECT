package gov.hhs.fha.nhinc.deferredresults.impl;

import static org.junit.Assert.assertEquals;

import gov.hhs.fha.nhinc.document.DocumentConstants;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.junit.Test;

public class AdapterResponseHelperTest {

    private static final String SUCCESS = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success";
    private static final String FAILURE = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure";

    public static RegistryResponseType createFailureWithMessage(String... values) {
        RegistryResponseType response = new RegistryResponseType();
        response.setStatus(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE);

        RegistryErrorList errorList = new RegistryErrorList();
        List<RegistryError> list = errorList.getRegistryError();

        for (String value : values) {
            RegistryError error = new RegistryError();
            error.setValue(value);
            list.add(error);
        }

        return response;
    }

    public static AdhocQueryResponse createSuccessResponse() {
        AdhocQueryResponse response = new AdhocQueryResponse();
        response.setRegistryObjectList(new RegistryObjectListType());
        response.setStatus(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_SUCCESS);
        return response;
    }

    @Test
    public void testCreateFailureWithMessage() {
        RegistryResponseType response = AdapterResponseHelper.createFailureWithMessage("Message 0", "Message 1",
            "Message 2");
        assertEquals(FAILURE, response.getStatus());

        List<RegistryError> responseList = response.getRegistryErrorList().getRegistryError();
        assertEquals(3, responseList.size());
        for (int i = 0; i < 3; i++) {
            assertEquals("Message " + i, responseList.get(i).getCodeContext());
        }
    }

    @Test
    public void testCreateSuccessResponse() {
        AdhocQueryResponse response = AdapterResponseHelper.createSuccessResponse();
        assertEquals(SUCCESS, response.getStatus());

    }
}
