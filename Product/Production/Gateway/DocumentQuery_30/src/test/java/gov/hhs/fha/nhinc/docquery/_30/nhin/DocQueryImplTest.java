/**
 * 
 */
package gov.hhs.fha.nhinc.docquery._30.nhin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docquery.inbound.InboundDocQuery;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

import javax.xml.ws.WebServiceContext;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

/**
 * @author msw
 * 
 */
public class DocQueryImplTest {

    @Test
    public void testImplementsSpecVersion() {
        final AssertionType assertion = new AssertionType();
        InboundDocQuery service = mock(InboundDocQuery.class);
        DocQueryImpl docQuery = new DocQueryImpl(service) {

            /*
             * (non-Javadoc)
             * 
             * @see gov.hhs.fha.nhinc.messaging.server.BaseService#getAssertion(javax.xml.ws.WebServiceContext,
             * gov.hhs.fha.nhinc.common.nhinccommon.AssertionType)
             */
            @Override
            protected AssertionType getAssertion(WebServiceContext context, AssertionType oAssertionIn) {
                return assertion;
            }
        };

        AdhocQueryRequest body = mock(AdhocQueryRequest.class);
        WebServiceContext context = mock(WebServiceContext.class);
        docQuery.respondingGatewayCrossGatewayQuery(body, context);

        verify(service).respondingGatewayCrossGatewayQuery(same(body), any(AssertionType.class));
        assertTrue(!StringUtils.isBlank(assertion.getImplementsSpecVersion()));
        assertEquals(NhincConstants.UDDI_SPEC_VERSION.SPEC_3_0.toString(), assertion.getImplementsSpecVersion());
    }

}
