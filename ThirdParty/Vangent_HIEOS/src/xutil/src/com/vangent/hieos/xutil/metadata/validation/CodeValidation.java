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

package com.vangent.hieos.xutil.metadata.validation;

import com.vangent.hieos.xutil.exception.MetadataException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.http.HttpClient;
import com.vangent.hieos.xutil.iosupport.Io;
import com.vangent.hieos.xutil.metadata.structure.Classification;
import com.vangent.hieos.xutil.metadata.structure.Metadata;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.response.RegistryErrorList;
import com.vangent.hieos.xutil.xml.Util;

import com.vangent.hieos.xutil.xconfig.XConfig;
import com.vangent.hieos.xutil.xconfig.XConfigAssigningAuthority;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;


//this gets invoked from both Validator.java and directly from Repository.  Should optimize the implementation so that codes.xml
//gets cached in memory. (Note: BHT (Added optimization code)).
public class CodeValidation {

    static Codes _codes = null;  // Singleton.
    Metadata m;
    RegistryErrorList rel;
    boolean is_submit;
    boolean xds_b;

    public CodeValidation(Metadata m, boolean is_submit, boolean xds_b, RegistryErrorList rel) throws XdsInternalException {
        this.m = m;
        this.rel = rel;
        this.is_submit = is_submit;
        this.xds_b = xds_b;
        loadCodes();
    }

    // this is used for easy access to mime lookup
    public CodeValidation() throws XdsInternalException {
        loadCodes();
    }

    /**
     *
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    private void loadCodes() throws XdsInternalException {
        if (_codes == null) {
            _codes = new Codes();
            _codes.loadCodes();
        }
    }

    public boolean isValidMimeType(String mime_type) {
        return _codes.mime_map.containsKey(mime_type);
//		QName name_att_qname = new QName("name");
//		QName code_att_qname = new QName("code");
//		OMElement mime_type_section = null;
//		for(Iterator it=codes.getChildrenWithName(new QName("CodeType")); it.hasNext();  ) {
//		OMElement ct = (OMElement) it.next();
//		if (ct.getAttributeValue(name_att_qname).equals("mimeType")) {
//		mime_type_section = ct;
//		break;
//		}
//		}
//		if (mime_type_section == null) throw new XdsInternalException("CodeValidation.java: Configuration Error: Cannot find mime type table");

//		for(Iterator it=mime_type_section.getChildElements(); it.hasNext();  ) {
//		OMElement code_ele = (OMElement) it.next();
//		if (code_ele.getAttributeValue(code_att_qname).equals(mime_type))
//		return true;
//		}
//		return false;
    }

    public Collection<String> getKnownFileExtensions() {
        return _codes.ext_map.keySet();
    }

    public String getMimeTypeForExt(String ext) {
        return _codes.ext_map.get(ext);
    }

    public String getExtForMimeType(String mime_type) {
        return _codes.mime_map.get(mime_type);
//		QName name_att_qname = new QName("name");
//		QName code_att_qname = new QName("code");
//		QName ext_att_qname = new QName("ext");
//		OMElement mime_type_section = null;
//		for(Iterator it=codes.getChildrenWithName(new QName("CodeType")); it.hasNext();  ) {
//		OMElement ct = (OMElement) it.next();
//		if (ct.getAttributeValue(name_att_qname).equals("mimeType")) {
//		mime_type_section = ct;
//		break;
//		}
//		}
//		if (mime_type_section == null) throw new XdsInternalException("CodeValidation.java: Configuration Error: Cannot find mime type table");

//		for(Iterator it=mime_type_section.getChildElements(); it.hasNext();  ) {
//		OMElement code_ele = (OMElement) it.next();
//		if (code_ele.getAttributeValue(code_att_qname).equals(mime_type))
//		return code_ele.getAttributeValue(ext_att_qname);
//		}
//		return null;
    }

    public ArrayList<String> getAssigningAuthorities() {
        return _codes.assigning_authorities;
    }

    public void run() throws MetadataException, XdsInternalException {
        ArrayList<String> all_object_ids = m.getObjectIds(m.getAllObjects());

        for (String obj_id : all_object_ids) {
            ArrayList<OMElement> classifications = m.getClassifications(obj_id);

            for (OMElement cl_ele : classifications) {

                Classification cl = new Classification(cl_ele);
                validate(cl);
            }
        }

        for (OMElement doc_ele : m.getExtrinsicObjects()) {
            String mime_type = doc_ele.getAttributeValue(MetadataSupport.mime_type_qname);
            if (!isValidMimeType(mime_type)) {
                err("Mime type, " + mime_type + ", is not available in this Affinity Domain");
            } else {
                val("Mime type " + mime_type, null);
            }

            String objectType = doc_ele.getAttributeValue(MetadataSupport.object_type_qname);
            if (!objectType.equals(MetadataSupport.XDSDocumentEntry_objectType_uuid)) {
                err("XDSDocumentEntry has incorrect objectType, found " + objectType + ", must be " + MetadataSupport.XDSDocumentEntry_objectType_uuid);
            } else {
                val("XDSDocumentEntry.objectType", null);
            }
        }
    }

    /**
     * 
     * @param cl
     */
    void validate(Classification cl) {
        String classification_scheme = cl.getClassificationScheme();

        if (classification_scheme == null) {
            String classification_node = cl.getClassificationNode();
            if (classification_node == null || classification_node.equals("")) {
                err("classificationScheme missing", cl);
                return;
            } else {
                return;
            }
        }
        if (classification_scheme.equals(MetadataSupport.XDSSubmissionSet_author_uuid)) {
            return;
        }
        if (classification_scheme.equals(MetadataSupport.XDSDocumentEntry_author_uuid)) {
            return;
        }
        String code = cl.getCodeValue();
        String coding_scheme = cl.getCodeScheme();

        if (code == null) {
            err("code (nodeRepresentation attribute) missing", cl);
            return;
        }
        if (coding_scheme == null) {
            err("codingScheme (Slot codingScheme) missing", cl);
            return;
        }
        for (OMElement code_type : MetadataSupport.childrenWithLocalName(_codes.codes, "CodeType")) {
            String class_scheme = code_type.getAttributeValue(MetadataSupport.classscheme_qname);

            // some codes don't have classScheme in their definition
            if (class_scheme != null && !class_scheme.equals(classification_scheme)) {
                continue;
            }

            for (OMElement code_ele : MetadataSupport.childrenWithLocalName(code_type, "Code")) {
                String code_name = code_ele.getAttributeValue(MetadataSupport.code_qname);
                String code_scheme = code_ele.getAttributeValue(MetadataSupport.codingscheme_qname);
                if (code_name.equals(code) &&
                        (code_scheme == null || code_scheme.equals(coding_scheme))) {
                    val("Coding of " + code_scheme, null);
                    return;
                }
            }
        }
        val("Coding of " + coding_scheme, " (" + code + ") Not Found");
        err("The code, " + code + ", is not found in the configuration for the Affinity Domain", cl);
    }

    /**
     *
     * @param topic
     * @param msg
     */
    void val(String topic, String msg) {
        if (msg == null) {
            msg = "Ok";
        }
        rel.add_validation(topic, msg, "CodeValidation.java");
    }

    /**
     *
     * @param msg
     * @param cl
     */
    void err(String msg, Classification cl) {
        rel.add_error(MetadataSupport.XDSRegistryMetadataError, cl.identifying_string() + ": " + msg, this.getClass().getName(), null);
    }

    /**
     *
     * @param msg
     */
    void err(String msg) {
        rel.add_error(MetadataSupport.XDSRegistryMetadataError, msg, this.getClass().getName(), null);
    }

    // Created by Bernie Thuman (to optimize Code Validation processing).  Most code was simply moved from
    // the CodeValidation class.
    public class Codes {

        OMElement codes;
        ArrayList<String> assigning_authorities;
        HashMap<String, String> mime_map;  // mime => ext
        HashMap<String, String> ext_map;   // ext => mime

        /**
         * 
         * @throws com.vangent.hieos.xutil.exception.XdsInternalException
         */
        void loadCodes() throws XdsInternalException {
            String fileCodesLocation = System.getenv("HIEOSxCodesFile");
            XConfig xconf = XConfig.getInstance();
            //String localCodesLocation = "http://localhost:8080/xref/codes/codes.xml";
            String localCodesLocation = xconf.getHomeCommunityProperty("CodesLocation");
            //String globalCodesLocation = "http://ihexds.nist.gov:9080/xdsref/codes/codes.xml";
            String codes_string = null;
            String from = null;

            if (fileCodesLocation != null) {
                try {
                    codes_string = Io.getStringFromInputStream(new FileInputStream(new File(fileCodesLocation)));
                    from = fileCodesLocation;
                } catch (Exception e) {
                    throw new XdsInternalException("Env Var HIEOSxCodesFile exists but codes.xml file cannot be loaded.", e);
                }
            } else {

                try {
                    codes_string = HttpClient.httpGet(localCodesLocation);
                    from = localCodesLocation;
                } catch (Exception e) {
                    throw new XdsInternalException("CodeValidation: Unable to retrieve code configuration file " + localCodesLocation);
                }
            }
            if (codes_string == null) {
                throw new XdsInternalException("CodeValidation.init(): GET codes.xml returned NULL from " + from);
            }
            if (codes_string.equals("")) {
                throw new XdsInternalException("CodeValidation.init(): GET codes.xml returned enpty from " + from);
            }

            codes = Util.parse_xml(codes_string);
            if (codes == null) {
                throw new XdsInternalException("CodeValidation: cannot parse code configuration file from " + from);
            }

            // Pull assigning authorities from XConfig here (instead of codes.xml).
            ArrayList<XConfigAssigningAuthority> assigningAuthoritiesConfig = xconf.getAssigningAuthorities();
            this.assigning_authorities = new ArrayList<String>();
            for (Iterator it = assigningAuthoritiesConfig.iterator(); it.hasNext();) {
                XConfigAssigningAuthority aa = (XConfigAssigningAuthority) it.next();
                //System.out.println("Assigning Authority: " + aa.getUniqueId());
                this.assigning_authorities.add(aa.getUniqueId());
            }

            /* OLD CODE - Removed by BHT (now pulls from XConfig)
             *
            assigning_authorities = new ArrayList<String>();
            for (OMElement aa_ele : MetadataSupport.childrenWithLocalName(codes, "AssigningAuthority")) {
            this.assigning_authorities.add(aa_ele.getAttributeValue(MetadataSupport.id_qname));
            }
             */
            build_mime_map();
        }

        /**
         * 
         * @throws com.vangent.hieos.xutil.exception.XdsInternalException
         */
        private void build_mime_map() throws XdsInternalException {
            QName name_att_qname = new QName("name");
            QName code_att_qname = new QName("code");
            QName ext_att_qname = new QName("ext");
            OMElement mime_type_section = null;
            for (Iterator it = codes.getChildrenWithName(new QName("CodeType")); it.hasNext();) {
                OMElement ct = (OMElement) it.next();
                if (ct.getAttributeValue(name_att_qname).equals("mimeType")) {
                    mime_type_section = ct;
                    break;
                }
            }
            if (mime_type_section == null) {
                throw new XdsInternalException("CodeValidation.java: Configuration Error: Cannot find mime type table");
            }

            mime_map = new HashMap<String, String>();
            ext_map = new HashMap<String, String>();

            for (Iterator it = mime_type_section.getChildElements(); it.hasNext();) {
                OMElement code_ele = (OMElement) it.next();
                String mime_type = code_ele.getAttributeValue(code_att_qname);
                String ext = code_ele.getAttributeValue(ext_att_qname);
                mime_map.put(mime_type, ext);
                ext_map.put(ext, mime_type);
            }
        }
    }
}
