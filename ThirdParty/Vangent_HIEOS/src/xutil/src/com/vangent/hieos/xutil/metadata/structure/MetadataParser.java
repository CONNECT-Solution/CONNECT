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

package com.vangent.hieos.xutil.metadata.structure;

import com.vangent.hieos.xutil.exception.MetadataException;
import com.vangent.hieos.xutil.exception.MetadataValidationException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.xml.Util;

import java.io.File;

import org.apache.axiom.om.OMElement;

public class MetadataParser {

	public MetadataParser() {
	}

	static public Metadata parseNonSubmission(OMElement e) throws MetadataException, MetadataValidationException {
		Metadata m = new Metadata();

		m.setGrokMetadata(false);

		if (e != null) {
			m.setMetadata(e);

			m.runParser();
		}

		return m;
	}


	static public Metadata parseNonSubmission(File metadata_file) throws MetadataException, MetadataValidationException, XdsInternalException {

		return parseNonSubmission(Util.parse_xml(metadata_file));

	}
	
	static public Metadata noParse(OMElement e) {
		Metadata m = new Metadata();

		m.setGrokMetadata(false);

		if (e != null) {
			m.setMetadata(e);

		}
		return m;
	}

	static public Metadata noParse(File metadata_file) throws MetadataException,XdsInternalException  {
		return noParse(Util.parse_xml(metadata_file));
	}
	
	static public Metadata parse(OMElement e)  throws MetadataException,XdsInternalException, MetadataValidationException {
		return new Metadata(e);
	}
}
