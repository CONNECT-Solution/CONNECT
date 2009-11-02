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

import com.vangent.hieos.xutil.exception.XdsInternalException;

import java.util.ArrayList;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;

abstract public class Translate extends MetadataSupport {

	abstract public OMElement translate(OMElement ro2, boolean must_dup) throws XdsInternalException;
	abstract OMElement deep_copy(OMElement from, OMNamespace new_namespace);

	public ArrayList<OMElement> translate(ArrayList<OMElement> in, boolean must_dup) throws XdsInternalException {
		ArrayList<OMElement> out = new ArrayList<OMElement>();
		for (int i=0; i<in.size(); i++) {
			OMElement e = (OMElement) in.get(i);
			OMElement output = translate(e, must_dup);
			if (output != null)
				out.add(output);	
		}
		return out;
	}

}
