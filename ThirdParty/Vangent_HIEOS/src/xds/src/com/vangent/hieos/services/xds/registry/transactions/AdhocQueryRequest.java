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

package com.vangent.hieos.services.xds.registry.transactions;

import com.vangent.hieos.services.xds.registry.storedquery.StoredQueryFactory;
import com.vangent.hieos.xutil.atna.XATNALogger;
import com.vangent.hieos.xutil.metadata.structure.MetadataTypes;
import com.vangent.hieos.xutil.exception.MetadataException;
import com.vangent.hieos.xutil.exception.MetadataValidationException;
import com.vangent.hieos.xutil.exception.SchemaValidationException;
import com.vangent.hieos.xutil.exception.XDSRegistryOutOfResourcesException;
import com.vangent.hieos.xutil.exception.XdsException;
import com.vangent.hieos.xutil.exception.XdsFormatException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.exception.XdsValidationException;
import com.vangent.hieos.xutil.response.AdhocQueryResponse;
import com.vangent.hieos.xutil.metadata.structure.Metadata;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.registry.RegistryUtility;
import com.vangent.hieos.xutil.response.Response;
import com.vangent.hieos.xutil.services.framework.XBaseTransaction;
import com.vangent.hieos.xutil.xlog.client.XLogMessage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.log4j.Logger;

public class AdhocQueryRequest extends XBaseTransaction {

    MessageContext messageContext;
    String service_name = "";
    boolean is_secure;
    private final static Logger logger = Logger.getLogger(AdhocQueryRequest.class);

    public AdhocQueryRequest(XLogMessage log_message, MessageContext messageContext, boolean is_secure, short xds_version) {
        this.log_message = log_message;
        this.messageContext = messageContext;
        this.is_secure = is_secure;
        this.xds_version = xds_version;
    }

    public void setServiceName(String service_name) {
        this.service_name = service_name;
    }

    public OMElement adhocQueryRequest(OMElement ahqr) {
        ahqr.build();

        OMNamespace ns = ahqr.getNamespace();
        String ns_uri = ns.getNamespaceURI();

        try {
            if (ns_uri.equals(MetadataSupport.ebQns3.getNamespaceURI())) {
                init(new AdhocQueryResponse(Response.version_3), xds_version, messageContext);
            } else if (ns_uri.equals(MetadataSupport.ebQns2.getNamespaceURI())) {
                init(new AdhocQueryResponse(Response.version_2), xds_version, messageContext);
            } else {
                init(new AdhocQueryResponse(Response.version_3), xds_version, messageContext);
                response.add_error("XDSRegistryError", "Invalid XML namespace on AdhocQueryRequest: " + ns_uri, this.getClass().getName(), log_message);
                return response.getResponse();
            }
        } catch (XdsInternalException e) {
            logger.fatal("Internal Error initializing AdhocQueryRequest transaction: " + e.getMessage());
            return null;
        }

        try {
            mustBeSimpleSoap();

            AdhocQueryRequestInternal(ahqr);

            //AUDIT:POINT
            //call to audit message for the Registry
            //for Transaction id = ITI-18. (Registry Stored Query)
            //Here the Registry is treated as source
            performAudit(
                    XATNALogger.TXN_ITI18,
                    ahqr,
                    null,
                    XATNALogger.ActorType.REGISTRY,
                    XATNALogger.OutcomeIndicator.SUCCESS);
        } catch (XdsValidationException e) {
            response.add_error("XDSRegistryError", "Validation Error: " + e.getMessage(), this.getClass().getName(), log_message);
        } catch (XdsFormatException e) {
            response.add_error("XDSRegistryError", "SOAP Format Error: " + e.getMessage(), this.getClass().getName(), log_message);
        } catch (XDSRegistryOutOfResourcesException e) {
            // query return limitation
            response.add_error("XDSRegistryOutOfResources", e.getMessage(), this.getClass().getName(), log_message);
        } catch (SchemaValidationException e) {
            response.add_error("XDSRegistryMetadataError", "SchemaValidationException: " + e.getMessage(), this.getClass().getName(), log_message);
        } catch (XdsInternalException e) {
            response.add_error("XDSRegistryError", "Internal Error: " + e.getMessage(), this.getClass().getName(), log_message);
            logger.fatal(logger_exception_details(e));
        } catch (MetadataValidationException e) {
            response.add_error("XDSRegistryError", "Metadata Error: " + e.getMessage(), this.getClass().getName(), log_message);
        //} catch (SqlRepairException e) {
        //    response.add_error("XDSRegistryError", "Could not decode SQL: " + e.getMessage(), this.getClass().getName(), log_message);
        //    logger.warn(logger_exception_details(e));
        } catch (MetadataException e) {
            response.add_error("XDSRegistryError", "Metadata error: " + e.getMessage(), this.getClass().getName(), log_message);
        } catch (SQLException e) {
            response.add_error("XDSRegistryError", "SQL error: " + e.getMessage(), this.getClass().getName(), log_message);
            logger.fatal(logger_exception_details(e));
        } catch (XdsException e) {
            response.add_error("XDSRegistryError", "XDS Error: " + e.getMessage(), this.getClass().getName(), log_message);
            logger.warn(logger_exception_details(e));
        } catch (Exception e) {
            response.add_error("General Exception", "Internal Error: " + e.getMessage(), this.getClass().getName(), log_message);
            logger.fatal(logger_exception_details(e));
        }

        this.log_response();

        OMElement res = null;
        try {
            res = response.getResponse();
        } catch (XdsInternalException e) {
        }
        return res;
    }

    public boolean isStoredQuery(OMElement ahqr) {
        for (Iterator it = ahqr.getChildElements(); it.hasNext();) {
            OMElement ele = (OMElement) it.next();
            String ele_name = ele.getLocalName();

            if (ele_name.equals("AdhocQuery")) {
                return true;
            }
        }
        return false;
    }

    void AdhocQueryRequestInternal(OMElement ahqr)
            throws SQLException, XdsException, XDSRegistryOutOfResourcesException, AxisFault,
            XdsValidationException {

        boolean found_query = false;

        for (Iterator it = ahqr.getChildElements(); it.hasNext();) {
            OMElement ele = (OMElement) it.next();
            String ele_name = ele.getLocalName();

            if (ele_name.equals("SQLQuery")) {
                log_message.setTestMessage("SQL");
                RegistryUtility.schema_validate_local(ahqr, MetadataTypes.METADATA_TYPE_Q);
                found_query = true;

                OMElement result = null;
                // AMS 04/21/2009 - FIXME - THE IF Condition NEVER exectues as it applies to EBXML V2.
                // At some point this logic needs to be refactored.
                // In the interim, the following code fragment was commented out to prevent compilation errors.
                // The compilation errors were owing to removal of a previously existing method in this
                // class, sql_query(ahqr), which in turn delegated to BackendRegistry.query(OMElememt).
                // The latter, which has also been removed from BackendRegistry used to initiate a REST call.

                //result = sql_query(ahqr);


                // move result elements to response
                if (result != null) {
                    Metadata m = new Metadata(result, false /* parse */, true /* find_wrapper */);
                    OMElement sqr = m.getWrapper();
                    if (sqr != null) {
                        for (Iterator it2 = sqr.getChildElements(); it2.hasNext();) {
                            OMElement e = (OMElement) it2.next();
                            ((AdhocQueryResponse) response).addQueryResults(e);
                        }
                    }
                }
            } else if (ele_name.equals("AdhocQuery")) {
                log_message.setTestMessage(service_name);

                RegistryUtility.schema_validate_local(ahqr, MetadataTypes.METADATA_TYPE_SQ);
                found_query = true;

                ArrayList<OMElement> results = stored_query(ahqr);

                //response.query_results = results;
                if (results != null) {
                    ((AdhocQueryResponse) response).addQueryResults(results);
                }
            }

        }
        if (!found_query) {
            response.add_error("XDSRegistryError", "Only SQLQuery and AdhocQuery accepted", this.getClass().getName(), log_message);
        }
        // BHT: (Removed multiple response log entry)
        // this.log_response();
    }

    public String getStoredQueryId(OMElement ahqr) {
        OMElement adhoc_query = MetadataSupport.firstChildWithLocalName(ahqr, "AdhocQuery");
        if (adhoc_query == null) {
            return null;
        }
        return adhoc_query.getAttributeValue(MetadataSupport.id_qname);
    }

    public String getHome(OMElement ahqr) {
        OMElement ahquery = MetadataSupport.firstChildWithLocalName(ahqr, "AdhocQuery");
        if (ahquery == null) {
            return null;
        }
        // BHT (FIX): Fixed to use home_qname versus id_qname.
        return ahquery.getAttributeValue(MetadataSupport.home_qname);
    }

    // Initiating Gateway shall specify the homeCommunityId attribute in all Cross-Community
    // Queries which do not contain a patient identifier.
    public boolean requiresHomeInXGQ(OMElement ahqr) {
        boolean requires = true;
        String query_id = getStoredQueryId(ahqr);
        if (query_id == null) {
            requires = false;
        }
        if (query_id.equals(MetadataSupport.SQ_FindDocuments)) {
            requires = false;
        }
        if (query_id.equals(MetadataSupport.SQ_FindFolders)) {
            requires = false;
        }
        if (query_id.equals(MetadataSupport.SQ_FindSubmissionSets)) {
            requires = false;
        }
        if (query_id.equals(MetadataSupport.SQ_GetAll)) {
            requires = false;
        }
        logger.info("query " + query_id + " requires home = " + requires);
        return requires;
    }

    ArrayList<OMElement> stored_query(OMElement ahqr)
            throws XdsException, XDSRegistryOutOfResourcesException, XdsValidationException {


//		new StoredQueryRequestSoapValidator(xds_version, messageContext).runWithException();

        try {
            StoredQueryFactory fact =
                    new StoredQueryFactory(
                    ahqr,           // AdhocQueryRequest
                    response,       // The response object.
                    log_message,    // For logging.
                    service_name,   // For logging.
                    is_secure);     // For logging.
            //fact.setServiceName(service_name);
            //fact.setLogMessage(log_message);
            //fact.setIsSecure(is_secure);
            //fact.setResponse(response);
            return fact.run();
        } catch (Exception e) {
            response.add_error("XDSRegistryError", e.getMessage(), this.getClass().getName(), log_message);
            return null;

        }

    }
}
