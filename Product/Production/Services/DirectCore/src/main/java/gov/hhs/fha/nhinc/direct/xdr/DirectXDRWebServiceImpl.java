package gov.hhs.fha.nhinc.direct.xdr;

import javax.xml.ws.WebServiceContext;

import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

public class DirectXDRWebServiceImpl extends org.nhind.xdr.DocumentRepositoryAbstract {

	WebServiceContext context = null;
	
	public RegistryResponseType provideAndRegisterDocumentSet(
			ProvideAndRegisterDocumentSetRequestType body, WebServiceContext wsContext) throws Exception {
		this.context = wsContext;
		
		return provideAndRegisterDocumentSet(body);
	}
}
