
package com.targetprocess.integration.iteration;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "IterationServiceSoap", targetNamespace = "http://targetprocess.com")
@SOAPBinding(use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface IterationServiceSoap {


    @WebMethod(operationName = "AddCommentToIteration", action = "http://targetprocess.com/AddCommentToIteration")
    @WebResult(name = "AddCommentToIterationResult", targetNamespace = "http://targetprocess.com")
    public int addCommentToIteration(
        @WebParam(name = "iterationID", targetNamespace = "http://targetprocess.com")
        int iterationID,
        @WebParam(name = "comment", targetNamespace = "http://targetprocess.com")
        CommentDTO comment);

    @WebMethod(operationName = "RetrieveAllForRelease", action = "http://targetprocess.com/RetrieveAllForRelease")
    @WebResult(name = "RetrieveAllForReleaseResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfIterationDTO retrieveAllForRelease(
        @WebParam(name = "releaseID", targetNamespace = "http://targetprocess.com")
        int releaseID);

    @WebMethod(operationName = "RemoveUserStoryFromIteration", action = "http://targetprocess.com/RemoveUserStoryFromIteration")
    public void removeUserStoryFromIteration(
        @WebParam(name = "iterationID", targetNamespace = "http://targetprocess.com")
        int iterationID,
        @WebParam(name = "generalID", targetNamespace = "http://targetprocess.com")
        int generalID);

    @WebMethod(operationName = "AddUserStoryToIteration", action = "http://targetprocess.com/AddUserStoryToIteration")
    @WebResult(name = "AddUserStoryToIterationResult", targetNamespace = "http://targetprocess.com")
    public int addUserStoryToIteration(
        @WebParam(name = "iterationID", targetNamespace = "http://targetprocess.com")
        int iterationID,
        @WebParam(name = "userStory", targetNamespace = "http://targetprocess.com")
        UserStoryDTO userStory);

    @WebMethod(operationName = "RetrieveAttachedRequestsForIteration", action = "http://targetprocess.com/RetrieveAttachedRequestsForIteration")
    @WebResult(name = "RetrieveAttachedRequestsForIterationResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfRequestGeneralDTO retrieveAttachedRequestsForIteration(
        @WebParam(name = "iterationID", targetNamespace = "http://targetprocess.com")
        int iterationID);

    @WebMethod(operationName = "RetrieveAllForProject", action = "http://targetprocess.com/RetrieveAllForProject")
    @WebResult(name = "RetrieveAllForProjectResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfIterationDTO retrieveAllForProject(
        @WebParam(name = "projectID", targetNamespace = "http://targetprocess.com")
        int projectID);

    @WebMethod(operationName = "Delete", action = "http://targetprocess.com/Delete")
    public void delete(
        @WebParam(name = "id", targetNamespace = "http://targetprocess.com")
        int id);

    @WebMethod(operationName = "RemoveBuildFromIteration", action = "http://targetprocess.com/RemoveBuildFromIteration")
    public void removeBuildFromIteration(
        @WebParam(name = "iterationID", targetNamespace = "http://targetprocess.com")
        int iterationID,
        @WebParam(name = "buildID", targetNamespace = "http://targetprocess.com")
        int buildID);

    @WebMethod(operationName = "RemoveAttachmentFromIteration", action = "http://targetprocess.com/RemoveAttachmentFromIteration")
    public void removeAttachmentFromIteration(
        @WebParam(name = "iterationID", targetNamespace = "http://targetprocess.com")
        int iterationID,
        @WebParam(name = "attachmentID", targetNamespace = "http://targetprocess.com")
        int attachmentID);

    @WebMethod(operationName = "RemoveCommentFromIteration", action = "http://targetprocess.com/RemoveCommentFromIteration")
    public void removeCommentFromIteration(
        @WebParam(name = "iterationID", targetNamespace = "http://targetprocess.com")
        int iterationID,
        @WebParam(name = "commentID", targetNamespace = "http://targetprocess.com")
        int commentID);

    @WebMethod(operationName = "AddRequestGeneralToIteration", action = "http://targetprocess.com/AddRequestGeneralToIteration")
    @WebResult(name = "AddRequestGeneralToIterationResult", targetNamespace = "http://targetprocess.com")
    public int addRequestGeneralToIteration(
        @WebParam(name = "iterationID", targetNamespace = "http://targetprocess.com")
        int iterationID,
        @WebParam(name = "requestGeneral", targetNamespace = "http://targetprocess.com")
        RequestGeneralDTO requestGeneral);

    @WebMethod(operationName = "RemoveRequestGeneralFromIteration", action = "http://targetprocess.com/RemoveRequestGeneralFromIteration")
    public void removeRequestGeneralFromIteration(
        @WebParam(name = "iterationID", targetNamespace = "http://targetprocess.com")
        int iterationID,
        @WebParam(name = "requestGeneralID", targetNamespace = "http://targetprocess.com")
        int requestGeneralID);

    @WebMethod(operationName = "RetrieveAttachmentsForIteration", action = "http://targetprocess.com/RetrieveAttachmentsForIteration")
    @WebResult(name = "RetrieveAttachmentsForIterationResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfAttachmentDTO retrieveAttachmentsForIteration(
        @WebParam(name = "iterationID", targetNamespace = "http://targetprocess.com")
        int iterationID);

    @WebMethod(operationName = "RetrievePage", action = "http://targetprocess.com/RetrievePage")
    @WebResult(name = "RetrievePageResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfIterationDTO retrievePage(
        @WebParam(name = "hql", targetNamespace = "http://targetprocess.com")
        String hql,
        @WebParam(name = "page", targetNamespace = "http://targetprocess.com")
        int page,
        @WebParam(name = "pageSize", targetNamespace = "http://targetprocess.com")
        int pageSize,
        @WebParam(name = "parameters", targetNamespace = "http://targetprocess.com")
        ArrayOfAnyType parameters);

    @WebMethod(operationName = "AddBuildToIteration", action = "http://targetprocess.com/AddBuildToIteration")
    @WebResult(name = "AddBuildToIterationResult", targetNamespace = "http://targetprocess.com")
    public int addBuildToIteration(
        @WebParam(name = "iterationID", targetNamespace = "http://targetprocess.com")
        int iterationID,
        @WebParam(name = "build", targetNamespace = "http://targetprocess.com")
        BuildDTO build);

    @WebMethod(operationName = "Update", action = "http://targetprocess.com/Update")
    public void update(
        @WebParam(name = "entity", targetNamespace = "http://targetprocess.com")
        IterationDTO entity);

    @WebMethod(operationName = "RetrieveAllForOwner", action = "http://targetprocess.com/RetrieveAllForOwner")
    @WebResult(name = "RetrieveAllForOwnerResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfIterationDTO retrieveAllForOwner(
        @WebParam(name = "ownerID", targetNamespace = "http://targetprocess.com")
        int ownerID);

    @WebMethod(operationName = "RetrieveCommentsForIteration", action = "http://targetprocess.com/RetrieveCommentsForIteration")
    @WebResult(name = "RetrieveCommentsForIterationResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfCommentDTO retrieveCommentsForIteration(
        @WebParam(name = "iterationID", targetNamespace = "http://targetprocess.com")
        int iterationID);

    @WebMethod(operationName = "RetrieveBuildsForIteration", action = "http://targetprocess.com/RetrieveBuildsForIteration")
    @WebResult(name = "RetrieveBuildsForIterationResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfBuildDTO retrieveBuildsForIteration(
        @WebParam(name = "iterationID", targetNamespace = "http://targetprocess.com")
        int iterationID);

    @WebMethod(operationName = "Create", action = "http://targetprocess.com/Create")
    @WebResult(name = "CreateResult", targetNamespace = "http://targetprocess.com")
    public int create(
        @WebParam(name = "entity", targetNamespace = "http://targetprocess.com")
        IterationDTO entity);

    @WebMethod(operationName = "Retrieve", action = "http://targetprocess.com/Retrieve")
    @WebResult(name = "RetrieveResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfIterationDTO retrieve(
        @WebParam(name = "hql", targetNamespace = "http://targetprocess.com")
        String hql,
        @WebParam(name = "parameters", targetNamespace = "http://targetprocess.com")
        ArrayOfAnyType parameters);

    @WebMethod(operationName = "GetIDs", action = "http://targetprocess.com/GetIDs")
    @WebResult(name = "GetIDsResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfInt getIDs(
        @WebParam(name = "hql", targetNamespace = "http://targetprocess.com")
        String hql,
        @WebParam(name = "parameters", targetNamespace = "http://targetprocess.com")
        ArrayOfAnyType parameters);

    @WebMethod(operationName = "RetrieveCount", action = "http://targetprocess.com/RetrieveCount")
    @WebResult(name = "RetrieveCountResult", targetNamespace = "http://targetprocess.com")
    public int retrieveCount(
        @WebParam(name = "hql", targetNamespace = "http://targetprocess.com")
        String hql,
        @WebParam(name = "parameters", targetNamespace = "http://targetprocess.com")
        ArrayOfAnyType parameters);

    @WebMethod(operationName = "RetrieveAllForLastCommentUser", action = "http://targetprocess.com/RetrieveAllForLastCommentUser")
    @WebResult(name = "RetrieveAllForLastCommentUserResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfIterationDTO retrieveAllForLastCommentUser(
        @WebParam(name = "lastCommentUserID", targetNamespace = "http://targetprocess.com")
        int lastCommentUserID);

    @WebMethod(operationName = "RetrieveUserStoriesForIteration", action = "http://targetprocess.com/RetrieveUserStoriesForIteration")
    @WebResult(name = "RetrieveUserStoriesForIterationResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfUserStoryDTO retrieveUserStoriesForIteration(
        @WebParam(name = "iterationID", targetNamespace = "http://targetprocess.com")
        int iterationID);

    @WebMethod(operationName = "GetByID", action = "http://targetprocess.com/GetByID")
    @WebResult(name = "GetByIDResult", targetNamespace = "http://targetprocess.com")
    public IterationDTO getByID(
        @WebParam(name = "id", targetNamespace = "http://targetprocess.com")
        int id);

    @WebMethod(operationName = "RetrieveAll", action = "http://targetprocess.com/RetrieveAll")
    @WebResult(name = "RetrieveAllResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfIterationDTO retrieveAll();

    @WebMethod(operationName = "AddAttachmentToIteration", action = "http://targetprocess.com/AddAttachmentToIteration")
    @WebResult(name = "AddAttachmentToIterationResult", targetNamespace = "http://targetprocess.com")
    public int addAttachmentToIteration(
        @WebParam(name = "iterationID", targetNamespace = "http://targetprocess.com")
        int iterationID,
        @WebParam(name = "fileName", targetNamespace = "http://targetprocess.com")
        String fileName,
        @WebParam(name = "description", targetNamespace = "http://targetprocess.com")
        String description);

}
