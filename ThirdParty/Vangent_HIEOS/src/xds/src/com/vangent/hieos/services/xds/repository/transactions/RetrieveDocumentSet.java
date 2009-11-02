/*
 * This code is subject to the HIEOS License, Version 1.0
 *
 * Copyright(c) 2008-2009 Vangent, Inc.  All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vangent.hieos.services.xds.repository.transactions;

import com.vangent.hieos.xutil.services.framework.ContentValidationService;
import com.vangent.hieos.xutil.atna.XATNALogger;
import com.vangent.hieos.xutil.metadata.structure.MetadataTypes;
import com.vangent.hieos.xutil.exception.MetadataException;
import com.vangent.hieos.xutil.exception.SchemaValidationException;
import com.vangent.hieos.xutil.exception.XdsException;
import com.vangent.hieos.xutil.exception.XdsFormatException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.registry.RegistryUtility;
import com.vangent.hieos.xutil.response.RetrieveMultipleResponse;
import com.vangent.hieos.xutil.services.framework.XBaseTransaction;
import com.vangent.hieos.services.xds.repository.support.Repository;
import com.vangent.hieos.xutil.services.framework.XAbstractService;
import com.vangent.hieos.xutil.xlog.client.XLogMessage;

import com.vangent.hieos.services.xds.repository.storage.XDSDocument;
import com.vangent.hieos.services.xds.repository.storage.XDSRepositoryStorage;

import java.util.ArrayList;

import javax.activation.FileDataSource;
import javax.activation.DataHandler;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMText;
import org.apache.axis2.context.MessageContext;
import org.apache.log4j.Logger;

/**
 *
 * @author Bernie Thuman (plugged in new repository storage mechanism).
 */
public class RetrieveDocumentSet extends XBaseTransaction {

    ContentValidationService validater;
    String registry_endpoint = null;
    MessageContext messageContext;
    boolean optimize = true;
    private final static Logger logger = Logger.getLogger(RetrieveDocumentSet.class);

    public RetrieveDocumentSet(XLogMessage log_message, short xds_version, MessageContext messageContext) {
        this.log_message = log_message;
        this.messageContext = messageContext;
        try {
            init(new RetrieveMultipleResponse(), xds_version, messageContext);
        } catch (XdsInternalException e) {
            logger.fatal(logger_exception_details(e));
            response.add_error("XDSRepositoryError", e.getMessage(), this.getClass().getName(), log_message);
        }
    }

    /**
     *
     * @param rds
     * @param validater
     * @param optimize
     * @param service
     * @return
     * @throws com.vangent.hieos.xutil.exception.SchemaValidationException
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    public OMElement retrieveDocumentSet(OMElement rds, ContentValidationService validater, boolean optimize, XAbstractService service) throws SchemaValidationException, XdsInternalException {
        this.validater = validater;
        this.optimize = optimize;

        OMNamespace ns = rds.getNamespace();
        String ns_uri = ns.getNamespaceURI();
        if (ns_uri == null || !ns_uri.equals(MetadataSupport.xdsB.getNamespaceURI())) {
            return service.start_up_error(rds, "RetrieveDocumentSet.java", XAbstractService.repository_actor, "Invalid namespace on RetrieveDocumentSetRequest (" + ns_uri + ")", true);
        }

        try {
            RegistryUtility.schema_validate_local(rds, MetadataTypes.METADATA_TYPE_RET);
        } catch (Exception e) {
            return service.start_up_error(rds, "RetrieveDocumentSet.java", XAbstractService.repository_actor, "Schema validation errors:\n" + e.getMessage(), true);
        }

        ArrayList<OMElement> retrieve_documents = null;

        try {

            mustBeMTOM();
            retrieve_documents = retrieve_documents(rds);

            //AUDIT:POINT
            //call to audit message for document repository
            //for Transaction id = ITI-43. (Retrieve Document Set)
            //Here document consumer is treated as document repository
            performAudit(
                    XATNALogger.TXN_ITI43,
                    rds,
                    null,
                    XATNALogger.ActorType.REPOSITORY,
                    XATNALogger.OutcomeIndicator.SUCCESS);
        } catch (XdsFormatException e) {
            response.add_error("XDSRepositoryError", "SOAP Format Error: " + e.getMessage(), this.getClass().getName(), log_message);
        } catch (MetadataException e) {
            response.add_error("XDSRepositoryError", "Request Validation Error:\n " + e.getMessage(), this.getClass().getName(), log_message);
        } catch (XdsException e) {
            response.add_error("XDSRepositoryError", e.getMessage(), this.getClass().getName(), log_message);
            logger.fatal(logger_exception_details(e));
        }


        OMElement registry_response = null;
        try {
            registry_response = response.getResponse();
        } catch (XdsInternalException e) {
            logger.fatal(logger_exception_details(e));
            log_message.addErrorParam("Internal Error", "Error generating response from Ret.b");
        }

        //OMElement rdsr = MetadataSupport.om_factory.createOMElement("RetrieveDocumentSetResponse", MetadataSupport.xds_b);
        //rdsr.addChild(registry_response);

        if (retrieve_documents != null) {
            for (OMElement ret_doc : retrieve_documents) {
                registry_response.addChild(ret_doc);
            }
        }

        this.log_response();

        return response.getRoot();
    }

    /**
     *
     * @param rds
     * @return
     * @throws com.vangent.hieos.xutil.exception.MetadataException
     * @throws com.vangent.hieos.xutil.exception.XdsException
     */
    private ArrayList<OMElement> retrieve_documents(OMElement rds) throws MetadataException, XdsException {
        ArrayList<OMElement> document_responses = new ArrayList<OMElement>();

        for (OMElement doc_request : MetadataSupport.childrenWithLocalName(rds, "DocumentRequest")) {

            String rep_id = null;
            String doc_id = null;

            try {
                rep_id = MetadataSupport.firstChildWithLocalName(doc_request, "RepositoryUniqueId").getText();
                if (rep_id == null || rep_id.equals("")) {
                    throw new Exception("");
                }
            } catch (Exception e) {
                throw new MetadataException("Cannot extract RepositoryUniqueId from DocumentRequest");
            }

            try {
                doc_id = MetadataSupport.firstChildWithLocalName(doc_request, "DocumentUniqueId").getText();
                if (doc_id == null || doc_id.equals("")) {
                    throw new Exception("");
                }
            } catch (Exception e) {
                throw new MetadataException("Cannot extract DocumentUniqueId from DocumentRequest");
            }

            OMElement document_response = retrieve_document(rep_id, doc_id);

            if (document_response != null) {
                document_responses.add(document_response);
            }
        }
        return document_responses;
    }

    /**
     *
     * @param rep_id
     * @param doc_id
     * @return
     * @throws com.vangent.hieos.xutil.exception.XdsException
     */
    private OMElement retrieve_document(String rep_id, String doc_id) throws XdsException {
        if (!rep_id.equals(Repository.getRepositoryUniqueId())) {
            response.add_error(MetadataSupport.XDSRepositoryWrongRepositoryUniqueId,
                    "Repository Unique ID in request " +
                    rep_id +
                    " does not match this repository's id " +
                    Repository.getRepositoryUniqueId(),
                    this.getClass().getName(), log_message);
            return null;
        }

        // Retrieve the document from disk.
        XDSDocument doc = new XDSDocument(rep_id);
        doc.setUniqueId(doc_id);
        XDSRepositoryStorage repoStorage = XDSRepositoryStorage.getInstance();
        doc = repoStorage.retrieve(doc);

        // Set up the DataHandler.
        ByteArrayDataSource ds = new ByteArrayDataSource();
        ds.setBytes(doc.getBytes());
        ds.setName(doc.getUniqueId());
        ds.setContentType(doc.getMimeType());
        javax.activation.DataHandler dataHandler = new DataHandler(ds);

        OMText t = MetadataSupport.om_factory.createOMText(dataHandler, optimize);
        t.setOptimize(optimize);
        OMElement document_response = MetadataSupport.om_factory.createOMElement("DocumentResponse", MetadataSupport.xdsB);

        OMElement repid_ele = MetadataSupport.om_factory.createOMElement("RepositoryUniqueId", MetadataSupport.xdsB);
        repid_ele.addChild(MetadataSupport.om_factory.createOMText(rep_id));
        document_response.addChild(repid_ele);

        OMElement docid_ele = MetadataSupport.om_factory.createOMElement("DocumentUniqueId", MetadataSupport.xdsB);
        docid_ele.addChild(MetadataSupport.om_factory.createOMText(doc_id));
        document_response.addChild(docid_ele);

        OMElement mimetype_ele = MetadataSupport.om_factory.createOMElement("mimeType", MetadataSupport.xdsB);
        mimetype_ele.addChild(MetadataSupport.om_factory.createOMText(doc.getMimeType()));
        document_response.addChild(mimetype_ele);

        OMElement document_ele = MetadataSupport.om_factory.createOMElement("Document", MetadataSupport.xdsB);
        document_ele.addChild(t);
        document_response.addChild(document_ele);


        return document_response;
    }

    /**
     *
     */
    public class ByteArrayDataSource implements javax.activation.DataSource {

        private byte[] bytes;
        private String contentType;
        private String name;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setBytes(byte[] bytes) {
            this.bytes = bytes;
        }

        public byte[] getBytes() {
            return bytes;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getContentType() {
            return contentType;
        }

        public InputStream getInputStream() {
            return new ByteArrayInputStream(bytes);
        }

        public OutputStream getOutputStream() {
            throw new UnsupportedOperationException();
        }
    }
}
