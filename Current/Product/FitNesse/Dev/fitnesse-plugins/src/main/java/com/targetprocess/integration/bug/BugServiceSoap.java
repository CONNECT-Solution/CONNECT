
package com.targetprocess.integration.bug;

import java.math.BigDecimal;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "BugServiceSoap", targetNamespace = "http://targetprocess.com")
@SOAPBinding(use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface BugServiceSoap {


    @WebMethod(operationName = "RetrieveAllForUserStory", action = "http://targetprocess.com/RetrieveAllForUserStory")
    @WebResult(name = "RetrieveAllForUserStoryResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfBugDTO retrieveAllForUserStory(
        @WebParam(name = "userStoryID", targetNamespace = "http://targetprocess.com")
        int userStoryID);

    @WebMethod(operationName = "RemoveRequestGeneralFromBug", action = "http://targetprocess.com/RemoveRequestGeneralFromBug")
    public void removeRequestGeneralFromBug(
        @WebParam(name = "bugID", targetNamespace = "http://targetprocess.com")
        int bugID,
        @WebParam(name = "requestGeneralID", targetNamespace = "http://targetprocess.com")
        int requestGeneralID);

    @WebMethod(operationName = "ChangeEffort", action = "http://targetprocess.com/ChangeEffort")
    public void changeEffort(
        @WebParam(name = "bugID", targetNamespace = "http://targetprocess.com")
        int bugID,
        @WebParam(name = "actorID", targetNamespace = "http://targetprocess.com")
        int actorID,
        @WebParam(name = "effort", targetNamespace = "http://targetprocess.com")
        BigDecimal effort);

    @WebMethod(operationName = "AssignUser", action = "http://targetprocess.com/AssignUser")
    public void assignUser(
        @WebParam(name = "bugID", targetNamespace = "http://targetprocess.com")
        int bugID,
        @WebParam(name = "userID", targetNamespace = "http://targetprocess.com")
        int userID);

    @WebMethod(operationName = "RetrieveAllForRelease", action = "http://targetprocess.com/RetrieveAllForRelease")
    @WebResult(name = "RetrieveAllForReleaseResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfBugDTO retrieveAllForRelease(
        @WebParam(name = "releaseID", targetNamespace = "http://targetprocess.com")
        int releaseID);

    @WebMethod(operationName = "AddBugWithAttachment", action = "http://targetprocess.com/AddBugWithAttachment")
    @WebResult(name = "AddBugWithAttachmentResult", targetNamespace = "http://targetprocess.com")
    public int addBugWithAttachment(
        @WebParam(name = "bug", targetNamespace = "http://targetprocess.com")
        BugDTO bug,
        @WebParam(name = "fileName", targetNamespace = "http://targetprocess.com")
        String fileName,
        @WebParam(name = "description", targetNamespace = "http://targetprocess.com")
        String description);

    @WebMethod(operationName = "AddTeamToBug", action = "http://targetprocess.com/AddTeamToBug")
    @WebResult(name = "AddTeamToBugResult", targetNamespace = "http://targetprocess.com")
    public int addTeamToBug(
        @WebParam(name = "bugID", targetNamespace = "http://targetprocess.com")
        int bugID,
        @WebParam(name = "team", targetNamespace = "http://targetprocess.com")
        TeamDTO team);

    @WebMethod(operationName = "Delete", action = "http://targetprocess.com/Delete")
    public void delete(
        @WebParam(name = "id", targetNamespace = "http://targetprocess.com")
        int id);

    @WebMethod(operationName = "RetrieveAllForProject", action = "http://targetprocess.com/RetrieveAllForProject")
    @WebResult(name = "RetrieveAllForProjectResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfBugDTO retrieveAllForProject(
        @WebParam(name = "projectID", targetNamespace = "http://targetprocess.com")
        int projectID);

    @WebMethod(operationName = "AssignUserByEmailOrLoginAsActor", action = "http://targetprocess.com/AssignUserByEmailOrLoginAsActor")
    public void assignUserByEmailOrLoginAsActor(
        @WebParam(name = "bugID", targetNamespace = "http://targetprocess.com")
        int bugID,
        @WebParam(name = "userEmailOrLogin", targetNamespace = "http://targetprocess.com")
        String userEmailOrLogin,
        @WebParam(name = "actorName", targetNamespace = "http://targetprocess.com")
        String actorName);

    @WebMethod(operationName = "RemoveCommentFromBug", action = "http://targetprocess.com/RemoveCommentFromBug")
    public void removeCommentFromBug(
        @WebParam(name = "bugID", targetNamespace = "http://targetprocess.com")
        int bugID,
        @WebParam(name = "commentID", targetNamespace = "http://targetprocess.com")
        int commentID);

    @WebMethod(operationName = "RemoveAttachmentFromBug", action = "http://targetprocess.com/RemoveAttachmentFromBug")
    public void removeAttachmentFromBug(
        @WebParam(name = "bugID", targetNamespace = "http://targetprocess.com")
        int bugID,
        @WebParam(name = "attachmentID", targetNamespace = "http://targetprocess.com")
        int attachmentID);

    @WebMethod(operationName = "RetrieveOpenForMe", action = "http://targetprocess.com/RetrieveOpenForMe")
    @WebResult(name = "RetrieveOpenForMeResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfBugDTO retrieveOpenForMe();

    @WebMethod(operationName = "RetrievePage", action = "http://targetprocess.com/RetrievePage")
    @WebResult(name = "RetrievePageResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfBugDTO retrievePage(
        @WebParam(name = "hql", targetNamespace = "http://targetprocess.com")
        String hql,
        @WebParam(name = "page", targetNamespace = "http://targetprocess.com")
        int page,
        @WebParam(name = "pageSize", targetNamespace = "http://targetprocess.com")
        int pageSize,
        @WebParam(name = "parameters", targetNamespace = "http://targetprocess.com")
        ArrayOfAnyType parameters);

    @WebMethod(operationName = "ChangeState", action = "http://targetprocess.com/ChangeState")
    public void changeState(
        @WebParam(name = "bugID", targetNamespace = "http://targetprocess.com")
        int bugID,
        @WebParam(name = "entityStateID", targetNamespace = "http://targetprocess.com")
        int entityStateID);

    @WebMethod(operationName = "Update", action = "http://targetprocess.com/Update")
    public void update(
        @WebParam(name = "entity", targetNamespace = "http://targetprocess.com")
        BugDTO entity);

    @WebMethod(operationName = "AddRevisionAssignableToBug", action = "http://targetprocess.com/AddRevisionAssignableToBug")
    @WebResult(name = "AddRevisionAssignableToBugResult", targetNamespace = "http://targetprocess.com")
    public int addRevisionAssignableToBug(
        @WebParam(name = "bugID", targetNamespace = "http://targetprocess.com")
        int bugID,
        @WebParam(name = "revisionAssignable", targetNamespace = "http://targetprocess.com")
        RevisionAssignableDTO revisionAssignable);

    @WebMethod(operationName = "RetrieveAttachedRequestsForBug", action = "http://targetprocess.com/RetrieveAttachedRequestsForBug")
    @WebResult(name = "RetrieveAttachedRequestsForBugResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfRequestGeneralDTO retrieveAttachedRequestsForBug(
        @WebParam(name = "bugID", targetNamespace = "http://targetprocess.com")
        int bugID);

    @WebMethod(operationName = "AssignUserByEmailOrLogin", action = "http://targetprocess.com/AssignUserByEmailOrLogin")
    public void assignUserByEmailOrLogin(
        @WebParam(name = "bugID", targetNamespace = "http://targetprocess.com")
        int bugID,
        @WebParam(name = "userEmailOrLogin", targetNamespace = "http://targetprocess.com")
        String userEmailOrLogin);

    @WebMethod(operationName = "RetrieveCommentsForBug", action = "http://targetprocess.com/RetrieveCommentsForBug")
    @WebResult(name = "RetrieveCommentsForBugResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfCommentDTO retrieveCommentsForBug(
        @WebParam(name = "bugID", targetNamespace = "http://targetprocess.com")
        int bugID);

    @WebMethod(operationName = "RetrieveAllForOwner", action = "http://targetprocess.com/RetrieveAllForOwner")
    @WebResult(name = "RetrieveAllForOwnerResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfBugDTO retrieveAllForOwner(
        @WebParam(name = "ownerID", targetNamespace = "http://targetprocess.com")
        int ownerID);

    @WebMethod(operationName = "RetrieveTeamsForBug", action = "http://targetprocess.com/RetrieveTeamsForBug")
    @WebResult(name = "RetrieveTeamsForBugResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfTeamDTO retrieveTeamsForBug(
        @WebParam(name = "bugID", targetNamespace = "http://targetprocess.com")
        int bugID);

    @WebMethod(operationName = "Create", action = "http://targetprocess.com/Create")
    @WebResult(name = "CreateResult", targetNamespace = "http://targetprocess.com")
    public int create(
        @WebParam(name = "entity", targetNamespace = "http://targetprocess.com")
        BugDTO entity);

    @WebMethod(operationName = "RetrieveAttachmentsForBug", action = "http://targetprocess.com/RetrieveAttachmentsForBug")
    @WebResult(name = "RetrieveAttachmentsForBugResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfAttachmentDTO retrieveAttachmentsForBug(
        @WebParam(name = "bugID", targetNamespace = "http://targetprocess.com")
        int bugID);

    @WebMethod(operationName = "RetrieveRevisionAssignablesForBug", action = "http://targetprocess.com/RetrieveRevisionAssignablesForBug")
    @WebResult(name = "RetrieveRevisionAssignablesForBugResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfRevisionAssignableDTO retrieveRevisionAssignablesForBug(
        @WebParam(name = "bugID", targetNamespace = "http://targetprocess.com")
        int bugID);

    @WebMethod(operationName = "RetrieveAllForPriority", action = "http://targetprocess.com/RetrieveAllForPriority")
    @WebResult(name = "RetrieveAllForPriorityResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfBugDTO retrieveAllForPriority(
        @WebParam(name = "priorityID", targetNamespace = "http://targetprocess.com")
        int priorityID);

    @WebMethod(operationName = "GetSeverities", action = "http://targetprocess.com/GetSeverities")
    @WebResult(name = "GetSeveritiesResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfSeverityDTO getSeverities();

    @WebMethod(operationName = "Retrieve", action = "http://targetprocess.com/Retrieve")
    @WebResult(name = "RetrieveResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfBugDTO retrieve(
        @WebParam(name = "hql", targetNamespace = "http://targetprocess.com")
        String hql,
        @WebParam(name = "parameters", targetNamespace = "http://targetprocess.com")
        ArrayOfAnyType parameters);

    @WebMethod(operationName = "RetrieveAllForBuild", action = "http://targetprocess.com/RetrieveAllForBuild")
    @WebResult(name = "RetrieveAllForBuildResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfBugDTO retrieveAllForBuild(
        @WebParam(name = "buildID", targetNamespace = "http://targetprocess.com")
        int buildID);

    @WebMethod(operationName = "RetrieveAllForIteration", action = "http://targetprocess.com/RetrieveAllForIteration")
    @WebResult(name = "RetrieveAllForIterationResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfBugDTO retrieveAllForIteration(
        @WebParam(name = "iterationID", targetNamespace = "http://targetprocess.com")
        int iterationID);

    @WebMethod(operationName = "RetrieveOpenForUser", action = "http://targetprocess.com/RetrieveOpenForUser")
    @WebResult(name = "RetrieveOpenForUserResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfBugDTO retrieveOpenForUser(
        @WebParam(name = "userID", targetNamespace = "http://targetprocess.com")
        int userID);

    @WebMethod(operationName = "AssignUserAsActor", action = "http://targetprocess.com/AssignUserAsActor")
    public void assignUserAsActor(
        @WebParam(name = "bugID", targetNamespace = "http://targetprocess.com")
        int bugID,
        @WebParam(name = "actorID", targetNamespace = "http://targetprocess.com")
        int actorID,
        @WebParam(name = "userID", targetNamespace = "http://targetprocess.com")
        int userID);

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

    @WebMethod(operationName = "RemoveRevisionAssignableFromBug", action = "http://targetprocess.com/RemoveRevisionAssignableFromBug")
    public void removeRevisionAssignableFromBug(
        @WebParam(name = "bugID", targetNamespace = "http://targetprocess.com")
        int bugID,
        @WebParam(name = "revisionAssignableID", targetNamespace = "http://targetprocess.com")
        int revisionAssignableID);

    @WebMethod(operationName = "GetPriorities", action = "http://targetprocess.com/GetPriorities")
    @WebResult(name = "GetPrioritiesResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfPriorityDTO getPriorities();

    @WebMethod(operationName = "RetrieveAllForSeverity", action = "http://targetprocess.com/RetrieveAllForSeverity")
    @WebResult(name = "RetrieveAllForSeverityResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfBugDTO retrieveAllForSeverity(
        @WebParam(name = "severityID", targetNamespace = "http://targetprocess.com")
        int severityID);

    @WebMethod(operationName = "RetrieveAll", action = "http://targetprocess.com/RetrieveAll")
    @WebResult(name = "RetrieveAllResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfBugDTO retrieveAll();

    @WebMethod(operationName = "GetByID", action = "http://targetprocess.com/GetByID")
    @WebResult(name = "GetByIDResult", targetNamespace = "http://targetprocess.com")
    public BugDTO getByID(
        @WebParam(name = "id", targetNamespace = "http://targetprocess.com")
        int id);

    @WebMethod(operationName = "RetrieveAllForLastCommentUser", action = "http://targetprocess.com/RetrieveAllForLastCommentUser")
    @WebResult(name = "RetrieveAllForLastCommentUserResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfBugDTO retrieveAllForLastCommentUser(
        @WebParam(name = "lastCommentUserID", targetNamespace = "http://targetprocess.com")
        int lastCommentUserID);

    @WebMethod(operationName = "AddAttachmentToBug", action = "http://targetprocess.com/AddAttachmentToBug")
    @WebResult(name = "AddAttachmentToBugResult", targetNamespace = "http://targetprocess.com")
    public int addAttachmentToBug(
        @WebParam(name = "bugID", targetNamespace = "http://targetprocess.com")
        int bugID,
        @WebParam(name = "fileName", targetNamespace = "http://targetprocess.com")
        String fileName,
        @WebParam(name = "description", targetNamespace = "http://targetprocess.com")
        String description);

    @WebMethod(operationName = "RetrieveAllForEntityState", action = "http://targetprocess.com/RetrieveAllForEntityState")
    @WebResult(name = "RetrieveAllForEntityStateResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfBugDTO retrieveAllForEntityState(
        @WebParam(name = "entityStateID", targetNamespace = "http://targetprocess.com")
        int entityStateID);

    @WebMethod(operationName = "RemoveTeamFromBug", action = "http://targetprocess.com/RemoveTeamFromBug")
    public void removeTeamFromBug(
        @WebParam(name = "bugID", targetNamespace = "http://targetprocess.com")
        int bugID,
        @WebParam(name = "teamID", targetNamespace = "http://targetprocess.com")
        int teamID);

    @WebMethod(operationName = "RetrieveActorEffortsForBug", action = "http://targetprocess.com/RetrieveActorEffortsForBug")
    @WebResult(name = "RetrieveActorEffortsForBugResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfActorEffortDTO retrieveActorEffortsForBug(
        @WebParam(name = "bugID", targetNamespace = "http://targetprocess.com")
        int bugID);

    @WebMethod(operationName = "AddCommentToBug", action = "http://targetprocess.com/AddCommentToBug")
    @WebResult(name = "AddCommentToBugResult", targetNamespace = "http://targetprocess.com")
    public int addCommentToBug(
        @WebParam(name = "bugID", targetNamespace = "http://targetprocess.com")
        int bugID,
        @WebParam(name = "comment", targetNamespace = "http://targetprocess.com")
        CommentDTO comment);

    @WebMethod(operationName = "AddRequestGeneralToBug", action = "http://targetprocess.com/AddRequestGeneralToBug")
    @WebResult(name = "AddRequestGeneralToBugResult", targetNamespace = "http://targetprocess.com")
    public int addRequestGeneralToBug(
        @WebParam(name = "bugID", targetNamespace = "http://targetprocess.com")
        int bugID,
        @WebParam(name = "requestGeneral", targetNamespace = "http://targetprocess.com")
        RequestGeneralDTO requestGeneral);

}
