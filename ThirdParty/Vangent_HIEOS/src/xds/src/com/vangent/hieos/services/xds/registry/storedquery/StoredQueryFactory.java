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

package com.vangent.hieos.services.xds.registry.storedquery;


import com.vangent.hieos.xutil.response.AdhocQueryResponse;
import com.vangent.hieos.xutil.response.Response;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.metadata.structure.ParamParser;
import com.vangent.hieos.xutil.query.StoredQuery;

import com.vangent.hieos.services.xds.registry.storedquery.FindDocuments;
import com.vangent.hieos.services.xds.registry.storedquery.FindFolders;
import com.vangent.hieos.services.xds.registry.storedquery.FindSubmissionSets;
import com.vangent.hieos.services.xds.registry.storedquery.GetAssociations;
import com.vangent.hieos.services.xds.registry.storedquery.GetDocuments;
import com.vangent.hieos.services.xds.registry.storedquery.GetDocumentsAndAssociations;
import com.vangent.hieos.services.xds.registry.storedquery.GetFolderAndContents;
import com.vangent.hieos.services.xds.registry.storedquery.GetFolders;
import com.vangent.hieos.services.xds.registry.storedquery.GetFoldersForDocument;
import com.vangent.hieos.services.xds.registry.storedquery.GetRelatedDocuments;
import com.vangent.hieos.services.xds.registry.storedquery.GetSubmissionSetAndContents;
import com.vangent.hieos.services.xds.registry.storedquery.GetSubmissionSets;

import com.vangent.hieos.xutil.xlog.client.XLogMessage;

import com.vangent.hieos.xutil.exception.MetadataException;
import com.vangent.hieos.xutil.exception.XDSRegistryOutOfResourcesException;
import com.vangent.hieos.xutil.exception.XdsException;
import com.vangent.hieos.xutil.exception.XdsInternalException;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.axiom.om.OMElement;

public class StoredQueryFactory {

    OMElement ahqr;
    boolean return_objects = false;
    HashMap<String, Object> params;
    String query_id;
    XLogMessage log_message = null;
    StoredQuery x;
    String service_name;
    boolean is_secure = false;
    Response response = null;

    /*
    public void setResponse(Response response) {
        this.response = response;
    }

    public void setIsSecure(boolean is) {
        is_secure = is;
    }

    public void setServiceName(String serviceName) {
        serviceName = service_name;
    }
     */

    public boolean isLeafClassReturnType() {
        OMElement response_option = MetadataSupport.firstChildWithLocalName(ahqr, "ResponseOption");
        if (response_option == null) {
            return true;
        }
        String return_type = response_option.getAttributeValue(MetadataSupport.return_type_qname);
        if (return_type == null || return_type.equals("") || !return_type.equals("LeafClass")) {
            return false;
        }
        return true;
    }

    public boolean hasParm(String parmName) {
        return params.containsKey(parmName);
    }

    public Object getParm(String parmName) {
        return params.get(parmName);
    }

    public StoredQueryFactory(OMElement ahqr, Response resp, XLogMessage lmsg, String sname, boolean secure) throws XdsInternalException, MetadataException, XdsException {
        this.ahqr = ahqr;
        this.response = resp;
        this.log_message = lmsg;
        this.service_name = sname;
        this.is_secure = secure;

        OMElement response_option = MetadataSupport.firstChildWithLocalName(ahqr, "ResponseOption");
        if (response_option == null) {
            throw new XdsInternalException("Cannot find /AdhocQueryRequest/ResponseOption element");
        }

        String return_type = response_option.getAttributeValue(MetadataSupport.return_type_qname);

        if (return_type == null) {
            throw new XdsException("Attribute returnType not found on query request");
        }
        if (return_type.equals("LeafClass")) {
            return_objects = true;
        } else if (return_type.equals("ObjectRef")) {
            return_objects = false;
        } else {
            throw new MetadataException("/AdhocQueryRequest/ResponseOption/@returnType must be LeafClass or ObjectRef. Found value " + return_type);
        }

        OMElement adhoc_query = MetadataSupport.firstChildWithLocalName(ahqr, "AdhocQuery");
        if (adhoc_query == null) {
            throw new XdsInternalException("Cannot find /AdhocQueryRequest/AdhocQuery element");
        }

        ParamParser parser = new ParamParser();
        params = parser.parse(ahqr);

        if (log_message != null) {
            log_message.addOtherParam("Parameters", params.toString());
        }

        if (this.response == null) {
            // BHT: Fixed bug (was not setting response instance).
            this.response = new AdhocQueryResponse(Response.version_3);
        }

        query_id = adhoc_query.getAttributeValue(MetadataSupport.id_qname);

        if (query_id.equals(MetadataSupport.SQ_FindDocuments)) {
            // FindDocuments
            if (log_message != null) {
                log_message.setTestMessage(service_name + " " + "FindDocuments");
            }
            x = new FindDocuments(params, return_objects, this.response, log_message, is_secure);
        } else if (query_id.equals(MetadataSupport.SQ_FindSubmissionSets)) {
            if (log_message != null) {
                log_message.setTestMessage(service_name + " " + "FindSubmissionSets");
            }
            // FindSubmissionSets
            x = new FindSubmissionSets(params, return_objects, this.response, log_message, is_secure);
        } else if (query_id.equals(MetadataSupport.SQ_FindFolders)) {
            // FindFolders
            if (log_message != null) {
                log_message.setTestMessage(service_name + " " + "FindFolders");
            }
            x = new FindFolders(params, return_objects, this.response, log_message, is_secure);
        } else if (query_id.equals(MetadataSupport.SQ_GetAll)) {
            // GetAll
            if (log_message != null) {
                log_message.setTestMessage(service_name + " " + "GetAll");
            }
            response.add_error("XDSRegistryError", "UnImplemented Stored Query query id = " + query_id, this.getClass().getName(), log_message);
        } else if (query_id.equals(MetadataSupport.SQ_GetDocuments)) {
            // GetDocuments
            if (log_message != null) {
                log_message.setTestMessage(service_name + " " + "GetDocuments");
            }
            x = new GetDocuments(params, return_objects, this.response, log_message, is_secure);
        } else if (query_id.equals(MetadataSupport.SQ_GetFolders)) {
            // GetFolders
            if (log_message != null) {
                log_message.setTestMessage(service_name + " " + "GetFolders");
            }
            x = new GetFolders(params, return_objects, this.response, log_message, is_secure);
        } else if (query_id.equals(MetadataSupport.SQ_GetAssociations)) {
            // GetAssociations
            if (log_message != null) {
                log_message.setTestMessage(service_name + " " + "GetAssociations");
            }
            x = new GetAssociations(params, return_objects, this.response, log_message, is_secure);
        } else if (query_id.equals(MetadataSupport.SQ_GetDocumentsAndAssociations)) {
            // GetDocumentsAndAssociations
            if (log_message != null) {
                log_message.setTestMessage("GetDocumentsAndAssociations");
            }
            x = new GetDocumentsAndAssociations(params, return_objects, this.response, log_message, is_secure);
        } else if (query_id.equals(MetadataSupport.SQ_GetSubmissionSets)) {
            // GetSubmissionSets
            if (log_message != null) {
                log_message.setTestMessage(service_name + " " + "GetSubmissionSets");
            }
            x = new GetSubmissionSets(params, return_objects, this.response, log_message, is_secure);
        } else if (query_id.equals(MetadataSupport.SQ_GetSubmissionSetAndContents)) {
            // GetSubmissionSetAndContents
            if (log_message != null) {
                log_message.setTestMessage(service_name + " " + "GetSubmissionSetAndContents");
            }
            x = new GetSubmissionSetAndContents(params, return_objects, this.response, log_message, is_secure);
        } else if (query_id.equals(MetadataSupport.SQ_GetFolderAndContents)) {
            // GetFolderAndContents
            if (log_message != null) {
                log_message.setTestMessage(service_name + " " + "GetFolderAndContents");
            }
            x = new GetFolderAndContents(params, return_objects, this.response, log_message, is_secure);
        } else if (query_id.equals(MetadataSupport.SQ_GetFoldersForDocument)) {
            // GetFoldersForDocument
            if (log_message != null) {
                log_message.setTestMessage(service_name + " " + "GetFoldersForDocument");
            }
            x = new GetFoldersForDocument(params, return_objects, this.response, log_message, is_secure);
        } else if (query_id.equals(MetadataSupport.SQ_GetRelatedDocuments)) {
            // GetRelatedDocuments
            if (log_message != null) {
                log_message.setTestMessage(service_name + " " + "GetRelatedDocuments");
            }
            x = new GetRelatedDocuments(params, return_objects, this.response, log_message, is_secure);
        } else {
            if (log_message != null) {
                log_message.setTestMessage(service_name + " " + query_id);
            }
            this.response.add_error("XDSRegistryError", "Unknown Stored Query query id = " + query_id, this.getClass().getName(), log_message);
        }
    }

    /*
    public void setLogMessage(Message log_message) {
        this.log_message = log_message;
    }
     */

    public ArrayList<OMElement> run() throws XDSRegistryOutOfResourcesException, XdsException {
        return x.run();
    }
}
