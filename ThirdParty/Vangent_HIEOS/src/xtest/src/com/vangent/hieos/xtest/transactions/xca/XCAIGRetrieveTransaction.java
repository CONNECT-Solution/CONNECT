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

package com.vangent.hieos.xtest.transactions.xca;

import com.vangent.hieos.xutil.exception.ExceptionUtil;
import com.vangent.hieos.xutil.exception.XdsException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.iosupport.Io;
import com.vangent.hieos.xutil.metadata.structure.Metadata;
import com.vangent.hieos.xutil.metadata.structure.MetadataParser;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.client.RetrieveB;
import com.vangent.hieos.xutil.client.RetContext;
import com.vangent.hieos.xutil.client.RetInfo;
import com.vangent.hieos.xtest.framework.Linkage;
import com.vangent.hieos.xtest.framework.TestConfig;
import com.vangent.hieos.xutil.xml.Util;
import com.vangent.hieos.xutil.metadata.structure.HomeAttribute;
import com.vangent.hieos.xtest.framework.BasicTransaction;
import com.vangent.hieos.xtest.framework.StepContext;
import com.vangent.hieos.xtest.transactions.xds.RetrieveTransaction;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.log4j.Logger;
import org.jaxen.JaxenException;

public class XCAIGRetrieveTransaction extends BasicTransaction {

    String metadata_filename = null;
    OMElement metadata = null;
    OMElement expected_contents = null;
    String expected_mime_type = null;
    String uri = null;
    OMElement uri_ref = null;
    HashMap<String, String> referenced_documents = new HashMap<String, String>();  // uid, filename
    Metadata reference_metadata = null;
    private final static Logger logger = Logger.getLogger(RetrieveTransaction.class);

    public String toString() {

        return "XCAIGRetrieveTransaction: *************" +
                "\nmetadata_filename = " + metadata_filename +
                "\nexpected_contents = " + isNull(expected_contents) +
                "\nexpected_mime_type = " + expected_mime_type +
                "\nuri = " + uri +
                "\nuri_ref = " + isNull(uri_ref) +
                "\nreferenced_documents = " + referenced_documents.toString() +
                "\nreference_metadata = " + metadataStructure(reference_metadata) +
                "\nuse_document_unique_id = " + use_repository_unique_id.toString() +
                "\nuse_id = " + use_id +
                "\nuse_xpath = " + use_xpath +
                "\nlinkage = " + linkage.toString() +
                "\nendpoint = " + endpoint +
                "\nactor config = " + TestConfig.endpoints +
                "\nrepos config = " + TestConfig.repositories +
                "\n****************";
    }

    String metadataStructure(Metadata m) {
        try {
            return m.structure();
        } catch (Exception e) {
        }
        return null;
    }

    String isNull(Object thing) {
        return (thing == null) ? "null" : "not null";
    }

    public XCAIGRetrieveTransaction(StepContext s_ctx, OMElement instruction, OMElement instruction_output) {
        super(s_ctx, instruction, instruction_output);
    }

    public void run()
            throws XdsException {
        parse_input(s_ctx, instruction, instruction_output);

        xds_version = BasicTransaction.xds_b;  // Force XDS version to XDS.b
        validate_xds_version();

        // metadata should be RequestDocumentSetRequest
        OMElement metadata_ele = null;
        if (metadata != null) {
            metadata_ele = metadata;
        } else {
            metadata_ele = Util.parse_xml(new File(metadata_filename));
        }

        // this looks useless, metadata here is the Retrieve request
        Metadata m = MetadataParser.noParse(metadata_ele);

        if (metadata_filename == null && metadata == null) {
            throw new XdsInternalException("No MetadataFile element or Metadata element found for RetrieveDocumentSetRequest Transaction instruction within step " + s_ctx.get("step_id"));
        }

        // compile in results of previous steps
        if (use_id.size() > 0) {
            compileUseIdLinkage(m, use_id);
        }

        if (use_xpath.size() > 0) {
            compileUseXPathLinkage(m, use_xpath);
        }

        if (use_repository_unique_id.size() > 0) {
            compileUseRepositoryUniqueId(m, use_repository_unique_id);
        }

        String homeXPath = "//*[local-name()='RetrieveDocumentSetRequest']/*[local-name()='DocumentRequest'][1]/*[local-name()='HomeCommunityId']";
        String expectedHomeCommunityId = null;
        try {
            AXIOMXPath xpathExpression = new AXIOMXPath(homeXPath);
            expectedHomeCommunityId = xpathExpression.stringValueOf(metadata_ele);
            //System.out.println("*** HOME = " + expectedHomeCommunityId + " ***");
        } catch (JaxenException e) {
            fatal("XCA IG Retrieve: " + ExceptionUtil.exception_details(e));
        }

        parseEndpoint("r.ig");

        s_ctx.add_name_value(instruction_output, "InputMetadata", Util.deep_copy(metadata_ele));

        s_ctx.add_name_value(instruction_output, "Linkage", this.linkage.toString());

        RetContext r_ctx = null;
        try {
            // map from doc uid -> info about doc
            // RetInfo holds size, hash, home etc
            HashMap<String, RetInfo> request_info = build_request_info(metadata_ele /* retrieve request */);

            // Bean that holds the context of the retrieve operation
            r_ctx = new RetContext();
            r_ctx.setRequestInfo(request_info);
            r_ctx.setRequest(metadata_ele);
            r_ctx.setExpectedError(s_ctx.expectedErrorMessage);

            System.out.println("** ENDPOINT **: " + endpoint);
            RetrieveB ret_b = new RetrieveB(r_ctx, endpoint);
            ret_b.setAsync(async);
            ret_b.setExpectedMimeType(this.expected_mime_type);
            ret_b.setStepContext(instruction_output);
            ret_b.setIsXca(false);  // 'false' forces a RetrieveDocumentSet versus CrossGatewayRetrieve
            ret_b.setSoap12(soap_1_2);
            ret_b.setReferenceMetadata(reference_metadata);
            OMElement result = ret_b.run();
            s_ctx.add_name_value(instruction_output, "Result", result);
            ret_b.validate(true /* validateRequest */);
            //System.out.println(result);

            // Further validate homeCommunityId in response (must have matched input).
            System.out.println("homeCommunityID = " + expectedHomeCommunityId);
            if (expectedHomeCommunityId != null) {
                if (!expectedHomeCommunityId.startsWith("urn:oid:")) {
                    s_ctx.set_error("Expected homeCommunityId value is [" + expectedHomeCommunityId + "]. It is required to have a [urn:oid:] prefix.");
                    failed();
                }

                HomeAttribute homeAtt = new HomeAttribute(expectedHomeCommunityId);
                String errors = homeAtt.validate(result);
                if (!errors.equals("")) {
                    s_ctx.set_error(errors);
                    failed();
                } else {
                    System.out.println("HomeCommunityID in response - OK!");
                }
            }
        } catch (Exception e) {
            throw new XdsInternalException(ExceptionUtil.exception_details(e));
        }

        add_step_status_to_output();

        // check that status == success
        String status = r_ctx.getRrp().get_registry_response_status();
        eval_expected_status(status, r_ctx.getRrp().get_error_code_contexts());

    }

    private HashMap<String, RetInfo> build_request_info(OMElement metadata_ele) throws XdsException {
        HashMap<String, RetInfo> request;
        request = new HashMap<String, RetInfo>();
        for (OMElement document_request : MetadataSupport.childrenWithLocalName(metadata_ele, "DocumentRequest")) {

            OMElement doc_uid_ele = MetadataSupport.firstChildWithLocalName(document_request, "DocumentUniqueId");
            String doc_uid = doc_uid_ele.getText();

            OMElement rep_uid_ele = MetadataSupport.firstChildWithLocalName(document_request, "RepositoryUniqueId");
            String rep_uid = rep_uid_ele.getText();

            RetInfo rqst = new RetInfo();
            rqst.setDoc_uid(doc_uid);
            rqst.setRep_uid(rep_uid);

            request.put(doc_uid, rqst);

            // linkage contains symbolic_name => real_name mapping
            // referenced_documents are keyed off symbolic_name
            // add real_name keys to referenced_documents
            for (Iterator<String> it = linkage.keySet().iterator(); it.hasNext();) {
                String symbolic_name = it.next();
                String real_name = linkage.get(symbolic_name);
                if (referenced_documents.containsKey(symbolic_name)) {
                    referenced_documents.put(real_name, referenced_documents.get(symbolic_name));
                }
            }

            if (referenced_documents.containsKey(doc_uid)) {
                String filename = referenced_documents.get(doc_uid);

                try {
                    FileInputStream fis = new FileInputStream(new File(filename));
                    rqst.setContents(Io.getBytesFromInputStream(fis));
                } catch (Exception e) {
                    throw new XdsInternalException("Cannot read ReferenceDocument: " + filename);
                }
            }
        }
        return request;
    }

    private void parse_input(StepContext s_ctx, OMElement instruction,
            OMElement instruction_output) throws XdsException {
        Iterator elements = instruction.getChildElements();
        while (elements.hasNext()) {
            OMElement part = (OMElement) elements.next();
            String part_name = part.getLocalName();
            if (part_name.equals("MetadataFile")) {
                metadata_filename = TestConfig.base_path + part.getText();
                s_ctx.add_name_value(instruction_output, "MetadataFile", metadata_filename);
            } else if (part_name.equals("Metadata")) {
                metadata_filename = "";
                metadata = part.getFirstElement();
            } else if (part_name.equals("ExpectedContents")) {
                expected_contents = part;
                s_ctx.add_name_value(instruction_output, "ExpectedContents", part);
            } else if (part_name.equals("ExpectedMimeType")) {
                expected_mime_type = part.getText();
                s_ctx.add_name_value(instruction_output, "ExpectedMimeType", part);
            } else if (part_name.equals("ReferenceDocument")) {
                String filename = null;
                String uid = null;
                filename = TestConfig.base_path + File.separator + part.getText();
                uid = part.getAttributeValue(new QName("uid"));
                referenced_documents.put(uid, filename);
                s_ctx.add_name_value(instruction_output, "ReferenceDocument", part);
            } else if (part_name.equals("ReferenceMetadata")) {
                String testdir = part.getAttributeValue(new QName("testdir"));
                String step = part.getAttributeValue(new QName("step"));
                if (testdir == null || testdir.equals("") | step == null || step.equals("")) {
                    throw new XdsInternalException("ReferenceMetadata instruction: both testdir and step are required attributes");
                }
                reference_metadata = new Linkage().getResult(testdir, step);
            } else if (part_name.equals("UseId")) {
                use_id.add(part);
                s_ctx.add_name_value(instruction_output, "UseId", part);
            } else if (part_name.equals("UseRepositoryUniqueId")) {
                this.use_repository_unique_id.add(part);
                s_ctx.add_name_value(instruction_output, "UseRepositoryUniqueId", part);
            } else if (part_name.equals("UseXPath")) {
                use_xpath.add(part);
                s_ctx.add_name_value(instruction_output, "UseXRef", part);
            } else if (part_name.equals("Assertions")) {
                parse_assertion_instruction(part);
            } else if (part_name.equals("XDSb")) {
                xds_version = BasicTransaction.xds_b;
            } else if (part_name.equals("SOAP11")) {
                soap_1_2 = false;
                this.s_ctx.add_simple_element(this.instruction_output, "SOAP11");
            } else if (part_name.equals("URI")) {
                uri = part.getText();
            } else if (part_name.equals("URIRef")) {
                uri_ref = part;
            } else if (part_name.equals("XDSa")) {
                xds_version = BasicTransaction.xds_a;
            //throw new XdsException("Retrieve transaction (in xdstest2) does not support XDS.a");
            } else {
                //				throw new XdsException("Don't understand instruction " + part_name + " inside step " + s_ctx.getId());
                BasicTransaction rt = this;
                rt.parse_instruction(part);
            }
        }
    }

    @Override
    protected String getRequestAction() {
        // TODO Auto-generated method stub
        return null;
    }
}
