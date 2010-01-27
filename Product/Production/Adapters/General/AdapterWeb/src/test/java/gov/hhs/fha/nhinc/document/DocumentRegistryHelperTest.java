package gov.hhs.fha.nhinc.document;

import gov.hhs.fha.nhinc.repository.model.DocumentQueryParams;
import gov.hhs.fha.nhinc.repository.service.DocumentService;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.fha.nhinc.util.format.UTCDateUtil;
import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.apache.commons.logging.Log;

/**
 * Unit test for the DocumentRegistryHelper class
 *
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class DocumentRegistryHelperTest
{
    Mockery context = new JUnit4Mockery(){{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    @Test
    public void testDocumentRegistryRegistryStoredQuery()
    {
        // Create mock objects
        final Log mockLog = context.mock(Log.class);
        final DocumentService docService = context.mock(DocumentService.class);
        final UTCDateUtil mockDateUtil = context.mock(UTCDateUtil.class);

        DocumentRegistryHelper registryHelper = new DocumentRegistryHelper()
        {

            @Override
            protected Log createLogger()
            {
                return mockLog;
            }

            @Override
            protected DocumentService getDocumentService()
            {
                return docService;
            }

            @Override
            protected UTCDateUtil createDateUtil()
            {
                return mockDateUtil;
            }
        };

        oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest body = loadRequestMessage(ADHOC_QUERY_MESSAGE_EVENT_CODE);
//        final DocumentQueryParams params = new DocumentQueryParams();
//        final EventCodeParam eventCodeParam = new EventCodeParam();
//        List<EventCodeParam> eventCodeParams = new ArrayList<EventCodeParam>();
//        eventCodeParams.add(eventCodeParam);
//        eventCodeParam.setEventCode("1234abcd");
//        eventCodeParam.setEventCodeScheme("1234abcdScheme");

        // Set expectations
        context.checking(new Expectations(){{
            allowing (mockLog).isDebugEnabled();
            allowing (mockLog).debug(with(any(String.class)));
            oneOf (docService).documentQuery(with(aNonNull(DocumentQueryParams.class)));
//            oneOf (docService).documentQuery(with(equal(params)));
        }});

        oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse response = registryHelper.documentRegistryRegistryStoredQuery(body);
        assertNotNull("Response was null", response);

    }

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest loadRequestMessage(String message)
    {
        oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest request = null;
        String contextPath = "oasis.names.tc.ebxml_regrep.xsd.query._3";
        Object unmarshalledObject = null;

        try
        {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext(contextPath);
            javax.xml.bind.Unmarshaller unmarshaller = jc.createUnmarshaller();
            StringReader stringReader = new StringReader(message);
            unmarshalledObject = unmarshaller.unmarshal(stringReader);
            if (unmarshalledObject instanceof JAXBElement)
            {
                JAXBElement jaxb = (JAXBElement) unmarshalledObject;
                unmarshalledObject = jaxb.getValue();
            }
        } catch (Exception e)
        {
            unmarshalledObject = null;
            e.printStackTrace();
            fail("Exception unmarshalling adhoc query: " + e.getMessage());
        }
        if(unmarshalledObject instanceof oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest)
        {
            request = (oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest)unmarshalledObject;
        }
        else
        {
            fail("Unmarshalled object is not an adhoc query. Is: " + ((unmarshalledObject == null) ? "null" : unmarshalledObject.getClass().getName()));
        }
        return request;
    }

    private static final String ADHOC_QUERY_MESSAGE =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<S:Envelope xmlns:S=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:saml=\"urn:oasis:names:tc:SAML:1.0:assertion\" xmlns:wsse11=\"http://docs.oasis-open.org/wss/oasis-wss-wssecurity-secext-1.1.xsd\" xmlns:exc14n=\"http://www.w3.org/2001/10/xml-exc-c14n#\">" +
        "	<S:Header>" +
        "		<To xmlns=\"http://www.w3.org/2005/08/addressing\">https://localhost:8181/RespondingGateway_Query_Service/DocQuery</To>" +
        "		<Action xmlns=\"http://www.w3.org/2005/08/addressing\">urn:ihe:iti:2007:CrossGatewayQuery</Action>" +
        "		<ReplyTo xmlns=\"http://www.w3.org/2005/08/addressing\">" +
        "			<Address>http://www.w3.org/2005/08/addressing/anonymous</Address>" +
        "		</ReplyTo>" +
        "		<MessageID xmlns=\"http://www.w3.org/2005/08/addressing\">uuid:f8d2b511-7d7f-46c9-964b-579f250f60f1</MessageID>" +
        "	</S:Header>" +
        "	<S:Body>" +
        "		<ns5:AdhocQueryRequest xmlns:ns2=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\" xmlns:ns3=\"urn:gov:hhs:fha:nhinc:gateway:samltokendata\" xmlns:ns4=\"urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0\" xmlns:ns5=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0\" xmlns:ns6=\"urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0\" maxResults=\"-1\" startIndex=\"0\" federated=\"false\">" +
        "			<ns5:ResponseOption returnComposedObjects=\"true\" returnType=\"LeafClass\"/>" +
        "			<ns2:AdhocQuery home=\"urn:oid:1.1\" id=\"urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d\">" +
        "				<ns2:Slot name=\"$XDSDocumentEntryStatus\">" +
        "					<ns2:ValueList>" +
        "						<ns2:Value>('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved')</ns2:Value>" +
        "					</ns2:ValueList>" +
        "				</ns2:Slot>" +
        "				<ns2:Slot name=\"$XDSDocumentEntryPatientId\">" +
        "					<ns2:ValueList>" +
        "						<ns2:Value>'D123401^^^&amp;1.1&amp;ISO'</ns2:Value>" +
        "					</ns2:ValueList>" +
        "				</ns2:Slot>" +
        "			</ns2:AdhocQuery>" +
        "		</ns5:AdhocQueryRequest>" +
        "	</S:Body>" +
        "</S:Envelope>";

    private static final String ADHOC_QUERY_MESSAGE_EVENT_CODE =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "		<ns5:AdhocQueryRequest xmlns:ns2=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\" xmlns:ns5=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0\" maxResults=\"-1\" startIndex=\"0\" federated=\"false\">" +
        "			<ns5:ResponseOption returnComposedObjects=\"true\" returnType=\"LeafClass\"/>" +
        "			<ns2:AdhocQuery home=\"urn:oid:1.1\" id=\"urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d\">" +
        "				<ns2:Slot name=\"$XDSDocumentEntryEventCodeList\">" +
        "					<ns2:ValueList>" +
        "						<ns2:Value>('1234abcd')</ns2:Value>" +
        "					</ns2:ValueList>" +
        "				</ns2:Slot>" +
        "				<ns2:Slot name=\"$XDSDocumentEntryEventCodeListScheme\">" +
        "					<ns2:ValueList>" +
        "						<ns2:Value>('1234abcdScheme')</ns2:Value>" +
        "					</ns2:ValueList>" +
        "				</ns2:Slot>" +
        "			</ns2:AdhocQuery>" +
        "		</ns5:AdhocQueryRequest>";
}
