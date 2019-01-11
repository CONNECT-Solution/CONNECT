/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
/*
 Copyright (c) 2010, NHIN Direct Project
 All rights reserved.

 Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer
 in the documentation and/or other materials provided with the distribution.
 3. Neither the name of the The NHIN Direct Project (nhindirect.org) nor the names of its contributors may be used to endorse or promote
 products derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS
 BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 THE POSSIBILITY OF SUCH DAMAGE.
 */

package gov.hhs.fha.nhinc.directconfig.service;

import gov.hhs.fha.nhinc.directconfig.service.jaxws.AddAnchors;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.AddAnchorsResponse;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.GetAnchor;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.GetAnchorResponse;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.GetAnchors;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.GetAnchorsForOwner;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.GetAnchorsForOwnerResponse;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.GetAnchorsResponse;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.GetIncomingAnchors;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.GetIncomingAnchorsResponse;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.GetOutgoingAnchors;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.GetOutgoingAnchorsResponse;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.ListAnchors;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.ListAnchorsResponse;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.RemoveAnchors;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.RemoveAnchorsForOwner;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.RemoveAnchorsForOwnerResponse;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.RemoveAnchorsResponse;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.SetAnchorStatusForOwner;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.SetAnchorStatusForOwnerResponse;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Service class for methods related to an Anchor object.
 */
@WebService
public interface AnchorService {

    /**
     * Add a collection of Anchors.
     *
     * @param anchors A collection of Anchors.
     * @throws ConfigurationServiceException
     */
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    @WebResult(name = "addAnchorResponse", targetNamespace = "http://nhind.org/config", partName = "parameters")
    @WebMethod(operationName = "addAnchor", action = "urn:AddAnchor")
    AddAnchorsResponse addAnchors(
            @WebParam(partName = "parameters", name = "addAnchor", targetNamespace = "http://nhind.org/config") AddAnchors addAnchors)
            throws ConfigurationServiceException;

    /**
     * Get an Anchor.
     *
     * @param owner The Anchor owner.
     * @param thumbprint The Anchor thumbprint.
     * @param options The Anchor options.
     * @return an Anchor.
     * @throws ConfigurationServiceException
     */
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    @WebResult(name = "getAnchorResponse", targetNamespace = "http://nhind.org/config", partName = "parameters")
    @WebMethod(operationName = "getAnchor", action = "urn:GetAnchor")
    GetAnchorResponse getAnchor(
            @WebParam(partName = "parameters", name = "getAnchor", targetNamespace = "http://nhind.org/config") GetAnchor getAnchor)
            throws ConfigurationServiceException;

    /**
     * Get a collection of Anchors.
     *
     * @param anchorIds A collection of Anchor IDs.
     * @param options The Anchor options.
     * @return a collection of Anchors.
     * @throws ConfigurationServiceException
     */
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    @WebResult(name = "getAnchorsResponse", targetNamespace = "http://nhind.org/config", partName = "parameters")
    @WebMethod(operationName = "getAnchors", action = "urn:GetAnchors")
    GetAnchorsResponse getAnchors(
            @WebParam(partName = "parameters", name = "getAnchors", targetNamespace = "http://nhind.org/config") GetAnchors getAnchors)
            throws ConfigurationServiceException;

    /**
     * Get a collection of Anchors for an owner.
     *
     * @param owner The Anchor owner.
     * @param options The Anchor options.
     * @return a collection of Anchors.
     * @throws ConfigurationServiceException
     */
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    @WebResult(name = "getAnchorsForOwnerResponse", targetNamespace = "http://nhind.org/config", partName = "parameters")
    @WebMethod(operationName = "getAnchorsForOwner", action = "urn:GetAnchorsForOwner")
    GetAnchorsForOwnerResponse getAnchorsForOwner(
            @WebParam(partName = "parameters", name = "getAnchorsForOwner", targetNamespace = "http://nhind.org/config") GetAnchorsForOwner getAnchorsForOwner)
            throws ConfigurationServiceException;

    /**
     * Get a collection of incoming Anchors.
     *
     * @param owner The Anchor owner.
     * @param options The Anchor options.
     * @return a collection of Anchors.
     * @throws ConfigurationServiceException
     */
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    @WebResult(name = "getIncomingAnchorsResponse", targetNamespace = "http://nhind.org/config", partName = "parameters")
    @WebMethod(operationName = "getIncomingAnchors", action = "urn:GetIncomingAnchors")
    GetIncomingAnchorsResponse getIncomingAnchors(
            @WebParam(partName = "parameters", name = "getIncomingAnchors", targetNamespace = "http://nhind.org/config") GetIncomingAnchors getIncomingAnchors)
            throws ConfigurationServiceException;

    /**
     * Get a collection of outgoing Anchors.
     *
     * @param owner The Anchor owner.
     * @param options The Anchor options.
     * @return a collection of Anchors.
     * @throws ConfigurationServiceException
     */
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    @WebResult(name = "getOutgoingAnchorsResponse", targetNamespace = "http://nhind.org/config", partName = "parameters")
    @WebMethod(operationName = "getOutgoingAnchors", action = "urn:GetOutgoingAnchors")
    GetOutgoingAnchorsResponse getOutgoingAnchors(
            @WebParam(partName = "parameters", name = "getOutgoingAnchors", targetNamespace = "http://nhind.org/config") GetOutgoingAnchors getOutgoingAnchors)
            throws ConfigurationServiceException;

    /**
     * Set an Anchor status for a given owner.
     *
     * @param owner The anchor owner.
     * @param status The anchor status.
     * @throws ConfigurationServiceException
     */
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    @WebResult(name = "setAnchorStatusForOwnerResponse", targetNamespace = "http://nhind.org/config", partName = "parameters")
    @WebMethod(operationName = "setAnchorStatusForOwner", action = "urn:SetAnchorStatusForOwner")
    SetAnchorStatusForOwnerResponse setAnchorStatusForOwner(
            @WebParam(partName = "parameters", name = "setAnchorStatusForOwner", targetNamespace = "http://nhind.org/config") SetAnchorStatusForOwner setAnchorStatusForOwner)
            throws ConfigurationServiceException;

    /**
     * Get a collection of Anchors.
     *
     * @param lastAnchorID The last Anchor ID.
     * @param maxResults The maximum number of results.
     * @param options The Anchor options.
     * @return a collection of Anchors.
     * @throws ConfigurationServiceException
     */
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    @WebResult(name = "listAnchorsResponse", targetNamespace = "http://nhind.org/config", partName = "parameters")
    @WebMethod(operationName = "listAnchors", action = "urn:ListAnchors")
    ListAnchorsResponse listAnchors(
            @WebParam(partName = "parameters", name = "setAnchorStatusForOwner", targetNamespace = "http://nhind.org/config") ListAnchors listAnchors)
            throws ConfigurationServiceException;

    /**
     * Remove an Anchor.
     *
     * @param anchorIds A collection of Anchor IDs.
     * @throws ConfigurationServiceException
     */
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    @WebResult(name = "removeAnchorsResponse", targetNamespace = "http://nhind.org/config", partName = "parameters")
    @WebMethod(operationName = "removeAnchors", action = "urn:RemoveAnchors")
    RemoveAnchorsResponse removeAnchors(
            @WebParam(partName = "parameters", name = "removeAnchors", targetNamespace = "http://nhind.org/config") RemoveAnchors removeAnchors)
            throws ConfigurationServiceException;

    /**
     * Remove the Anchors for an owner.
     *
     * @param owner The Anchor owner.
     * @throws ConfigurationServiceException
     */
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    @WebResult(name = "removeAnchorsForOwnerResponse", targetNamespace = "http://nhind.org/config", partName = "parameters")
    @WebMethod(operationName = "removeAnchorsForOwner", action = "urn:RemoveAnchorsForOwner")
    RemoveAnchorsForOwnerResponse removeAnchorsForOwner(
            @WebParam(partName = "parameters", name = "removeAnchorsForOwner", targetNamespace = "http://nhind.org/config") RemoveAnchorsForOwner removeAnchorsForOwner)
            throws ConfigurationServiceException;

}
