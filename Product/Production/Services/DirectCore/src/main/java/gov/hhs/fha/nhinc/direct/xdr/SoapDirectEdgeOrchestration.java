/*
 * Copyright (c) 2009-2013, United States Government, as represented by the Secretary of Health and Human Services.
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

import gov.hhs.fha.nhinc.direct.DirectAdapterFactory;
import gov.hhs.fha.nhinc.direct.DirectSender;
import gov.hhs.fha.nhinc.direct.addressparsing.FromAddressParser;
import gov.hhs.fha.nhinc.direct.addressparsing.FromAddressParserFactory;
import gov.hhs.fha.nhinc.direct.addressparsing.ToAddressParser;
import gov.hhs.fha.nhinc.direct.addressparsing.ToAddressParserFactory;
import gov.hhs.fha.nhinc.direct.xdr.audit.SoapEdgeAuditor;
import gov.hhs.fha.nhinc.direct.xdr.audit.SoapEdgeAuditorFactory;
import gov.hhs.fha.nhinc.xdcommon.XDCommonResponseHelper;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;

import java.util.Set;

import javax.mail.Address;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.commons.collections.CollectionUtils;
import org.nhindirect.xd.common.DirectDocuments;
import org.nhindirect.xd.transform.XdsDirectDocumentsTransformer;
import org.nhindirect.xd.transform.exception.TransformationException;
import org.nhindirect.xd.transform.impl.DefaultXdsDirectDocumentsTransformer;

public class SoapDirectEdgeOrchestration {

    private XdsDirectDocumentsTransformer xdsDirectDocumentsTransformer = new DefaultXdsDirectDocumentsTransformer();
    private DirectSender directSender;
    private SoapEdgeAuditor auditor = new SoapEdgeAuditorFactory().getAuditor();
    private ToAddressParser toParser = new ToAddressParserFactory().getToParser();

    /**
     * Handle an incoming ProvideAndRegisterDocumentSetRequestType object and transform to XDM.
     * 
     * @param prdst
     *            The incoming ProvideAndRegisterDocumentSetRequestType object
     * @param context
     *            The values of the ws headers
     * @return a RegistryResponseType object
     * @throws Exception
     */
    public RegistryResponseType orchestrate(ProvideAndRegisterDocumentSetRequestType prdst, SoapEdgeContext context)
            throws Exception {
        RegistryResponseType resp = null;

        auditor.audit(SoapEdgeAuditor.PRINCIPAL, SoapEdgeAuditor.REQUESTRECIEVED_CATEGORY,
                SoapEdgeAuditor.REQUESTRECIEVED_MESSAGE, context);

        resp = sendMessage(prdst, context);

        auditor.audit(SoapEdgeAuditor.PRINCIPAL, SoapEdgeAuditor.RESPONSERETURNED_CATEGORY,
                SoapEdgeAuditor.RESPONSERETURNED_MESSAGE, context);

        return resp;
    }

    /**
     * @param prdst
     *            XDR message to be sent to direct
     * @return Status of success or failure + error list
     * @throws TransformationException
     */
    protected RegistryResponseType sendMessage(ProvideAndRegisterDocumentSetRequestType prdst, SoapEdgeContext context)
            throws TransformationException {
        DirectDocuments documents = xdsDirectDocumentsTransformer.transform(prdst);

        Set<Address> addressTo = toParser.parse(context.getDirectTo(), documents);
        if (CollectionUtils.isEmpty(addressTo)) {
            throw new TransformationException("No 'To' addresses in soap context.", null);
        }

        FromAddressParser fromParser = new FromAddressParserFactory().getFromParser();
        Address addressFrom = fromParser.parse(context.getDirectFrom(), documents);

        getDirectSender().sendOutboundDirect(addressFrom, addressTo.toArray(new Address[0]), documents,
                context.getMessageId());

        return new XDCommonResponseHelper().createSuccess();
    }

    protected void setDocumentsTransformer(XdsDirectDocumentsTransformer docTransformer) {
        xdsDirectDocumentsTransformer = docTransformer;
    }

    public void setToAddressParser(ToAddressParser toParser) {
        this.toParser = toParser;
    }

    /**
     * We do this initialization lazily because the spring configuration is an external dependency we don't set up in
     * the unit test.
     */
    private synchronized DirectSender getDirectSender() {
        if (directSender == null) {
            directSender = new DirectAdapterFactory().getDirectSender();
        }
        return directSender;
    }
}
