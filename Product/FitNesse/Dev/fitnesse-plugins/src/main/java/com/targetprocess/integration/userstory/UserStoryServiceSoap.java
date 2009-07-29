
package com.targetprocess.integration.userstory;

import java.math.BigDecimal;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "UserStoryServiceSoap", targetNamespace = "http://targetprocess.com")
@SOAPBinding(use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface UserStoryServiceSoap {


    @WebMethod(operationName = "AddTeamToUserStory", action = "http://targetprocess.com/AddTeamToUserStory")
    @WebResult(name = "AddTeamToUserStoryResult", targetNamespace = "http://targetprocess.com")
    public int addTeamToUserStory(
        @WebParam(name = "userStoryID", targetNamespace = "http://targetprocess.com")
        int userStoryID,
        @WebParam(name = "team", targetNamespace = "http://targetprocess.com")
        TeamDTO team);

    @WebMethod(operationName = "ChangeEffort", action = "http://targetprocess.com/ChangeEffort")
    public void changeEffort(
        @WebParam(name = "userStoryID", targetNamespace = "http://targetprocess.com")
        int userStoryID,
        @WebParam(name = "actorID", targetNamespace = "http://targetprocess.com")
        int actorID,
        @WebParam(name = "effort", targetNamespace = "http://targetprocess.com")
        BigDecimal effort);

    @WebMethod(operationName = "RemoveTeamFromUserStory", action = "http://targetprocess.com/RemoveTeamFromUserStory")
    public void removeTeamFromUserStory(
        @WebParam(name = "userStoryID", targetNamespace = "http://targetprocess.com")
        int userStoryID,
        @WebParam(name = "teamID", targetNamespace = "http://targetprocess.com")
        int teamID);

    @WebMethod(operationName = "AssignUser", action = "http://targetprocess.com/AssignUser")
    public void assignUser(
        @WebParam(name = "userStoryID", targetNamespace = "http://targetprocess.com")
        int userStoryID,
        @WebParam(name = "userID", targetNamespace = "http://targetprocess.com")
        int userID);

    @WebMethod(operationName = "RetrieveAllForRelease", action = "http://targetprocess.com/RetrieveAllForRelease")
    @WebResult(name = "RetrieveAllForReleaseResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfUserStoryDTO retrieveAllForRelease(
        @WebParam(name = "releaseID", targetNamespace = "http://targetprocess.com")
        int releaseID);

    @WebMethod(operationName = "RemoveTaskFromUserStory", action = "http://targetprocess.com/RemoveTaskFromUserStory")
    public void removeTaskFromUserStory(
        @WebParam(name = "userStoryID", targetNamespace = "http://targetprocess.com")
        int userStoryID,
        @WebParam(name = "generalID", targetNamespace = "http://targetprocess.com")
        int generalID);

    @WebMethod(operationName = "RetrieveAttachmentsForUserStory", action = "http://targetprocess.com/RetrieveAttachmentsForUserStory")
    @WebResult(name = "RetrieveAttachmentsForUserStoryResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfAttachmentDTO retrieveAttachmentsForUserStory(
        @WebParam(name = "userStoryID", targetNamespace = "http://targetprocess.com")
        int userStoryID);

    @WebMethod(operationName = "Delete", action = "http://targetprocess.com/Delete")
    public void delete(
        @WebParam(name = "id", targetNamespace = "http://targetprocess.com")
        int id);

    @WebMethod(operationName = "RetrieveAllForProject", action = "http://targetprocess.com/RetrieveAllForProject")
    @WebResult(name = "RetrieveAllForProjectResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfUserStoryDTO retrieveAllForProject(
        @WebParam(name = "projectID", targetNamespace = "http://targetprocess.com")
        int projectID);

    @WebMethod(operationName = "AddRevisionAssignableToUserStory", action = "http://targetprocess.com/AddRevisionAssignableToUserStory")
    @WebResult(name = "AddRevisionAssignableToUserStoryResult", targetNamespace = "http://targetprocess.com")
    public int addRevisionAssignableToUserStory(
        @WebParam(name = "userStoryID", targetNamespace = "http://targetprocess.com")
        int userStoryID,
        @WebParam(name = "revisionAssignable", targetNamespace = "http://targetprocess.com")
        RevisionAssignableDTO revisionAssignable);

    @WebMethod(operationName = "AssignUserByEmailOrLoginAsActor", action = "http://targetprocess.com/AssignUserByEmailOrLoginAsActor")
    public void assignUserByEmailOrLoginAsActor(
        @WebParam(name = "userStoryID", targetNamespace = "http://targetprocess.com")
        int userStoryID,
        @WebParam(name = "userEmailOrLogin", targetNamespace = "http://targetprocess.com")
        String userEmailOrLogin,
        @WebParam(name = "actorName", targetNamespace = "http://targetprocess.com")
        String actorName);

    @WebMethod(operationName = "RetrieveCommentsForUserStory", action = "http://targetprocess.com/RetrieveCommentsForUserStory")
    @WebResult(name = "RetrieveCommentsForUserStoryResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfCommentDTO retrieveCommentsForUserStory(
        @WebParam(name = "userStoryID", targetNamespace = "http://targetprocess.com")
        int userStoryID);

    @WebMethod(operationName = "RetrieveOpenForMe", action = "http://targetprocess.com/RetrieveOpenForMe")
    @WebResult(name = "RetrieveOpenForMeResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfUserStoryDTO retrieveOpenForMe();

    @WebMethod(operationName = "AddCommentToUserStory", action = "http://targetprocess.com/AddCommentToUserStory")
    @WebResult(name = "AddCommentToUserStoryResult", targetNamespace = "http://targetprocess.com")
    public int addCommentToUserStory(
        @WebParam(name = "userStoryID", targetNamespace = "http://targetprocess.com")
        int userStoryID,
        @WebParam(name = "comment", targetNamespace = "http://targetprocess.com")
        CommentDTO comment);

    @WebMethod(operationName = "AddAttachmentToUserStory", action = "http://targetprocess.com/AddAttachmentToUserStory")
    @WebResult(name = "AddAttachmentToUserStoryResult", targetNamespace = "http://targetprocess.com")
    public int addAttachmentToUserStory(
        @WebParam(name = "userStoryID", targetNamespace = "http://targetprocess.com")
        int userStoryID,
        @WebParam(name = "fileName", targetNamespace = "http://targetprocess.com")
        String fileName,
        @WebParam(name = "description", targetNamespace = "http://targetprocess.com")
        String description);

    @WebMethod(operationName = "RetrievePage", action = "http://targetprocess.com/RetrievePage")
    @WebResult(name = "RetrievePageResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfUserStoryDTO retrievePage(
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
        @WebParam(name = "userStoryID", targetNamespace = "http://targetprocess.com")
        int userStoryID,
        @WebParam(name = "entityStateID", targetNamespace = "http://targetprocess.com")
        int entityStateID);

    @WebMethod(operationName = "RetrieveTeamsForUserStory", action = "http://targetprocess.com/RetrieveTeamsForUserStory")
    @WebResult(name = "RetrieveTeamsForUserStoryResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfTeamDTO retrieveTeamsForUserStory(
        @WebParam(name = "userStoryID", targetNamespace = "http://targetprocess.com")
        int userStoryID);

    @WebMethod(operationName = "Update", action = "http://targetprocess.com/Update")
    public void update(
        @WebParam(name = "entity", targetNamespace = "http://targetprocess.com")
        UserStoryDTO entity);

    @WebMethod(operationName = "RemoveAttachmentFromUserStory", action = "http://targetprocess.com/RemoveAttachmentFromUserStory")
    public void removeAttachmentFromUserStory(
        @WebParam(name = "userStoryID", targetNamespace = "http://targetprocess.com")
        int userStoryID,
        @WebParam(name = "attachmentID", targetNamespace = "http://targetprocess.com")
        int attachmentID);

    @WebMethod(operationName = "AssignUserByEmailOrLogin", action = "http://targetprocess.com/AssignUserByEmailOrLogin")
    public void assignUserByEmailOrLogin(
        @WebParam(name = "userStoryID", targetNamespace = "http://targetprocess.com")
        int userStoryID,
        @WebParam(name = "userEmailOrLogin", targetNamespace = "http://targetprocess.com")
        String userEmailOrLogin);

    @WebMethod(operationName = "RetrieveAllForOwner", action = "http://targetprocess.com/RetrieveAllForOwner")
    @WebResult(name = "RetrieveAllForOwnerResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfUserStoryDTO retrieveAllForOwner(
        @WebParam(name = "ownerID", targetNamespace = "http://targetprocess.com")
        int ownerID);

    @WebMethod(operationName = "AddRequestGeneralToUserStory", action = "http://targetprocess.com/AddRequestGeneralToUserStory")
    @WebResult(name = "AddRequestGeneralToUserStoryResult", targetNamespace = "http://targetprocess.com")
    public int addRequestGeneralToUserStory(
        @WebParam(name = "userStoryID", targetNamespace = "http://targetprocess.com")
        int userStoryID,
        @WebParam(name = "requestGeneral", targetNamespace = "http://targetprocess.com")
        RequestGeneralDTO requestGeneral);

    @WebMethod(operationName = "RetrieveAttachedRequestsForUserStory", action = "http://targetprocess.com/RetrieveAttachedRequestsForUserStory")
    @WebResult(name = "RetrieveAttachedRequestsForUserStoryResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfRequestGeneralDTO retrieveAttachedRequestsForUserStory(
        @WebParam(name = "userStoryID", targetNamespace = "http://targetprocess.com")
        int userStoryID);

    @WebMethod(operationName = "Create", action = "http://targetprocess.com/Create")
    @WebResult(name = "CreateResult", targetNamespace = "http://targetprocess.com")
    public int create(
        @WebParam(name = "entity", targetNamespace = "http://targetprocess.com")
        UserStoryDTO entity);

    @WebMethod(operationName = "RetrieveActorEffortsForUserStory", action = "http://targetprocess.com/RetrieveActorEffortsForUserStory")
    @WebResult(name = "RetrieveActorEffortsForUserStoryResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfActorEffortDTO retrieveActorEffortsForUserStory(
        @WebParam(name = "userStoryID", targetNamespace = "http://targetprocess.com")
        int userStoryID);

    @WebMethod(operationName = "RetrieveAllForPriority", action = "http://targetprocess.com/RetrieveAllForPriority")
    @WebResult(name = "RetrieveAllForPriorityResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfUserStoryDTO retrieveAllForPriority(
        @WebParam(name = "priorityID", targetNamespace = "http://targetprocess.com")
        int priorityID);

    @WebMethod(operationName = "Retrieve", action = "http://targetprocess.com/Retrieve")
    @WebResult(name = "RetrieveResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfUserStoryDTO retrieve(
        @WebParam(name = "hql", targetNamespace = "http://targetprocess.com")
        String hql,
        @WebParam(name = "parameters", targetNamespace = "http://targetprocess.com")
        ArrayOfAnyType parameters);

    @WebMethod(operationName = "RetrieveRevisionAssignablesForUserStory", action = "http://targetprocess.com/RetrieveRevisionAssignablesForUserStory")
    @WebResult(name = "RetrieveRevisionAssignablesForUserStoryResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfRevisionAssignableDTO retrieveRevisionAssignablesForUserStory(
        @WebParam(name = "userStoryID", targetNamespace = "http://targetprocess.com")
        int userStoryID);

    @WebMethod(operationName = "RetrieveAllForIteration", action = "http://targetprocess.com/RetrieveAllForIteration")
    @WebResult(name = "RetrieveAllForIterationResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfUserStoryDTO retrieveAllForIteration(
        @WebParam(name = "iterationID", targetNamespace = "http://targetprocess.com")
        int iterationID);

    @WebMethod(operationName = "RemoveRevisionAssignableFromUserStory", action = "http://targetprocess.com/RemoveRevisionAssignableFromUserStory")
    public void removeRevisionAssignableFromUserStory(
        @WebParam(name = "userStoryID", targetNamespace = "http://targetprocess.com")
        int userStoryID,
        @WebParam(name = "revisionAssignableID", targetNamespace = "http://targetprocess.com")
        int revisionAssignableID);

    @WebMethod(operationName = "RetrieveOpenForUser", action = "http://targetprocess.com/RetrieveOpenForUser")
    @WebResult(name = "RetrieveOpenForUserResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfUserStoryDTO retrieveOpenForUser(
        @WebParam(name = "userID", targetNamespace = "http://targetprocess.com")
        int userID);

    @WebMethod(operationName = "AssignUserAsActor", action = "http://targetprocess.com/AssignUserAsActor")
    public void assignUserAsActor(
        @WebParam(name = "userStoryID", targetNamespace = "http://targetprocess.com")
        int userStoryID,
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

    @WebMethod(operationName = "AddTaskToUserStory", action = "http://targetprocess.com/AddTaskToUserStory")
    @WebResult(name = "AddTaskToUserStoryResult", targetNamespace = "http://targetprocess.com")
    public int addTaskToUserStory(
        @WebParam(name = "userStoryID", targetNamespace = "http://targetprocess.com")
        int userStoryID,
        @WebParam(name = "task", targetNamespace = "http://targetprocess.com")
        TaskDTO task);

    @WebMethod(operationName = "GetPriorities", action = "http://targetprocess.com/GetPriorities")
    @WebResult(name = "GetPrioritiesResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfPriorityDTO getPriorities();

    @WebMethod(operationName = "RetrieveAll", action = "http://targetprocess.com/RetrieveAll")
    @WebResult(name = "RetrieveAllResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfUserStoryDTO retrieveAll();

    @WebMethod(operationName = "GetByID", action = "http://targetprocess.com/GetByID")
    @WebResult(name = "GetByIDResult", targetNamespace = "http://targetprocess.com")
    public UserStoryDTO getByID(
        @WebParam(name = "id", targetNamespace = "http://targetprocess.com")
        int id);

    @WebMethod(operationName = "RetrieveAllForLastCommentUser", action = "http://targetprocess.com/RetrieveAllForLastCommentUser")
    @WebResult(name = "RetrieveAllForLastCommentUserResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfUserStoryDTO retrieveAllForLastCommentUser(
        @WebParam(name = "lastCommentUserID", targetNamespace = "http://targetprocess.com")
        int lastCommentUserID);

    @WebMethod(operationName = "RemoveCommentFromUserStory", action = "http://targetprocess.com/RemoveCommentFromUserStory")
    public void removeCommentFromUserStory(
        @WebParam(name = "userStoryID", targetNamespace = "http://targetprocess.com")
        int userStoryID,
        @WebParam(name = "commentID", targetNamespace = "http://targetprocess.com")
        int commentID);

    @WebMethod(operationName = "RetrieveAllForEntityState", action = "http://targetprocess.com/RetrieveAllForEntityState")
    @WebResult(name = "RetrieveAllForEntityStateResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfUserStoryDTO retrieveAllForEntityState(
        @WebParam(name = "entityStateID", targetNamespace = "http://targetprocess.com")
        int entityStateID);

    @WebMethod(operationName = "RetrieveTasksForUserStory", action = "http://targetprocess.com/RetrieveTasksForUserStory")
    @WebResult(name = "RetrieveTasksForUserStoryResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfTaskDTO retrieveTasksForUserStory(
        @WebParam(name = "userStoryID", targetNamespace = "http://targetprocess.com")
        int userStoryID);

    @WebMethod(operationName = "RemoveRequestGeneralFromUserStory", action = "http://targetprocess.com/RemoveRequestGeneralFromUserStory")
    public void removeRequestGeneralFromUserStory(
        @WebParam(name = "userStoryID", targetNamespace = "http://targetprocess.com")
        int userStoryID,
        @WebParam(name = "requestGeneralID", targetNamespace = "http://targetprocess.com")
        int requestGeneralID);

    @WebMethod(operationName = "RetrieveAllForFeature", action = "http://targetprocess.com/RetrieveAllForFeature")
    @WebResult(name = "RetrieveAllForFeatureResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfUserStoryDTO retrieveAllForFeature(
        @WebParam(name = "featureID", targetNamespace = "http://targetprocess.com")
        int featureID);

}
