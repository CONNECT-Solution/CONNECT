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
package gov.hhs.fha.nhinc.docquery._30.entity;

import gov.hhs.fha.nhinc.aspect.OutboundMessageEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.docquery.aspect.AdhocQueryRequestTransformingBuilder;
import gov.hhs.fha.nhinc.docquery.aspect.AdhocQueryResponseDescriptionBuilder;
import gov.hhs.fha.nhinc.docquery.entity.proxy.EntityDocQueryProxy;
import gov.hhs.fha.nhinc.docquery.entity.proxy.EntityDocQueryProxyObjectFactory;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.Addressing;
import javax.xml.ws.soap.SOAPBinding;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

/**
 * The Class EntityDocQueryUnsecured.
 *
 * @author msw
 */
@BindingType(value = SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class EntityDocQueryUnsecuredProxy implements gov.hhs.fha.nhinc.entitydocquery.EntityDocQueryPortType {

    @OutboundMessageEvent(beforeBuilder = AdhocQueryRequestTransformingBuilder.class,
        afterReturningBuilder = AdhocQueryResponseDescriptionBuilder.class, serviceType = "Document Query",
        version = "3.0")
    @Override
    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQueryRequestType request) {
        EntityDocQueryProxyObjectFactory factory = new EntityDocQueryProxyObjectFactory();
        EntityDocQueryProxy proxy = factory.getEntityDocQueryProxy();
        AdhocQueryRequest msg = request.getAdhocQueryRequest();
        AssertionType assertion = request.getAssertion();
        NhinTargetCommunitiesType targets = request.getNhinTargetCommunities();
        return proxy.respondingGatewayCrossGatewayQuery(msg, assertion, targets);
    }
}
