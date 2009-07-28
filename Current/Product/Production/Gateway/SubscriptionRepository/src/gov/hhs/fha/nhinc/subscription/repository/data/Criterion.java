package gov.hhs.fha.nhinc.subscription.repository.data;

import java.io.Serializable;

/**
 * Single criteria value. A criterion may be repeated which prevents the use
 * of a map object for storage.
 * 
 * @author Neil Webb
 */
public class Criterion implements Serializable
{
    private static final long serialVersionUID = 4222108360038714991L;
    private String key;
    private String value;

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
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
        final Criterion other = (Criterion) obj;
        if (this.key != other.key && (this.key == null || !this.key.equals(other.key)))
        {
            return false;
        }
        if (this.value != other.value && (this.value == null || !this.value.equals(other.value)))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 67 * hash + (this.key != null ? this.key.hashCode() : 0);
        hash = 67 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }
}
