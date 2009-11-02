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
package com.vangent.hieos.xutil.registry;

import com.vangent.hieos.xutil.response.ErrorLogger;
import com.vangent.hieos.xutil.metadata.structure.Metadata;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.metadata.structure.MetadataParser;
import com.vangent.hieos.xutil.exception.MetadataException;
import com.vangent.hieos.xutil.exception.MetadataValidationException;
import com.vangent.hieos.xutil.exception.XMLParserException;
import com.vangent.hieos.xutil.exception.XdsException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.xml.Parse;
import com.vangent.hieos.xutil.xlog.client.XLogMessage;

import java.util.ArrayList;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.log4j.Logger;

public class BackendRegistry {

    private final static Logger logger = Logger.getLogger(BackendRegistry.class);
    ErrorLogger response;
    XLogMessage log_message;
    String reason = "";

    public BackendRegistry(ErrorLogger response, XLogMessage log_message) {
        this.response = response;
        this.log_message = log_message;
    }

    public BackendRegistry(ErrorLogger response, XLogMessage log_message, String reason) {
        this.response = response;
        this.log_message = log_message;
        this.reason = reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    static QName object_ref_qname = new QName("ObjectRef");
    static QName id_qname = new QName("id");

    public ArrayList<String> queryForObjectRefs(String sql) throws XMLParserException, XdsException {
        OMElement result = query(sql, false /* leaf_class */);

        ArrayList<String> ors = new ArrayList<String>();

        if (result == null) // error occured
        {
            return ors;
        }

        OMElement sql_query_result = MetadataSupport.firstChildWithLocalName(result, "RegistryObjectList");

        if (sql_query_result != null) {
            for (OMElement or : MetadataSupport.childrenWithLocalName(sql_query_result, "ObjectRef")) {
                String id = or.getAttributeValue(id_qname);
                if (id != null && !id.equals("")) {
                    ors.add(id);
                }
            }
        }

        return ors;
    }

    public OMElement query(String sql, boolean leaf_class) throws XMLParserException, MetadataException, MetadataValidationException, XdsInternalException, XdsException {
        OMElement result = basic_query(sql, leaf_class);

        // add in homeCommunityId to all major
        Metadata m = MetadataParser.parseNonSubmission(result);

        m.fixClassifications();

        return result;
    }

    public ArrayList<String> objectRefQuery(String sql)
            throws MetadataValidationException, XMLParserException, XdsException {
        OMElement response = basic_query(sql, true);
        Metadata m = MetadataParser.parseNonSubmission(response);
        return m.getObjectIds(m.getObjectRefs());
    }
    static String sql_query_V3_header =
            "<query:AdhocQueryRequest\n" +
            "xmlns=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0\"\n" +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "xmlns:lcm=\"urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0\"\n" +
            "xmlns:rs=\"urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0\"\n" +
            "xmlns:rim=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\"\n" +
            "xmlns:query=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0\"\n" +
            "xsi:schemaLocation=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0 http://oasis-open.org/committees/regrep/documents/3.0/schema/query.xsd\">\n";

    public OMElement basic_query(String sql, boolean leaf_class)
            throws XMLParserException, XdsInternalException, XdsException {
        String query_string = sql_query_V3_header +
                "<query:ResponseOption returnType=\"" +
                ((leaf_class) ? "LeafClass" : "ObjectRef") +
                "\" returnComposedObjects=\"true\">\n" +
                "</query:ResponseOption>\n " +
                "<rim:AdhocQuery id=\"tempId\">\n" +
                "<rim:QueryExpression queryLanguage=\"urn:oasis:names:tc:ebxml-regrep:QueryLanguage:SQL-92\">\n" +
                //		sql +
                "</rim:QueryExpression>\n" +
                "</rim:AdhocQuery>\n" +
                "</query:AdhocQueryRequest>\n";

        OMElement query_element = Parse.parse_xml_string(query_string);

        //AMS 04/26/2009 - FIXME - Handle the condition that there might be no children with name AdhocQuery.
        // Or does validation already account for that?? ---- RESEARCH
        OMElement query_request = MetadataSupport.firstChildWithLocalName(query_element, "AdhocQuery");//.get(0);
        OMElement sql_query = MetadataSupport.firstChildWithLocalName(query_request, "QueryExpression");
        sql_query.setText(sql);

        OMElement result = submit_to_backend_registry(query_element);
        return result;
    }

    public OMElement submit_to_backend_registry(OMElement omElement) throws XdsException {

        if (log_message != null) {
            log_message.addOtherParam("omar (ebxmlrr 3.x) request (" + reason + ")", omElement.toString());
        }

        OMElement result = null;
        try {
            OmarRegistry or = new OmarRegistry(omElement);
            result = or.process();
        } catch (Exception e) {
            logger.error("*****   Exception in calling Omar (ebxmlrr 3.x) Registry  ****  " + e.getMessage());
            // AMS 04/22/2009 - FIXME - Exceptions thrown could be JAXRException, RegistryException - encapsulate into XdsInternalException ???
            // AMS 04/22/2009 - FIXME - Is the following statement correct?
            response.add_error("XDSRegistryError", "Error parsing response from omar (ebxmlrr 3.x)", RegistryUtility.exception_details(e), log_message);
        }

        if (log_message != null) {
            log_message.addOtherParam("omar (ebxmlrr 3.x) response", (result != null) ? result.toString() : "null");
        }
        return result;
    }

    void insert_version_info(OMElement parent) {
        if (MetadataSupport.firstChildWithLocalName(parent, "VersionInfo") != null) {
            return;
        }
        OMElement vi = MetadataSupport.om_factory.createOMElement("VersionInfo", MetadataSupport.ebRIMns3);
        vi.addAttribute("versionName", "1.1", null);
        parent.addChild(vi);
    }
}
