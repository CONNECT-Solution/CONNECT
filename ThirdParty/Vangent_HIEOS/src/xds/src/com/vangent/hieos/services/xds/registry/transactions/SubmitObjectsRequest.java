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

import com.vangent.hieos.xutil.services.framework.ContentValidationService;
import com.vangent.hieos.xutil.atna.XATNALogger;
import com.vangent.hieos.xutil.metadata.structure.MetadataTypes;
import com.vangent.hieos.adt.verify.Verify;
import com.vangent.hieos.xutil.hl7.date.Hl7Date;
import com.vangent.hieos.xutil.exception.MetadataException;
import com.vangent.hieos.xutil.exception.MetadataValidationException;
import com.vangent.hieos.xutil.exception.SchemaValidationException;
import com.vangent.hieos.xutil.exception.XdsDeprecatedException;
import com.vangent.hieos.xutil.exception.XdsException;
import com.vangent.hieos.xutil.exception.XdsFormatException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.exception.XdsPatientIdDoesNotMatchException;
import com.vangent.hieos.xutil.exception.XdsUnknownPatientIdException;
import com.vangent.hieos.xutil.registry.BackendRegistry;
import com.vangent.hieos.xutil.metadata.structure.IdParser;
import com.vangent.hieos.xutil.metadata.structure.Metadata;
import com.vangent.hieos.xutil.metadata.structure.MetadataParser;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
//import com.vangent.hieos.xutil.registry.Properties;
//import com.vangent.xconfig.XConfig;
import com.vangent.hieos.xutil.query.RegistryObjectValidator;
import com.vangent.hieos.xutil.response.RegistryResponse;
import com.vangent.hieos.xutil.registry.RegistryUtility;
import com.vangent.hieos.xutil.response.Response;
import com.vangent.hieos.xutil.services.framework.XBaseTransaction;
import com.vangent.hieos.xutil.metadata.structure.Structure;
import com.vangent.hieos.xutil.metadata.validation.Validator;
import com.vangent.hieos.services.xds.registry.storedquery.SQFactory;
import com.vangent.hieos.xutil.xlog.client.XLogMessage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.vangent.hieos.xutil.xconfig.XConfig;

import javax.xml.transform.TransformerConfigurationException;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.MessageContext;
//import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class SubmitObjectsRequest extends XBaseTransaction {
    //AUDIT:POINT
    //Message context was added when trying to send audit message

    MessageContext messageContext;
    boolean submit_raw = false;
    ContentValidationService validater;
    //short xds_version;
    private final static Logger logger = Logger.getLogger(SubmitObjectsRequest.class);
    //static Properties properties = null;
    //static ArrayList<String> sourceIds = null;


    /* BHT: Removed
    static {
    //properties = Properties.loader();
    BasicConfigurator.configure();
    } */
    public SubmitObjectsRequest(XLogMessage log_message, short xds_version, MessageContext messageContext) {
        this.log_message = log_message;
        this.xds_version = xds_version;
        //AUDIT:POINT
        //Message context was initialized when trying to send audit message
        this.messageContext = messageContext;
        try {
            init(new RegistryResponse((xds_version == xds_a) ? Response.version_2 : Response.version_3), xds_version, messageContext);
        //loadSourceIds();
        } catch (XdsInternalException e) {
            logger.fatal(logger_exception_details(e));
        }
    }

    /* Not a requirement to validate source Ids according to XDS.b spec.
    void loadSourceIds() throws XdsInternalException {
    if (sourceIds != null) return;
    String sids = properties.getString("sourceIds");
    if (sids == null || sids.equals(""))
    throw new XdsInternalException("Registry: sourceIds not configured");
    String[] parts = sids.split(",");
    sourceIds = new ArrayList<String>();
    for (int i=0; i<parts.length; i++) {
    sourceIds.add(parts[i].trim());
    }
    }*/
    public OMElement submitObjectsRequest(OMElement sor, ContentValidationService validater) {
        this.validater = validater;

        try {
            sor.build();

            mustBeSimpleSoap();

            SubmitObjectsRequestInternal(sor);
            //AUDIT:POINT
            //call to audit message for document repository
            //for Transaction id = ITI-42. (Register Document set-b)
            //Here document consumer is treated as document repository
            performAudit(
                    XATNALogger.TXN_ITI42,
                    sor,
                    null,
                    XATNALogger.ActorType.REGISTRY,
                    XATNALogger.OutcomeIndicator.SUCCESS);

        } catch (XdsFormatException e) {
            response.add_error("XDSRegistryError", "SOAP Format Error: " + e.getMessage(), this.getClass().getName(), log_message);
        } catch (XdsDeprecatedException e) {
            response.add_error("XDSRegistryDeprecatedDocumentError", "XDS Deprecated Document Error:\n " + e.getMessage(), this.getClass().getName(), log_message);
            logger.warn(logger_exception_details(e));
        } catch (XdsUnknownPatientIdException e) {
            response.add_error("XDSUnknownPatientId", "XDS Unknown Patient Id:\n " + e.getMessage(), this.getClass().getName(), log_message);
            logger.warn(logger_exception_details(e));
        } catch (XdsPatientIdDoesNotMatchException e) {
            response.add_error("XDSPatientIdDoesNotMatch", "Patient ID does not match:\n " + e.getMessage(), this.getClass().getName(), log_message);
            logger.warn(logger_exception_details(e));
        } catch (XdsInternalException e) {
            response.add_error("XDSRegistryError", "XDS Internal Error:\n " + e.getMessage(), this.getClass().getName(), log_message);
            logger.fatal(logger_exception_details(e));
        } catch (MetadataValidationException e) {
            response.add_error("XDSRegistryMetadataError", "Metadata Validation Errors:\n " + e.getMessage(), this.getClass().getName(), log_message);
        } catch (SchemaValidationException e) {
            response.add_error("XDSRegistryMetadataError", "Schema Validation Errors:\n" + e.getMessage(), this.getClass().getName(), log_message);
        } catch (XdsException e) {
            response.add_error("XDSRegistryError", "Exception:\n " + e.getMessage(), this.getClass().getName(), log_message);
            logger.warn(logger_exception_details(e));
        } catch (TransformerConfigurationException e) {
            response.add_error("XDSRegistryError", "Internal Error: Transformer Configuration Error: " + e.getMessage(), this.getClass().getName(), log_message);
            logger.fatal(logger_exception_details(e));
        } catch (SQLException e) {
            response.add_error("XDSRegistryError", "Internal Logging error: SQLException: " + e.getMessage(), this.getClass().getName(), log_message);
            logger.fatal(logger_exception_details(e));
        } catch (Exception e) {
            response.add_error("XDSRegistryError", "XDS General Error:\n " + e.getMessage(), this.getClass().getName(), log_message);
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

    void SubmitObjectsRequestInternal(OMElement sor)
            throws SQLException, SchemaValidationException, MetadataValidationException, XdsInternalException, TransformerConfigurationException,
            MetadataValidationException, XdsException {
        boolean status;

        String sor_string = sor.toString();

        // AMS 04/21/2009 - FIXME
        // At some point this logic needs to be refactored.
        // In the interim, the following code fragment was commented out to prevent compilation errors.
        // The compilation errors were owing to removal of a previously existing method in this
        // class, submit_to_backend_registry(sor.toString()), which  used to initiate a REST call.

        //if (submit_raw) {
        // status = submit_to_backend_registry(sor.toString());
        //  } else
        // {

        if (xds_version == xds_b) {
            RegistryUtility.schema_validate_local(sor, MetadataTypes.METADATA_TYPE_Rb);
        } else {
            RegistryUtility.schema_validate_local(sor, MetadataTypes.METADATA_TYPE_R);
        }


        try {
            Metadata m = new Metadata(sor);

            log_message.addOtherParam("SSuid", m.getSubmissionSetUniqueId());

            ArrayList<String> doc_uids = new ArrayList<String>();
            for (String id : m.getExtrinsicObjectIds()) {
                String uid = m.getUniqueIdValue(id);
                if (uid != null && !uid.equals("")) {
                    doc_uids.add(uid);
                }
            }
            log_message.addOtherParam("DOCuids", doc_uids.toString());

            ArrayList<String> fol_uids = new ArrayList<String>();
            for (String id : m.getFolderIds()) {
                String uid = m.getUniqueIdValue(id);
                if (uid != null && !uid.equals("")) {
                    fol_uids.add(uid);
                }
            }
            log_message.addOtherParam("FOLuids", fol_uids.toString());
            log_message.addOtherParam("Structure", m.structure());

            Validator val = new Validator(m, response.registryErrorList, true, xds_version == xds_b, log_message);
            val.run();

            RegistryObjectValidator rov = new RegistryObjectValidator(response, log_message);
            rov.validateProperUids(m);

            if (response.has_errors()) {
                logger.error("metadata validator failed");
            }

            if (response.has_errors()) {
                return;
            }

            if (this.validater != null && !this.validater.runContentValidationService(m, response)) {
                return;
            }


            String patient_id = m.getSubmissionSetPatientId();
            log_message.addOtherParam("Patient ID", patient_id);

            validate_patient_id(patient_id);


            //validateSourceId(m);



            // check for references to registry contents
            ArrayList referenced_objects = m.getReferencedObjects();
            if (referenced_objects.size() > 0) {
                ArrayList missing = rov.validateApproved(referenced_objects);
                if (missing != null) {
                    throw new XdsDeprecatedException("The following registry objects were referenced by this submission but are not present, as Approved documents, in the registry: " +
                            missing);
                }

                // make allowance for by reference inclusion
                missing = rov.validateSamePatientId(m.getReferencedObjectsThatMustHaveSamePatientId(), patient_id);
                if (missing != null) {
                    throw new XdsPatientIdDoesNotMatchException("The following registry objects were referenced by this submission but do not reference the same patient ID: " +
                            missing);
                }

            }

            // allocate uuids for symbolic ids
            IdParser ra = new IdParser(m);

            ra.compileSymbolicNamesIntoUuids();

            // check that submission does not include any object ids that are already in registry
            ArrayList<String> ids_in_submission = m.getAllDefinedIds();
            RegistryObjectValidator roval = new RegistryObjectValidator(response, log_message);
            ArrayList<String> ids_already_in_registry = roval.validateNotExists(ids_in_submission);
            if (ids_already_in_registry.size() != 0) {
                response.add_error(MetadataSupport.XDSRegistryMetadataError,
                        "The following UUIDs which are present in the submission are already present in registry: " + ids_already_in_registry,
                        this.getClass().getName(),
                        log_message);
            }

            // Set XDSFolder.lastUpdateTime
            if (m.getFolders().size() != 0) {
                String timestamp = new Hl7Date().now();
                for (OMElement fol : m.getFolders()) {
                    m.setSlot(fol, "lastUpdateTime", timestamp);
                }
            }

            // If this submission includes a DocumentEntry replace and the original DocumentEntry is in a folder
            // then the replacement document must be put into the folder as well.  This must happen here
            // so the following logic to update folder lastUpdateTime can be triggered.

            HashMap<String, String> rplcToOrigIds = new HashMap<String, String>();
            for (OMElement assoc : m.getAssociations()) {
                if (MetadataSupport.xdsB_ihe_assoc_type_rplc.equals(m.getAssocType(assoc))) {
                    rplcToOrigIds.put(m.getAssocSource(assoc), m.getAssocTarget(assoc));
                }
            }

            for (String replacementDocumentId : rplcToOrigIds.keySet()) {
                String originalDocumentId = rplcToOrigIds.get(replacementDocumentId);
                // for each original document, find the collection of folders it belongs to
                Metadata me = new SQFactory(this, false).findFoldersForDocumentByUuid(originalDocumentId);
                ArrayList<String> folderIds = me.getObjectIds(me.getObjectRefs());
                // for each folder, add an association placing replacment in that folder
                // This brings up interesting question, should the Assoc between SS and Assoc be generated also?  YES!
                for (String fid : folderIds) {

                    OMElement assoc = m.add_association(m.mkAssociation(MetadataSupport.xdsB_eb_assoc_type_has_member, fid, replacementDocumentId));
                    OMElement assoc2 = m.add_association(m.mkAssociation(MetadataSupport.xdsB_eb_assoc_type_has_member, m.getSubmissionSetId(), assoc.getAttributeValue(MetadataSupport.id_qname)));
                }
            }


            BackendRegistry reg = new BackendRegistry(response, log_message);
            // if this submission adds a document to a folder then update that folder's lastUpdateTime Slot
            ArrayList<String> registryPackagesToUpdate = new ArrayList<String>();
            for (OMElement assoc : m.getAssociations()) {
                if (MetadataSupport.xdsB_eb_assoc_type_has_member.equals(m.getAssocType(assoc))) {
                    String sourceId = m.getAssocSource(assoc);
                    if (!m.getSubmissionSetId().equals(sourceId) &&
                            !m.getFolderIds().contains(sourceId)) {
                        // Assoc src not part of the submission
                        logger.info("Adding to Folder (1)" + sourceId);
                        if (new Structure(new Metadata(), false).isFolder(sourceId)) {
                            logger.info("Adding to Folder (2)" + sourceId);

                            OMElement res = reg.basic_query("SELECT * from RegistryPackage rp WHERE rp.id='" + sourceId + "'",
                                    true /* leaf_class */);

                            Metadata fm = MetadataParser.parseNonSubmission(res);
                            // Set XDSFolder.lastUpdateTime
                            if (fm.getFolders().size() != 0) {
                                String timestamp = new Hl7Date().now();
                                for (OMElement fol : fm.getFolders()) {
                                    fm.setSlot(fol, "lastUpdateTime", timestamp);
                                }
                            }

                            OMElement to_backend = fm.getV3SubmitObjectsRequest();
                            log_message.addOtherParam("From Registry Adaptor", to_backend.toString());
                            status = submit_to_backend_registry(reg, to_backend);
                            if (!status) {
                                return;
                            }
                        }
                    }
                }
            }

            OMElement to_backend = m.getV3SubmitObjectsRequest();
            log_message.addOtherParam("From Registry Adaptor", to_backend.toString());
            status = submit_to_backend_registry(reg, to_backend);
            if (!status) {
                return;
            }

            // Approve
            ArrayList approvable_object_ids = ra.approvable_object_ids(m);
            if (approvable_object_ids.size() > 0) {
                OMElement approve = ra.getApproveObjectsRequest(approvable_object_ids);
                log_message.addOtherParam("Approve", approve.toString());
                submit_to_backend_registry(reg, approve);
            }

            // Deprecate
            ArrayList deprecatable_object_ids = m.getObjectIdsToDeprecate();
            // add to the list of things to deprecate, any XFRM or APND documents hanging off documents
            // in the deprecatable_object_ids list
            deprecatable_object_ids.addAll(new RegistryObjectValidator(response, log_message).getXFRMandAPNDDocuments(deprecatable_object_ids));
            if (deprecatable_object_ids.size() > 0) {
                // validate that these are documents first
                ArrayList missing = rov.validateDocuments(deprecatable_object_ids);
                if (missing != null) {
                    throw new XdsException("The following documents were referenced by this submission but are not present in the registry: " +
                            missing);
                }

                OMElement deprecate = ra.getDeprecateObjectsRequest(deprecatable_object_ids);
                log_message.addOtherParam("Deprecate", deprecate.toString());
                submit_to_backend_registry(reg, deprecate);
            }
            log_response();
        } catch (MetadataException e) {
            response.add_error("XDSRegistryError", e.getMessage(), this.getClass().getName(), log_message);
            return;
        }
    }

    /**
     *
     * @param patient_id
     * @throws java.sql.SQLException
     * @throws com.vangent.hieos.xutil.exception.XdsException
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    private void validate_patient_id(String patient_id) throws SQLException,
            XdsException, XdsInternalException {
        //if (Properties.loader().getBoolean("validate_patient_id")) {
        if (XConfig.getInstance().getHomeCommunityPropertyAsBoolean("validatePatientId")) {
            Verify v = new Verify();
            boolean known_patient_id = v.isValid(patient_id);
            if (!known_patient_id) {
                throw new XdsUnknownPatientIdException("PatientId " + patient_id + " is not known to the Registry");
            }
        }
    }

    //AMS 04/29/2009 - FIXME invoked by XdsRaw. REMOVE at some point.
    public void setSubmitRaw(boolean val) {
        submit_raw = val;
    }

    /* AMS 04/21/2009 - Added new method. */
    private boolean submit_to_backend_registry(BackendRegistry br, OMElement omElement) throws XdsException {
        OMElement result = br.submit_to_backend_registry(omElement);
        return getResult(result);// Method should be renamed to getRegistrySubmissionStatus ...
    }

    /**
     *
     * @param result
     * @return
     */
    private boolean getResult(OMElement result) {
        if (result == null) {
            return false;
        }

        String value = result.getAttributeValue(MetadataSupport.status_qname);
        if (value == null) {
            return false;
        }
        if (value.indexOf(":") == -1) {
            return false;
        }
        String[] parts = value.split(":");
        if ("Success".equals(parts[parts.length - 1])) {
            return true;
        } else {
            return false;
        }
    }
}
