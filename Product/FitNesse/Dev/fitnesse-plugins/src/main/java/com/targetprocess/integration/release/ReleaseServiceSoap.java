
package com.targetprocess.integration.release;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "ReleaseServiceSoap", targetNamespace = "http://targetprocess.com")
@SOAPBinding(use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface ReleaseServiceSoap {


    @WebMethod(operationName = "RetrieveCommentsForRelease", action = "http://targetprocess.com/RetrieveCommentsForRelease")
    @WebResult(name = "RetrieveCommentsForReleaseResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfCommentDTO retrieveCommentsForRelease(
        @WebParam(name = "releaseID", targetNamespace = "http://targetprocess.com")
        int releaseID);

    @WebMethod(operationName = "RetrieveFeaturesForRelease", action = "http://targetprocess.com/RetrieveFeaturesForRelease")
    @WebResult(name = "RetrieveFeaturesForReleaseResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfFeatureDTO retrieveFeaturesForRelease(
        @WebParam(name = "releaseID", targetNamespace = "http://targetprocess.com")
        int releaseID);

    @WebMethod(operationName = "RetrieveBuildsForRelease", action = "http://targetprocess.com/RetrieveBuildsForRelease")
    @WebResult(name = "RetrieveBuildsForReleaseResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfBuildDTO retrieveBuildsForRelease(
        @WebParam(name = "releaseID", targetNamespace = "http://targetprocess.com")
        int releaseID);

    @WebMethod(operationName = "RemoveCommentFromRelease", action = "http://targetprocess.com/RemoveCommentFromRelease")
    public void removeCommentFromRelease(
        @WebParam(name = "releaseID", targetNamespace = "http://targetprocess.com")
        int releaseID,
        @WebParam(name = "commentID", targetNamespace = "http://targetprocess.com")
        int commentID);

    @WebMethod(operationName = "RetrieveAllForProject", action = "http://targetprocess.com/RetrieveAllForProject")
    @WebResult(name = "RetrieveAllForProjectResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfReleaseDTO retrieveAllForProject(
        @WebParam(name = "projectID", targetNamespace = "http://targetprocess.com")
        int projectID);

    @WebMethod(operationName = "AddFeatureToRelease", action = "http://targetprocess.com/AddFeatureToRelease")
    @WebResult(name = "AddFeatureToReleaseResult", targetNamespace = "http://targetprocess.com")
    public int addFeatureToRelease(
        @WebParam(name = "releaseID", targetNamespace = "http://targetprocess.com")
        int releaseID,
        @WebParam(name = "feature", targetNamespace = "http://targetprocess.com")
        FeatureDTO feature);

    @WebMethod(operationName = "Delete", action = "http://targetprocess.com/Delete")
    public void delete(
        @WebParam(name = "id", targetNamespace = "http://targetprocess.com")
        int id);

    @WebMethod(operationName = "RemoveRequestGeneralFromRelease", action = "http://targetprocess.com/RemoveRequestGeneralFromRelease")
    public void removeRequestGeneralFromRelease(
        @WebParam(name = "releaseID", targetNamespace = "http://targetprocess.com")
        int releaseID,
        @WebParam(name = "requestGeneralID", targetNamespace = "http://targetprocess.com")
        int requestGeneralID);

    @WebMethod(operationName = "AddAttachmentToRelease", action = "http://targetprocess.com/AddAttachmentToRelease")
    @WebResult(name = "AddAttachmentToReleaseResult", targetNamespace = "http://targetprocess.com")
    public int addAttachmentToRelease(
        @WebParam(name = "releaseID", targetNamespace = "http://targetprocess.com")
        int releaseID,
        @WebParam(name = "fileName", targetNamespace = "http://targetprocess.com")
        String fileName,
        @WebParam(name = "description", targetNamespace = "http://targetprocess.com")
        String description);

    @WebMethod(operationName = "RetrieveAttachedRequestsForRelease", action = "http://targetprocess.com/RetrieveAttachedRequestsForRelease")
    @WebResult(name = "RetrieveAttachedRequestsForReleaseResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfRequestGeneralDTO retrieveAttachedRequestsForRelease(
        @WebParam(name = "releaseID", targetNamespace = "http://targetprocess.com")
        int releaseID);

    @WebMethod(operationName = "RetrievePage", action = "http://targetprocess.com/RetrievePage")
    @WebResult(name = "RetrievePageResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfReleaseDTO retrievePage(
        @WebParam(name = "hql", targetNamespace = "http://targetprocess.com")
        String hql,
        @WebParam(name = "page", targetNamespace = "http://targetprocess.com")
        int page,
        @WebParam(name = "pageSize", targetNamespace = "http://targetprocess.com")
        int pageSize,
        @WebParam(name = "parameters", targetNamespace = "http://targetprocess.com")
        ArrayOfAnyType parameters);

    @WebMethod(operationName = "AddBuildToRelease", action = "http://targetprocess.com/AddBuildToRelease")
    @WebResult(name = "AddBuildToReleaseResult", targetNamespace = "http://targetprocess.com")
    public int addBuildToRelease(
        @WebParam(name = "releaseID", targetNamespace = "http://targetprocess.com")
        int releaseID,
        @WebParam(name = "build", targetNamespace = "http://targetprocess.com")
        BuildDTO build);

    @WebMethod(operationName = "Update", action = "http://targetprocess.com/Update")
    public void update(
        @WebParam(name = "entity", targetNamespace = "http://targetprocess.com")
        ReleaseDTO entity);

    @WebMethod(operationName = "RemoveIterationFromRelease", action = "http://targetprocess.com/RemoveIterationFromRelease")
    public void removeIterationFromRelease(
        @WebParam(name = "releaseID", targetNamespace = "http://targetprocess.com")
        int releaseID,
        @WebParam(name = "generalID", targetNamespace = "http://targetprocess.com")
        int generalID);

    @WebMethod(operationName = "RetrieveAllForOwner", action = "http://targetprocess.com/RetrieveAllForOwner")
    @WebResult(name = "RetrieveAllForOwnerResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfReleaseDTO retrieveAllForOwner(
        @WebParam(name = "ownerID", targetNamespace = "http://targetprocess.com")
        int ownerID);

    @WebMethod(operationName = "AddCommentToRelease", action = "http://targetprocess.com/AddCommentToRelease")
    @WebResult(name = "AddCommentToReleaseResult", targetNamespace = "http://targetprocess.com")
    public int addCommentToRelease(
        @WebParam(name = "releaseID", targetNamespace = "http://targetprocess.com")
        int releaseID,
        @WebParam(name = "comment", targetNamespace = "http://targetprocess.com")
        CommentDTO comment);

    @WebMethod(operationName = "Create", action = "http://targetprocess.com/Create")
    @WebResult(name = "CreateResult", targetNamespace = "http://targetprocess.com")
    public int create(
        @WebParam(name = "entity", targetNamespace = "http://targetprocess.com")
        ReleaseDTO entity);

    @WebMethod(operationName = "Retrieve", action = "http://targetprocess.com/Retrieve")
    @WebResult(name = "RetrieveResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfReleaseDTO retrieve(
        @WebParam(name = "hql", targetNamespace = "http://targetprocess.com")
        String hql,
        @WebParam(name = "parameters", targetNamespace = "http://targetprocess.com")
        ArrayOfAnyType parameters);

    @WebMethod(operationName = "RetrieveAttachmentsForRelease", action = "http://targetprocess.com/RetrieveAttachmentsForRelease")
    @WebResult(name = "RetrieveAttachmentsForReleaseResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfAttachmentDTO retrieveAttachmentsForRelease(
        @WebParam(name = "releaseID", targetNamespace = "http://targetprocess.com")
        int releaseID);

    @WebMethod(operationName = "RetrieveIterationsForRelease", action = "http://targetprocess.com/RetrieveIterationsForRelease")
    @WebResult(name = "RetrieveIterationsForReleaseResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfIterationDTO retrieveIterationsForRelease(
        @WebParam(name = "releaseID", targetNamespace = "http://targetprocess.com")
        int releaseID);

    @WebMethod(operationName = "GetIDs", action = "http://targetprocess.com/GetIDs")
    @WebResult(name = "GetIDsResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfInt getIDs(
        @WebParam(name = "hql", targetNamespace = "http://targetprocess.com")
        String hql,
        @WebParam(name = "parameters", targetNamespace = "http://targetprocess.com")
        ArrayOfAnyType parameters);

    @WebMethod(operationName = "RemoveAttachmentFromRelease", action = "http://targetprocess.com/RemoveAttachmentFromRelease")
    public void removeAttachmentFromRelease(
        @WebParam(name = "releaseID", targetNamespace = "http://targetprocess.com")
        int releaseID,
        @WebParam(name = "attachmentID", targetNamespace = "http://targetprocess.com")
        int attachmentID);

    @WebMethod(operationName = "RetrieveCount", action = "http://targetprocess.com/RetrieveCount")
    @WebResult(name = "RetrieveCountResult", targetNamespace = "http://targetprocess.com")
    public int retrieveCount(
        @WebParam(name = "hql", targetNamespace = "http://targetprocess.com")
        String hql,
        @WebParam(name = "parameters", targetNamespace = "http://targetprocess.com")
        ArrayOfAnyType parameters);

    @WebMethod(operationName = "RetrieveAll", action = "http://targetprocess.com/RetrieveAll")
    @WebResult(name = "RetrieveAllResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfReleaseDTO retrieveAll();

    @WebMethod(operationName = "RetrieveAllForLastCommentUser", action = "http://targetprocess.com/RetrieveAllForLastCommentUser")
    @WebResult(name = "RetrieveAllForLastCommentUserResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfReleaseDTO retrieveAllForLastCommentUser(
        @WebParam(name = "lastCommentUserID", targetNamespace = "http://targetprocess.com")
        int lastCommentUserID);

    @WebMethod(operationName = "AddIterationToRelease", action = "http://targetprocess.com/AddIterationToRelease")
    @WebResult(name = "AddIterationToReleaseResult", targetNamespace = "http://targetprocess.com")
    public int addIterationToRelease(
        @WebParam(name = "releaseID", targetNamespace = "http://targetprocess.com")
        int releaseID,
        @WebParam(name = "iteration", targetNamespace = "http://targetprocess.com")
        IterationDTO iteration);

    @WebMethod(operationName = "GetByID", action = "http://targetprocess.com/GetByID")
    @WebResult(name = "GetByIDResult", targetNamespace = "http://targetprocess.com")
    public ReleaseDTO getByID(
        @WebParam(name = "id", targetNamespace = "http://targetprocess.com")
        int id);

    @WebMethod(operationName = "RemoveBuildFromRelease", action = "http://targetprocess.com/RemoveBuildFromRelease")
    public void removeBuildFromRelease(
        @WebParam(name = "releaseID", targetNamespace = "http://targetprocess.com")
        int releaseID,
        @WebParam(name = "buildID", targetNamespace = "http://targetprocess.com")
        int buildID);

    @WebMethod(operationName = "AddRequestGeneralToRelease", action = "http://targetprocess.com/AddRequestGeneralToRelease")
    @WebResult(name = "AddRequestGeneralToReleaseResult", targetNamespace = "http://targetprocess.com")
    public int addRequestGeneralToRelease(
        @WebParam(name = "releaseID", targetNamespace = "http://targetprocess.com")
        int releaseID,
        @WebParam(name = "requestGeneral", targetNamespace = "http://targetprocess.com")
        RequestGeneralDTO requestGeneral);

    @WebMethod(operationName = "RemoveFeatureFromRelease", action = "http://targetprocess.com/RemoveFeatureFromRelease")
    public void removeFeatureFromRelease(
        @WebParam(name = "releaseID", targetNamespace = "http://targetprocess.com")
        int releaseID,
        @WebParam(name = "generalID", targetNamespace = "http://targetprocess.com")
        int generalID);

}
