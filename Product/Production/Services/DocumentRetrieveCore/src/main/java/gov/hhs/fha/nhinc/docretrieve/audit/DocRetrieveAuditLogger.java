package gov.hhs.fha.nhinc.docretrieve.audit;

import gov.hhs.fha.nhinc.audit.AuditLogger;
import gov.hhs.fha.nhinc.audit.transform.AuditTransforms;
import gov.hhs.fha.nhinc.docretrieve.audit.transform.DocRetrieveAuditTransforms;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

public class DocRetrieveAuditLogger
		extends AuditLogger<RetrieveDocumentSetRequestType, RetrieveDocumentSetResponseType> {

	@Override
	protected AuditTransforms<RetrieveDocumentSetRequestType, RetrieveDocumentSetResponseType> getAuditTransforms() {
		return new DocRetrieveAuditTransforms();
	}

}
