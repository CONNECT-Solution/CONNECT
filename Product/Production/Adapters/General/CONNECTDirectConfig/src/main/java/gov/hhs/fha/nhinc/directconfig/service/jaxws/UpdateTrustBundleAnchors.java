
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import java.util.Calendar;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import gov.hhs.fha.nhinc.directconfig.entity.TrustBundleAnchor;

@XmlRootElement(name = "updateTrustBundleAnchors", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateTrustBundleAnchors", namespace = "http://nhind.org/config", propOrder = {
    "trustBundleId",
    "attemptTime",
    "newAnchorSet"
})
public class UpdateTrustBundleAnchors {

    @XmlElement(name = "trustBundleId", namespace = "")
    private long trustBundleId;
    @XmlElement(name = "attemptTime", namespace = "")
    private Calendar attemptTime;
    @XmlElement(name = "newAnchorSet", namespace = "")
    private Collection<TrustBundleAnchor> newAnchorSet;

    /**
     * 
     * @return
     *     returns long
     */
    public long getTrustBundleId() {
        return this.trustBundleId;
    }

    /**
     * 
     * @param trustBundleId
     *     the value for the trustBundleId property
     */
    public void setTrustBundleId(long trustBundleId) {
        this.trustBundleId = trustBundleId;
    }

    /**
     * 
     * @return
     *     returns Calendar
     */
    public Calendar getAttemptTime() {
        return this.attemptTime;
    }

    /**
     * 
     * @param attemptTime
     *     the value for the attemptTime property
     */
    public void setAttemptTime(Calendar attemptTime) {
        this.attemptTime = attemptTime;
    }

    /**
     * 
     * @return
     *     returns Collection<TrustBundleAnchor>
     */
    public Collection<TrustBundleAnchor> getNewAnchorSet() {
        return this.newAnchorSet;
    }

    /**
     * 
     * @param newAnchorSet
     *     the value for the newAnchorSet property
     */
    public void setNewAnchorSet(Collection<TrustBundleAnchor> newAnchorSet) {
        this.newAnchorSet = newAnchorSet;
    }

}
