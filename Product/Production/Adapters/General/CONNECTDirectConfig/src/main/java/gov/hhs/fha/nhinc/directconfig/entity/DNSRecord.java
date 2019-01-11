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

package gov.hhs.fha.nhinc.directconfig.entity;

import gov.hhs.fha.nhinc.directconfig.entity.helpers.DNSRecordUtils;
import java.io.IOException;
import java.util.Calendar;
import org.bouncycastle.util.Arrays;

/**
 * The JPA Domain class representing a DNS record. This is a generic DNS record that can represent (in theory) any DNS
 * record type.
 *
 * @author Greg Meyer
 * @since 1.1
 */
public class DNSRecord {
    private Long id;
    private String name;
    private int type;
    private int dclass;
    private long ttl;
    private byte[] data;
    private Calendar createTime;

    /**
     * Construct a DNSRecord.
     */
    public DNSRecord() {
    }

    /**
     * Gets the internal id of the record. The record id is the primary key of the record JPA store.
     *
     * @return The internal id of the record.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the internal id of the record.
     *
     * @param id The internal id of the record.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name associated with this DNS entry. The is generally the name that is used for lookup purposes.
     *
     * @return The name associated with this DNS entry.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name associated with this DNS entry.
     *
     * @param name The name associated with this DNS entry.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the type of the DNS record such as A, SRV, CERT, MX, and SOA.
     *
     * @return The type of the DNS record.
     */
    public int getType() {
        return type;
    }

    /**
     * Gets the type of the DNS record such as A, SRV, CERT, MX, and SOA.
     *
     * @param type The type of the DNS record.
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Gets the DNS record class such as IN, HS, and CH.
     *
     * @return The DNS record class.
     */
    public int getDclass() {
        return dclass;
    }

    /**
     * Sets the DNS record class.
     *
     * @param dclass The DNS record class.
     */
    public void setDclass(int dclass) {
        this.dclass = dclass;
    }

    /**
     * Gets the record time to live in seconds. The ttl represents how long a record can cached before it is considered
     * stale.
     *
     * @return The record time to live in seconds.
     */
    public long getTtl() {
        return ttl;
    }

    /**
     * Sets the record time to live in seconds.
     *
     * @param ttl The record time to live in seconds.
     */
    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    /**
     * Gets the date/time the record was created.
     *
     * @return Gets the date/time the record was created.
     */
    public Calendar getCreateTime() {
        if (createTime == null) {
            setCreateTime(Calendar.getInstance());
        }

        return createTime;
    }

    /**
     * Sets the date/time the record was created.
     *
     * @param timestamp The date/time the record was created.
     */
    public void setCreateTime(Calendar timestamp) {
        createTime = timestamp;
    }

    /**
     * Get the rdata of the record. Rdata is generally the value of a DNS lookup such an IP address for an A lookup or
     * an X509 certificate for a CERT lookup.
     *
     * @return The Rdata of the record.
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Sets the rdata of the record.
     *
     * @param data The rdata of the record.
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * Converts a raw wire transfer format of a record to a DNS record.
     *
     * @param data The raw byte stream of a record in wire transfer format.
     * @return A DNSRecord converted from the wire format.
     * @throws IOException
     */
    public static DNSRecord fromWire(byte[] data) throws IOException {
        return DNSRecordUtils.fromWire(data);
    }

    /**
     * Converts a DNS record to a raw wire transfer format.
     *
     * @param rec The DNSRecord to convert.
     * @return A byte array representation of the DNSRecord in raw wire transfer format.
     * @throws IOException
     */
    public static byte[] toWire(DNSRecord rec) throws IOException {
        return DNSRecordUtils.toWire(rec);
    }

    /**
     * {@inheritDoc}
     *
     * @param ob
     * @return
     */
    @Override
    public boolean equals(Object ob) {
        if (!(ob instanceof DNSRecord)) {
            return false;
        }

        DNSRecord rec = (DNSRecord) ob;

        return (rec.dclass == dclass && rec.type == type && rec.name.equals(name) && Arrays.areEqual(rec.getData(),
                data));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 61 * hash + this.type;
        hash = 61 * hash + this.dclass;
        hash = 61 * hash + java.util.Arrays.hashCode(this.data);
        return hash;
    }
}
