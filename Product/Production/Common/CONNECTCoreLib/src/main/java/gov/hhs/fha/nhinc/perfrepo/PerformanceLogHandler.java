/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The CONNECTCoreLib
 * Software was developed in conjunction with the National Cancer Institute
 * (NCI) by NCI employees and 5AM Solutions, Inc. (5AM). To the extent
 * government employees are authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 *
 * This CONNECTCoreLib Software License (the License) is between NCI and You. You (or
 * Your) shall mean a person or an entity, and all other entities that control,
 * are controlled by, or are under common control with the entity. Control for
 * purposes of this definition means (i) the direct or indirect power to cause
 * the direction or management of such entity, whether by contract or otherwise,
 * or (ii) ownership of fifty percent (50%) or more of the outstanding shares,
 * or (iii) beneficial ownership of such entity.
 *
 * This License is granted provided that You agree to the conditions described
 * below. NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up,
 * no-charge, irrevocable, transferable and royalty-free right and license in
 * its rights in the CONNECTCoreLib Software to (i) use, install, access, operate,
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the CONNECTCoreLib Software; (ii) distribute and
 * have distributed to and by third parties the CONNECTCoreLib Software and any
 * modifications and derivative works thereof; and (iii) sublicense the
 * foregoing rights set out in (i) and (ii) to third parties, including the
 * right to license such rights to further third parties. For sake of clarity,
 * and not by way of limitation, NCI shall have no right of accounting or right
 * of payment from You or Your sub-licensees for the rights granted under this
 * License. This License is granted at no charge to You.
 *
 * Your redistributions of the source code for the Software must retain the
 * above copyright notice, this list of conditions and the disclaimer and
 * limitation of liability of Article 6, below. Your redistributions in object
 * code form must reproduce the above copyright notice, this list of conditions
 * and the disclaimer of Article 6 in the documentation and/or other materials
 * provided with the distribution, if any.
 *
 * Your end-user documentation included with the redistribution, if any, must
 * include the following acknowledgment: This product includes software
 * developed by 5AM and the National Cancer Institute. If You do not include
 * such end-user documentation, You shall include this acknowledgment in the
 * Software itself, wherever such third-party acknowledgments normally appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", or "5AM"
 * to endorse or promote products derived from this Software. This License does
 * not authorize You to use any trademarks, service marks, trade names, logos or
 * product names of either NCI or 5AM, except as required to comply with the
 * terms of this License.
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this
 * Software into Your proprietary programs and into any third party proprietary
 * programs. However, if You incorporate the Software into third party
 * proprietary programs, You agree that You are solely responsible for obtaining
 * any permission from such third parties required to incorporate the Software
 * into such third party proprietary programs and for informing Your
 * sub-licensees, including without limitation Your end-users, of their
 * obligation to secure any required permissions from such third parties before
 * incorporating the Software into such third party proprietary software
 * programs. In the event that You fail to obtain such permissions, You agree
 * to indemnify NCI for any claims against NCI by such third parties, except to
 * the extent prohibited by law, resulting from Your failure to obtain such
 * permissions.
 *
 * For sake of clarity, and not by way of limitation, You may add Your own
 * copyright statement to Your modifications and to the derivative works, and
 * You may provide additional or different license terms and conditions in Your
 * sublicenses of modifications of the Software, or any derivative works of the
 * Software as a whole, provided Your use, reproduction, and distribution of the
 * Work otherwise complies with the conditions stated in this License.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC. OR THEIR
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.perfrepo;

import java.util.Set;

import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;

/**
 * @author zmelnick
 *
 */
public class PerformanceLogHandler implements SOAPHandler<SOAPMessageContext> {

    @Override
    public void close(MessageContext message) {
    }

    @Override
    public Set getHeaders() {
        return null;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext message) {
        if (isOutboundMessage(message) == true) {
            return handleRequest(message);
        } else {
            return handleResponse(message);
        }
    }

    private boolean handleResponse(SOAPMessageContext message) {
        PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(message.get(MessageContext.WSDL_SERVICE).toString(),
                getMessageType(message), NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                HomeCommunityMap.getLocalHomeCommunityId());

        return true;
    }

    private boolean handleRequest(SOAPMessageContext message) {
        PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(message.get(MessageContext.WSDL_SERVICE).toString(),
                getMessageType(message), NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                HomeCommunityMap.getLocalHomeCommunityId());
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext message) {
        return false;
    }

    private boolean isOutboundMessage(SOAPMessageContext message) {
        return (Boolean) message.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY);
    }

    private String getMessageType(SOAPMessageContext message) {
        String messagetype = message.get(SOAPMessageContext.WSDL_INTERFACE).toString();

        if (messagetype.contains(NhincConstants.AUDIT_LOG_ENTITY_INTERFACE)) {
            return NhincConstants.AUDIT_LOG_ENTITY_INTERFACE;
        } else if (messagetype.contains(NhincConstants.AUDIT_LOG_NHIN_INTERFACE)) {
            return NhincConstants.AUDIT_LOG_NHIN_INTERFACE;
        } else if (messagetype.contains(NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE)) {
            return NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE;
        }
        return null;

    }

}
