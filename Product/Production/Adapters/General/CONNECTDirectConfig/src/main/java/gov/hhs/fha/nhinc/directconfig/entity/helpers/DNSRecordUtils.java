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

package gov.hhs.fha.nhinc.directconfig.entity.helpers;

import gov.hhs.fha.nhinc.directconfig.entity.DNSRecord;
import gov.hhs.fha.nhinc.directconfig.exception.ConfigurationStoreException;
import java.io.IOException;
import java.net.InetAddress;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAKey;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.CERTRecord;
import org.xbill.DNS.DClass;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.SOARecord;
import org.xbill.DNS.SRVRecord;
import org.xbill.DNS.Section;

/**
 * Utility class for creating and serializing DNS records.
 * @author Greg Meyer
 * @since 1.1
 */
public class DNSRecordUtils
{
    /**
     * Creates a DNS A type record.
     * @param name The record name.  Generally a fully qualified domain name such as host.example.com.
     * @param ttl The time to live in seconds.
     * @param ip The ip4 address that the name will resolve.
     * @return A DNSRecord representing an A type record.
     * @throws ConfigurationStoreException
     */
    public static DNSRecord createARecord(String name, long ttl, String ip) throws ConfigurationStoreException
    {
        if (!name.endsWith("."))
            name = name + ".";

        try
        {
            ARecord rec = new ARecord(Name.fromString(name), DClass.IN, ttl, InetAddress.getByName(ip));

            return DNSRecord.fromWire(rec.toWireCanonical());
        }
        catch (Exception e)
        {
            throw new ConfigurationStoreException("Failed to create DNS A record: " + e.getMessage(), e);
        }
    }

    /**
     * Create a DNS SRV type record.
     * @param name The service name.
     * @param target The target name that is hosting the service.
     * @param ttl The time to live in seconds.
     * @param port The port that the service is offered on.
     * @param priority The priority of the service.  Lower priorities are preferred.
     * @param weight A value used to select between services with the same priority.
     * @return A DNSRecord representing an SRV type record.
     * @throws ConfigurationStoreException
     */
    public static DNSRecord createSRVRecord(String name, String target, long ttl, int port, int priority, int weight) throws ConfigurationStoreException
    {
        if (!name.endsWith("."))
            name = name + ".";

        if (!target.endsWith("."))
            target = target + ".";

        try
        {
            SRVRecord rec = new SRVRecord(Name.fromString(name), DClass.IN, ttl, priority, weight, port, Name.fromString(target));

            return DNSRecord.fromWire(rec.toWireCanonical());
        }
        catch (Exception e)
        {
            throw new ConfigurationStoreException("Failed to create DNS SRV record: " + e.getMessage(), e);
        }
    }

    /**
     * Creates a DNS CERT record containing an X509 public certificate.
     * @param address The name or address corresponding to the certificate.
     * @param ttl The time to live in seconds.
     * @param cert The X509 public certificate to be stored with the name/address.
     * @return A DNSRecord representing a CERT type record.
     * @throws ConfigurationStoreException
     */
    public static DNSRecord createX509CERTRecord(String address, long ttl, X509Certificate cert) throws ConfigurationStoreException
    {
        if (!address.endsWith("."))
            address = address + ".";

        try
        {
            int keyTag = 0;
            if (cert.getPublicKey() instanceof RSAKey)
            {
                RSAKey key = (RSAKey)cert.getPublicKey();
                byte[] modulus = key.getModulus().toByteArray();

                keyTag = (modulus[modulus.length - 2] << 8) & 0xFF00;

                keyTag |= modulus[modulus.length - 1] & 0xFF;
            }

            CERTRecord rec = new CERTRecord(Name.fromString(address), DClass.IN, ttl, CERTRecord.PKIX, keyTag,
                    5 /*public key alg, RFC 4034*/, cert.getEncoded());

            return DNSRecord.fromWire(rec.toWireCanonical());
        }
        catch (Exception e)
        {
            throw new ConfigurationStoreException("Failed to create DNS CERT record: " + e.getMessage(), e);
        }
    }

    /**
     * Creates a DNS MX record.
     * @param name The email domain or host used to determine where email should be sent to.
     * @param target The host server that email should be sent to.
     * @param ttl The time to live in seconds.
     * @param priority The priority of the target host.  Lower priorities are preferred.
     * @return A DNSRecord representing an MX type record.
     * @throws ConfigurationStoreException
     */
    public static DNSRecord createMXRecord(String name, String target, long ttl, int priority) throws ConfigurationStoreException
    {
        if (!name.endsWith("."))
            name = name + ".";

        if (!target.endsWith("."))
            target = target + ".";

        try
        {
            MXRecord rec = new MXRecord(Name.fromString(name), DClass.IN, ttl, priority, Name.fromString(target));

            return DNSRecord.fromWire(rec.toWireCanonical());
        }
        catch (Exception e)
        {
            throw new ConfigurationStoreException("Failed to create DNS MX record: " + e.getMessage(), e);
        }
    }

    /**
     * Create a DNS SOA record.
     * @param name The root name of the zone.
     * @param ttl The time to live in seconds.
     * @param nameServer Name of the server that responds authoritatively to this zone.
     * @param hostMaster The email address of the administrator of the zone.  The @ symbol is replaced with a "."
     * @param serial The current serial number of record.
     * @param refresh The time in seconds the a slave server tries to refresh its information from the master.
     * @param retry The time in seconds that a slave server retires after a failed refresh.
     * @param expire Time in seconds when the zone data is not longer authoritative.
     * @param minumum See RFC 2308.
     * @return A DNSRecord representing an SOA record.
     */
    public static DNSRecord createSOARecord(String name, long ttl, String nameServer, String hostMaster,
            int serial, long refresh, long retry, long expire, long minumum)
    {
        if (!name.endsWith("."))
            name = name + ".";

        if (!nameServer.endsWith("."))
            nameServer = nameServer + ".";

        if (!hostMaster.endsWith("."))
            hostMaster = hostMaster + ".";

        try
        {
            SOARecord rec = new SOARecord(Name.fromString(name), DClass.IN, ttl, Name.fromString(nameServer),
                    Name.fromString(hostMaster), serial, refresh, retry, expire, minumum);

            return DNSRecord.fromWire(rec.toWireCanonical());
        }
        catch (Exception e)
        {
            throw new ConfigurationStoreException("Failed to create DNS MX record: " + e.getMessage(), e);
        }
    }

    /**
     * Converts a DNS record to a raw wire transfer format.
     * @param rec The DNSRecord to convert.
     * @return A byte array representation of the DNSRecord in raw wire transfer format.
     * @throws IOException
     */
    public static byte[] toWire(DNSRecord rec) throws IOException
    {
        Record retVal = Record.newRecord(Name.fromString(rec.getName()), rec.getType(), rec.getDclass(),
                rec.getTtl(), rec.getData());

        return retVal.toWireCanonical();
    }

    /**
     * Converts a raw wire transfer format of a record to a DNS record.
     * @param data  The raw byte stream of a record in wire transfer format.
     * @return A DNSRecord converted from the wire format.
     * @throws IOException
     */
    public static DNSRecord fromWire(byte[] data) throws IOException
    {
        Record rec = Record.fromWire(data, Section.ANSWER);

        DNSRecord retVal = new DNSRecord();

        retVal.setDclass(rec.getDClass());
        retVal.setName(rec.getName().toString());
        retVal.setData(rec.rdataToWireCanonical());
        retVal.setTtl(rec.getTTL());
        retVal.setType(rec.getType());

        return retVal;
    }
}
