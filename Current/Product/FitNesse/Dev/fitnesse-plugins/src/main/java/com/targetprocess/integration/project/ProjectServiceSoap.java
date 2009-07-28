
package com.targetprocess.integration.project;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "ProjectServiceSoap", targetNamespace = "http://targetprocess.com")
@SOAPBinding(use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface ProjectServiceSoap {


    @WebMethod(operationName = "RetrieveAllForProgramOfProject", action = "http://targetprocess.com/RetrieveAllForProgramOfProject")
    @WebResult(name = "RetrieveAllForProgramOfProjectResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfProjectDTO retrieveAllForProgramOfProject(
        @WebParam(name = "programOfProjectID", targetNamespace = "http://targetprocess.com")
        int programOfProjectID);

    @WebMethod(operationName = "AddAttachmentToProject", action = "http://targetprocess.com/AddAttachmentToProject")
    @WebResult(name = "AddAttachmentToProjectResult", targetNamespace = "http://targetprocess.com")
    public int addAttachmentToProject(
        @WebParam(name = "projectID", targetNamespace = "http://targetprocess.com")
        int projectID,
        @WebParam(name = "fileName", targetNamespace = "http://targetprocess.com")
        String fileName,
        @WebParam(name = "description", targetNamespace = "http://targetprocess.com")
        String description);

    @WebMethod(operationName = "AddProjectMemberToProject", action = "http://targetprocess.com/AddProjectMemberToProject")
    @WebResult(name = "AddProjectMemberToProjectResult", targetNamespace = "http://targetprocess.com")
    public int addProjectMemberToProject(
        @WebParam(name = "projectID", targetNamespace = "http://targetprocess.com")
        int projectID,
        @WebParam(name = "projectMember", targetNamespace = "http://targetprocess.com")
        ProjectMemberDTO projectMember);

    @WebMethod(operationName = "AddReleaseToProject", action = "http://targetprocess.com/AddReleaseToProject")
    @WebResult(name = "AddReleaseToProjectResult", targetNamespace = "http://targetprocess.com")
    public int addReleaseToProject(
        @WebParam(name = "projectID", targetNamespace = "http://targetprocess.com")
        int projectID,
        @WebParam(name = "release", targetNamespace = "http://targetprocess.com")
        ReleaseDTO release);

    @WebMethod(operationName = "RemoveRequestGeneralFromProject", action = "http://targetprocess.com/RemoveRequestGeneralFromProject")
    public void removeRequestGeneralFromProject(
        @WebParam(name = "projectID", targetNamespace = "http://targetprocess.com")
        int projectID,
        @WebParam(name = "requestGeneralID", targetNamespace = "http://targetprocess.com")
        int requestGeneralID);

    @WebMethod(operationName = "RetrieveProjectMembersForProject", action = "http://targetprocess.com/RetrieveProjectMembersForProject")
    @WebResult(name = "RetrieveProjectMembersForProjectResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfProjectMemberDTO retrieveProjectMembersForProject(
        @WebParam(name = "projectID", targetNamespace = "http://targetprocess.com")
        int projectID);

    @WebMethod(operationName = "RetrieveAttachmentsForProject", action = "http://targetprocess.com/RetrieveAttachmentsForProject")
    @WebResult(name = "RetrieveAttachmentsForProjectResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfAttachmentDTO retrieveAttachmentsForProject(
        @WebParam(name = "projectID", targetNamespace = "http://targetprocess.com")
        int projectID);

    @WebMethod(operationName = "AddRequestGeneralToProject", action = "http://targetprocess.com/AddRequestGeneralToProject")
    @WebResult(name = "AddRequestGeneralToProjectResult", targetNamespace = "http://targetprocess.com")
    public int addRequestGeneralToProject(
        @WebParam(name = "projectID", targetNamespace = "http://targetprocess.com")
        int projectID,
        @WebParam(name = "requestGeneral", targetNamespace = "http://targetprocess.com")
        RequestGeneralDTO requestGeneral);

    @WebMethod(operationName = "Delete", action = "http://targetprocess.com/Delete")
    public void delete(
        @WebParam(name = "id", targetNamespace = "http://targetprocess.com")
        int id);

    @WebMethod(operationName = "RetrieveAllForProject", action = "http://targetprocess.com/RetrieveAllForProject")
    @WebResult(name = "RetrieveAllForProjectResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfProjectDTO retrieveAllForProject(
        @WebParam(name = "projectID", targetNamespace = "http://targetprocess.com")
        int projectID);

    @WebMethod(operationName = "RetrieveAllForProcess", action = "http://targetprocess.com/RetrieveAllForProcess")
    @WebResult(name = "RetrieveAllForProcessResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfProjectDTO retrieveAllForProcess(
        @WebParam(name = "processID", targetNamespace = "http://targetprocess.com")
        int processID);

    @WebMethod(operationName = "RemoveAttachmentFromProject", action = "http://targetprocess.com/RemoveAttachmentFromProject")
    public void removeAttachmentFromProject(
        @WebParam(name = "projectID", targetNamespace = "http://targetprocess.com")
        int projectID,
        @WebParam(name = "attachmentID", targetNamespace = "http://targetprocess.com")
        int attachmentID);

    @WebMethod(operationName = "RetrieveAllForCompany", action = "http://targetprocess.com/RetrieveAllForCompany")
    @WebResult(name = "RetrieveAllForCompanyResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfProjectDTO retrieveAllForCompany(
        @WebParam(name = "companyID", targetNamespace = "http://targetprocess.com")
        int companyID);

    @WebMethod(operationName = "RetrieveFeaturesForProject", action = "http://targetprocess.com/RetrieveFeaturesForProject")
    @WebResult(name = "RetrieveFeaturesForProjectResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfFeatureDTO retrieveFeaturesForProject(
        @WebParam(name = "projectID", targetNamespace = "http://targetprocess.com")
        int projectID);

    @WebMethod(operationName = "RetrievePage", action = "http://targetprocess.com/RetrievePage")
    @WebResult(name = "RetrievePageResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfProjectDTO retrievePage(
        @WebParam(name = "hql", targetNamespace = "http://targetprocess.com")
        String hql,
        @WebParam(name = "page", targetNamespace = "http://targetprocess.com")
        int page,
        @WebParam(name = "pageSize", targetNamespace = "http://targetprocess.com")
        int pageSize,
        @WebParam(name = "parameters", targetNamespace = "http://targetprocess.com")
        ArrayOfAnyType parameters);

    @WebMethod(operationName = "RetrieveAttachedRequestsForProject", action = "http://targetprocess.com/RetrieveAttachedRequestsForProject")
    @WebResult(name = "RetrieveAttachedRequestsForProjectResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfRequestGeneralDTO retrieveAttachedRequestsForProject(
        @WebParam(name = "projectID", targetNamespace = "http://targetprocess.com")
        int projectID);

    @WebMethod(operationName = "RemoveCommentFromProject", action = "http://targetprocess.com/RemoveCommentFromProject")
    public void removeCommentFromProject(
        @WebParam(name = "projectID", targetNamespace = "http://targetprocess.com")
        int projectID,
        @WebParam(name = "commentID", targetNamespace = "http://targetprocess.com")
        int commentID);

    @WebMethod(operationName = "Update", action = "http://targetprocess.com/Update")
    public void update(
        @WebParam(name = "entity", targetNamespace = "http://targetprocess.com")
        ProjectDTO entity);

    @WebMethod(operationName = "RemoveFeatureFromProject", action = "http://targetprocess.com/RemoveFeatureFromProject")
    public void removeFeatureFromProject(
        @WebParam(name = "projectID", targetNamespace = "http://targetprocess.com")
        int projectID,
        @WebParam(name = "generalID", targetNamespace = "http://targetprocess.com")
        int generalID);

    @WebMethod(operationName = "AddFeatureToProject", action = "http://targetprocess.com/AddFeatureToProject")
    @WebResult(name = "AddFeatureToProjectResult", targetNamespace = "http://targetprocess.com")
    public int addFeatureToProject(
        @WebParam(name = "projectID", targetNamespace = "http://targetprocess.com")
        int projectID,
        @WebParam(name = "feature", targetNamespace = "http://targetprocess.com")
        FeatureDTO feature);

    @WebMethod(operationName = "RetrieveAllForOwner", action = "http://targetprocess.com/RetrieveAllForOwner")
    @WebResult(name = "RetrieveAllForOwnerResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfProjectDTO retrieveAllForOwner(
        @WebParam(name = "ownerID", targetNamespace = "http://targetprocess.com")
        int ownerID);

    @WebMethod(operationName = "RetrieveCommentsForProject", action = "http://targetprocess.com/RetrieveCommentsForProject")
    @WebResult(name = "RetrieveCommentsForProjectResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfCommentDTO retrieveCommentsForProject(
        @WebParam(name = "projectID", targetNamespace = "http://targetprocess.com")
        int projectID);

    @WebMethod(operationName = "Create", action = "http://targetprocess.com/Create")
    @WebResult(name = "CreateResult", targetNamespace = "http://targetprocess.com")
    public int create(
        @WebParam(name = "entity", targetNamespace = "http://targetprocess.com")
        ProjectDTO entity);

    @WebMethod(operationName = "Retrieve", action = "http://targetprocess.com/Retrieve")
    @WebResult(name = "RetrieveResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfProjectDTO retrieve(
        @WebParam(name = "hql", targetNamespace = "http://targetprocess.com")
        String hql,
        @WebParam(name = "parameters", targetNamespace = "http://targetprocess.com")
        ArrayOfAnyType parameters);

    @WebMethod(operationName = "RemoveProjectMemberFromProject", action = "http://targetprocess.com/RemoveProjectMemberFromProject")
    public void removeProjectMemberFromProject(
        @WebParam(name = "projectID", targetNamespace = "http://targetprocess.com")
        int projectID,
        @WebParam(name = "projectMemberID", targetNamespace = "http://targetprocess.com")
        int projectMemberID);

    @WebMethod(operationName = "AddCommentToProject", action = "http://targetprocess.com/AddCommentToProject")
    @WebResult(name = "AddCommentToProjectResult", targetNamespace = "http://targetprocess.com")
    public int addCommentToProject(
        @WebParam(name = "projectID", targetNamespace = "http://targetprocess.com")
        int projectID,
        @WebParam(name = "comment", targetNamespace = "http://targetprocess.com")
        CommentDTO comment);

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

    @WebMethod(operationName = "RemoveReleaseFromProject", action = "http://targetprocess.com/RemoveReleaseFromProject")
    public void removeReleaseFromProject(
        @WebParam(name = "projectID", targetNamespace = "http://targetprocess.com")
        int projectID,
        @WebParam(name = "generalID", targetNamespace = "http://targetprocess.com")
        int generalID);

    @WebMethod(operationName = "RetrieveAll", action = "http://targetprocess.com/RetrieveAll")
    @WebResult(name = "RetrieveAllResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfProjectDTO retrieveAll();

    @WebMethod(operationName = "RetrieveAllForLastCommentUser", action = "http://targetprocess.com/RetrieveAllForLastCommentUser")
    @WebResult(name = "RetrieveAllForLastCommentUserResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfProjectDTO retrieveAllForLastCommentUser(
        @WebParam(name = "lastCommentUserID", targetNamespace = "http://targetprocess.com")
        int lastCommentUserID);

    @WebMethod(operationName = "GetByID", action = "http://targetprocess.com/GetByID")
    @WebResult(name = "GetByIDResult", targetNamespace = "http://targetprocess.com")
    public ProjectDTO getByID(
        @WebParam(name = "id", targetNamespace = "http://targetprocess.com")
        int id);

    @WebMethod(operationName = "RetrieveReleasesForProject", action = "http://targetprocess.com/RetrieveReleasesForProject")
    @WebResult(name = "RetrieveReleasesForProjectResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfReleaseDTO retrieveReleasesForProject(
        @WebParam(name = "projectID", targetNamespace = "http://targetprocess.com")
        int projectID);

}
