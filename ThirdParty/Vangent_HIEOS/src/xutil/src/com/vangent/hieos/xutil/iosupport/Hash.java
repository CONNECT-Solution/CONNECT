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

package com.vangent.hieos.xutil.iosupport;

import com.vangent.hieos.xutil.exception.XdsInternalException;

public class Hash {
	public String compute_hash(ByteBuffer buffer) throws XdsInternalException {
		Sha1Bean sha = new Sha1Bean();
		sha.setByteStream(buffer.get());
		String hash = null;
		try {
			hash = sha.getSha1String();
		}
		catch (Exception e) {
			XdsInternalException ne = new XdsInternalException(e.getMessage());
			ne.setStackTrace(e.getStackTrace());
			throw ne;
		}
		return hash;
	}
	
	public String compute_hash(String doc)  throws XdsInternalException {
		return compute_hash(doc.getBytes());
	}

	public String compute_hash(byte[] bytes)  throws XdsInternalException {
		ByteBuffer b = new ByteBuffer();
		b.append(bytes, 0, bytes.length);
		return compute_hash(b);
	}
}
