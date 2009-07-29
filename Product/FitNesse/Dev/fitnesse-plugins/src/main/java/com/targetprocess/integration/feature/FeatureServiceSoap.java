
package com.targetprocess.integration.feature;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "FeatureServiceSoap", targetNamespace = "http://targetprocess.com")
@SOAPBinding(use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface FeatureServiceSoap {


    @WebMethod(operationName = "RemoveUserStoryFromFeature", action = "http://targetprocess.com/RemoveUserStoryFromFeature")
    public void removeUserStoryFromFeature(
        @WebParam(name = "featureID", targetNamespace = "http://targetprocess.com")
        int featureID,
        @WebParam(name = "generalID", targetNamespace = "http://targetprocess.com")
        int generalID);

    @WebMethod(operationName = "AddCommentToFeature", action = "http://targetprocess.com/AddCommentToFeature")
    @WebResult(name = "AddCommentToFeatureResult", targetNamespace = "http://targetprocess.com")
    public int addCommentToFeature(
        @WebParam(name = "featureID", targetNamespace = "http://targetprocess.com")
        int featureID,
        @WebParam(name = "comment", targetNamespace = "http://targetprocess.com")
        CommentDTO comment);

    @WebMethod(operationName = "RemoveRequestGeneralFromFeature", action = "http://targetprocess.com/RemoveRequestGeneralFromFeature")
    public void removeRequestGeneralFromFeature(
        @WebParam(name = "featureID", targetNamespace = "http://targetprocess.com")
        int featureID,
        @WebParam(name = "requestGeneralID", targetNamespace = "http://targetprocess.com")
        int requestGeneralID);

    @WebMethod(operationName = "RetrieveAllForRelease", action = "http://targetprocess.com/RetrieveAllForRelease")
    @WebResult(name = "RetrieveAllForReleaseResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfFeatureDTO retrieveAllForRelease(
        @WebParam(name = "releaseID", targetNamespace = "http://targetprocess.com")
        int releaseID);

    @WebMethod(operationName = "RetrieveAttachmentsForFeature", action = "http://targetprocess.com/RetrieveAttachmentsForFeature")
    @WebResult(name = "RetrieveAttachmentsForFeatureResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfAttachmentDTO retrieveAttachmentsForFeature(
        @WebParam(name = "featureID", targetNamespace = "http://targetprocess.com")
        int featureID);

    @WebMethod(operationName = "RemoveCommentFromFeature", action = "http://targetprocess.com/RemoveCommentFromFeature")
    public void removeCommentFromFeature(
        @WebParam(name = "featureID", targetNamespace = "http://targetprocess.com")
        int featureID,
        @WebParam(name = "commentID", targetNamespace = "http://targetprocess.com")
        int commentID);

    @WebMethod(operationName = "RetrieveAllForProject", action = "http://targetprocess.com/RetrieveAllForProject")
    @WebResult(name = "RetrieveAllForProjectResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfFeatureDTO retrieveAllForProject(
        @WebParam(name = "projectID", targetNamespace = "http://targetprocess.com")
        int projectID);

    @WebMethod(operationName = "Delete", action = "http://targetprocess.com/Delete")
    public void delete(
        @WebParam(name = "id", targetNamespace = "http://targetprocess.com")
        int id);

    @WebMethod(operationName = "AddAttachmentToFeature", action = "http://targetprocess.com/AddAttachmentToFeature")
    @WebResult(name = "AddAttachmentToFeatureResult", targetNamespace = "http://targetprocess.com")
    public int addAttachmentToFeature(
        @WebParam(name = "featureID", targetNamespace = "http://targetprocess.com")
        int featureID,
        @WebParam(name = "fileName", targetNamespace = "http://targetprocess.com")
        String fileName,
        @WebParam(name = "description", targetNamespace = "http://targetprocess.com")
        String description);

    @WebMethod(operationName = "RetrievePage", action = "http://targetprocess.com/RetrievePage")
    @WebResult(name = "RetrievePageResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfFeatureDTO retrievePage(
        @WebParam(name = "hql", targetNamespace = "http://targetprocess.com")
        String hql,
        @WebParam(name = "page", targetNamespace = "http://targetprocess.com")
        int page,
        @WebParam(name = "pageSize", targetNamespace = "http://targetprocess.com")
        int pageSize,
        @WebParam(name = "parameters", targetNamespace = "http://targetprocess.com")
        ArrayOfAnyType parameters);

    @WebMethod(operationName = "Update", action = "http://targetprocess.com/Update")
    public void update(
        @WebParam(name = "entity", targetNamespace = "http://targetprocess.com")
        FeatureDTO entity);

    @WebMethod(operationName = "RetrieveAllForOwner", action = "http://targetprocess.com/RetrieveAllForOwner")
    @WebResult(name = "RetrieveAllForOwnerResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfFeatureDTO retrieveAllForOwner(
        @WebParam(name = "ownerID", targetNamespace = "http://targetprocess.com")
        int ownerID);

    @WebMethod(operationName = "AddRequestGeneralToFeature", action = "http://targetprocess.com/AddRequestGeneralToFeature")
    @WebResult(name = "AddRequestGeneralToFeatureResult", targetNamespace = "http://targetprocess.com")
    public int addRequestGeneralToFeature(
        @WebParam(name = "featureID", targetNamespace = "http://targetprocess.com")
        int featureID,
        @WebParam(name = "requestGeneral", targetNamespace = "http://targetprocess.com")
        RequestGeneralDTO requestGeneral);

    @WebMethod(operationName = "Create", action = "http://targetprocess.com/Create")
    @WebResult(name = "CreateResult", targetNamespace = "http://targetprocess.com")
    public int create(
        @WebParam(name = "entity", targetNamespace = "http://targetprocess.com")
        FeatureDTO entity);

    @WebMethod(operationName = "RetrieveAllForPriority", action = "http://targetprocess.com/RetrieveAllForPriority")
    @WebResult(name = "RetrieveAllForPriorityResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfFeatureDTO retrieveAllForPriority(
        @WebParam(name = "priorityID", targetNamespace = "http://targetprocess.com")
        int priorityID);

    @WebMethod(operationName = "Retrieve", action = "http://targetprocess.com/Retrieve")
    @WebResult(name = "RetrieveResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfFeatureDTO retrieve(
        @WebParam(name = "hql", targetNamespace = "http://targetprocess.com")
        String hql,
        @WebParam(name = "parameters", targetNamespace = "http://targetprocess.com")
        ArrayOfAnyType parameters);

    @WebMethod(operationName = "RetrieveUserStoriesForFeature", action = "http://targetprocess.com/RetrieveUserStoriesForFeature")
    @WebResult(name = "RetrieveUserStoriesForFeatureResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfUserStoryDTO retrieveUserStoriesForFeature(
        @WebParam(name = "featureID", targetNamespace = "http://targetprocess.com")
        int featureID);

    @WebMethod(operationName = "GetIDs", action = "http://targetprocess.com/GetIDs")
    @WebResult(name = "GetIDsResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfInt getIDs(
        @WebParam(name = "hql", targetNamespace = "http://targetprocess.com")
        String hql,
        @WebParam(name = "parameters", targetNamespace = "http://targetprocess.com")
        ArrayOfAnyType parameters);

    @WebMethod(operationName = "RetrieveAttachedRequestsForFeature", action = "http://targetprocess.com/RetrieveAttachedRequestsForFeature")
    @WebResult(name = "RetrieveAttachedRequestsForFeatureResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfRequestGeneralDTO retrieveAttachedRequestsForFeature(
        @WebParam(name = "featureID", targetNamespace = "http://targetprocess.com")
        int featureID);

    @WebMethod(operationName = "RetrieveCount", action = "http://targetprocess.com/RetrieveCount")
    @WebResult(name = "RetrieveCountResult", targetNamespace = "http://targetprocess.com")
    public int retrieveCount(
        @WebParam(name = "hql", targetNamespace = "http://targetprocess.com")
        String hql,
        @WebParam(name = "parameters", targetNamespace = "http://targetprocess.com")
        ArrayOfAnyType parameters);

    @WebMethod(operationName = "GetPriorities", action = "http://targetprocess.com/GetPriorities")
    @WebResult(name = "GetPrioritiesResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfPriorityDTO getPriorities();

    @WebMethod(operationName = "AddUserStoryToFeature", action = "http://targetprocess.com/AddUserStoryToFeature")
    @WebResult(name = "AddUserStoryToFeatureResult", targetNamespace = "http://targetprocess.com")
    public int addUserStoryToFeature(
        @WebParam(name = "featureID", targetNamespace = "http://targetprocess.com")
        int featureID,
        @WebParam(name = "userStory", targetNamespace = "http://targetprocess.com")
        UserStoryDTO userStory);

    @WebMethod(operationName = "RemoveAttachmentFromFeature", action = "http://targetprocess.com/RemoveAttachmentFromFeature")
    public void removeAttachmentFromFeature(
        @WebParam(name = "featureID", targetNamespace = "http://targetprocess.com")
        int featureID,
        @WebParam(name = "attachmentID", targetNamespace = "http://targetprocess.com")
        int attachmentID);

    @WebMethod(operationName = "RetrieveAllForLastCommentUser", action = "http://targetprocess.com/RetrieveAllForLastCommentUser")
    @WebResult(name = "RetrieveAllForLastCommentUserResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfFeatureDTO retrieveAllForLastCommentUser(
        @WebParam(name = "lastCommentUserID", targetNamespace = "http://targetprocess.com")
        int lastCommentUserID);

    @WebMethod(operationName = "GetByID", action = "http://targetprocess.com/GetByID")
    @WebResult(name = "GetByIDResult", targetNamespace = "http://targetprocess.com")
    public FeatureDTO getByID(
        @WebParam(name = "id", targetNamespace = "http://targetprocess.com")
        int id);

    @WebMethod(operationName = "RetrieveAll", action = "http://targetprocess.com/RetrieveAll")
    @WebResult(name = "RetrieveAllResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfFeatureDTO retrieveAll();

    @WebMethod(operationName = "RetrieveCommentsForFeature", action = "http://targetprocess.com/RetrieveCommentsForFeature")
    @WebResult(name = "RetrieveCommentsForFeatureResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfCommentDTO retrieveCommentsForFeature(
        @WebParam(name = "featureID", targetNamespace = "http://targetprocess.com")
        int featureID);

}
