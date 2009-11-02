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

package com.vangent.hieos.xutil.client;

import com.vangent.hieos.xutil.iosupport.Sha1Bean;

public class RetInfo {

	protected String doc_uid;
	protected String rep_uid;
	
	private byte[] contents;
	protected String content_type;
	protected String hash;
	protected String  home;
	protected int size;
	
	protected StringBuffer errors;

	public RetInfo() { doc_uid = ""; rep_uid = ""; content_type = ""; hash = null; size=-1; contents = null; errors = new StringBuffer();}

	public String toString() {
		return "RetInfo:\ndoc_uid=" + doc_uid + 
		"\nrep_uid=" + rep_uid + 
		"\ncontents has " + ((contents != null) ? contents.length : "(no contents)") + 
		" bytes\nhash=" + hash + 
		"\nsize=" + size  + 
		"\nerrors=" + errors.toString() + 
		"\n";
	}
	
	public String getHome() {
		return home;
	}
	
	public void setHome(String home) {
		this.home = home;
	}

	public String getDoc_uid() {
		return doc_uid;
	}

	public void setDoc_uid(String doc_uid) {
		this.doc_uid = doc_uid;
	}

	public String getRep_uid() {
		return rep_uid;
	}

	public void setRep_uid(String rep_uid) {
		this.rep_uid = rep_uid;
	}

	public String getContent_type() {
		return content_type;
	}

	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	public void setSize(String size) {
		this.size = Integer.parseInt(size);
	}

	private String sha1(byte[] buf) throws Exception {
		Sha1Bean sb = new Sha1Bean();
		sb.setByteStream(buf);
		return sb.getSha1String();
	}

	public void setContents(byte[] data) throws Exception { 
		this.contents = data; 
		this.hash = this.sha1(data); 
		this.size = data.length; 
	}

	public byte[] getContents() { return contents; }

	public String getErrors() { return errors.toString(); }

	public void addError(String msg) { errors.append(msg + "\n"); }

}
