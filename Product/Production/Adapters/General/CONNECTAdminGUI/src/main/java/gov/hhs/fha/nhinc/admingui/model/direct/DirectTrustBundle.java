/**
 * Copyright (c) 2009-2014, United States Government, as represented by the Secretary of Health and Human Services. All
 * rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met: Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or other materials provided with the
 * distribution. Neither the name of the United States Government nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*
 */
package gov.hhs.fha.nhinc.admingui.model.direct;

/**
 *
 * @author jasonasmith
 */
public class DirectTrustBundle {
    
    private int position;
    private String tbName;
    private String tbUrl;
    private String tbCheckSum;
    private String tbCreated;
    private String tbCurrentAsOf;
    private String tbLastRefresh;
    private String tbRefreshInterval;

    public DirectTrustBundle(){
        
    }

    public DirectTrustBundle(int position, String tbName, String tbUrl, String tbCheckSum, String tbCreated, String tbCurrentAsOf, String tbLastRefresh, String tbRefreshInterval) {
        this.position = position;
        this.tbName = tbName;
        this.tbUrl = tbUrl;
        this.tbCheckSum = tbCheckSum;
        this.tbCreated = tbCreated;
        this.tbCurrentAsOf = tbCurrentAsOf;
        this.tbLastRefresh = tbLastRefresh;
        this.tbRefreshInterval = tbRefreshInterval;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
    
    public String getTbName() {
        return tbName;
    }

    public void setTbName(String tbName) {
        this.tbName = tbName;
    }

    public String getTbUrl() {
        return tbUrl;
    }

    public void setTbUrl(String tbUrl) {
        this.tbUrl = tbUrl;
    }

    public String getTbCheckSum() {
        return tbCheckSum;
    }

    public void setTbCheckSum(String tbCheckSum) {
        this.tbCheckSum = tbCheckSum;
    }

    public String getTbCreated() {
        return tbCreated;
    }

    public void setTbCreated(String tbCreated) {
        this.tbCreated = tbCreated;
    }

    public String getTbCurrentAsOf() {
        return tbCurrentAsOf;
    }

    public void setTbCurrentAsOf(String tbCurrentAsOf) {
        this.tbCurrentAsOf = tbCurrentAsOf;
    }

    public String getTbLastRefresh() {
        return tbLastRefresh;
    }

    public void setTbLastRefresh(String tbLastRefresh) {
        this.tbLastRefresh = tbLastRefresh;
    }

    public String getTbRefreshInterval() {
        return tbRefreshInterval;
    }

    public void setTbRefreshInterval(String tbRefreshInterval) {
        this.tbRefreshInterval = tbRefreshInterval;
    }
    
    
}
