/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.docsubmission.aspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.hhs.fha.nhinc.event.BeanPropertyArgumentTransformer;
import gov.hhs.fha.nhinc.event.BeanTransformerTest;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.junit.Test;

public class DocSubmissionArgTransformerBuilderTest extends BeanTransformerTest<DocSubmissionArgTransformerBuilder> {

    @Override
    public DocSubmissionArgTransformerBuilder getBuilder() {
        return new DocSubmissionArgTransformerBuilder();
    }

    @Test
    public void correctArgTransformerDelegate() {
        assertTrue(builder instanceof BeanPropertyArgumentTransformer);
        assertEquals(DocSubmissionBaseEventDescriptionBuilder.class, builder.getDelegate().getClass());
    }

    @Test
    public void unwrapsResponseIfNecessary() {
        RegistryResponseType baseResponseMock = mock(RegistryResponseType.class);
        assertEquals(baseResponseMock, builder.transformReturnValue(baseResponseMock));

        XDRAcknowledgementType wrapperMock = mock(XDRAcknowledgementType.class);
        when(wrapperMock.getMessage()).thenReturn(baseResponseMock);
        assertEquals(baseResponseMock, builder.transformReturnValue(wrapperMock));
    }

    @Override
    public Class<?> getTransformToClass() {
        return ProvideAndRegisterDocumentSetRequestType.class;
    }

    @Override
    public Class<?>[] getExpectedWrapperClasses() {
        return new Class<?>[] {
                gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetRequestType.class,
                gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetRequestType.class,
                gov.hhs.fha.nhinc.common.nhinccommoninternalorch.RespondingGatewayProvideAndRegisterDocumentSetRequestType.class,
                gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType.class,
                gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType.class };
    }
}
