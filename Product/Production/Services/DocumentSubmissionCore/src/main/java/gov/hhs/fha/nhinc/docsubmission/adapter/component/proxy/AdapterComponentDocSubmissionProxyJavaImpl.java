/*
 * Copyright (c) 2009-2018, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.docsubmission.adapter.component.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docrepository.adapter.dao.DocumentDao;
import gov.hhs.fha.nhinc.docsubmission.adapter.component.AdapterComponentDocSubmissionOrchImpl;
import gov.hhs.fha.nhinc.docsubmission.adapter.component.XDRHelper;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jhoppesc
 */
public class AdapterComponentDocSubmissionProxyJavaImpl implements AdapterComponentDocSubmissionProxy {
    private static final Logger LOG = LoggerFactory.getLogger(AdapterComponentDocSubmissionProxyJavaImpl.class);

    DocumentDao documentDAO;

    @Override
    public RegistryResponseType provideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType msg,
        AssertionType assertion) {
        LOG.trace("Using Java Implementation for Adapter Doc Submission Service");
        RegistryResponseType result = null;

        XDRHelper helper = new XDRHelper();
        RegistryErrorList errorList = helper.validateDocumentMetaData(msg);
        if (NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR.equals(errorList.getHighestSeverity())) {
            result = helper.createErrorResponse(errorList);
        } else {
            LOG.info(" Request contained {} documents.", msg.getDocument().size());
            LOG.info(" Request Id: {}", msg.getSubmitObjectsRequest().getId());

            // Forward the message to the routers, so the intended recipients can get the message too.
            RegistryResponseType routeResult = AdapterComponentDocSubmissionOrchImpl.provideAndRegisterDocumentSetB(msg,
                assertion);
            if (XDRHelper.XDS_RETRIEVE_RESPONSE_STATUS_FAILURE.equals(routeResult.getStatus())) {
                LOG.warn("Routing failed with errors while forwarding the document");
            }
        }

        return result;
    }

}
