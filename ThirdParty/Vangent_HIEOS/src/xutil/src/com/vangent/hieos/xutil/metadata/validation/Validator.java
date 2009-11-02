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

import com.vangent.hieos.xutil.metadata.structure.Structure;
import com.vangent.hieos.xutil.exception.MetadataException;
import com.vangent.hieos.xutil.exception.MetadataValidationException;
import com.vangent.hieos.xutil.exception.XdsException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.metadata.structure.Metadata;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.response.RegistryErrorList;
import com.vangent.hieos.xutil.xlog.client.XLogMessage;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;

public class Validator {
	RegistryErrorList rel;
	Metadata m;
	boolean is_submit;
	boolean is_xdsb;
	Structure s;
	Attribute a;
	CodeValidation cv;
	PatientId pid;
	UniqueId uid;
	ArrayList<String> assigning_authorities;
	XLogMessage log_message;

	public Validator(Metadata m, RegistryErrorList rel, boolean is_submit, boolean is_xdsb, XLogMessage log_message) throws XdsException {
		this.rel = rel;
		this.m = m;
		this.is_submit = is_submit;
		this.is_xdsb = is_xdsb;
		this.log_message = log_message;

		s = new Structure(m, is_submit, rel, log_message);
		a = new Attribute(m, is_submit, is_xdsb, rel);
		try {
			cv = new CodeValidation(m, is_submit, is_xdsb, rel);
		}
		catch (XdsInternalException e) {
			rel.add_error(MetadataSupport.XDSRegistryError, e.getMessage(), this.getClass().getName(), null);
			throw new XdsInternalException(e.getLocalizedMessage(), e);
		}
		assigning_authorities = cv.getAssigningAuthorities();

		pid = new PatientId(m, rel, is_submit, is_xdsb);
		uid = new UniqueId(m, rel);
	}
	
	/* Commented out (BHT) -- not used.
     public ArrayList<String> getAssigningAuthority() {
		return this.assigning_authorities;
	}*/


	public void run() throws XdsInternalException, MetadataValidationException, XdsException {

		//System.out.println("Metadata Validator");

		try {
			s.run();

			a.run();

			cv.run();
		}
		catch (XdsInternalException e) {
			rel.add_error(MetadataSupport.XDSRegistryError, e.getMessage(), this.getClass().getName(), null);
		}
		catch (MetadataException e) {
			rel.add_error(MetadataSupport.XDSRegistryError, e.getMessage(), this.getClass().getName(), null);
		}

		pid.run();

		for (OMElement ele : m.getRegistryPackages()) 
			validate_internal_classifications(ele);
		for (OMElement ele : m.getExtrinsicObjects()) 
			validate_internal_classifications(ele);

		uid.run();

		rel.getRegistryErrorList(); // forces output of validation report
		//System.out.println("Metadata Validator Done");
	}


	// internal classifications must point to object that contains them

	void validate_internal_classifications(OMElement e) throws MetadataValidationException, MetadataException {
		String e_id = e.getAttributeValue(MetadataSupport.id_qname);
		if (e_id == null || e_id.equals(""))
			return;
		for (Iterator it=e.getChildElements(); it.hasNext(); ) {
			OMElement child = (OMElement) it.next();
			OMAttribute classified_object_att = child.getAttribute(MetadataSupport.classified_object_qname);
			if (classified_object_att != null) {
				String value = classified_object_att.getAttributeValue();
				if ( !e_id.equals(value)) {
					throw new MetadataValidationException("Classification " + m.getIdentifyingString(child) + 
							"\n   is nested inside " + m.getIdentifyingString(e) +
							"\n   but classifies object " + m.getIdentifyingString(value));
				}
			}
		}
	}

	void val(String topic, String msg ) {
		if (msg == null) msg = "Ok";
		rel.add_validation(topic, msg, "Validator.java");
	}



	void err(String msg) {
		rel.add_error(MetadataSupport.XDSRegistryMetadataError, msg, this.getClass().getName(), null);
	}
}
