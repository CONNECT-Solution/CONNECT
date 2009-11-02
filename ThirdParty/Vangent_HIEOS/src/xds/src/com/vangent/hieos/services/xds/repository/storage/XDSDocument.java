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
package com.vangent.hieos.services.xds.repository.storage;

/**
 *
 * @author Bernie Thuman
 */
public class XDSDocument {

    private String repositoryId = null;
    private String uniqueId = null;     // XDSDocumentEntry.uniqueId
    private String documentId = null;
    private String mimeType = null;
    private byte[] bytes = null;
    private String hash = null;
    private int length = -1;

    /**
     * 
     * @param repositoryId
     */
    public XDSDocument(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    /**
     * Get the value of uniqueId
     *
     * @return the value of uniqueId
     */
    public String getUniqueId() {
        return uniqueId;
    }

    /**
     * Set the value of uniqueId
     *
     * @param uniqueId new value of uniqueId
     */
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    /**
     * Get the value of length
     *
     * @return the value of length
     */
    public int getLength() {
        return length;
    }

    /**
     * Set the value of length
     *
     * @param length new value of length
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * Get the value of hash
     *
     * @return the value of hash
     */
    public String getHash() {
        return hash;
    }

    /**
     * Set the value of hash
     *
     * @param hash new value of hash
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * Get the value of bytes
     *
     * @return the value of bytes
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * Set the value of bytes
     *
     * @param bytes new value of bytes
     */
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * Get the value of mimeType
     *
     * @return the value of mimeType
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Set the value of mimeType
     *
     * @param mimeType new value of mimeType
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Get the value of documentId
     *
     * @return the value of documentId
     */
    public String getDocumentId() {
        return documentId;
    }

    /**
     * Set the value of documentId
     *
     * @param documentId new value of documentId
     */
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    /**
     * Get the value of repositoryId
     *
     * @return the value of repositoryId
     */
    public String getRepositoryId() {
        return repositoryId;
    }
}
