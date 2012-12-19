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
package gov.hhs.fha.nhinc.direct.xdr;

import gov.hhs.fha.nhinc.direct.DirectClient;
import gov.hhs.fha.nhinc.direct.DirectClientFactory;
import gov.hhs.fha.nhinc.direct.addressparsing.FromAddressParser;
import gov.hhs.fha.nhinc.direct.addressparsing.FromAddressParserFactory;
import gov.hhs.fha.nhinc.direct.addressparsing.ToAddressParser;
import gov.hhs.fha.nhinc.direct.addressparsing.ToAddressParserFactory;
import gov.hhs.fha.nhinc.direct.xdr.audit.SoapEdgeAuditor;
import gov.hhs.fha.nhinc.direct.xdr.audit.SoapEdgeAuditorFactory;
import gov.hhs.fha.nhinc.xdcommon.XDCommonResponseHelper;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;

import javax.mail.Address;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.log4j.Logger;
import org.nhindirect.xd.common.DirectDocuments;
import org.nhindirect.xd.transform.XdsDirectDocumentsTransformer;
import org.nhindirect.xd.transform.exception.TransformationException;
import org.nhindirect.xd.transform.impl.DefaultXdsDirectDocumentsTransformer;

public class SoapDirectEdgeOrchestration {
    private static final Logger LOG = Logger.getLogger(SoapDirectEdgeOrchestration.class);

    private XdsDirectDocumentsTransformer xdsDirectDocumentsTransformer = null;
    private DirectClient directClient = null;
    private SoapEdgeAuditor auditor = null;

    /**
     * Handle an incoming ProvideAndRegisterDocumentSetRequestType object and transform to XDM.
     * 
     * @param prdst The incoming ProvideAndRegisterDocumentSetRequestType object
     * @param context The values of the ws headers
     * @return a RegistryResponseType object
     * @throws Exception
     */
    public RegistryResponseType orchestrate(ProvideAndRegisterDocumentSetRequestType prdst,
            SoapEdgeContext context) throws Exception {
        RegistryResponseType resp = null;

        try {
            // TODO patID and subsetId for atn
            // String patId = messageId;
            // String subsetId = messageId;
            getAuditor().audit(SoapEdgeAuditor.PRINCIPAL, SoapEdgeAuditor.REQUESTRECIEVED_CATEGORY,
                    SoapEdgeAuditor.REQUESTRECIEVED_MESSAGE, context);

            resp = sendMessage(prdst, context );

            getAuditor().audit(SoapEdgeAuditor.PRINCIPAL, SoapEdgeAuditor.RESPONSERETURNED_CATEGORY,
                    SoapEdgeAuditor.RESPONSERETURNED_MESSAGE, context);

        } catch (Exception e) {
            LOG.error("Error orchestrating Direct Soap edge message.", e);
            throw (e);
        }

        return resp;
    }

    /**
     * @param prdst XDR message to be sent to direct
     * @return Status of success or failure + error list
     * @throws TransformationException
     */
    protected RegistryResponseType sendMessage(ProvideAndRegisterDocumentSetRequestType prdst, SoapEdgeContext context)
            throws TransformationException {
        DirectDocuments documents = getDefaultXdsDirectDocumentsTransformer().transform(prdst);

        ToAddressParser toParser = new ToAddressParserFactory().getToParser();
        Address[] addressTo = toParser.parse(context.getDirectTo(), documents);

        FromAddressParser fromParser = new FromAddressParserFactory().getFromParser();
        Address addressFrom = fromParser.parse(context.getDirectFrom(), documents);

        getDirectClient().processAndSend(addressFrom, addressTo, documents, context.getMessageId());

        return new XDCommonResponseHelper().createSuccess();
    }

    /**
     * @return The DirectClient impl
     */
    private DirectClient getDirectClient() {
        if (directClient == null) {
            directClient = new DirectClientFactory().getDirectClient();
        }
        return directClient;
    }

    /**
     * @return The XdsDirectDocumentsTransformer impl
     */
    private XdsDirectDocumentsTransformer getDefaultXdsDirectDocumentsTransformer() {
        if (xdsDirectDocumentsTransformer == null) {
            xdsDirectDocumentsTransformer = new DefaultXdsDirectDocumentsTransformer();
        }
        return xdsDirectDocumentsTransformer;
    }

    /**
     * @return The SoapEdgeAuditor impl
     */
    private SoapEdgeAuditor getAuditor() {
        if (auditor == null) {
            auditor = new SoapEdgeAuditorFactory().getAuditor();
        }
        return auditor;
    }

}
