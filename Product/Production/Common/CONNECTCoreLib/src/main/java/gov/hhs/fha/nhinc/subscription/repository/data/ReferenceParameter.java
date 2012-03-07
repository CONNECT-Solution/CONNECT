/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package gov.hhs.fha.nhinc.subscription.repository.data;

import java.io.Serializable;

/**
 * Data class for a single reference parameter
 * 
 * @author Neil Webb
 */
public class ReferenceParameter implements Serializable {
    private static final long serialVersionUID = -5508110987527377957L;
    private String namespace;
    private String namespacePrefix;
    private String elementName;
    private String value;

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespacePrefix() {
        return namespacePrefix;
    }

    public void setNamespacePrefix(String namespacePrefix) {
        this.namespacePrefix = namespacePrefix;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ReferenceParameter other = (ReferenceParameter) obj;
        if (this.namespace != other.namespace && (this.namespace == null || !this.namespace.equals(other.namespace))) {
            System.out.println("Reference parameter namespace did not equal");
            System.out.println("This reference parameter namespace: " + this.namespace);
            System.out.println("Other reference parameter namespace: " + other.namespace);
            return false;
        }
        // Namespace prefix is not guaranteed to be the same
        // if (this.namespacePrefix != other.namespacePrefix && (this.namespacePrefix == null ||
        // !this.namespacePrefix.equals(other.namespacePrefix)))
        // {
        // return false;
        // }
        if (this.elementName != other.elementName
                && (this.elementName == null || !this.elementName.equals(other.elementName))) {
            System.out.println("Reference parameter element name did not equal");
            System.out.println("This reference parameter element name: " + this.elementName);
            System.out.println("Other reference parameter element name: " + other.elementName);
            return false;
        }
        if (this.value != other.value && (this.value == null || !this.value.equals(other.value))) {
            System.out.println("Reference parameter value did not equal");
            System.out.println("This reference parameter value: " + this.value);
            System.out.println("Other reference parameter value: " + other.value);
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.namespace != null ? this.namespace.hashCode() : 0);
        hash = 17 * hash + (this.namespacePrefix != null ? this.namespacePrefix.hashCode() : 0);
        hash = 17 * hash + (this.elementName != null ? this.elementName.hashCode() : 0);
        hash = 17 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }
}
