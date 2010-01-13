/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.document;

import ihe.iti.xds_b._2007.DocumentManagerPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author cmatser
 */
@WebService(serviceName = "DocumentManager_Service", portName = "DocumentManager_Port_Soap", endpointInterface = "ihe.iti.xds_b._2007.DocumentManagerPortType", targetNamespace = "urn:ihe:iti:xds-b:2007", wsdlLocation = "META-INF/wsdl/DocumentManagerService/DocumentManager.wsdl")
@Stateless
public class DocumentManagerService implements DocumentManagerPortType {

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse documentManagerQueryDynamicDocumentArchive(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest body) {
        return new DocumentManagerImpl().documentManagerQueryDynamicDocumentArchive(body);
    }

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType documentManagerRetrieveDynamicDocument(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body) {
        return new DocumentManagerImpl().documentManagerRetrieveDynamicDocument(body);
    }

    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType documentManagerStoreDynamicDocument(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body) {
        return new DocumentManagerImpl().documentManagerStoreDynamicDocument(body);
    }

    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType documentManagerArchiveDynamicDocument(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body) {
        return new DocumentManagerImpl().documentManagerArchiveDynamicDocument(body);
    }

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse documentManagerQueryInboundRepository(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest body) {
        return new DocumentManagerImpl().documentManagerQueryInboundRepository(body);
    }

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType documentManagerRetrieveInboundDocument(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body) {
        return new DocumentManagerImpl().documentManagerRetrieveInboundDocument(body);
    }

    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType documentManagerStoreInboundDocument(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body) {
        return new DocumentManagerImpl().documentManagerStoreInboundDocument(body);
    }

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse documentManagerQueryPolicyRepository(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest body) {
        return new DocumentManagerImpl().documentManagerQueryPolicyRepository(body);
    }

    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType documentManagerStorePolicy(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body) {
        return new DocumentManagerImpl().documentManagerStorePolicy(body);
    }

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType documentManagerRetrievePolicy(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body) {
        return new DocumentManagerImpl().documentManagerRetrievePolicy(body);
    }

    public gov.hhs.fha.nhinc.common.docmgr.GenerateUniqueIdResponseType generateUniqueId(gov.hhs.fha.nhinc.common.docmgr.GenerateUniqueIdRequestType request) {
        return new DocumentManagerImpl().generateUniqueId(request);
    }

}
