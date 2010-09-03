/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.subscription.repository.data;

import java.io.Serializable;

/**
 * Data class for a single reference parameter
 * 
 * @author Neil Webb
 */
public class ReferenceParameter implements Serializable
{
    private static final long serialVersionUID = -5508110987527377957L;
    private String namespace;
    private String namespacePrefix;
    private String elementName;
    private String value;

    public String getElementName()
    {
        return elementName;
    }

    public void setElementName(String elementName)
    {
        this.elementName = elementName;
    }

    public String getNamespace()
    {
        return namespace;
    }

    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }

    public String getNamespacePrefix()
    {
        return namespacePrefix;
    }

    public void setNamespacePrefix(String namespacePrefix)
    {
        this.namespacePrefix = namespacePrefix;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final ReferenceParameter other = (ReferenceParameter) obj;
        if (this.namespace != other.namespace && (this.namespace == null || !this.namespace.equals(other.namespace)))
        {
            System.out.println("Reference parameter namespace did not equal");
            System.out.println("This reference parameter namespace: " + this.namespace);
            System.out.println("Other reference parameter namespace: " + other.namespace);
            return false;
        }
        // Namespace prefix is not guaranteed to be the same
//        if (this.namespacePrefix != other.namespacePrefix && (this.namespacePrefix == null || !this.namespacePrefix.equals(other.namespacePrefix)))
//        {
//            return false;
//        }
        if (this.elementName != other.elementName && (this.elementName == null || !this.elementName.equals(other.elementName)))
        {
            System.out.println("Reference parameter element name did not equal");
            System.out.println("This reference parameter element name: " + this.elementName);
            System.out.println("Other reference parameter element name: " + other.elementName);
            return false;
        }
        if (this.value != other.value && (this.value == null || !this.value.equals(other.value)))
        {
            System.out.println("Reference parameter value did not equal");
            System.out.println("This reference parameter value: " + this.value);
            System.out.println("Other reference parameter value: " + other.value);
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 17 * hash + (this.namespace != null ? this.namespace.hashCode() : 0);
        hash = 17 * hash + (this.namespacePrefix != null ? this.namespacePrefix.hashCode() : 0);
        hash = 17 * hash + (this.elementName != null ? this.elementName.hashCode() : 0);
        hash = 17 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }
}
