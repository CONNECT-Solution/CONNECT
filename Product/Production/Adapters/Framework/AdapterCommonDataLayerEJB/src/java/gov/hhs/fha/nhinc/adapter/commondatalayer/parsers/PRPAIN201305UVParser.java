/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adapter.commondatalayer.parsers;

import javax.xml.bind.JAXBElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.CE;
import org.hl7.v3.ENExplicit;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.PRPAIN201305UV02MCCIMT000100UV01Message;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectAdministrativeGender;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectBirthTime;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectName;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;

/**
 *
 * @author kim
 */
public class PRPAIN201305UVParser {

   PRPAIN201305UV02MCCIMT000100UV01Message message = null;
   private static Log logger = LogFactory.getLog(PRPAIN201305UVParser.class);

   public PRPAIN201305UVParser(PRPAIN201305UV02MCCIMT000100UV01Message msg) {
      message = msg;
   }

   public String getDateOfBirth() {
      String dateOfBirth = null;
      PRPAMT201306UV02ParameterList paramList = getParamList();
      if (paramList != null && paramList.getLivingSubjectBirthTime().size() > 0) {
         PRPAMT201306UV02LivingSubjectBirthTime birthTime = paramList.getLivingSubjectBirthTime().get(0);
         if (birthTime.getValue().size() > 0) {
            IVLTSExplicit tsBirthTime = birthTime.getValue().get(0);
            if (tsBirthTime != null) {
               dateOfBirth = tsBirthTime.getValue();
            }
         }
      }

      return dateOfBirth;
   }

   public String getSubjectLastName() {
      PRPAMT201306UV02ParameterList paramList = getParamList();
      if (paramList != null && paramList.getLivingSubjectName().size() > 0) {
         PRPAMT201306UV02LivingSubjectName subjectName = paramList.getLivingSubjectName().get(0);
         if (subjectName.getValue().size() > 0) {
            ENExplicit name = subjectName.getValue().get(0);
            return getName(name, "org.hl7.v3.EnExplicitFamily");
         }
      }

      return null;
   }

   public String getSubjectFirstName() {
      PRPAMT201306UV02ParameterList paramList = getParamList();
      if (paramList != null && paramList.getLivingSubjectName().size() > 0) {
         PRPAMT201306UV02LivingSubjectName subjectName = paramList.getLivingSubjectName().get(0);
         if (subjectName.getValue().size() > 0) {
            ENExplicit name = subjectName.getValue().get(0);
            return getName(name, "org.hl7.v3.EnExplicitGiven");
         }
      }

      return null;
   }

   public String getSubjectGender() {
      PRPAMT201306UV02ParameterList paramList = getParamList();
      if (paramList != null && paramList.getLivingSubjectAdministrativeGender().size() > 0) {
         PRPAMT201306UV02LivingSubjectAdministrativeGender genderValue = paramList.getLivingSubjectAdministrativeGender().get(0);
         if (genderValue.getValue().size() > 0) {
            CE genderCode = genderValue.getValue().get(0);
            if (genderCode != null) {
               return genderCode.getCode();
            }
         }
      }

      return null;
   }

   private String getName(ENExplicit name, String className) {
      String retName = "";

      logger.debug("**********parse name of type className=" + className);
      if (name != null) {
         for (int i = 0; i < name.getContent().size(); i++) {
            JAXBElement o = (JAXBElement) name.getContent().get(i);
            if (o != null && o.getValue() != null) {
               logger.debug("**********object has className=" + o.getValue().getClass().getName());
               if (className.equals("org.hl7.v3.EnExplicitFamily") &&
                   o.getValue().getClass().getName().equals(className)) {
                  EnExplicitFamily ob = (EnExplicitFamily) o.getValue();
                  retName = ob.getContent();
                  break;
               } else if (className.equals("org.hl7.v3.EnExplicitGiven") &&
                          o.getValue().getClass().getName().equals(className)) {
                  EnExplicitGiven ob = (EnExplicitGiven) o.getValue();
                  retName = ob.getContent();
                  break;
               }
            }
         }
      }

      return retName;
   }

   private PRPAMT201306UV02ParameterList getParamList() {
      if (message.getControlActProcess() != null) {
         PRPAMT201306UV02QueryByParameter queryParams = message.getControlActProcess().getQueryByParameter().getValue();
         if (queryParams != null) {
            return queryParams.getParameterList();
         }
      }

      return null;
   }
}
