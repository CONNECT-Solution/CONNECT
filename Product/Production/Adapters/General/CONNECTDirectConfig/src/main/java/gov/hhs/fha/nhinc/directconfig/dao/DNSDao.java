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

package gov.hhs.fha.nhinc.directconfig.dao;

import gov.hhs.fha.nhinc.directconfig.entity.DNSRecord;
import java.util.Collection;



/**
 * DAO interface for DNS records
 * @author Greg Meyer
 * @since 1.1
 */
public interface DNSDao
{
	/**
	 * Gets the number of records in the DNS store.
	 * @return The number of records in the DNS store.
	 */
	public int count();

	/**
	 * Gets DNS records by record name.
	 * @param name The record name.
	 * @return A collection of records matching the name and of any type.
	 */
	public Collection<DNSRecord> get(String name);

	/**
	 * Gets DNS records by record name and a specific record type.
	 * @param name The record name.
	 * @param type The record type to search for.
	 * @return A collection of records matching the name and record type.
	 */
	public Collection<DNSRecord> get(String name, int type);

	/**
	 * Gets all DNS records or a given type.  Using type ANY will return all records in the store.
	 * @param type The record type to search for.
	 * @return A collection of records matching the record type.
	 */
	public Collection<DNSRecord> get(int type);

	/**
	 * Gets DNS records by the internal record ids.
	 * @param recordIds Array of record ids to search for.
	 * @return A collection of records matching the record ids.
	 */
	public Collection<DNSRecord> get(long[] recordIds);

	/**
	 * Gets a single DNS record for an internal record id.
	 * @param recordId The internal record id to search for.
	 * @return A DNS record matching the record id.
	 */
	public DNSRecord get(long recordId);

	/**
	 * Adds multiple new DNS records to the store.  The type cannot be ANY.
	 * @param records The records to add the store.  If a record already exists, then an exception is thrown.
	 */
	public void add(Collection<DNSRecord> records);

	/**
	 * Removes a single DNS record by an existing internal record id.
	 * @param recordId The internal record id to delete.
	 */
	public void remove(long recordId);

	/**
	 * Removes DNS records by existing internal record ids.
	 * @param recordIds The internal record ids to delete.
	 */
	public void remove(long[] recordIds);

	/**
	 * Removes DNS records matching the DNS records' name and type.
	 * @param records Records to delete.  Matching is done by name and type.
	 */
	public void remove(Collection<DNSRecord> records);

	/**
	 * Update a DNS record for a specific internal id.  If a record does not exist, then an exception is thrown.  The type cannot be ANY.
	 * @param id The internal record id to update.
	 * @param record Data to update the record with.
	 */
	public void update(long id, DNSRecord record);
}
