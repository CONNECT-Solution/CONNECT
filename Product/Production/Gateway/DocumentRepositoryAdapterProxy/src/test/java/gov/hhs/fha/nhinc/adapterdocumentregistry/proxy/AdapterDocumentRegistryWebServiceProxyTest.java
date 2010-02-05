package gov.hhs.fha.nhinc.adapterdocumentregistry.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rayj
 */
@Ignore // TODO: Move to an integration test
public class AdapterDocumentRegistryWebServiceProxyTest {

    public AdapterDocumentRegistryWebServiceProxyTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testQueryForDocument() throws Exception {
    }

    @Test
    public void InvokeMock() throws Exception {
        String url = "http://localhost:8088/mockDocumentRegistry";
        AdapterDocumentRegistryProxyObjectFactory factory = new AdapterDocumentRegistryProxyObjectFactory();
        AdapterDocumentRegistryProxy proxy = factory.getAdapterDocumentRegistryProxy();

        AdhocQueryRequest adhocQuery = new AdhocQueryRequest();
        AssertionType assertion = new AssertionType();
        NhinTargetSystemType target = new NhinTargetSystemType();
        target.setUrl(url);

        AdhocQueryResponse response = proxy.queryForDocument(adhocQuery, assertion, target);
        assertNotNull(response);
    }
}