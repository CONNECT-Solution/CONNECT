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

import com.vangent.hieos.xutil.response.RegistryErrorList;
import com.vangent.hieos.xutil.metadata.structure.Metadata;
import com.vangent.hieos.xutil.exception.SchemaValidationException;
import com.vangent.hieos.xutil.exception.XdsException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.metadata.validation.Validator;
import com.vangent.hieos.xutil.xml.SchemaValidation;
import com.vangent.hieos.xutil.xlog.client.XLogMessage;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.axiom.om.OMElement;


public class RegistryUtility {

	static public void schema_validate_local(OMElement ahqr, int metadata_type)
	throws XdsInternalException, SchemaValidationException {
		String schema_messages = null;
		try {
			schema_messages = SchemaValidation.validate_local(ahqr, metadata_type);
		} catch (Exception e) {
			throw new XdsInternalException("Schema Validation threw internal error: " + e.getMessage());
		}
		if (schema_messages != null && schema_messages.length() > 0)
			throw new SchemaValidationException("Input did not validate against schema:" + schema_messages);
	}

	static public RegistryErrorList metadata_validator(Metadata m, boolean is_submit) throws XdsException {
		RegistryErrorList rel = new RegistryErrorList((m.isVersion2() ? RegistryErrorList.version_2 : RegistryErrorList.version_3));
		Validator v = new Validator(m, rel, is_submit, !m.isVersion2(), (XLogMessage)null);
		v.run();
		return rel;
	}

   
	public static String exception_details(Exception e) {
        /*
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		e.printStackTrace(ps); */
        e.printStackTrace();
		return "Exception thrown: " + e.getClass().getName() + " : " + e.getMessage();
		//return "Exception thrown: " + e.getClass().getName() + "\n" + e.getMessage() + "\n" + new String(baos.toByteArray());
    }

    
	public static String exception_trace(Exception e) {
		if (e == null) 
			return "";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		StackTraceElement ste[] = e.getStackTrace();
		ps.print("\n");
		for (int i=0; i<ste.length && i<15; i++)
			ps.print("\t" + ste[i].toString() + "\n");
		//e.printStackTrace(ps);

		return new String(baos.toByteArray());
	}

}
