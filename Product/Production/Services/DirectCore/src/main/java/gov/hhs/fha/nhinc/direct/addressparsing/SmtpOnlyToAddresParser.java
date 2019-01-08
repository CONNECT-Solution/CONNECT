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
package gov.hhs.fha.nhinc.direct.addressparsing;

import gov.hhs.fha.nhinc.direct.DirectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.nhindirect.xd.common.DirectDocuments;
import org.nhindirect.xd.routing.RoutingResolver;
import org.nhindirect.xd.routing.impl.RoutingResolverImpl;
import org.nhindirect.xd.transform.parse.ParserHL7;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmtpOnlyToAddresParser implements ToAddressParser {

    private static final Logger LOG = LoggerFactory.getLogger(SmtpOnlyToAddresParser.class);
    RoutingResolver resolver = null;

    @Override
    public Set<Address> parse(String addresses, DirectDocuments documents) {
        Set<Address> addressTo = new HashSet<>();

        // Get endpoints (first check direct:to header, then go to
        // intendedRecipients)
        List<String> forwards = new ArrayList<>();
        if (null != addresses && StringUtils.isNotBlank(addresses)) {
            try {
                forwards = Arrays.asList(new URI(addresses).getSchemeSpecificPart());
            } catch (URISyntaxException e) {
                LOG.error("Unable to parse Direct To header, attempting to parse XDR intended recipients: "
                    + e.getLocalizedMessage(), e);
            }
        } else if (documents != null && documents.getSubmissionSet() != null) {
            forwards = ParserHL7.parseDirectRecipients(documents);
        }

        if (CollectionUtils.isNotEmpty(forwards) && getResolver().hasSmtpEndpoints(forwards)) {
            for (String recipient : getResolver().getSmtpEndpoints(forwards)) {
                try {
                    addressTo.add(new InternetAddress(recipient));
                } catch (AddressException e) {
                    LOG.error("Unable to convert " + recipient + " to an InternetAdress: " + e.getLocalizedMessage(),
                        e);
                }
            }
        } else {
            throw new DirectException("There were no SMTP endpoints provided.");
        }
        return addressTo;
    }

    private RoutingResolver getResolver() {
        if (resolver == null) {
            resolver = new RoutingResolverImpl();
        }

        return resolver;
    }
}
