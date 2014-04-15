
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "listAddresss", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listAddresss", namespace = "http://nhind.org/config", propOrder = {
    "lastEmailAddress",
    "maxResults"
})
public class ListAddresss {

    @XmlElement(name = "lastEmailAddress", namespace = "")
    private String lastEmailAddress;
    @XmlElement(name = "maxResults", namespace = "")
    private int maxResults;

    /**
     * 
     * @return
     *     returns String
     */
    public String getLastEmailAddress() {
        return this.lastEmailAddress;
    }

    /**
     * 
     * @param lastEmailAddress
     *     the value for the lastEmailAddress property
     */
    public void setLastEmailAddress(String lastEmailAddress) {
        this.lastEmailAddress = lastEmailAddress;
    }

    /**
     * 
     * @return
     *     returns int
     */
    public int getMaxResults() {
        return this.maxResults;
    }

    /**
     * 
     * @param maxResults
     *     the value for the maxResults property
     */
    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

}
