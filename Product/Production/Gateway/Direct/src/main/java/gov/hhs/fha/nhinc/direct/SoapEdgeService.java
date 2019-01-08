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
package gov.hhs.fha.nhinc.direct;

import gov.hhs.fha.nhinc.direct.xdr.DirectXDRWebServiceImpl;
import gov.hhs.fha.nhinc.xdcommon.XDCommonResponseHelper;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import java.util.UUID;
import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.SOAPBinding;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 * The Class XDR.
 *
 * @author mweaver
 */
@BindingType(SOAPBinding.SOAP12HTTP_BINDING)
public class SoapEdgeService implements ihe.iti.xds_b._2007.DocumentRepositoryPortType {

    /** The context. */
    @Resource
    protected WebServiceContext context;

    /** The endpoint. */
    protected String endpoint = null;

    /** The message id. */
    protected String messageId = null;

    /** The relates to. */
    protected String relatesTo = null;

    /** The action. */
    protected String action = null;

    /** The to. */
    protected String to = null;

    /*
     * (non-Javadoc)
     *
     * @see
     * org.nhind.xdr.DocumentRepositoryAbstract#documentRepositoryProvideAndRegisterDocumentSetB(ihe.iti.xds_b._2007
     * .ProvideAndRegisterDocumentSetRequestType)
     */
    @Override
    public RegistryResponseType documentRepositoryProvideAndRegisterDocumentSetB(
            ProvideAndRegisterDocumentSetRequestType body) {
        RegistryResponseType resp;
        try {
            DirectXDRWebServiceImpl impl = new DirectXDRWebServiceImpl();
            resp = impl.provideAndRegisterDocumentSet(body, context);
        } catch (Exception x) {

            relatesTo = messageId;
            action = "urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-bResponse";
            messageId = UUID.randomUUID().toString();
            to = endpoint;

            XDCommonResponseHelper helper = new XDCommonResponseHelper();
            resp = helper.createError(x);

        }
        return resp;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.nhind.xdr.DocumentRepositoryAbstract#documentRepositoryRetrieveDocumentSet(ihe.iti.xds_b._2007.
     * RetrieveDocumentSetRequestType)
     */
    @Override
    public RetrieveDocumentSetResponseType documentRepositoryRetrieveDocumentSet(RetrieveDocumentSetRequestType body) {
        throw new UnsupportedOperationException("Not implemented for XDR");
    }

}
