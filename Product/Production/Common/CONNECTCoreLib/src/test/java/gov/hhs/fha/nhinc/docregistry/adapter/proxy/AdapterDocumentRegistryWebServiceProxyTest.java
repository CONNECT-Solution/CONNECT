package gov.hhs.fha.nhinc.docregistry.adapter.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docregistry.adapter.proxy.AdapterComponentDocRegistryProxy;
import gov.hhs.fha.nhinc.docregistry.adapter.proxy.AdapterComponentDocRegistryProxyObjectFactory;
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
        AdapterComponentDocRegistryProxyObjectFactory factory = new AdapterComponentDocRegistryProxyObjectFactory();
        AdapterComponentDocRegistryProxy proxy = factory.getAdapterComponentDocRegistryProxy();

        AdhocQueryRequest adhocQuery = new AdhocQueryRequest();
        AssertionType assertion = new AssertionType();
        NhinTargetSystemType target = new NhinTargetSystemType();
        target.setUrl(url);

        AdhocQueryResponse response = proxy.registryStoredQuery(adhocQuery, assertion);
        assertNotNull(response);
    }
}