package gov.hhs.fha.nhinc.transform.policy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import java.util.List;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
public class DocRetrieveDeferredTransformHelper {

    private static final String ActionInValue = "DocRetrieveDeferredResponseIn";
    private static final String ActionOutValue = "DocRetrieveDeferredResponseOut";
    private static Log log = null;
    private boolean debugEnabled = false;

    /**
     * default constructor
     */
    public DocRetrieveDeferredTransformHelper() {
        log = createLogger();
        debugEnabled = log.isDebugEnabled();
    }

    /**
     *
     * @return Log
     */
    protected Log createLogger() {
        return (log != null) ? log : LogFactory.getLog(this.getClass());
    }

    /**
     * 
     * @param message
     * @param assertion
     * @return CheckPolicyRequestType
     */
    public CheckPolicyRequestType transformNhinDocRetrieveDeferredRespToCheckPolicy(RetrieveDocumentSetResponseType message, AssertionType assertion) {
        if (debugEnabled) {
            log.debug("-- Begin DocRetrieveDeferredTransformHelper.transformNhinDocRetrieveDeferredRespToCheckPolicy --");
        }
        CheckPolicyRequestType result = new CheckPolicyRequestType();
        HomeCommunityType hc = assertion.getHomeCommunity();
        RequestType request = new RequestType();
        if (assertion == null) {
            log.error("Missing Assertion");
            return result;
        }
        if (message == null) {
            log.error("Missing message");
            return result;
        }
        if (debugEnabled) {
            log.debug("transformNhinDocRetrieveDeferredRespToCheckPolicy - adding assertion data");
        }
        AssertionHelper assertHelp = new AssertionHelper();
        assertHelp.appendAssertionDataToRequest(request, assertion);
        request.setAction(ActionHelper.actionFactory(ActionInValue));
        SubjectType subject = createSubject(hc, assertion);
        request.getSubject().add(subject);
        List<DocumentResponse> docRequestList = message.getDocumentResponse();
        if (docRequestList != null && docRequestList.size() > 0) {
            for (DocumentResponse eachResponse : docRequestList) {
                request.getResource().add(getResource(eachResponse));
            }
        }
        result.setRequest(request);
        result.setAssertion(assertion);
        if (debugEnabled) {
            log.debug("-- End DocRetrieveDeferredTransformHelper.transformNhinDocRetrieveDeferredToCheckPolicy --");
        }
        return result;
    }

    /**
     * 
     * @param message
     * @param assertion
     * @param target
     * @return CheckPolicyRequestType
     */
    public CheckPolicyRequestType transformEntityDocRetrieveDeferredRespToCheckPolicy(RetrieveDocumentSetResponseType message, AssertionType assertion, String target) {
        if (debugEnabled) {
            log.debug("-- Begin DocRetrieveDeferredTransformHelper.transformEntityDocRetrieveDeferredRespToCheckPolicy --");
        }

        CheckPolicyRequestType result = new CheckPolicyRequestType();
        if (message == null) {
            log.error("Request is null.");
            return result;
        }
        if (target == null || target.isEmpty()) {
            log.error("target is missing");
            return result;
        }

        if (message.getDocumentResponse() == null) {
            log.error("missing body");
            return result;
        }
        if (assertion == null) {
            log.error("missing assertion");
            return result;
        }

        HomeCommunityType hc = new HomeCommunityType();
        hc.setHomeCommunityId(target);

        RequestType request = new RequestType();
        log.debug("transformEntityDocRetrieveDeferredRespToCheckPolicy - adding subject");
        SubjectType subject = createSubject(hc, assertion);
        request.getSubject().add(subject);
        List<DocumentResponse> docRequestList = message.getDocumentResponse();
        if (docRequestList != null && docRequestList.size() > 0) {
            for (DocumentResponse eachResponse : docRequestList) {
                request.getResource().add(getResource(eachResponse));
            }
        }

        if (debugEnabled) {
            log.debug("transformEntityDocRetrieveDeferredRespToCheckPolicy - adding assertion data");
        }
        AssertionHelper assertHelp = new AssertionHelper();
        assertHelp.appendAssertionDataToRequest(request, assertion);
        request.setAction(ActionHelper.actionFactory(ActionOutValue));
        result.setRequest(request);
        result.setAssertion(assertion);
        if (debugEnabled) {
            log.debug("-- End DocRetrieveDeferredTransformHelper.transformEntityDocRetrieveDeferredRespToCheckPolicy --");
        }
        return result;

    }

    /**
     * 
     * @param hc
     * @param assertion
     * @return SubjectType
     */
    protected SubjectType createSubject(HomeCommunityType hc, AssertionType assertion) {
        if (debugEnabled) {
            log.debug("-- Begin DocRetrieveDeferredTransformHelper.createSubject --");
        }
        SubjectHelper subjHelp = new SubjectHelper();
        SubjectType subject = subjHelp.subjectFactory(hc, assertion);
        subject.setSubjectCategory(SubjectHelper.SubjectCategory);
        if (debugEnabled) {
            log.debug("-- End DocRetrieveDeferredTransformHelper.createSubject --");
        }
        return subject;
    }

    /**
     * 
     * @param documentRequest
     * @return ResourceType
     */
    private static ResourceType getResource(DocumentResponse documentResponse) {
        String homeCommunityId = documentResponse.getHomeCommunityId();
        String repositoryUniqueId = documentResponse.getRepositoryUniqueId();
        String documentId = documentResponse.getDocumentUniqueId();
        ResourceType resource = new ResourceType();
        AttributeHelper attrHelper = new AttributeHelper();
        resource.getAttribute().add(attrHelper.attributeFactory(Constants.HomeCommunityAttributeId, Constants.DataTypeString, homeCommunityId));
        resource.getAttribute().add(attrHelper.attributeFactory(Constants.RespositoryAttributeId, Constants.DataTypeString, repositoryUniqueId));
        resource.getAttribute().add(attrHelper.attributeFactory(Constants.DocumentAttributeId, Constants.DataTypeString, documentId));
        return resource;
    }
}
