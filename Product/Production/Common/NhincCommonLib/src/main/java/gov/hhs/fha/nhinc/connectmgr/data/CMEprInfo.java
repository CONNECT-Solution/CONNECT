package gov.hhs.fha.nhinc.connectmgr.data;

/**
 * This class represents Endpoint Reference (EPR) Information 
 * 
 * @author Les Westberg
 */
public class CMEprInfo
{
    // Member variables
    //------------------
    private String uniformServiceName = "";
    private String namespacePrefix = "";
    private String namespaceURI = "";
    private String portName = "";
    private String serviceName = "";

    /**
     * Returns the uniform service name for this EPR.  This is the key by which
     * this service is known in the UDDI.
     * 
     * @return The uniform service name for this EPR.
     */
    public String getUniformServiceName()
    {
        return uniformServiceName;
    }

    /**
     * Sets the uniform service name for this EPR.  This is the key by which
     * this service is known in the UDDI.
     * 
     * @param uniformServiceName The uniform service name for this EPR.
     * 
     */
    public void setUniformServiceName(String uniformServiceName)
    {
        this.uniformServiceName = uniformServiceName;
    }

    
    /**
     * Return the namespace prefix.
     * 
     * @return The namespace prefix.
     */
    public String getNamespacePrefix()
    {
        return namespacePrefix;
    }

    /**
     * Set the namespace prefix.
     * 
     * @param namespacePrefix The namespace prefix.
     */
    public void setNamespacePrefix(String namespacePrefix)
    {
        this.namespacePrefix = namespacePrefix;
    }

    /**
     * Returns the namespace URI for the endpoint.
     * 
     * @return Returns the namespace URI for the endpoint.
     */
    public String getNamespaceURI()
    {
        return namespaceURI;
    }

    /**
     * Sets the namespace URI for the endpoint.
     * 
     * @param namespaceURI The namespace URI for the endpoint.
     */
    public void setNamespaceURI(String namespaceURI)
    {
        this.namespaceURI = namespaceURI;
    }

    /**
     * Returns the port name for the endpoint.
     * 
     * @return The port name for this endpoint.
     */
    public String getPortName()
    {
        return portName;
    }

    /**
     * Sets the port name for the endpoint.
     * 
     * @param portName The port name for the endpoint.
     */
    public void setPortName(String portName)
    {
        this.portName = portName;
    }

    /**
     * Returns the service name for the endpoint.
     * 
     * @return The service name for the endpoint.
     */
    public String getServiceName()
    {
        return serviceName;
    }

    /**
     * Sets the service name for the endpoint.
     * 
     * @param serviceName The service name for the endpoint.
     */
    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }
    
    
}
