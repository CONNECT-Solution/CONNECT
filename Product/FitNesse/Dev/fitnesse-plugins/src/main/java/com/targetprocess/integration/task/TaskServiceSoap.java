
package com.targetprocess.integration.task;

import java.math.BigDecimal;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "TaskServiceSoap", targetNamespace = "http://targetprocess.com")
@SOAPBinding(use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface TaskServiceSoap {


    @WebMethod(operationName = "RetrieveAllForUserStory", action = "http://targetprocess.com/RetrieveAllForUserStory")
    @WebResult(name = "RetrieveAllForUserStoryResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfTaskDTO retrieveAllForUserStory(
        @WebParam(name = "userStoryID", targetNamespace = "http://targetprocess.com")
        int userStoryID);

    @WebMethod(operationName = "RemoveRevisionAssignableFromTask", action = "http://targetprocess.com/RemoveRevisionAssignableFromTask")
    public void removeRevisionAssignableFromTask(
        @WebParam(name = "taskID", targetNamespace = "http://targetprocess.com")
        int taskID,
        @WebParam(name = "revisionAssignableID", targetNamespace = "http://targetprocess.com")
        int revisionAssignableID);

    @WebMethod(operationName = "ChangeEffort", action = "http://targetprocess.com/ChangeEffort")
    public void changeEffort(
        @WebParam(name = "taskID", targetNamespace = "http://targetprocess.com")
        int taskID,
        @WebParam(name = "actorID", targetNamespace = "http://targetprocess.com")
        int actorID,
        @WebParam(name = "effort", targetNamespace = "http://targetprocess.com")
        BigDecimal effort);

    @WebMethod(operationName = "AddTeamToTask", action = "http://targetprocess.com/AddTeamToTask")
    @WebResult(name = "AddTeamToTaskResult", targetNamespace = "http://targetprocess.com")
    public int addTeamToTask(
        @WebParam(name = "taskID", targetNamespace = "http://targetprocess.com")
        int taskID,
        @WebParam(name = "team", targetNamespace = "http://targetprocess.com")
        TeamDTO team);

    @WebMethod(operationName = "AssignUser", action = "http://targetprocess.com/AssignUser")
    public void assignUser(
        @WebParam(name = "taskID", targetNamespace = "http://targetprocess.com")
        int taskID,
        @WebParam(name = "userID", targetNamespace = "http://targetprocess.com")
        int userID);

    @WebMethod(operationName = "RemoveCommentFromTask", action = "http://targetprocess.com/RemoveCommentFromTask")
    public void removeCommentFromTask(
        @WebParam(name = "taskID", targetNamespace = "http://targetprocess.com")
        int taskID,
        @WebParam(name = "commentID", targetNamespace = "http://targetprocess.com")
        int commentID);

    @WebMethod(operationName = "RetrieveRevisionAssignablesForTask", action = "http://targetprocess.com/RetrieveRevisionAssignablesForTask")
    @WebResult(name = "RetrieveRevisionAssignablesForTaskResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfRevisionAssignableDTO retrieveRevisionAssignablesForTask(
        @WebParam(name = "taskID", targetNamespace = "http://targetprocess.com")
        int taskID);

    @WebMethod(operationName = "RetrieveAllForRelease", action = "http://targetprocess.com/RetrieveAllForRelease")
    @WebResult(name = "RetrieveAllForReleaseResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfTaskDTO retrieveAllForRelease(
        @WebParam(name = "releaseID", targetNamespace = "http://targetprocess.com")
        int releaseID);

    @WebMethod(operationName = "RemoveRequestGeneralFromTask", action = "http://targetprocess.com/RemoveRequestGeneralFromTask")
    public void removeRequestGeneralFromTask(
        @WebParam(name = "taskID", targetNamespace = "http://targetprocess.com")
        int taskID,
        @WebParam(name = "requestGeneralID", targetNamespace = "http://targetprocess.com")
        int requestGeneralID);

    @WebMethod(operationName = "RetrieveAttachedRequestsForTask", action = "http://targetprocess.com/RetrieveAttachedRequestsForTask")
    @WebResult(name = "RetrieveAttachedRequestsForTaskResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfRequestGeneralDTO retrieveAttachedRequestsForTask(
        @WebParam(name = "taskID", targetNamespace = "http://targetprocess.com")
        int taskID);

    @WebMethod(operationName = "RetrieveTeamsForTask", action = "http://targetprocess.com/RetrieveTeamsForTask")
    @WebResult(name = "RetrieveTeamsForTaskResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfTeamDTO retrieveTeamsForTask(
        @WebParam(name = "taskID", targetNamespace = "http://targetprocess.com")
        int taskID);

    @WebMethod(operationName = "Delete", action = "http://targetprocess.com/Delete")
    public void delete(
        @WebParam(name = "id", targetNamespace = "http://targetprocess.com")
        int id);

    @WebMethod(operationName = "RetrieveAllForProject", action = "http://targetprocess.com/RetrieveAllForProject")
    @WebResult(name = "RetrieveAllForProjectResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfTaskDTO retrieveAllForProject(
        @WebParam(name = "projectID", targetNamespace = "http://targetprocess.com")
        int projectID);

    @WebMethod(operationName = "AssignUserByEmailOrLoginAsActor", action = "http://targetprocess.com/AssignUserByEmailOrLoginAsActor")
    public void assignUserByEmailOrLoginAsActor(
        @WebParam(name = "taskID", targetNamespace = "http://targetprocess.com")
        int taskID,
        @WebParam(name = "userEmailOrLogin", targetNamespace = "http://targetprocess.com")
        String userEmailOrLogin,
        @WebParam(name = "actorName", targetNamespace = "http://targetprocess.com")
        String actorName);

    @WebMethod(operationName = "AddCommentToTask", action = "http://targetprocess.com/AddCommentToTask")
    @WebResult(name = "AddCommentToTaskResult", targetNamespace = "http://targetprocess.com")
    public int addCommentToTask(
        @WebParam(name = "taskID", targetNamespace = "http://targetprocess.com")
        int taskID,
        @WebParam(name = "comment", targetNamespace = "http://targetprocess.com")
        CommentDTO comment);

    @WebMethod(operationName = "RetrieveAttachmentsForTask", action = "http://targetprocess.com/RetrieveAttachmentsForTask")
    @WebResult(name = "RetrieveAttachmentsForTaskResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfAttachmentDTO retrieveAttachmentsForTask(
        @WebParam(name = "taskID", targetNamespace = "http://targetprocess.com")
        int taskID);

    @WebMethod(operationName = "RetrieveOpenForMe", action = "http://targetprocess.com/RetrieveOpenForMe")
    @WebResult(name = "RetrieveOpenForMeResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfTaskDTO retrieveOpenForMe();

    @WebMethod(operationName = "RetrievePage", action = "http://targetprocess.com/RetrievePage")
    @WebResult(name = "RetrievePageResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfTaskDTO retrievePage(
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
        @WebParam(name = "taskID", targetNamespace = "http://targetprocess.com")
        int taskID,
        @WebParam(name = "entityStateID", targetNamespace = "http://targetprocess.com")
        int entityStateID);

    @WebMethod(operationName = "AddRevisionAssignableToTask", action = "http://targetprocess.com/AddRevisionAssignableToTask")
    @WebResult(name = "AddRevisionAssignableToTaskResult", targetNamespace = "http://targetprocess.com")
    public int addRevisionAssignableToTask(
        @WebParam(name = "taskID", targetNamespace = "http://targetprocess.com")
        int taskID,
        @WebParam(name = "revisionAssignable", targetNamespace = "http://targetprocess.com")
        RevisionAssignableDTO revisionAssignable);

    @WebMethod(operationName = "Update", action = "http://targetprocess.com/Update")
    public void update(
        @WebParam(name = "entity", targetNamespace = "http://targetprocess.com")
        TaskDTO entity);

    @WebMethod(operationName = "AssignUserByEmailOrLogin", action = "http://targetprocess.com/AssignUserByEmailOrLogin")
    public void assignUserByEmailOrLogin(
        @WebParam(name = "taskID", targetNamespace = "http://targetprocess.com")
        int taskID,
        @WebParam(name = "userEmailOrLogin", targetNamespace = "http://targetprocess.com")
        String userEmailOrLogin);

    @WebMethod(operationName = "RetrieveAllForOwner", action = "http://targetprocess.com/RetrieveAllForOwner")
    @WebResult(name = "RetrieveAllForOwnerResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfTaskDTO retrieveAllForOwner(
        @WebParam(name = "ownerID", targetNamespace = "http://targetprocess.com")
        int ownerID);

    @WebMethod(operationName = "AddRequestGeneralToTask", action = "http://targetprocess.com/AddRequestGeneralToTask")
    @WebResult(name = "AddRequestGeneralToTaskResult", targetNamespace = "http://targetprocess.com")
    public int addRequestGeneralToTask(
        @WebParam(name = "taskID", targetNamespace = "http://targetprocess.com")
        int taskID,
        @WebParam(name = "requestGeneral", targetNamespace = "http://targetprocess.com")
        RequestGeneralDTO requestGeneral);

    @WebMethod(operationName = "Create", action = "http://targetprocess.com/Create")
    @WebResult(name = "CreateResult", targetNamespace = "http://targetprocess.com")
    public int create(
        @WebParam(name = "entity", targetNamespace = "http://targetprocess.com")
        TaskDTO entity);

    @WebMethod(operationName = "RetrieveAllForPriority", action = "http://targetprocess.com/RetrieveAllForPriority")
    @WebResult(name = "RetrieveAllForPriorityResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfTaskDTO retrieveAllForPriority(
        @WebParam(name = "priorityID", targetNamespace = "http://targetprocess.com")
        int priorityID);

    @WebMethod(operationName = "RetrieveCommentsForTask", action = "http://targetprocess.com/RetrieveCommentsForTask")
    @WebResult(name = "RetrieveCommentsForTaskResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfCommentDTO retrieveCommentsForTask(
        @WebParam(name = "taskID", targetNamespace = "http://targetprocess.com")
        int taskID);

    @WebMethod(operationName = "Retrieve", action = "http://targetprocess.com/Retrieve")
    @WebResult(name = "RetrieveResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfTaskDTO retrieve(
        @WebParam(name = "hql", targetNamespace = "http://targetprocess.com")
        String hql,
        @WebParam(name = "parameters", targetNamespace = "http://targetprocess.com")
        ArrayOfAnyType parameters);

    @WebMethod(operationName = "RetrieveAllForIteration", action = "http://targetprocess.com/RetrieveAllForIteration")
    @WebResult(name = "RetrieveAllForIterationResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfTaskDTO retrieveAllForIteration(
        @WebParam(name = "iterationID", targetNamespace = "http://targetprocess.com")
        int iterationID);

    @WebMethod(operationName = "RetrieveOpenForUser", action = "http://targetprocess.com/RetrieveOpenForUser")
    @WebResult(name = "RetrieveOpenForUserResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfTaskDTO retrieveOpenForUser(
        @WebParam(name = "userID", targetNamespace = "http://targetprocess.com")
        int userID);

    @WebMethod(operationName = "AssignUserAsActor", action = "http://targetprocess.com/AssignUserAsActor")
    public void assignUserAsActor(
        @WebParam(name = "taskID", targetNamespace = "http://targetprocess.com")
        int taskID,
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

    @WebMethod(operationName = "GetPriorities", action = "http://targetprocess.com/GetPriorities")
    @WebResult(name = "GetPrioritiesResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfPriorityDTO getPriorities();

    @WebMethod(operationName = "RetrieveAll", action = "http://targetprocess.com/RetrieveAll")
    @WebResult(name = "RetrieveAllResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfTaskDTO retrieveAll();

    @WebMethod(operationName = "GetByID", action = "http://targetprocess.com/GetByID")
    @WebResult(name = "GetByIDResult", targetNamespace = "http://targetprocess.com")
    public TaskDTO getByID(
        @WebParam(name = "id", targetNamespace = "http://targetprocess.com")
        int id);

    @WebMethod(operationName = "RetrieveAllForLastCommentUser", action = "http://targetprocess.com/RetrieveAllForLastCommentUser")
    @WebResult(name = "RetrieveAllForLastCommentUserResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfTaskDTO retrieveAllForLastCommentUser(
        @WebParam(name = "lastCommentUserID", targetNamespace = "http://targetprocess.com")
        int lastCommentUserID);

    @WebMethod(operationName = "AddAttachmentToTask", action = "http://targetprocess.com/AddAttachmentToTask")
    @WebResult(name = "AddAttachmentToTaskResult", targetNamespace = "http://targetprocess.com")
    public int addAttachmentToTask(
        @WebParam(name = "taskID", targetNamespace = "http://targetprocess.com")
        int taskID,
        @WebParam(name = "fileName", targetNamespace = "http://targetprocess.com")
        String fileName,
        @WebParam(name = "description", targetNamespace = "http://targetprocess.com")
        String description);

    @WebMethod(operationName = "RetrieveAllForEntityState", action = "http://targetprocess.com/RetrieveAllForEntityState")
    @WebResult(name = "RetrieveAllForEntityStateResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfTaskDTO retrieveAllForEntityState(
        @WebParam(name = "entityStateID", targetNamespace = "http://targetprocess.com")
        int entityStateID);

    @WebMethod(operationName = "RemoveTeamFromTask", action = "http://targetprocess.com/RemoveTeamFromTask")
    public void removeTeamFromTask(
        @WebParam(name = "taskID", targetNamespace = "http://targetprocess.com")
        int taskID,
        @WebParam(name = "teamID", targetNamespace = "http://targetprocess.com")
        int teamID);

    @WebMethod(operationName = "RemoveAttachmentFromTask", action = "http://targetprocess.com/RemoveAttachmentFromTask")
    public void removeAttachmentFromTask(
        @WebParam(name = "taskID", targetNamespace = "http://targetprocess.com")
        int taskID,
        @WebParam(name = "attachmentID", targetNamespace = "http://targetprocess.com")
        int attachmentID);

    @WebMethod(operationName = "RetrieveActorEffortsForTask", action = "http://targetprocess.com/RetrieveActorEffortsForTask")
    @WebResult(name = "RetrieveActorEffortsForTaskResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfActorEffortDTO retrieveActorEffortsForTask(
        @WebParam(name = "taskID", targetNamespace = "http://targetprocess.com")
        int taskID);

}
