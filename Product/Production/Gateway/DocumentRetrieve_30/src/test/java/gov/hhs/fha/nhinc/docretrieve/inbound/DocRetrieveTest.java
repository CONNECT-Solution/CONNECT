/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package gov.hhs.fha.nhinc.docretrieve.inbound;

import gov.hhs.fha.nhinc.aspect.InboundMessageEvent;
import gov.hhs.fha.nhinc.aspect.InboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetRequestTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetResponseTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import java.lang.reflect.Method;
import java.util.Properties;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.lang.StringUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mockito.Mockito.mock;

/**
 * @author bhumphrey
 *
 */
public class DocRetrieveTest {

    @Test
    public void delegatesToService() {
        DocRetrieve docRetrieve = new DocRetrieve();
        RetrieveDocumentSetRequestType body = new RetrieveDocumentSetRequestType();
        InboundDocRetrieve service = mock(InboundDocRetrieve.class);
        docRetrieve.setInboundDocRetrieve(service);

        docRetrieve.respondingGatewayCrossGatewayRetrieve(body);
    }

    @Test
    public void testImplementsSpecVersion() {
        final AssertionType assertion = new AssertionType();
        DocRetrieve docRetrieve = new DocRetrieve() {

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
        RetrieveDocumentSetRequestType body = new RetrieveDocumentSetRequestType();
        InboundDocRetrieve service = mock(InboundDocRetrieve.class);
        docRetrieve.setInboundDocRetrieve(service);

        docRetrieve.respondingGatewayCrossGatewayRetrieve(body);

        assertTrue(!StringUtils.isBlank(assertion.getImplementsSpecVersion()));
        assertEquals(NhincConstants.UDDI_SPEC_VERSION.SPEC_3_0.toString(), assertion.getImplementsSpecVersion());
    }

    @Test
    public void hasInboundMessageEvent() throws Exception {
        Class<DocRetrieve> clazz = DocRetrieve.class;
        Method method = clazz.getMethod("respondingGatewayCrossGatewayRetrieve", RetrieveDocumentSetRequestType.class);
        InboundMessageEvent annotation = method.getAnnotation(InboundMessageEvent.class);
        assertNotNull(annotation);
        assertEquals(RetrieveDocumentSetRequestTypeDescriptionBuilder.class, annotation.beforeBuilder());
        assertEquals(RetrieveDocumentSetResponseTypeDescriptionBuilder.class, annotation.afterReturningBuilder());
        assertEquals("Retrieve Document", annotation.serviceType());
        assertEquals("3.0", annotation.version());
    }

    @Test
    public void hasInboundProcessingEventStandard() throws Exception {
        Class<StandardInboundDocRetrieve> clazz = StandardInboundDocRetrieve.class;
        Method method = clazz.getMethod("respondingGatewayCrossGatewayRetrieve", RetrieveDocumentSetRequestType.class,
            AssertionType.class, Properties.class);
        InboundProcessingEvent annotation = method.getAnnotation(InboundProcessingEvent.class);
        assertNotNull(annotation);
        assertEquals(RetrieveDocumentSetRequestTypeDescriptionBuilder.class, annotation.beforeBuilder());
        assertEquals(RetrieveDocumentSetResponseTypeDescriptionBuilder.class, annotation.afterReturningBuilder());
        assertEquals("Retrieve Document", annotation.serviceType());
        assertEquals("", annotation.version());
    }

}
