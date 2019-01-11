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

import gov.hhs.fha.nhinc.directconfig.entity.CertPolicy;
import gov.hhs.fha.nhinc.directconfig.entity.CertPolicyGroup;
import gov.hhs.fha.nhinc.directconfig.entity.CertPolicyGroupDomainReltn;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.CertPolicyUse;
import java.util.Collection;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import org.nhindirect.policy.PolicyLexicon;

@WebService
public interface CertificatePolicyService
{
    @WebMethod(operationName = "getPolicies", action = "urn:GetPolicies")
	public Collection<CertPolicy> getPolicies() throws ConfigurationServiceException;

    @WebMethod(operationName = "getPolicyByName", action = "urn:GetPolicyByName")
	public CertPolicy getPolicyByName(@WebParam(name = "policyName") String policyName) throws ConfigurationServiceException;

    @WebMethod(operationName = "getPolicyById", action = "urn:GetPolicyById")
	public CertPolicy getPolicyById(@WebParam(name = "policyId") long id) throws ConfigurationServiceException;

    @WebMethod(operationName = "addPolicy", action = "urn:AddPolicy")
	public void addPolicy(@WebParam(name = "policy") CertPolicy policy) throws ConfigurationServiceException;

    @WebMethod(operationName = "deletePolicies", action = "urn:DeletePolicies")
	public void deletePolicies(@WebParam(name = "policyIds") long[] policyIds) throws ConfigurationServiceException;

    @WebMethod(operationName = "updatePolicyAttributes", action = "urn:UpdatePolicyAttributes")
	public void updatePolicyAttributes(@WebParam(name = "policyId") long id, @WebParam(name = "policyName") String policyName,
			@WebParam(name = "policyLexicon") PolicyLexicon lexicon,  @WebParam(name = "policyData") byte[] policyData) throws ConfigurationServiceException;

    @WebMethod(operationName = "getPolicyGroups", action = "urn:GetPolicyGroups")
	public Collection<CertPolicyGroup> getPolicyGroups() throws ConfigurationServiceException;

    @WebMethod(operationName = "getPolicyGroupByName", action = "urn:GetPolicyGroupByName")
	public CertPolicyGroup getPolicyGroupByName(@WebParam(name = "policyGroupName")  String policyGroupName) throws ConfigurationServiceException;

    @WebMethod(operationName = "getPolicyGroupById", action = "urn:GetPolicyGroupById")
	public CertPolicyGroup getPolicyGroupById(@WebParam(name = "policyGroupId") long id) throws ConfigurationServiceException;

    @WebMethod(operationName = "addPolicyGroup", action = "urn:AddPolicyGroup")
	public void addPolicyGroup(@WebParam(name = "policyGroup") CertPolicyGroup group) throws ConfigurationServiceException;

    @WebMethod(operationName = "deletePolicyGroups", action = "urn:DeletePolicyGroups")
	public void deletePolicyGroups(@WebParam(name = "policyGroupIds") long[] groupIds) throws ConfigurationServiceException;

    @WebMethod(operationName = "updateGroupAttributes", action = "urn:UpdateGroupAttributes")
	public void updateGroupAttributes(@WebParam(name = "policyGroupId") long id,
			@WebParam(name = "policyGroupName")String groupName) throws ConfigurationServiceException;

    @WebMethod(operationName = "addPolicyUseToGroup", action = "urn:AddPolicyUseToGroup")
	public void addPolicyUseToGroup(@WebParam(name = "policyGroupId") long groupId, @WebParam(name = "policyId") long policyId,
			@WebParam(name = "policyUse") CertPolicyUse policyUse,
			@WebParam(name = "incoming") boolean incoming, @WebParam(name = "outgoing") boolean outgoing) throws ConfigurationServiceException;

    @WebMethod(operationName = "removePolicyUseFromGroup", action = "urn:RemovePolicyUseFromGroup")
	public void removePolicyUseFromGroup(@WebParam(name = "policyGroupReltnId") long policyGroupReltnId) throws ConfigurationServiceException;

    @WebMethod(operationName = "associatePolicyGroupToDomain", action = "urn:AssociatePolicyGroupToDomain")
	public void associatePolicyGroupToDomain(@WebParam(name = "domainId") long domainId,
			@WebParam(name = "policyGroupId") long policyGroupId) throws ConfigurationServiceException;

    @WebMethod(operationName = "disassociatePolicyGroupFromDomain", action = "urn:DisassociatePolicyGroupFromDomain")
	public void disassociatePolicyGroupFromDomain(@WebParam(name = "domainId") long domainId,
			@WebParam(name = "policyGroupId") long policyGroupId) throws ConfigurationServiceException;

    @WebMethod(operationName = "disassociatePolicyGroupsFromDomain", action = "urn:DisassociatePolicyGroupsFromDomain")
	public void disassociatePolicyGroupsFromDomain(@WebParam(name = "domainId") long domainId) throws ConfigurationServiceException;

    @WebMethod(operationName = "disassociatePolicyGroupFromDomains", action = "urn:DisassociatePolicyGroupFromDomains")
	public void disassociatePolicyGroupFromDomains(@WebParam(name = "policyGroupId") long policyGroupId) throws ConfigurationServiceException;

    @WebMethod(operationName = "getPolicyGroupDomainReltns", action = "urn:GetPolicyGroupDomainReltns")
	public Collection<CertPolicyGroupDomainReltn> getPolicyGroupDomainReltns() throws ConfigurationServiceException;

    @WebMethod(operationName = "getPolicyGroupsByDomain", action = "urn:GetPolicyGroupsByDomain")
	public Collection<CertPolicyGroupDomainReltn> getPolicyGroupsByDomain(
			@WebParam(name = "domainId") long domainId) throws ConfigurationServiceException;
}
