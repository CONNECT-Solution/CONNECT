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

package com.vangent.hieos.xtest.framework;

import java.util.ArrayList;

public class StringSub {
	ArrayList<String> from;
	ArrayList<String> to;
	StringBuffer buf;

	public StringSub(String content) {
		setString(content);
		from = new ArrayList<String>();
		to = new ArrayList<String>();
	}

	public StringSub() {
		from = new ArrayList<String>();
		to = new ArrayList<String>();
	}

	public void setString(String content) {
		buf = new StringBuffer(content);
	}

	public void addSub(String from, String to) {
		this.from.add(from);
		this.to.add(to);
	}

	private void doSub() {
		for (int i=0; i<from.size(); i++) {
			String from = this.from.get(i);
			String to = this.to.get(i);

			int cnt=10000;
			while(true) {

				int idx = buf.indexOf(from);
				
				if (idx == -1) 
					break;
				
				int len = from.length();

				buf.replace(idx, idx+len, to);
				
				cnt--;
				if (cnt<=0) 
					break;   // safety valve
			}

		}
	}

	public String toString() {
		doSub();
		return buf.toString();
	}
}
