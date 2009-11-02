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

public class ByteBuffer {
	int chunk_size = 4000;
	byte[] buffer = null;
	int fill=0;

	public ByteBuffer() {

	}

	public ByteBuffer(int size) {
		chunk_size = size;
	}

	public void append(byte[] data, int offset, int size) {
		plan(size);
		for(int i=0; i<size; i++) {
			buffer[fill++] = data[offset+i];
		}
	}
	
	public byte[] get() {
		return buffer;
	}
	
	public int size() {
		return this.fill;
	}

	void plan(int size_to_add) {
		if (buffer == null) {
			buffer = new byte[grow_size(size_to_add)];
			fill = 0;
			return;
		}
		if (size_to_add < available())
			return;
		int new_size = grow_size(size_to_add + buffer.length);
		byte[] new_buffer = new byte[new_size];
		for (int i=0; i<this.fill; i++)
			new_buffer[i] = buffer[i];
		buffer = new_buffer;
	}
	
	int available() {
		int old_size = (buffer == null) ? 0 : buffer.length;
		int available = old_size - this.fill;
		return available;
	}

	int grow_size(int add_size) {
		int old_size = (buffer == null) ? 0 : buffer.length;
		if (add_size >= available()) 
			return (add_size + old_size) + chunk_size;
		return 0;
	}

	int max(int a, int b) { return (a < b) ? b : a; }


}
