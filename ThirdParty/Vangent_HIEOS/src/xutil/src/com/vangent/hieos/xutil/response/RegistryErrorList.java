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
package com.vangent.hieos.xutil.response;

import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.xml.Util;
import com.vangent.hieos.xutil.xlog.client.XLogMessage;

import java.util.HashMap;
import java.util.Iterator;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.log4j.Logger;

public class RegistryErrorList extends ErrorLogger {

    public final static short version_2 = 2;
    public final static short version_3 = 3;
    String errors_and_warnings = "";
    boolean has_errors = false;
    boolean has_warnings = false;
    OMElement rel = null;
    StringBuffer validations = null;
    short version;
    protected OMNamespace ebRSns;
    protected OMNamespace ebRIMns;
    protected OMNamespace ebQns;
    boolean log = true;  // generate log entries?
    boolean format_for_html = false;
    private final static Logger logger = Logger.getLogger(RegistryErrorList.class);
    boolean verbose = true;
    boolean partialSuccess = false;

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public RegistryErrorList(short version) throws XdsInternalException {
        init(version, true /* log */);
    }

    public RegistryErrorList(short version, boolean log) throws XdsInternalException {
        init(version, log);
    }

    public void format_for_html(boolean value) {
        this.format_for_html = value;
    }

    void init(short version, boolean log) throws XdsInternalException {
        if (version != version_2 && version != version_3) {
            throw new XdsInternalException("Class com.vangent.hieos.xutil.response.Response created without valid version");
        }
        this.version = version;
        if (version == version_2) {
            ebRSns = MetadataSupport.ebRSns2;
            ebRIMns = MetadataSupport.ebRIMns2;
            ebQns = MetadataSupport.ebQns2;
        } else {
            ebRSns = MetadataSupport.ebRSns3;
            ebRIMns = MetadataSupport.ebRIMns3;
            ebQns = MetadataSupport.ebQns3;
        }
        this.log = log;
        this.validations = new StringBuffer();
    }

    public boolean has_errors() {
        return has_errors;
    }

    public void forcePartialSuccessStatus() {
        partialSuccess = true;
    }

    public String getStatus() {
        if (partialSuccess) {
            return "PartialSuccess";
        }
        if (has_errors()) {
            return "Failure";
        }
        return "Success";
    }

    public short getVersion() {
        return version;
    }

    public OMElement getRegistryErrorList() {
        //System.out.println(this.validations.toString());
        return registryErrorList();
    }

    OMElement registryErrorList() {
        if (rel == null) {
            rel = MetadataSupport.om_factory.createOMElement("RegistryErrorList", ebRSns);
        }
        return rel;
    }
    static final QName codeContextQName = new QName("codeContext");

    public String getErrorsAndWarnings() {
        //		if ( !format_for_html)
        //			return errors_and_warnings;

        StringBuffer buf = new StringBuffer();
        for (Iterator<OMElement> it = getRegistryErrorList().getChildElements(); it.hasNext();) {
            OMElement ele = it.next();
            if (format_for_html) {
                buf.append("<p>" + ele.getAttributeValue(codeContextQName) + "</p>\n");
            } else {
                buf.append(ele.getAttributeValue(codeContextQName)).append("\n");
            }
        }
        return buf.toString();
    }

    public void add_warning(String code, String msg, String location, XLogMessage log_message) {
        errors_and_warnings += "Warning: " + msg + "\n";
        warning(msg);
        addWarning(msg, code, location);

        if (log) {
            try {
                log_message.addErrorParam("Warning", msg);
            } catch (Exception e) {
                // oh well - can't fix it from here
            }
        }
    }

    public void add_validation(String topic, String msg, String location) {
        validations.append(topic + ": " +
                ((msg != null) ? msg + " " : "") +
                ((location != null) ? " @" + location : "") +
                "\n");
    }

    public void add_error(String code, String msg, String location, XLogMessage log_message) {
        errors_and_warnings += "Error: " + code + " " + msg + "\n";
        error(msg);
        addError(msg, code, location);

        if (log) {
            try {
                if (log_message != null) {
                    log_message.addErrorParam("Error", msg + "\n" + location);
                }
            } catch (Exception e) {
                // oh well - can't fix it from here
            }
        }
    }

    HashMap<String, String> getErrorDetails(OMElement registryError) {
        HashMap<String, String> err = new HashMap<String, String>();

        for (Iterator<OMAttribute> it = registryError.getAllAttributes(); it.hasNext();) {
            OMAttribute att = it.next();
            String name = att.getLocalName();
            String value = att.getAttributeValue();
            err.put(name, value);
        }

        return err;
    }

    public void addRegistryErrorList(OMElement rel, XLogMessage log_message) throws XdsInternalException {
        for (Iterator it = rel.getChildElements(); it.hasNext();) {
            OMElement registry_error = (OMElement) it.next();

            if (log_message != null) {
                HashMap<String, String> err = getErrorDetails(registry_error);
                log_message.addErrorParam("Error", err.get("codeContext"));
            }

            OMElement registry_error_2 = Util.deep_copy(registry_error);
            logger.error("registry_error2 is \n" + registry_error_2.toString());

            if (this.getVersion() == RegistryErrorList.version_3) {
                registry_error_2.setNamespace(MetadataSupport.ebRSns3);
            }
            registryErrorList().addChild(registry_error_2);
            if (registry_error.getAttributeValue(MetadataSupport.severity_qname).equals("Error")) {
                has_errors = true;
            } else {
                has_warnings = true;
            }
        }
    }

    public boolean hasContent() {
        return this.has_errors || this.has_warnings;
    }

    public void addError(String context, String code, String location) {
        if (context == null) {
            context = "";
        }
        if (code == null) {
            code = "";
        }
        if (location == null) {
            location = "";
        }
        OMElement error = MetadataSupport.om_factory.createOMElement("RegistryError", ebRSns);
        error.addAttribute("codeContext", context, null);
        error.addAttribute("errorCode", code, null);
        error.addAttribute("location", location, null);
        error.addAttribute("severity", "Error", null);
        registryErrorList().addChild(error);
        this.has_errors = true;
    }

    public void delError(String context) {
        OMElement errs = registryErrorList();

        for (Iterator<OMElement> it = errs.getChildElements(); it.hasNext();) {
            OMElement e = it.next();
            if (context != null) {
                String ctx = e.getAttributeValue(codeContextQName);
                if (ctx != null && ctx.indexOf(context) != -1) {
                    e.detach();
                }
                continue;
            }
        }
    }

    public void addWarning(String context, String code, String location) {
        if (context == null) {
            context = "";
        }
        if (code == null) {
            code = "";
        }
        if (location == null) {
            location = "";
        }
        OMElement error = MetadataSupport.om_factory.createOMElement("RegistryError", ebRSns);
        error.addAttribute("codeContext", context, null);
        error.addAttribute("errorCode", code, null);
        error.addAttribute("location", location, null);
        error.addAttribute("severity", "Warning", null);
        registryErrorList().addChild(error);
        this.has_warnings = true;
    }

    public void error(String msg) {
        if (verbose) {
            System.out.println("ERROR: " + msg);
        }
    }

    public void warning(String msg) {
        if (verbose) {
            System.out.println("WARNING: " + msg);
        }
    }
}
