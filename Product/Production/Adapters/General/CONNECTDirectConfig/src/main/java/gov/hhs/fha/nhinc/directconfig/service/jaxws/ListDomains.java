
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "listDomains", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listDomains", namespace = "http://nhind.org/config", propOrder = {
    "names",
    "maxResults"
})
public class ListDomains {

    @XmlElement(name = "names", namespace = "")
    private String names;
    @XmlElement(name = "maxResults", namespace = "")
    private int maxResults;

    /**
     * 
     * @return
     *     returns String
     */
    public String getNames() {
        return this.names;
    }

    /**
     * 
     * @param names
     *     the value for the names property
     */
    public void setNames(String names) {
        this.names = names;
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
