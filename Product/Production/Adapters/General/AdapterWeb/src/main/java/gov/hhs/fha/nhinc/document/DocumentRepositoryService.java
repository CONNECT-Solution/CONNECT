package gov.hhs.fha.nhinc.document;

import javax.jws.WebService;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "DocumentRepository_Service", portName = "DocumentRepository_Port_Soap", endpointInterface = "ihe.iti.xds_b._2007.DocumentRepositoryPortType", targetNamespace = "urn:ihe:iti:xds-b:2007", wsdlLocation = "WEB-INF/wsdl/DocumentRepositoryService/AdapterComponentDocRepository.wsdl")
public class DocumentRepositoryService {

    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType documentRepositoryProvideAndRegisterDocumentSetB(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body)
    {
        return new DocumentRepositoryHelper().documentRepositoryProvideAndRegisterDocumentSet(body);
    }

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType documentRepositoryRetrieveDocumentSet(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body)
    {
        return new DocumentRepositoryHelper().documentRepositoryRetrieveDocumentSet(body);
    }

}
