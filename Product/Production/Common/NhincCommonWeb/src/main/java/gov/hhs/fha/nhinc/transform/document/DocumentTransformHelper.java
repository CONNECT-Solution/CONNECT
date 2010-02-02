package gov.hhs.fha.nhinc.transform.document;

import gov.hhs.fha.nhinc.common.nhinccommondocumenttransform.ReplaceAdhocQueryPatientIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommondocumenttransform.ReplaceAdhocQueryPatientIdRequestType;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class to perform transform operations for document query messages.
 * 
 * @author Neil Webb
 */
public class DocumentTransformHelper 
{
    private static Log log = LogFactory.getLog(DocumentTransformHelper.class);
    
    /**
     * Replace the patient identifier information in an AdhocQuery message with the information
     * provided in the request. The information replaced includes the community id, assigning 
     * authority, and patient identifier.
     * 
     * @param replaceAdhocQueryPatientIdRequest Request message containing the AdhocQuery message
     * and patient identifier information
     * @return Altered AdhocQuery Message
     */
    public ReplaceAdhocQueryPatientIdResponseType replaceAdhocQueryPatientId(ReplaceAdhocQueryPatientIdRequestType replaceAdhocQueryPatientIdRequest)
    {
        log.debug("DocumentTransformHelper.replaceAdhocQueryPatientId() -- Begin");
        ReplaceAdhocQueryPatientIdResponseType response = new ReplaceAdhocQueryPatientIdResponseType();
        
        if ((replaceAdhocQueryPatientIdRequest != null) &&
            (replaceAdhocQueryPatientIdRequest.getAdhocQueryRequest() != null))
        {
            // Collect input data
            String homeCommunityId = replaceAdhocQueryPatientIdRequest.getHomeCommunityId();
            String assigningAuthorityId = null;
            String patientId = null;
            if(replaceAdhocQueryPatientIdRequest.getQualifiedSubjectIdentifier() != null)
            {
                assigningAuthorityId = replaceAdhocQueryPatientIdRequest.getQualifiedSubjectIdentifier().getAssigningAuthorityIdentifier();
                patientId = replaceAdhocQueryPatientIdRequest.getQualifiedSubjectIdentifier().getSubjectIdentifier();
            }
            
            // Call transform
            DocumentQueryTransform transform = new DocumentQueryTransform();
            AdhocQueryRequest adhocQueryRequest = transform.replaceAdhocQueryPatientId(replaceAdhocQueryPatientIdRequest.getAdhocQueryRequest(), homeCommunityId, assigningAuthorityId, patientId);
            response.setAdhocQueryRequest(adhocQueryRequest);
            
        }

        log.debug("DocumentQueryTransform.replaceAdhocQueryPatientId() -- End");
        return response;
    }
    
}
