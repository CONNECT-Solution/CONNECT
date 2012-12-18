/* 
 * Copyright (c) 2010, NHIN Direct Project
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright 
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in the 
 *    documentation and/or other materials provided with the distribution.  
 * 3. Neither the name of the the NHIN Direct Project (nhindirect.org)
 *    nor the names of its contributors may be used to endorse or promote products 
 *    derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.nhind.xdr;

import gov.hhs.fha.nhinc.direct.DirectAdapterFactory;
import gov.hhs.fha.nhinc.direct.DirectSender;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import javax.naming.InitialContext;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.nhindirect.xd.common.DirectDocuments;
import org.nhindirect.xd.routing.RoutingResolver;
import org.nhindirect.xd.routing.impl.RoutingResolverImpl;
import org.nhindirect.xd.soap.ThreadData;
import org.nhindirect.xd.transform.XdsDirectDocumentsTransformer;
import org.nhindirect.xd.transform.impl.DefaultXdsDirectDocumentsTransformer;
import org.nhindirect.xd.transform.parse.ParserHL7;
//import com.gsihealth.auditclient.AuditMessageGenerator;
//import com.gsihealth.auditclient.type.AuditMethodEnum;

/* 
 Copyright (c) 2010, NHIN Direct Project
 All rights reserved.

 Authors:
 Vincent Lewis     vincent.lewis@gsihealth.com

 Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

 Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer 
 in the documentation and/or other materials provided with the distribution.  Neither the name of the The NHIN Direct Project (nhindirect.org). 
 nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS 
 BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE 
 GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
 THE POSSIBILITY OF SUCH DAMAGE.
 */
public abstract class DocumentRepositoryAbstract {
    protected String endpoint = null;
    protected String messageId = null;
    protected String relatesTo = null;
    protected String action = null;
    protected String to = null;

    protected String directTo = null;
    protected String directFrom = null;

    private String thisHost = null;
    private String remoteHost = null;
    private String pid = null;
    private String from = null;
    private String replyEmail = null;

    private RoutingResolver resolver = null;
    // private AuditMessageGenerator auditMessageGenerator = null;
    private XdsDirectDocumentsTransformer xdsDirectDocumentsTransformer = new DefaultXdsDirectDocumentsTransformer();

    private static final Logger LOG = Logger.getLogger(DocumentRepositoryAbstract.class);

    /**
     * Handle an incoming ProvideAndRegisterDocumentSetRequestType object and transform to XDM or relay to another XDR
     * endponit.
     * 
     * @param prdst The incoming ProvideAndRegisterDocumentSetRequestType object
     * @return a RegistryResponseType object
     * @throws Exception
     */
    protected RegistryResponseType provideAndRegisterDocumentSet(ProvideAndRegisterDocumentSetRequestType prdst)
            throws Exception {
        RegistryResponseType resp = null;

        try {
            getHeaderData();
            @SuppressWarnings("unused")
            InitialContext ctx = new InitialContext();

            DirectDocuments documents = xdsDirectDocumentsTransformer.transform(prdst);

            List<String> forwards = new ArrayList<String>();

            // Get endpoints (first check direct:to header, then go to
            // intendedRecipients)
            if (StringUtils.isNotBlank(directTo))
                forwards = Arrays.asList((new URI(directTo).getSchemeSpecificPart()));
            else {
                forwards = ParserHL7.parseDirectRecipients(documents);

            }

            // messageId = UUID.randomUUID().toString(); remove this , its is
            // not righ,
            // we should keep the message id of the original message for a lot
            // of reasons vpl

            // TODO patID and subsetId for atn
            // String patId = messageId;
            // String subsetId = messageId;
            // getAuditMessageGenerator().provideAndRegisterAudit( messageId,
            // remoteHost, endpoint, to, thisHost, patId, subsetId, pid);

            if (!getResolver().hasSmtpEndpoints(forwards) && !getResolver().hasXdEndpoints(forwards)) {
                resp = new RegistryResponseType();
                resp.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
                RegistryErrorList rel = new RegistryErrorList();
                rel.setHighestSeverity(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);
                List<RegistryError> rl = rel.getRegistryError();
                RegistryError re = new RegistryError();
                re.setErrorCode("XDSRepositoryError");
                re.setSeverity(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);
                re.setCodeContext("There were no SMTP or XD endpoints provided.");
                re.setLocation("XDR.java");
                rl.add(re);
                resp.setRegistryErrorList(rel);
            } else {

                // Send to SMTP endpoints
                if (getResolver().hasSmtpEndpoints(forwards)) {
                    // Get a reply address (first check direct:from header, then
                    // go to authorPerson)
                    if (StringUtils.isNotBlank(directFrom))
                        replyEmail = (new URI(directFrom)).getSchemeSpecificPart();
                    else {
                        // replyEmail =
                        // documents.getSubmissionSet().getAuthorPerson();
                        replyEmail = documents.getSubmissionSet().getAuthorTelecommunication();

                        // replyEmail =
                        // StringUtils.splitPreserveAllTokens(replyEmail,
                        // "^")[0];
                        replyEmail = ParserHL7.parseXTN(replyEmail);
                        replyEmail = StringUtils.contains(replyEmail, "@") ? replyEmail : "nhindirect@nhindirect.org";
                    }

                    LOG.info("SENDING EMAIL TO " + getResolver().getSmtpEndpoints(forwards) + " with message id "
                            + messageId);

                    Address[] addressTo = new InternetAddress[getResolver().getSmtpEndpoints(forwards).size()];
                    int i = 0;
                    for (String recipient : getResolver().getSmtpEndpoints(forwards)) {
                        addressTo[i++] = new InternetAddress(recipient);
                    }

                    getDirectSender().sendOutboundDirect(new InternetAddress(replyEmail), addressTo, documents,
                            messageId);

                    // getAuditMessageGenerator().provideAndRegisterAuditSource(
                    // messageId, remoteHost, endpoint, to, thisHost, patId,
                    // subsetId, pid);
                }

                // Send to XD endpoints
                if (!getResolver().getXdEndpoints(forwards).isEmpty()) {
                    resp = new RegistryResponseType();
                    resp.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
                    RegistryErrorList rel = new RegistryErrorList();
                    rel.setHighestSeverity(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);
                    List<RegistryError> rl = rel.getRegistryError();
                    RegistryError re = new RegistryError();
                    re.setErrorCode("XDSRepositoryError");
                    re.setSeverity(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);
                    re.setCodeContext("The CONNECT Product does not support a Direct XDR backbone");
                    re.setLocation("XDR.java");
                    rl.add(re);
                    resp.setRegistryErrorList(rel);
                }

                resp = getRepositoryProvideResponse(messageId);
            }

            relatesTo = messageId;
            action = "urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-bResponse";
            to = endpoint;

            setHeaderData();
        } catch (Exception e) {
            e.printStackTrace();
            throw (e);
        }

        return resp;
    }

    private DirectSender getDirectSender() {
        return new DirectAdapterFactory().getDirectSender();
    }

    /**
     * Create a RegistryResponseType object.
     * 
     * @param messageId The message ID
     * @return a RegistryResponseType object
     * @throws Exception
     */
    protected RegistryResponseType getRepositoryProvideResponse(String messageId) throws Exception {
        RegistryResponseType rrt = null;
        try { // Call Web Service Operation

            String status = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success"; // TODO
                                                                                          // initialize
                                                                                          // WS
                                                                                          // operation
                                                                                          // arguments
                                                                                          // here

            try {

                rrt = new RegistryResponseType();
                rrt.setStatus(status);

            } catch (Exception ex) {
                LOG.info("not sure what this ");
                ex.printStackTrace();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rrt;
    }

    private RoutingResolver getResolver() {
        if (resolver == null) {
            /*
             * String configService = null;
             * 
             * try { configService = getServletContext().getInitParameter(PARAM_CONFIG_SERVICE);
             * 
             * } catch (Exception x) { // eat it }
             * 
             * if (StringUtils.isNotBlank(configService)) { try { resolver = new RoutingResolverImpl(configService);
             * config = new XdConfig(configService); } catch (Exception e) { LOGGER.warning(
             * "Unable to create resolver from URL, falling back to default"); resolver = new RoutingResolverImpl(); } }
             * else {
             */
            resolver = new RoutingResolverImpl();
            /* } */
        }

        return resolver;
    }

    // abstract protected AuditMessageGenerator getAuditMessageGenerator();

    /**
     * Set the value of auditMessageGenerator.
     * 
     * @param auditMessageGenerator the value of auditMessageGenerator.
     */
    /*
     * public void setAuditMessageGenerator(AuditMessageGenerator auditMessageGenerator) { this.auditMessageGenerator =
     * auditMessageGenerator; }
     */

    /**
     * Set the value of resolver.
     * 
     * @param resolver the value of resolver.
     */
    public void setResolver(RoutingResolver resolver) {
        this.resolver = resolver;
    }

    /**
     * Extract header values from a ThreadData object.
     */
    protected void getHeaderData() {
        Long threadId = new Long(Thread.currentThread().getId());
        LOG.info("DTHREAD ID " + threadId);

        ThreadData threadData = new ThreadData(threadId);
        this.endpoint = threadData.getReplyAddress();
        this.messageId = threadData.getMessageId();
        this.to = threadData.getTo();
        this.thisHost = threadData.getThisHost();
        this.remoteHost = threadData.getRemoteHost();
        this.pid = threadData.getPid();
        this.action = threadData.getAction();
        this.from = threadData.getFrom();

        this.directTo = threadData.getDirectTo();
        this.directFrom = threadData.getDirectFrom();

        LOG.info(threadData.toString());
    }

    /**
     * Build a ThreadData object with header information.
     */
    protected void setHeaderData() {
        Long threadId = new Long(Thread.currentThread().getId());
        LOG.info("THREAD ID " + threadId);

        ThreadData threadData = new ThreadData(threadId);
        threadData.setTo(this.to);
        threadData.setMessageId(this.messageId);
        threadData.setRelatesTo(this.relatesTo);
        threadData.setAction(this.action);
        threadData.setThisHost(this.thisHost);
        threadData.setRemoteHost(this.remoteHost);
        threadData.setPid(this.pid);
        threadData.setFrom(this.from);

        threadData.setDirectTo(this.directTo);
        threadData.setDirectFrom(this.directFrom);

        LOG.info(threadData.toString());
    }
}
