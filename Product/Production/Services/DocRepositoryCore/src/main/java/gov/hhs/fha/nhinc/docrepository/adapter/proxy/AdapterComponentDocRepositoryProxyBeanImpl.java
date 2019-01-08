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
package gov.hhs.fha.nhinc.docrepository.adapter.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.document.DocumentConstants;
import gov.hhs.fha.nhinc.largefile.LargeFileUtils;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author akong
 */
public class AdapterComponentDocRepositoryProxyBeanImpl implements AdapterComponentDocRepositoryProxy {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterComponentDocRepositoryProxyBeanImpl.class);

    private String hcid;
    private String repositoryId;
    private String documentUniqueId;
    private String mimeType;
    private String document;

    @Override
    public RetrieveDocumentSetResponseType retrieveDocument(RetrieveDocumentSetRequestType request,
            AssertionType assertion) {
        LOG.debug("Using Bean Implementation for Adapter Component Doc Repository Service");
        return createRetrieveDocumentSetResponseType();
    }

    @Override
    public RegistryResponseType provideAndRegisterDocumentSet(ProvideAndRegisterDocumentSetRequestType body,
            AssertionType assertion) {
        LOG.debug("Using Bean Implementation for Adapter Component Doc Repository Service");
        return new RegistryResponseType();
    }

    public void setHomeCommunityId(String hcid) {
        this.hcid = hcid;
    }

    public void setRepositoryUniqueId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public void setDocumentUniqueId(String documentUniqueId) {
        this.documentUniqueId = documentUniqueId;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    private RetrieveDocumentSetResponseType createRetrieveDocumentSetResponseType() {
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();

        RegistryResponseType registryResponse = new RegistryResponseType();
        registryResponse.setStatus(DocumentConstants.XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS);

        DocumentResponse documentResponse = new DocumentResponse();
        if (this.hcid != null) {
            documentResponse.setHomeCommunityId(hcid);
        }
        if (this.repositoryId != null) {
            documentResponse.setRepositoryUniqueId(this.repositoryId);
        }
        if (this.documentUniqueId != null) {
            documentResponse.setDocumentUniqueId(this.documentUniqueId);
        }
        if (this.mimeType != null) {
            documentResponse.setMimeType(mimeType);
        }
        if (this.document != null) {
            documentResponse.setDocument(LargeFileUtils.getInstance().convertToDataHandler(this.document));
        }

        response.setRegistryResponse(registryResponse);
        response.getDocumentResponse().add(documentResponse);

        return response;
    }
}
