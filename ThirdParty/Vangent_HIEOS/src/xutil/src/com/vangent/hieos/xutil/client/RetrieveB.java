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

package com.vangent.hieos.xutil.client;

import com.vangent.hieos.xutil.soap.SoapActionFactory;
import com.vangent.hieos.xutil.registry.*;
import com.vangent.hieos.xutil.response.RegistryResponseParser;
import com.vangent.hieos.xutil.soap.Mtom;
import com.vangent.hieos.xutil.metadata.structure.Metadata;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.metadata.structure.MetadataTypes;
import com.vangent.hieos.xutil.exception.ExceptionUtil;
import com.vangent.hieos.xutil.exception.MetadataException;
import com.vangent.hieos.xutil.exception.MetadataValidationException;
import com.vangent.hieos.xutil.exception.SchemaValidationException;
import com.vangent.hieos.xutil.exception.XdsConfigurationException;
import com.vangent.hieos.xutil.exception.XdsException;
import com.vangent.hieos.xutil.exception.XdsIOException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.exception.XdsPreparsedException;
import com.vangent.hieos.xutil.exception.XdsWSException;
import com.vangent.hieos.xutil.soap.Soap;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.namespace.QName;
import javax.xml.parsers.FactoryConfigurationError;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;

public class RetrieveB extends OmLogger {

    String endpoint = null;
    protected RetContext r_ctx = null;
    protected OMElement log_parent = null;
    protected Metadata reference_metadata = null;
    private String expected_mime_type = null;
    boolean is_xca = false;
    boolean soap12 = true;
    boolean async = false;

    public void setIsXca(boolean isXca) {
        is_xca = isXca;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    protected RetrieveB() {
    }

    public RetrieveB(RetContext r_ctx, String endpoint) {
        this.r_ctx = r_ctx;
        this.endpoint = endpoint;
    }

    public void setStepContext(OMElement log_parent) {
        this.log_parent = log_parent;
    }

    public void setExpectedMimeType(String type) {
        expected_mime_type = type;
    }

    public void setReferenceMetadata(Metadata reference_metadata) {
        this.reference_metadata = reference_metadata;
    }

    public void setSoap12(boolean v) {
        soap12 = v;
    }

    OMElement build_request(RetContext rc) {
        String ns = MetadataSupport.xdsB.getNamespaceURI();
        OMElement rdsr = MetadataSupport.om_factory.createOMElement(new QName(ns, "RetrieveDocumentSetRequest"));
        for (String uid : rc.getRequestInfo().keySet()) {
            RetInfo ri = rc.getRequestInfo().get(uid);

            OMElement dr = MetadataSupport.om_factory.createOMElement(new QName(ns, "DocumentRequest"));
            rdsr.addChild(dr);

            if (ri.getHome() != null && !ri.getHome().equals("")) {
                OMElement hci = MetadataSupport.om_factory.createOMElement(new QName(ns, "HomeCommunityId"));
                hci.setText(ri.getHome());
                dr.addChild(hci);
            }

            OMElement ruid = MetadataSupport.om_factory.createOMElement(new QName(ns, "RepositoryUniqueId"));
            ruid.setText(ri.getRep_uid());
            dr.addChild(ruid);

            OMElement duid = MetadataSupport.om_factory.createOMElement(new QName(ns, "DocumentUniqueId"));
            duid.setText(ri.getDoc_uid());
            dr.addChild(duid);
        }
        return rdsr;
    }

    public OMElement run()
            throws XdsInternalException, FactoryConfigurationError,
            XdsException, XdsIOException, MetadataException,
            XdsConfigurationException, MetadataValidationException, XdsWSException {

        OMElement result = null;
        OMElement request = r_ctx.getRequest();
        if (request == null) {
            r_ctx.setRequest(build_request(r_ctx));
        }

        result = call(r_ctx.getRequest(), endpoint); // AxisFault will signal the caller that the endpoint did not work
        r_ctx.setResult(result);

        return result;
    }

    public void validate(boolean validateRequest) throws XdsInternalException, MetadataException, XdsPreparsedException {
        OMElement result = r_ctx.getResult();

        if (result == null) {
            throw new XdsInternalException("Response is null");
        }

        OMElement registry_response = MetadataSupport.firstChildWithLocalName(result, "RegistryResponse");
        if (registry_response == null) {
            throw new XdsInternalException("Did not find RegistryResponse within RetrieveDocumentSetResponse");
        }

        // schema validate
        RegistryResponseParser rrp = new RegistryResponseParser(registry_response);
        r_ctx.setRrp(rrp);

        String errors = rrp.get_regrep_error_msg();

        if (r_ctx.getExpectedError() != null) {
            if (errors == null || errors.indexOf(r_ctx.getExpectedError()) == -1) {
                throw new MetadataException("Expected error " + r_ctx.getExpectedError() + " not found");
            }
        } else {

            //System.out.println("Retrieve errors: [" + errors + "]");
            if (errors != null && !errors.equals("")) {
                throw new XdsPreparsedException("Error: " + errors);
            }
        }

        try {
            RegistryUtility.schema_validate_local(result, MetadataTypes.METADATA_TYPE_RET);
        } catch (SchemaValidationException e) {
            throw new XdsInternalException("Schema validation of Retrieve Response: " + e.getMessage());
        }

        // validate response contents
        try {
            r_ctx.setResponseInfo(parse_rep_response(result));
            String validation_errors = validate_retrieve(validateRequest);
            if (validation_errors != null && !validation_errors.equals("") && r_ctx.getExpectedError() == null) {
                throw new XdsPreparsedException(validation_errors);
            }
        } catch (Exception e) {
            throw new XdsPreparsedException("Result validation threw exception: " + ExceptionUtil.exception_details(e));
        }
    }

    private OMElement call(OMElement metadata_ele, String endpoint)
            throws XdsWSException, XdsException {
        Soap soap = new Soap();
        soap.setAsync(async);
        soap.soapCall(metadata_ele, endpoint,
                true, // mtom
                true, // addressing
                soap12, // soap12
                getRequestAction(),
                getResponseAction());
        return soap.getResult();
    }

    protected String getResponseAction() {
        return SoapActionFactory.getResponseAction(getRequestAction());
    }

    protected String getRequestAction() {
        if (async) {
            if (is_xca) {
                return "urn:ihe:iti:2007:CrossGatewayRetrieveAsync";
            } else {
                return "urn:ihe:iti:2007:RetrieveDocumentSetAsync";
            }
        } else {
            if (is_xca) {
                return "urn:ihe:iti:2007:CrossGatewayRetrieve";
            } else {
                return "urn:ihe:iti:2007:RetrieveDocumentSet";
            }
        }
    }

    HashMap<String, RetInfo> parse_rep_response(OMElement response) throws IOException, MetadataException, Exception {
        HashMap<String, RetInfo> map = new HashMap<String, RetInfo>();

        for (OMElement doc_response : MetadataSupport.childrenWithLocalName(response, "DocumentResponse")) {
            RetInfo rr = new RetInfo();

            OMElement doc_uid_ele = MetadataSupport.firstChildWithLocalName(doc_response, "DocumentUniqueId");
            rr.setDoc_uid((doc_uid_ele != null) ? doc_uid_ele.getText() : null);

            OMElement rep_uid_ele = MetadataSupport.firstChildWithLocalName(doc_response, "RepositoryUniqueId");
            rr.setRep_uid((rep_uid_ele != null) ? rep_uid_ele.getText() : null);

            OMElement mime_type_ele = MetadataSupport.firstChildWithLocalName(doc_response, "mimeType");
            rr.setContent_type((mime_type_ele != null) ? mime_type_ele.getText() : null);

            OMElement document_content_ele = MetadataSupport.firstChildWithLocalName(doc_response, "Document");

            Mtom mtom = new Mtom();
            mtom.decode(document_content_ele);
//			if ( !mtom.isOptimized()) {
//				throw new XdsFormatException("Response to RetrieveDocumentSet is not in MTOM format");
//			}

            String mtom_mime = mtom.getContent_type();
            boolean isOptimized = mtom.isOptimized();
            if (this.log_parent != null) {
                this.add_simple_element_with_id(log_parent, "IsOptimized", (isOptimized) ? "true" : "false");
            }
            if (mtom_mime != null && mtom_mime.equals("application/octet-stream") && isOptimized) {
                mtom_mime = rr.getContent_type();
            } else if (mtom_mime != null && rr.getContent_type() != null && !rr.getContent_type().equals(mtom_mime)) {
                rr.addError("Mime Type attribute (" + rr.getContent_type() + ") does not match Content-Type (" + mtom_mime + ")");
            }
            rr.setContents(mtom.getContents());

            if (rr.getDoc_uid() == null) {
                throw new MetadataException("parse_rep_result(): Document uniqueId not found in response");
            }

            map.put(rr.getDoc_uid(), rr);
        }

        return map;
    }

    protected String validate_retrieve(boolean validateRequest) throws MetadataException {
        HashMap<String, RetInfo> request = r_ctx.getRequestInfo();
        HashMap<String, RetInfo> response = r_ctx.getResponseInfo();
        StringBuffer errors = new StringBuffer();
        HashMap<String, OMElement> uid_doc_map = null;   // UUID -> ExtrinsicObject

        if (reference_metadata != null) {
            uid_doc_map = reference_metadata.getDocumentUidMap();
        }

        if (request.size() != response.size()) {
            errors.append("Requested [" + request.size() + "] docs, got [" + response.size() + "]\n");
        }

        for (String req_doc : request.keySet()) {
            //System.out.println("validating " + req_doc);
            RetInfo req = request.get(req_doc);
            RetInfo rsp = response.get(req_doc);

            String doc_uid = req.getDoc_uid();
            OMElement eo = null;
            if (uid_doc_map != null) {
                eo = uid_doc_map.get(doc_uid);
            }
            String query_size = null;
            String query_hash = null;
            String query_mime_type = null;
            if (uid_doc_map == null) {
            } else if (eo == null) {
                errors.append("Retrieve validation: Document with uid = [" + doc_uid + "] not present in query output\n");
            } else {
                query_size = this.reference_metadata.getSlotValue(eo, "size", 0);
                query_hash = this.reference_metadata.getSlotValue(eo, "hash", 0);
                query_mime_type = this.reference_metadata.getMimeType(eo);
            }

            if (query_mime_type == null) {
                query_mime_type = this.expected_mime_type;
            }

            if (rsp == null) {
                errors.append("No response for document <" + req_doc +
                        "> - only have responses for documents <" + response.keySet() +
                        ">\n");
                continue;
            }
            //			errors.append("Request:\n" + req.toString() + "\n");
            //			errors.append("Response:\n" + rsp.toString() + "\n");

            if (req.getRep_uid() == null) {
                errors.append("Request repositoryUniqueId is null\n");
                continue;
            }

            if (!req.getRep_uid().equals(rsp.getRep_uid())) {
                errors.append("Request repositoryUniqueId does not match response - [" + req.getRep_uid() + "] vs [" + rsp.getRep_uid() + "]\n");
            }

            if (rsp.getContents() == null || req.getContents() == null) {
                boolean err = false;
                if ((validateRequest == true) && (req.getContents() == null)) {
                    errors.append("Reference document not accessible\n");
                    err = true;
                }
                if (rsp.getContents() == null) {
                    errors.append("No document data\n");
                    err = true;
                }
                if (err) {
                    continue;
                }
            }

            if (this.log_parent != null) {
                this.add_name_value(
                        this.log_parent,
                        "ContentType",
                        this.create_name_value("Original", req.getContent_type()),
                        this.create_name_value("Query", query_mime_type),
                        this.create_name_value("Retrieve", rsp.getContent_type()));
                this.add_name_value(
                        this.log_parent,
                        "Hash",
                        this.create_name_value("Original", req.getHash()),
                        this.create_name_value("Query", query_hash),
                        this.create_name_value("Retrieve", rsp.getHash()));
                this.add_name_value(
                        this.log_parent,
                        "Size",
                        this.create_name_value("Original", String.valueOf(req.getSize())),
                        this.create_name_value("Query", query_size),
                        this.create_name_value("Retrieve", String.valueOf(rsp.getSize())));
            }

            //
            // mime type
            //
            if (req.getContent_type() == null || req.getContent_type().equals("")) {
                // in some tests it isn't available
            } else if (rsp.getContent_type() == null) {
                errors.append("Null Content-Type - expected [" + req.getContent_type() + "]");
            } else if (!rsp.getContent_type().equals(req.getContent_type())) {
                errors.append("Content type does not match - submission has [" + req.getContent_type() + "] and Retrieve response has [" + rsp.getContent_type() + "]\n");
            }

            if (query_mime_type != null && !query_mime_type.equals(rsp.getContent_type())) {
                errors.append("Content type from query response has [" + query_mime_type + "] and Retrieve response has [" + rsp.getContent_type() + "]\n");
            }

            //
            // hash
            //
            if (req.getHash() != null && rsp.getHash() != null && !rsp.getHash().equals(req.getHash().toLowerCase())) {
                errors.append("Hash does not match - submission has [" + req.getHash() + "] and Retrieve response has [" + rsp.getHash() + "]\n");
            }

            if (query_hash != null && !query_hash.equals(rsp.getHash())) {
                errors.append("Hash does not match - query response has [" + query_hash + "] and Retrieve response has [" + rsp.getHash() + "]\n");
            }

            //
            // size
            //
            if (rsp.getSize() != req.getSize() && req.getSize() != -1) {
                errors.append("Size does not match - metadata has [" + req.getSize() + "] and Retrieve response has [" + rsp.getSize() + "]\n");
            }

            if (query_size != null) {
                int query_size_int = Integer.parseInt(query_size);
                if (query_size_int != rsp.getSize()) {
                    errors.append("Size does not match - query response has [" + query_size_int + "] and Retreive response has [" + rsp.getSize() + "]\n");
                }
            }

        }

        if (errors.length() == 0) {
            return "";
        } else {
            return "\nRetrieve Validation Errors:\n" + errors.toString();
        }

    }
}
