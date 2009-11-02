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

package com.vangent.hieos.xutil.query;

import com.vangent.hieos.xutil.metadata.structure.Metadata;
import com.vangent.hieos.xutil.exception.MetadataException;

import org.apache.axiom.om.OMElement;
import org.apache.log4j.Logger;

public class BasicQuery {
	protected final static Logger logger = Logger.getLogger(BasicQuery.class);

	public void secure_URI(Metadata metadata) throws MetadataException {
		for (OMElement doc : metadata.getExtrinsicObjects()) {
			int updated = 0;
			for (int sl=0; sl<10; sl++) {
				String uri_value = metadata.getSlotValue(doc, "URI", sl);
				if (uri_value == null) break;
				boolean save = false;
				if (uri_value.indexOf("http:") != -1) {
					updated++;
					save = true;
					uri_value = uri_value.replaceAll("http", "https");
				}
				if (uri_value.indexOf("8080") != -1) {
					updated++;
					save = true;
					uri_value = uri_value.replaceAll("8080", "8181");
				}
				if (save) {
					metadata.setSlotValue(doc, "URI", sl, uri_value);
				}
				if (updated >= 2) break;
			}
		}
	}

}
