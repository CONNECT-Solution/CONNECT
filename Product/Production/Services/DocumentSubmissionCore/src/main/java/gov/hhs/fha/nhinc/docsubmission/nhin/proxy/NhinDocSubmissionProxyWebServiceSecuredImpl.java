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
package gov.hhs.fha.nhinc.docsubmission.nhin.proxy;

import gov.hhs.fha.nhinc.aspect.NwhinInvocationEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docsubmission.DocSubmissionUtils;
import gov.hhs.fha.nhinc.docsubmission.aspect.DocSubmissionBaseEventDescriptionBuilder;
import gov.hhs.fha.nhinc.docsubmission.nhin.proxy.service.NhinDocSubmission20ServicePortDescriptor;
import gov.hhs.fha.nhinc.docsubmission.nhin.proxy.service.NhinDocSubmissionServicePortDescriptor;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.largefile.LargePayloadException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.util.MessageGeneratorUtils;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xdr._2007.DocumentRepositoryXDRPortType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NhinDocSubmissionProxyWebServiceSecuredImpl implements NhinDocSubmissionProxy {

    private static final Logger LOG = LoggerFactory.getLogger(NhinDocSubmissionProxyWebServiceSecuredImpl.class);
    private WebServiceProxyHelper proxyHelper = null;

    public NhinDocSubmissionProxyWebServiceSecuredImpl() {
    }

    protected DocSubmissionUtils getDocSubmissionUtils() {
        return DocSubmissionUtils.getInstance();
    }

    protected MessageGeneratorUtils getMessageGeneratorUtils() {
        return MessageGeneratorUtils.getInstance();
    }

    protected WebServiceProxyHelper getWebServiceProxyHelper() {
        if (proxyHelper == null) {
            proxyHelper = new WebServiceProxyHelper();
        }
        return proxyHelper;
    }

    public ServicePortDescriptor<DocumentRepositoryXDRPortType> getServicePortDescriptor(
        NhincConstants.GATEWAY_API_LEVEL apiLevel) {
        switch (apiLevel) {
            case LEVEL_g0:
                return new NhinDocSubmissionServicePortDescriptor();
            default:
                return new NhinDocSubmission20ServicePortDescriptor();
        }
    }

    protected CONNECTClient<DocumentRepositoryXDRPortType> getCONNECTClientSecured(
        ServicePortDescriptor<DocumentRepositoryXDRPortType> portDescriptor, AssertionType assertion, String url,
        NhinTargetSystemType target, String serviceName) {
        return CONNECTClientFactory.getInstance().getCONNECTClientSecured(portDescriptor, assertion, url,
            target, serviceName);
    }

    @NwhinInvocationEvent(beforeBuilder = DocSubmissionBaseEventDescriptionBuilder.class, afterReturningBuilder
        = DocSubmissionBaseEventDescriptionBuilder.class, serviceType = "Document Submission", version = "")
    @Override
    public RegistryResponseType provideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType request,
        AssertionType assertion, NhinTargetSystemType targetSystem, NhincConstants.GATEWAY_API_LEVEL apiLevel) {
        LOG.debug("Begin provideAndRegisterDocumentSetB");
        RegistryResponseType response = new RegistryResponseType();

        try {
            String url = getWebServiceProxyHelper().getUrlFromTargetSystemByGatewayAPILevel(targetSystem,
                NhincConstants.NHINC_XDR_SERVICE_NAME, apiLevel);
            getDocSubmissionUtils().convertFileLocationToDataIfEnabled(request);

            ServicePortDescriptor<DocumentRepositoryXDRPortType> portDescriptor = getServicePortDescriptor(apiLevel);

            CONNECTClient<DocumentRepositoryXDRPortType> client = getCONNECTClientSecured(portDescriptor, assertion,
                url, targetSystem, NhincConstants.NHINC_XDR_SERVICE_NAME);
            client.enableMtom();

            response = (RegistryResponseType) client.invokePort(DocumentRepositoryXDRPortType.class,
                "documentRepositoryProvideAndRegisterDocumentSetB", request);

        } catch (LargePayloadException lpe) {
            response = getMessageGeneratorUtils().createMissingDocumentRegistryResponse();
            throw new ErrorEventException(lpe, response, "Unable to call Nhin Doc Submission");
        } catch (Exception ex) {
            response = getMessageGeneratorUtils().createRegistryErrorResponseWithAckFailure(ex.getMessage());
            throw new ErrorEventException(ex, response, "Unable to call Nhin Doc Submission");
        }

        LOG.debug("End provideAndRegisterDocumentSetB");
        return response;
    }

}
