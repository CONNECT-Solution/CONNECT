
package com.targetprocess.integration.time;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "TimeServiceSoap", targetNamespace = "http://targetprocess.com")
@SOAPBinding(use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface TimeServiceSoap {


    @WebMethod(operationName = "Create", action = "http://targetprocess.com/Create")
    @WebResult(name = "CreateResult", targetNamespace = "http://targetprocess.com")
    public int create(
        @WebParam(name = "entity", targetNamespace = "http://targetprocess.com")
        TimeDTO entity);

    @WebMethod(operationName = "Retrieve", action = "http://targetprocess.com/Retrieve")
    @WebResult(name = "RetrieveResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfTimeDTO retrieve(
        @WebParam(name = "hql", targetNamespace = "http://targetprocess.com")
        String hql,
        @WebParam(name = "parameters", targetNamespace = "http://targetprocess.com")
        ArrayOfAnyType parameters);

    @WebMethod(operationName = "RetrieveAllForCustomActivity", action = "http://targetprocess.com/RetrieveAllForCustomActivity")
    @WebResult(name = "RetrieveAllForCustomActivityResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfTimeDTO retrieveAllForCustomActivity(
        @WebParam(name = "customActivityID", targetNamespace = "http://targetprocess.com")
        int customActivityID);

    @WebMethod(operationName = "GetIDs", action = "http://targetprocess.com/GetIDs")
    @WebResult(name = "GetIDsResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfInt getIDs(
        @WebParam(name = "hql", targetNamespace = "http://targetprocess.com")
        String hql,
        @WebParam(name = "parameters", targetNamespace = "http://targetprocess.com")
        ArrayOfAnyType parameters);

    @WebMethod(operationName = "RetrieveAllForProject", action = "http://targetprocess.com/RetrieveAllForProject")
    @WebResult(name = "RetrieveAllForProjectResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfTimeDTO retrieveAllForProject(
        @WebParam(name = "projectID", targetNamespace = "http://targetprocess.com")
        int projectID);

    @WebMethod(operationName = "Delete", action = "http://targetprocess.com/Delete")
    public void delete(
        @WebParam(name = "id", targetNamespace = "http://targetprocess.com")
        int id);

    @WebMethod(operationName = "RetrieveCount", action = "http://targetprocess.com/RetrieveCount")
    @WebResult(name = "RetrieveCountResult", targetNamespace = "http://targetprocess.com")
    public int retrieveCount(
        @WebParam(name = "hql", targetNamespace = "http://targetprocess.com")
        String hql,
        @WebParam(name = "parameters", targetNamespace = "http://targetprocess.com")
        ArrayOfAnyType parameters);

    @WebMethod(operationName = "GetByID", action = "http://targetprocess.com/GetByID")
    @WebResult(name = "GetByIDResult", targetNamespace = "http://targetprocess.com")
    public TimeDTO getByID(
        @WebParam(name = "id", targetNamespace = "http://targetprocess.com")
        int id);

    @WebMethod(operationName = "RetrieveAll", action = "http://targetprocess.com/RetrieveAll")
    @WebResult(name = "RetrieveAllResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfTimeDTO retrieveAll();

    @WebMethod(operationName = "RetrievePage", action = "http://targetprocess.com/RetrievePage")
    @WebResult(name = "RetrievePageResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfTimeDTO retrievePage(
        @WebParam(name = "hql", targetNamespace = "http://targetprocess.com")
        String hql,
        @WebParam(name = "page", targetNamespace = "http://targetprocess.com")
        int page,
        @WebParam(name = "pageSize", targetNamespace = "http://targetprocess.com")
        int pageSize,
        @WebParam(name = "parameters", targetNamespace = "http://targetprocess.com")
        ArrayOfAnyType parameters);

    @WebMethod(operationName = "RetrieveAllForActor", action = "http://targetprocess.com/RetrieveAllForActor")
    @WebResult(name = "RetrieveAllForActorResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfTimeDTO retrieveAllForActor(
        @WebParam(name = "actorID", targetNamespace = "http://targetprocess.com")
        int actorID);

    @WebMethod(operationName = "Update", action = "http://targetprocess.com/Update")
    public void update(
        @WebParam(name = "entity", targetNamespace = "http://targetprocess.com")
        TimeDTO entity);

    @WebMethod(operationName = "RetrieveAllForUser", action = "http://targetprocess.com/RetrieveAllForUser")
    @WebResult(name = "RetrieveAllForUserResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfTimeDTO retrieveAllForUser(
        @WebParam(name = "userID", targetNamespace = "http://targetprocess.com")
        int userID);

    @WebMethod(operationName = "RetrieveAllForAssignable", action = "http://targetprocess.com/RetrieveAllForAssignable")
    @WebResult(name = "RetrieveAllForAssignableResult", targetNamespace = "http://targetprocess.com")
    public ArrayOfTimeDTO retrieveAllForAssignable(
        @WebParam(name = "assignableID", targetNamespace = "http://targetprocess.com")
        int assignableID);

}
