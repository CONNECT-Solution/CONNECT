/*
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 *
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 *
 * All other rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.assemblymanager.builder.cda.modules;

//doc assembly
import gov.hhs.fha.nhinc.assemblymanager.builder.DocumentBuilder;
import gov.hhs.fha.nhinc.assemblymanager.builder.DocumentBuilderException;

//logging
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//hl7
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.IVXBTSExplicit;
import org.hl7.v3.POCDMT000040DocumentationOf;
import org.hl7.v3.POCDMT000040ServiceEvent;

/**
 *
 * @author abarger
 */
public class DocumentationOfModule extends DocumentBuilder {

    private static Log log = LogFactory.getLog(ParticipantModule.class);
    private static String SERVICE_EVENT_CLASS_CODE = "PCPR";
    private static String EFFECTIVE_TIME_NULL_FLAVOR = "UNK";

    public POCDMT000040DocumentationOf build() throws DocumentBuilderException
    {
         //create documentationOf object
         POCDMT000040DocumentationOf documentationOf = objectFactory.createPOCDMT000040DocumentationOf();

         //create effectiveTime
         IVLTSExplicit time = objectFactory.createIVLTSExplicit();

         //Allscripts does not return these values - set the nullFlavor
         IVXBTSExplicit lowVal = new IVXBTSExplicit();
         lowVal.getNullFlavor().add(EFFECTIVE_TIME_NULL_FLAVOR);
   //      lowVal.setValue("20020601");
         time.getContent().add(this.objectFactory.createIVLTSExplicitLow(lowVal));

         IVXBTSExplicit highVal = new IVXBTSExplicit();
         highVal.getNullFlavor().add(EFFECTIVE_TIME_NULL_FLAVOR);
      //   highVal.setValue("20040601");
         time.getContent().add(this.objectFactory.createIVLTSExplicitHigh(highVal));
         
         //create ServiceEvent
         POCDMT000040ServiceEvent serviceEvent = objectFactory.createPOCDMT000040ServiceEvent();
         serviceEvent.getClassCode().add(SERVICE_EVENT_CLASS_CODE);
         serviceEvent.setEffectiveTime(time);
         documentationOf.setServiceEvent(serviceEvent);


        
         return documentationOf;
    }

}
