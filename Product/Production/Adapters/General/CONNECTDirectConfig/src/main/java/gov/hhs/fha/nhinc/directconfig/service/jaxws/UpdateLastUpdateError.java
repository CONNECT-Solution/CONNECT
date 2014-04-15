
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import java.util.Calendar;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.nhindirect.config.store.BundleRefreshError;

@XmlRootElement(name = "updateLastUpdateError", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateLastUpdateError", namespace = "http://nhind.org/config", propOrder = {
    "trustBundleId",
    "attemptTime",
    "error"
})
public class UpdateLastUpdateError {

    @XmlElement(name = "trustBundleId", namespace = "")
    private long trustBundleId;
    @XmlElement(name = "attemptTime", namespace = "")
    private Calendar attemptTime;
    @XmlElement(name = "error", namespace = "")
    private BundleRefreshError error;

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
     *     returns BundleRefreshError
     */
    public BundleRefreshError getError() {
        return this.error;
    }

    /**
     * 
     * @param error
     *     the value for the error property
     */
    public void setError(BundleRefreshError error) {
        this.error = error;
    }

}
