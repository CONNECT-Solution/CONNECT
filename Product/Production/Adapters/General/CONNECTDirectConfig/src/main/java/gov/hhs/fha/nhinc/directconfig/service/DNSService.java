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

import gov.hhs.fha.nhinc.directconfig.entity.DNSRecord;
import java.util.Collection;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Web service class for DNS service operations.
 *
 * @author Greg Meyer
 * @since 1.1
 */
@WebService
public interface DNSService
{
	/**
	 * Gets the number of records in the DNS store.
	 * @return The number of records in the DNS store.
	 * @throws ConfigurationServiceException
	 */
    @WebMethod(operationName = "getDNSCount", action = "urn:GetDNSCount")
    public int getDNSCount() throws ConfigurationServiceException;

	/**
	 * Gets DNS records by record name.
	 * @param name The record name.
	 * @return A collection of records matching the name and of any type.
	 * @throws ConfigurationServiceException
	 */
    @WebMethod(operationName = "getDNSByName", action = "urn:GetDNSByName")
    public Collection<DNSRecord> getDNSByName(@WebParam(name = "name") String name) throws ConfigurationServiceException;

	/**
	 * Gets all DNS records or a given type.  Using type ANY will return all records in the store.
	 * @param type The record type to search for.
	 * @return A collection of records matching the record type.
	 * @throws ConfigurationServiceException
	 */
    @WebMethod(operationName = "getDNSByType", action = "urn:GetDNSByType")
    public Collection<DNSRecord> getDNSByType(@WebParam(name = "type") int type) throws ConfigurationServiceException;

	/**
	 * Gets DNS records by record name and a specific record type.
	 * @param name The record name.
	 * @param type The record type to search for.
	 * @return A collection of records matching the name and record type.
	 * @throws ConfigurationServiceException
	 */
    @WebMethod(operationName = "getDNSByNameAndType", action = "urn:GetDNSByNameAndType")
    public Collection<DNSRecord> getDNSByNameAndType(@WebParam(name = "name") String name,
    		@WebParam(name = "type") int type) throws ConfigurationServiceException;

	/**
	 * Gets a single DNS record for an internal record id.
	 * @param recordId The internal record id to search for.
	 * @return A DNS record matching the record id.
	 * @throws ConfigurationServiceException
	 */
    @WebMethod(operationName = "getDNSByRecordId", action = "urn:GetDNSByRecordId")
    public DNSRecord getDNSByRecordId(@WebParam(name = "recordId") long recordId) throws ConfigurationServiceException;

	/**
	 * Gets DNS records by the internal record ids.
	 * @param recordIds Array of record ids to search for.
	 * @return A collection of records matching the record ids.
	 * @throws ConfigurationServiceException
	 */
    @WebMethod(operationName = "getDNSByRecordIds", action = "urn:GetDNSByRecordIds")
    public Collection<DNSRecord> getDNSByRecordIds(@WebParam(name = "recordIds") long[] recordIds) throws ConfigurationServiceException;

	/**
	 * Adds multiple new DNS records to the store.  The type cannot be ANY.
	 * @param records The records to add the store.  If a record already exists, then an exception is thrown.
	 * @throws ConfigurationServiceException
	 */
    @WebMethod(operationName = "addDNS", action = "urn:AddDNS")
    public void addDNS(@WebParam(name = "records") Collection<DNSRecord> records) throws ConfigurationServiceException;

	/**
	 * Removes DNS records matching the DNS records' name and type.
	 * @param records Records to delete.  Matching is done by name and type.
	 * @throws ConfigurationServiceException
	 */
    @WebMethod(operationName = "removeDNS", action = "urn:RemoveDNS")
    public void removeDNS(@WebParam(name = "records") Collection<DNSRecord> records) throws ConfigurationServiceException;

	/**
	 * Removes a single DNS record by an existing internal record id.
	 * @param recordId The internal record id to delete.
	 * @throws ConfigurationServiceException
	 */
    @WebMethod(operationName = "removeDNSByRecordId", action = "urn:RemoveDNSByRecordId")
    public void removeDNSByRecordId(@WebParam(name = "recordId") long recordId) throws ConfigurationServiceException;

	/**
	 * Removes DNS records by existing internal record ids.
	 * @param recordIds The internal record ids to delete.
	 * @throws ConfigurationServiceException
	 */
    @WebMethod(operationName = "removeDNSByRecordIds", action = "urn:RemoveDNSByRecordIds")
    public void removeDNSByRecordIds(@WebParam(name = "recordIds") long[] recordIds) throws ConfigurationServiceException;

	/**
	 * Update a DNS record for a specific internal id.  If a record does not exist, then an exception is thrown.  The type cannot be ANY.
	 * @param recordId The internal record id to update.
	 * @param record Data to update the record with.
	 * @throws ConfigurationServiceException
	 */
    @WebMethod(operationName = "updateDNS", action = "urn:UpdateDNS")
    public void updateDNS(@WebParam(name = "recordId") long recordId,
    		@WebParam(name = "record") DNSRecord record) throws ConfigurationServiceException;
}
