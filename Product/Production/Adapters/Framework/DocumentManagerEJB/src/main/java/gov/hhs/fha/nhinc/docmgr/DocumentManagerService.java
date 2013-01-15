/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.

 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information. *
 * All other rights reserved.
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
package gov.hhs.fha.nhinc.docmgr;

import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author cmatser
 */
@WebService(serviceName = "DocumentManager_Service", portName = "DocumentManager_Port_Soap", endpointInterface = "ihe.iti.xds_b._2007.DocumentManagerPortType", targetNamespace = "urn:ihe:iti:xds-b:2007", wsdlLocation = "META-INF/wsdl/DocumentManagerService/DocumentManager.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Stateless
public class DocumentManagerService {

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse documentManagerQueryForDocument(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest body) {
        return new DocumentManagerImpl().documentManagerQueryForDocument(body);
    }

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType documentManagerRetrieveDocument(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body) {
        return new DocumentManagerImpl().documentManagerRetrieveDocument(body);
    }

    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType documentManagerStoreDocument(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body) {
        return new DocumentManagerImpl().documentManagerStoreDocument(body);
    }

    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType documentManagerArchiveDocument(gov.hhs.fha.nhinc.common.docmgr.ArchiveDocumentRequestType body) {
        return new DocumentManagerImpl().documentManagerArchiveDocument(body);
    }

    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType documentManagerUpdateDocumentSlot(gov.hhs.fha.nhinc.common.docmgr.UpdateDocumentSlotRequestType body) {
        return new DocumentManagerImpl().documentManagerUpdateDocumentSlot(body);
    }

    public gov.hhs.fha.nhinc.common.docmgr.GenerateUniqueIdResponseType generateUniqueId(gov.hhs.fha.nhinc.common.docmgr.GenerateUniqueIdRequestType request) {
        return new DocumentManagerImpl().generateUniqueId(request);
    }
}
