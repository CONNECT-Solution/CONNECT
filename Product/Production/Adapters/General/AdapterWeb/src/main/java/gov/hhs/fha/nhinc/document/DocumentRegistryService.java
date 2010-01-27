package gov.hhs.fha.nhinc.document;


import javax.jws.WebService;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "DocumentRegistry_Service", portName = "DocumentRegistry_Port_Soap", endpointInterface = "ihe.iti.xds_b._2007.DocumentRegistryPortType", targetNamespace = "urn:ihe:iti:xds-b:2007", wsdlLocation = "WEB-INF/wsdl/DocumentRegistryService/AdapterComponentDocRegistry.wsdl")
public class DocumentRegistryService {

    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType documentRegistryRegisterDocumentSetB(oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest body)
    {
        throw new UnsupportedOperationException("Not supported.");
    }

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse documentRegistryRegistryStoredQuery(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest body)
    {
        return new DocumentRegistryHelper().documentRegistryRegistryStoredQuery(body);
    }

    public org.hl7.v3.MCCIIN000002UV01 documentRegistryPRPAIN201301UV02(org.hl7.v3.PRPAIN201301UV02 body)
    {
        throw new UnsupportedOperationException("Not supported.");
    }

    public org.hl7.v3.MCCIIN000002UV01 documentRegistryPRPAIN201302UV02(org.hl7.v3.PRPAIN201302UV02 body)
    {
        throw new UnsupportedOperationException("Not supported.");
    }

    public org.hl7.v3.MCCIIN000002UV01 documentRegistryPRPAIN201304UV02(org.hl7.v3.PRPAIN201304UV02 body)
    {
        throw new UnsupportedOperationException("Not supported.");
    }

}
