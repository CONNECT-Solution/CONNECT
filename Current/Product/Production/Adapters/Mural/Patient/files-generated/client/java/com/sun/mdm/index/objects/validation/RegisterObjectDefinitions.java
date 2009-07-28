package com.sun.mdm.index.objects.validation;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import com.sun.mdm.index.objects.validation.PatternValidator;
import com.sun.mdm.index.objects.validation.ReferenceDescriptor;
import com.sun.mdm.index.objects.validation.ObjectDescriptor;
import com.sun.mdm.index.objects.validation.UserReferenceDescriptor;
import com.sun.mdm.index.objects.validation.UserCodeValidator;
import com.sun.mdm.index.objects.validation.FieldType;
import com.sun.mdm.index.objects.validation.FieldDescriptor;
import com.sun.mdm.index.objects.validation.StringValueValidator;
import com.sun.mdm.index.objects.validation.DateValueValidator;
import com.sun.mdm.index.objects.validation.LongValueValidator;
import com.sun.mdm.index.objects.validation.TimestampValueValidator;
import com.sun.mdm.index.objects.validation.IntegerValueValidator;
import com.sun.mdm.index.objects.validation.FloatValueValidator;

public class RegisterObjectDefinitions {

    public RegisterObjectDefinitions() {
    }

    public ArrayList registerObjects() {

        ArrayList list = new ArrayList();
        ObjectDescriptor od;
        od = new ObjectDescriptor();
        od.setObjectName("Patient");
        try {
            od.addFieldDescriptor(new FieldDescriptor("PatientId", FieldType.STRING, 
                FieldDescriptor.NONE));            od.addValueValidator("PatientId", new StringValueValidator(new Integer(30), null));            od.addFieldDescriptor(new FieldDescriptor("PersonCatCode", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("PersonCatCode", new StringValueValidator(new Integer(8), null));            od.addFieldDescriptor(new FieldDescriptor("FirstName", FieldType.STRING, 
                FieldDescriptor.REQUIRED + FieldDescriptor.UPDATEABLE));            od.addValueValidator("FirstName", new StringValueValidator(new Integer(40), null));            od.addFieldDescriptor(new FieldDescriptor("FirstName_Std", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("FirstName_Std", new StringValueValidator(new Integer(40), null));            od.addFieldDescriptor(new FieldDescriptor("FirstName_Phon", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("FirstName_Phon", new StringValueValidator(new Integer(8), null));            od.addFieldDescriptor(new FieldDescriptor("MiddleName", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("MiddleName", new StringValueValidator(new Integer(30), null));            od.addFieldDescriptor(new FieldDescriptor("LastName", FieldType.STRING, 
                FieldDescriptor.REQUIRED + FieldDescriptor.UPDATEABLE));            od.addValueValidator("LastName", new StringValueValidator(new Integer(40), null));            od.addFieldDescriptor(new FieldDescriptor("LastName_Std", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("LastName_Std", new StringValueValidator(new Integer(40), null));            od.addFieldDescriptor(new FieldDescriptor("LastName_Phon", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("LastName_Phon", new StringValueValidator(new Integer(8), null));            od.addFieldDescriptor(new FieldDescriptor("Suffix", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("Suffix", new StringValueValidator(new Integer(10), null));
            od.addReferenceDescriptor("Suffix", new ReferenceDescriptor("SUFFIX"));            od.addFieldDescriptor(new FieldDescriptor("Title", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("Title", new StringValueValidator(new Integer(8), null));
            od.addReferenceDescriptor("Title", new ReferenceDescriptor("TITLE"));            od.addFieldDescriptor(new FieldDescriptor("SSN", FieldType.STRING, 
                FieldDescriptor.REQUIRED + FieldDescriptor.UPDATEABLE));            od.addValueValidator("SSN", new StringValueValidator(new Integer(16), null));
            od.addPatternValidator("SSN", new PatternValidator("[0-9]{9}"));            od.addFieldDescriptor(new FieldDescriptor("DOB", FieldType.DATE, 
                FieldDescriptor.REQUIRED + FieldDescriptor.UPDATEABLE));            od.addFieldDescriptor(new FieldDescriptor("Death", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("Death", new StringValueValidator(new Integer(1), null));            od.addFieldDescriptor(new FieldDescriptor("Gender", FieldType.STRING, 
                FieldDescriptor.REQUIRED + FieldDescriptor.UPDATEABLE));            od.addValueValidator("Gender", new StringValueValidator(new Integer(8), null));
            od.addReferenceDescriptor("Gender", new ReferenceDescriptor("GENDER"));            od.addFieldDescriptor(new FieldDescriptor("MStatus", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("MStatus", new StringValueValidator(new Integer(8), null));
            od.addReferenceDescriptor("MStatus", new ReferenceDescriptor("MSTATUS"));            od.addFieldDescriptor(new FieldDescriptor("Race", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("Race", new StringValueValidator(new Integer(8), null));
            od.addReferenceDescriptor("Race", new ReferenceDescriptor("RACE"));            od.addFieldDescriptor(new FieldDescriptor("Ethnic", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("Ethnic", new StringValueValidator(new Integer(8), null));
            od.addReferenceDescriptor("Ethnic", new ReferenceDescriptor("ETHNIC"));            od.addFieldDescriptor(new FieldDescriptor("Religion", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("Religion", new StringValueValidator(new Integer(8), null));
            od.addReferenceDescriptor("Religion", new ReferenceDescriptor("RELIGION"));            od.addFieldDescriptor(new FieldDescriptor("Language", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("Language", new StringValueValidator(new Integer(8), null));
            od.addReferenceDescriptor("Language", new ReferenceDescriptor("LANGUAGE"));            od.addFieldDescriptor(new FieldDescriptor("SpouseName", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("SpouseName", new StringValueValidator(new Integer(100), null));            od.addFieldDescriptor(new FieldDescriptor("MotherName", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("MotherName", new StringValueValidator(new Integer(100), null));            od.addFieldDescriptor(new FieldDescriptor("MotherMN", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("MotherMN", new StringValueValidator(new Integer(40), null));            od.addFieldDescriptor(new FieldDescriptor("FatherName", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("FatherName", new StringValueValidator(new Integer(100), null));            od.addFieldDescriptor(new FieldDescriptor("Maiden", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("Maiden", new StringValueValidator(new Integer(40), null));            od.addFieldDescriptor(new FieldDescriptor("PobCity", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("PobCity", new StringValueValidator(new Integer(30), null));            od.addFieldDescriptor(new FieldDescriptor("PobState", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("PobState", new StringValueValidator(new Integer(30), null));            od.addFieldDescriptor(new FieldDescriptor("PobCountry", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("PobCountry", new StringValueValidator(new Integer(20), null));            od.addFieldDescriptor(new FieldDescriptor("VIPFlag", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("VIPFlag", new StringValueValidator(new Integer(8), null));            od.addFieldDescriptor(new FieldDescriptor("VetStatus", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("VetStatus", new StringValueValidator(new Integer(8), null));            od.addFieldDescriptor(new FieldDescriptor("Status", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("Status", new StringValueValidator(new Integer(8), null));            od.addFieldDescriptor(new FieldDescriptor("DriverLicense", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("DriverLicense", new StringValueValidator(new Integer(20), null));            od.addFieldDescriptor(new FieldDescriptor("DriverLicenseSt", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("DriverLicenseSt", new StringValueValidator(new Integer(10), null));            od.addFieldDescriptor(new FieldDescriptor("Dod", FieldType.DATE, 
                FieldDescriptor.UPDATEABLE));            od.addFieldDescriptor(new FieldDescriptor("DeathCertificate", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("DeathCertificate", new StringValueValidator(new Integer(10), null));            od.addFieldDescriptor(new FieldDescriptor("Nationality", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("Nationality", new StringValueValidator(new Integer(8), null));
            od.addReferenceDescriptor("Nationality", new ReferenceDescriptor("NATIONAL"));            od.addFieldDescriptor(new FieldDescriptor("Citizenship", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("Citizenship", new StringValueValidator(new Integer(8), null));
            od.addReferenceDescriptor("Citizenship", new ReferenceDescriptor("CITIZEN"));            list.add(od);
        } catch (ValidationException e) {
            throw new RuntimeException(e.getMessage());
        }
        od = new ObjectDescriptor();
        od.setObjectName("Alias");
        try {
            od.addFieldDescriptor(new FieldDescriptor("AliasId", FieldType.STRING, 
                FieldDescriptor.NONE));            od.addValueValidator("AliasId", new StringValueValidator(new Integer(30), null));            od.addFieldDescriptor(new FieldDescriptor("FirstName", FieldType.STRING, 
                FieldDescriptor.REQUIRED + FieldDescriptor.UPDATEABLE));            od.addValueValidator("FirstName", new StringValueValidator(new Integer(40), null));            od.addFieldDescriptor(new FieldDescriptor("MiddleName", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("MiddleName", new StringValueValidator(new Integer(30), null));            od.addFieldDescriptor(new FieldDescriptor("LastName", FieldType.STRING, 
                FieldDescriptor.REQUIRED + FieldDescriptor.UPDATEABLE));            od.addValueValidator("LastName", new StringValueValidator(new Integer(40), null));            list.add(od);
        } catch (ValidationException e) {
            throw new RuntimeException(e.getMessage());
        }
        od = new ObjectDescriptor();
        od.setObjectName("Address");
        try {
            od.addFieldDescriptor(new FieldDescriptor("AddressId", FieldType.STRING, 
                FieldDescriptor.NONE));            od.addValueValidator("AddressId", new StringValueValidator(new Integer(30), null));            od.addFieldDescriptor(new FieldDescriptor("AddressType", FieldType.STRING, 
                FieldDescriptor.REQUIRED + FieldDescriptor.UPDATEABLE));            od.addValueValidator("AddressType", new StringValueValidator(new Integer(8), null));
            od.addReferenceDescriptor("AddressType", new ReferenceDescriptor("ADDRTYPE"));            od.addFieldDescriptor(new FieldDescriptor("AddressLine1", FieldType.STRING, 
                FieldDescriptor.REQUIRED + FieldDescriptor.UPDATEABLE));            od.addValueValidator("AddressLine1", new StringValueValidator(new Integer(40), null));            od.addFieldDescriptor(new FieldDescriptor("AddressLine1_HouseNo", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("AddressLine1_HouseNo", new StringValueValidator(new Integer(10), null));            od.addFieldDescriptor(new FieldDescriptor("AddressLine1_StDir", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("AddressLine1_StDir", new StringValueValidator(new Integer(5), null));            od.addFieldDescriptor(new FieldDescriptor("AddressLine1_StName", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("AddressLine1_StName", new StringValueValidator(new Integer(40), null));            od.addFieldDescriptor(new FieldDescriptor("AddressLine1_StPhon", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("AddressLine1_StPhon", new StringValueValidator(new Integer(8), null));            od.addFieldDescriptor(new FieldDescriptor("AddressLine1_StType", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("AddressLine1_StType", new StringValueValidator(new Integer(5), null));            od.addFieldDescriptor(new FieldDescriptor("AddressLine2", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("AddressLine2", new StringValueValidator(new Integer(40), null));            od.addFieldDescriptor(new FieldDescriptor("City", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("City", new StringValueValidator(new Integer(30), null));            od.addFieldDescriptor(new FieldDescriptor("StateCode", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("StateCode", new StringValueValidator(new Integer(10), null));            od.addFieldDescriptor(new FieldDescriptor("PostalCode", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("PostalCode", new StringValueValidator(new Integer(8), null));            od.addFieldDescriptor(new FieldDescriptor("PostalCodeExt", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("PostalCodeExt", new StringValueValidator(new Integer(4), null));            od.addFieldDescriptor(new FieldDescriptor("County", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("County", new StringValueValidator(new Integer(20), null));            od.addFieldDescriptor(new FieldDescriptor("CountryCode", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("CountryCode", new StringValueValidator(new Integer(20), null));            list.add(od);
        } catch (ValidationException e) {
            throw new RuntimeException(e.getMessage());
        }
        od = new ObjectDescriptor();
        od.setObjectName("Phone");
        try {
            od.addFieldDescriptor(new FieldDescriptor("PhoneId", FieldType.STRING, 
                FieldDescriptor.NONE));            od.addValueValidator("PhoneId", new StringValueValidator(new Integer(30), null));            od.addFieldDescriptor(new FieldDescriptor("PhoneType", FieldType.STRING, 
                FieldDescriptor.REQUIRED + FieldDescriptor.UPDATEABLE));            od.addValueValidator("PhoneType", new StringValueValidator(new Integer(8), null));
            od.addReferenceDescriptor("PhoneType", new ReferenceDescriptor("PHONTYPE"));            od.addFieldDescriptor(new FieldDescriptor("Phone", FieldType.STRING, 
                FieldDescriptor.REQUIRED + FieldDescriptor.UPDATEABLE));            od.addValueValidator("Phone", new StringValueValidator(new Integer(20), null));
            od.addPatternValidator("Phone", new PatternValidator("[0-9]{10}"));            od.addFieldDescriptor(new FieldDescriptor("PhoneExt", FieldType.STRING, 
                FieldDescriptor.UPDATEABLE));            od.addValueValidator("PhoneExt", new StringValueValidator(new Integer(6), null));            list.add(od);
        } catch (ValidationException e) {
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }

}
