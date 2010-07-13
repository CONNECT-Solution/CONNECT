/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.mpilib;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author Admin
 */
public class MpiLibTest {

    public MpiLibTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getZeroPatients() {
        System.out.println("getZeroPatients");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        int expResult = 0;
        Patients patientlist = mpi.getPatients();
        int result = patientlist.size();
    }

    @Test
    public void getOnePatient() {
        System.out.println("getOnePatient");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        Patient patient = new Patient();
        patient.getName().setLastName("ADAMS");
        patient.getName().setFirstName("ADAM");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        assertEquals(1, mpi.getPatients().size());
    }

    @Test
    public void getTwoPatients() {
        System.out.println("getTwoPatients");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        Patient patient;

        patient = new Patient();
        patient.getName().setLastName("ADAMS");
        patient.getName().setFirstName("ADAM");
        mpi.AddUpdate(patient);

        patient = new Patient();
        patient.getName().setLastName("BUSTER");
        patient.getName().setFirstName("BIGS");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        assertEquals(2, mpi.getPatients().size());
    }

    @Test
    public void searchNoData() {
        System.out.println("searchNoData");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        Patient searchparams = new Patient();
        searchparams.getName().setLastName("EASTER");

        Patients searchresults = mpi.Search(searchparams);

        assertNotNull(searchresults);
        assertEquals(0, searchresults.size());
    }

    @Test
    public void searchWithDataNoHit() {
        System.out.println("searchWithDataNoHit");
        MiniMpi mpi = loadStockMpi();

        Patient searchparams = new Patient();
        searchparams.getName().setLastName("XYZ");

        Patients searchresults = mpi.Search(searchparams);

        assertNotNull(searchresults);
        assertEquals(0, searchresults.size());
    }

    @Test
    public void searchSingleHit() {
        System.out.println("searchSingleHit");
        MiniMpi mpi = loadStockMpi();

        Patient searchparams = new Patient();
        searchparams.getName().setLastName("ADAMS");

        Patients searchresults = mpi.Search(searchparams);

        assertNotNull(searchresults);
        assertEquals(1, searchresults.size());
    }

    @Test
    public void searchMulitHit() {
        System.out.println("searchMulitHit");
        MiniMpi mpi = loadStockMpi();

        Patient searchparams = new Patient();
        searchparams.getName().setLastName("EASTER");

        Patients searchresults = mpi.Search(searchparams);

        assertNotNull(searchresults);
        assertEquals(2, searchresults.size());
    }
    
    @Test
    public void searchMixCaseNoHit() {
        System.out.println("searchMixCaseNoHit");
        MiniMpi mpi = loadStockMpi();

        Patient searchparams = new Patient();
        searchparams.getName().setLastName("xYz");

        Patients searchresults = mpi.Search(searchparams);

        assertNotNull(searchresults);
        assertEquals(0, searchresults.size());
    }
    @Test
    public void searchMixCaseSingleHit()
    {
        System.out.println("searchMixCaseSingleHit");
        MiniMpi mpi = loadStockMpi();

        Patient searchparams = new Patient();
        searchparams.getName().setLastName("adAms");
        searchparams.getName().setFirstName("adAm");
        
        Patients searchresults = mpi.Search(searchparams);

        assertNotNull(searchresults);
        assertEquals(1, searchresults.size());        
    }
    @Test
    public void searchMixCaseMulitHit() {
        System.out.println("searchMixCaseMulitHit");
        MiniMpi mpi = loadStockMpi();

        Patient searchparams = new Patient();
        searchparams.getName().setLastName("eAstEr");

        Patients searchresults = mpi.Search(searchparams);

        assertNotNull(searchresults);
        assertEquals(2, searchresults.size());
    }
    @Test
    public void addOnePatient() {
        System.out.println("addOnePatient");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        Patient patient = new Patient();
        //patient.getName().setLastName("ADAMS");
        //patient.getName().setFirstName("ADAM");
        patient.getName().setLastName("VALLURIPALLI");
        patient.getName().setFirstName("SAIBABU");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        assertEquals(1, mpi.getPatients().size());
    }

    @Test
    public void addTwoPatients() {
        System.out.println("addTwoPatients");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        Patient patient;

        patient = new Patient();
        patient.getName().setLastName("ADAMS");
        patient.getName().setFirstName("ADAM");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        patient = new Patient();
        patient.getName().setLastName("JOHN");
        patient.getName().setFirstName("SMITH");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        assertEquals(2, mpi.getPatients().size());
    }

    @Test
    public void addSamePatientTwice() {
        System.out.println("addSamePatientTwice");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        Patient patient;

        patient = new Patient();
        patient.getName().setLastName("ADAMS");
        patient.getName().setFirstName("ADAM");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        patient = new Patient();
        patient.getName().setLastName("ADAMS");
        patient.getName().setFirstName("ADAM");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        assertEquals(1, mpi.getPatients().size());
    }

    @Test
    public void addSamePatientTwiceWithDemographicsUpdate() {
        System.out.println("addSamePatientTwice");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        Patient patient;

        patient = new Patient();
        patient.setOptedIn(true);
        patient.getName().setLastName("SINGLE");
        patient.getName().setFirstName("LADY");
        patient.getIdentifiers().add("id1", "A");
        mpi.AddUpdate(patient);

        patient = new Patient();
        patient.setOptedIn(true);
        patient.getName().setLastName("MARRIED");
        patient.getName().setFirstName("LADY");
        patient.getIdentifiers().add("id1", "A");
        mpi.AddUpdate(patient);

        assertEquals(1, mpi.getPatients().size());

        Patient searchCriteria = new Patient();
        searchCriteria.getIdentifiers().add("id1", "A");
        Patients searchResults = mpi.Search(searchCriteria);
        assertEquals(1, searchResults.size());
        Patient searchResult = searchResults.get(0);
        assertEquals(patient.getName().getFirstName(), searchResult.getName().getFirstName());
        assertEquals(patient.getName().getLastName(), searchResult.getName().getLastName());
    }

    @Test
    public void addSamePatientTwiceWithDemographicsGenderUpdate() {
        System.out.println("addSamePatientTwice");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        Patient patient;

        patient = new Patient();
        patient.getName().setLastName("SINGLE");
        patient.getName().setFirstName("LADY");
        patient.getIdentifiers().add("id1", "A");
        patient.setGender("M");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        patient = new Patient();
        patient.getName().setLastName("SINGLE");
        patient.getName().setFirstName("LADY");
        patient.getIdentifiers().add("id1", "A");
        patient.setGender("F");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        assertEquals(1, mpi.getPatients().size());

        Patient searchCriteria = new Patient();
        searchCriteria.getIdentifiers().add("id1", "A");
        Patients searchResults = mpi.Search(searchCriteria);
        assertEquals(1, searchResults.size());
        Patient searchResult = searchResults.get(0);
        assertEquals(patient.getName().getFirstName(), searchResult.getName().getFirstName());
        assertEquals(patient.getName().getLastName(), searchResult.getName().getLastName());
        assertEquals(patient.getGender(), searchResult.getGender());
    }

    @Test
    public void AddTwoPatientsWithSameLastName() {
        System.out.println("AddTwoPatientsWithSameLastName");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        Patient patient;

        patient = new Patient();
        patient.getName().setLastName("ADAMS");
        patient.getName().setFirstName("ADAM");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        patient = new Patient();
        patient.getName().setLastName("ADAMS");
        patient.getName().setFirstName("BOB");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        assertEquals(2, mpi.getPatients().size());
    }

    @Test
    public void AddPatientWithGender() {
        System.out.println("AddPatientWithGender");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        Patient patient;

        patient = new Patient();
        patient.getName().setLastName("ADAMS");
        patient.getName().setFirstName("ADAM");
        patient.setGender("M");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        assertEquals(1, mpi.getPatients().size());
        
        Patients searchResults = mpi.Search(patient);
        assertEquals(1, searchResults.size());
        Patient searchResult = searchResults.get(0);
        assertEquals(patient.getName().getFirstName(), searchResult.getName().getFirstName());
        assertEquals(patient.getName().getLastName(), searchResult.getName().getLastName());
        assertEquals(patient.getGender(), searchResult.getGender());
    }

    @Test
    public void PersistIdentifiers() {
        System.out.println("AddTwoPatientsWithSameLastName");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        Patient patient;
        Patient patient2;
         
        patient = new Patient();
        patient.getName().setLastName("ADAMS");
        patient.getName().setFirstName("ADAM");
        patient.getIdentifiers().add("id1", "A");
        patient.getIdentifiers().add("id2", "B");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        assertEquals(1, mpi.getPatients().size());
        patient2 = mpi.getPatients().get(0);

        assertEquals(patient2.getIdentifiers().get(0).getId(), patient.getIdentifiers().get(0).getId());
        assertEquals(patient2.getIdentifiers().get(0).getOrganizationId(), patient.getIdentifiers().get(0).getOrganizationId());

        assertEquals(patient2.getIdentifiers().get(1).getId(), patient.getIdentifiers().get(1).getId());
        assertEquals(patient2.getIdentifiers().get(1).getOrganizationId(), patient.getIdentifiers().get(1).getOrganizationId());
    }

    @Test
    public void SearchByIdOneResult() {
        System.out.println("AddTwoPatientsWithSameLastName");
        MiniMpi mpi = loadStockMpi();

        Patient searchparams = new Patient();
        searchparams.getIdentifiers().add("MRN-200", "GeneralHospital-OID");

        Patients searchresults = mpi.Search(searchparams);

        assertNotNull(searchresults);
        assertEquals(1, searchresults.size());

        Patient patient = searchresults.get(0);
        assertEquals("EASTER", patient.getName().getLastName());
        assertEquals("EGG", patient.getName().getFirstName());
    }

    @Test
    public void AddPatientWithMultipleIds() {
        System.out.println("AddTwoPatientsWithSameLastName");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        Patient patient;

        patient = new Patient();
        patient.getName().setLastName("ADAMS");
        patient.getName().setFirstName("ADAM");
        patient.getIdentifiers().add("id1", "A");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        assertEquals(mpi.getPatients().size(), 1);

        patient = new Patient();
        patient.getName().setLastName("ADAMS");
        patient.getName().setFirstName("ADAM");
        patient.getIdentifiers().add("id2", "B");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        assertEquals(mpi.getPatients().size(), 1);

        Patient searchParams = new Patient();
        searchParams.getName().setLastName("ADAMS");
        Patients searchResults = mpi.Search(searchParams);
        assertEquals(searchResults.size(), 1);
        patient = searchResults.get(0);
        assertEquals(patient.getIdentifiers().size(), 2);
    }

    @Test
    public void searchWithNoCriteria() {
        System.out.println("searchWithNoCriteria");
        MiniMpi mpi = loadStockMpi();

        Patient searchparams = new Patient();

        Patients searchresults = mpi.Search(searchparams);

        assertNotNull(searchresults);
        assertEquals(0, searchresults.size());
    }

    @Test
    public void PersistPatientList() {
        System.out.println("PersistPatientList");
        MiniMpi mpi;

        mpi = loadStockMpi();
        int PatientListSize = mpi.getPatients().size();

        mpi = MiniMpi.GetMpiInstance("testMPI.xml");

        assertEquals(PatientListSize, mpi.getPatients().size());
    }
//    @Test
//    public void LoadCustomFile()
//    {
//        MiniMpi mpi = MiniMpi.GetMpiInstance("customMPI.xml");
//        assertEquals(mpi.getPatients().size(),6);
//    }
    
    @Test
    public void SaveCustomFile()
    {
       System.out.println("SaveCustomFile");
        MiniMpi mpi = MiniMpi.GetMpiInstance("sampleCustomMPI.xml");
        mpi.Reset();

        Patient patient;

        patient = new Patient();
        patient.getName().setLastName("ADAMS");
        patient.getName().setFirstName("ADAM");
        patient.getIdentifiers().add("id1", "A");
        patient.getIdentifiers().add("id2", "B");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        MiniMpi mpi2 = MiniMpi.GetMpiInstance("sampleCustomMPI.xml");
        
        assertEquals(mpi.getPatients().size(), mpi2.getPatients().size());
        Patient patient2 = mpi2.getPatients().get(0);

        assertEquals(patient.getIdentifiers().get(0).getId(), patient2.getIdentifiers().get(0).getId());
        assertEquals(patient.getIdentifiers().get(0).getOrganizationId(), patient2.getIdentifiers().get(0).getOrganizationId());

        assertEquals(patient.getIdentifiers().get(1).getId(), patient2.getIdentifiers().get(1).getId());
        assertEquals(patient.getIdentifiers().get(1).getOrganizationId(), patient2.getIdentifiers().get(1).getOrganizationId());

        java.io.File f = new java.io.File("sampleCustomMPI.xml");
        f.delete();
        
    }
    @Test
    public void testName()
    {
        System.out.println("testName");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        Patient patient;

        patient = new Patient();
        patient.getName().setLastName("LastName");
        patient.getName().setFirstName("FirstName");
        patient.getName().setFirstName("MiddleName");
        patient.getName().setSuffix("Suffix");
        patient.getName().setTitle("title");
        patient.getIdentifiers().add("id1", "A");
        patient.getIdentifiers().add("id2", "B");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        assertEquals(1, mpi.getPatients().size());


        Patients searchResults = mpi.Search(patient);

        assertEquals(searchResults.size(),1);

        assertEquals(searchResults.get(0).getName().getFirstName(), patient.getName().getFirstName());
        assertEquals(searchResults.get(0).getName().getLastName(), patient.getName().getLastName());
        assertEquals(searchResults.get(0).getName().getMiddleName(), patient.getName().getMiddleName());
        assertEquals(searchResults.get(0).getName().getMiddleInitial(), patient.getName().getMiddleInitial());
        //assertEquals(searchResults.get(0).get)

    }
    @Test
    public void testPhoneNumber()
    {
        System.out.println("testPhoneNumber");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();
        String number = "70312345678";

        Patient patient;

        patient = new Patient();
        patient.getName().setLastName("LastName");
        patient.getName().setFirstName("FirstName");
        patient.getName().setFirstName("MiddleName");

        
        patient.getIdentifiers().add("id1", "A");
        patient.getIdentifiers().add("id2", "B");
        patient.setOptedIn(true);

        patient.getPhoneNumbers().add(new PhoneNumber(number));

        Address add = new Address();
        add.setCity("Chantilly");
        add.setState("VA");
        add.setStreet1("5155 Parkstone Drive");
        add.setStreet2("Att:Developer");
        add.setZip("20151");

        patient.getAddresses().add(add);
        mpi.AddUpdate(patient);

        assertEquals(1, mpi.getPatients().size());


        Patients searchResults = mpi.Search(patient);

        assertEquals(searchResults.size(),1);
        assertEquals(searchResults.get(0).getPhoneNumbers().size(),1);
        assertEquals(searchResults.get(0).getName().getFirstName(), patient.getName().getFirstName());
        assertEquals(searchResults.get(0).getName().getLastName(), patient.getName().getLastName());
        assertEquals(searchResults.get(0).getName().getMiddleName(), patient.getName().getMiddleName());
        assertEquals(searchResults.get(0).getName().getMiddleInitial(), patient.getName().getMiddleInitial());
        assertEquals(searchResults.get(0).getPhoneNumbers().size(), patient.getPhoneNumbers().size());
        assertEquals(searchResults.get(0).getPhoneNumbers().get(0).getPhoneNumber(), patient.getPhoneNumbers().get(0).getPhoneNumber());
        assertEquals(searchResults.get(0).getPhoneNumbers().get(0).getPhoneNumber(), number);

    }
    @Test
    public void testMultiplePhoneNumber()
    {
        System.out.println("testMultiplePhoneNumber");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        Patient patient;
        String number1 = "70312345678";
        String number2 = "20212345678";
        patient = new Patient();
        patient.getName().setLastName("LastName");
        patient.getName().setFirstName("FirstName");
        patient.getName().setFirstName("MiddleName");
        patient.getIdentifiers().add("id1", "A");
        patient.getIdentifiers().add("id2", "B");
        patient.setOptedIn(true);
        patient.getPhoneNumbers().add(new PhoneNumber(number1));
        patient.getPhoneNumbers().add(new PhoneNumber(number2));
        mpi.AddUpdate(patient);

        assertEquals(1, mpi.getPatients().size());


        Patients searchResults = mpi.Search(patient);

        assertEquals(searchResults.size(),1);

        assertEquals(searchResults.get(0).getName().getLastName(), patient.getName().getLastName());

        assertEquals(2,searchResults.get(0).getPhoneNumbers().size());
        String actualNumber1 = searchResults.get(0).getPhoneNumbers().get(0).getPhoneNumber();
        String actualNumber2 = searchResults.get(0).getPhoneNumbers().get(1).getPhoneNumber();

        assertEquals(number1, actualNumber1);
        assertEquals(number2, actualNumber2);

    }
    @Test
    public void testAddress()
    {
        System.out.println("testAddress");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();
        Patient patient;

        patient = new Patient();
        patient.getName().setLastName("LastName");
        patient.getName().setFirstName("FirstName");
        patient.getName().setFirstName("MiddleName");


        patient.getIdentifiers().add("id1", "A");
        patient.getIdentifiers().add("id2", "B");
        patient.setOptedIn(true);

        Address add = new Address();
        add.setCity("Chantilly");
        add.setState("VA");
        add.setStreet1("5155 Parkstone Drive");
        add.setStreet2("Att:Developer");
        add.setZip("20151");

        patient.getAddresses().add(add);
        mpi.AddUpdate(patient);

        assertEquals(1, mpi.getPatients().size());


        Patients searchResults = mpi.Search(patient);

        assertEquals(searchResults.size(),1);
        assertEquals(searchResults.get(0).getAddresses().size(), 1);
        assertEquals("Chantilly",searchResults.get(0).getAddresses().get(0).getCity());
        assertEquals("VA",searchResults.get(0).getAddresses().get(0).getState());
        assertEquals("5155 Parkstone Drive",searchResults.get(0).getAddresses().get(0).getStreet1());
        assertEquals("20151",searchResults.get(0).getAddresses().get(0).getZip());

    }
   @Test
    public void testAddress_Multi()
    {
        System.out.println("testAddress_Multi");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();
        Patient patient;

        patient = new Patient();
        patient.getName().setLastName("LastName");
        patient.getName().setFirstName("FirstName");
        patient.getName().setFirstName("MiddleName");


        patient.getIdentifiers().add("id1", "A");
        patient.getIdentifiers().add("id2", "B");
        patient.setOptedIn(true);

        Address add = new Address();
        add.setCity("Chantilly");
        add.setState("VA");
        add.setStreet1("5155 Parkstone Drive");
        add.setStreet2("Att:Developer");
        add.setZip("20151");

        Address add2 = new Address();
        add2.setCity("Melbourne");
        add2.setState("FL");
        add2.setStreet1("1025 West NASA Boulevard");
        add2.setStreet2("Att:Developer");
        add2.setZip("32919-0001");

        patient.getAddresses().add(add);
        patient.getAddresses().add(add2);
        
        
        
        mpi.AddUpdate(patient);

        assertEquals(1, mpi.getPatients().size());


        Patients searchResults = mpi.Search(patient);

        assertEquals(1, searchResults.size());
        assertEquals(2, searchResults.get(0).getAddresses().size());

        assertEquals("VA",searchResults.get(0).getAddresses().get(0).getState());
        assertEquals("FL",searchResults.get(0).getAddresses().get(1).getState());
    }

   public void createConformanceMPI()
   {
       MiniMpi mpi = loadConformanceMPI();

       MpiDataSaver.SaveMpi(mpi.getPatients(), "conformanceMPI.xml");
   }
   @Test
   public void testMultiPatientName()
   {
        System.out.println("testMultiplePhoneNumber");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        Patient patient;

        patient = new Patient();


        PersonName name1 = new PersonName();
        name1.setFirstName("FirstName");
        name1.setLastName("LastName");
        name1.setMiddleName("MiddleName");
        name1.setSuffix("Suffix");
        name1.setTitle("Title");

        patient.getNames().add(name1);

        PersonName name2 = new PersonName();
        name2.setFirstName("FirstName2");
        name2.setLastName("LastName2");
        name2.setMiddleName("MiddleName2");
        name2.setSuffix("Suffix2");
        name2.setTitle("Title2");

        patient.getNames().add(name2);
        
        patient.setOptedIn(true);

        mpi.AddUpdate(patient);

        assertEquals(1, mpi.getPatients().size());


        Patients searchResults = mpi.Search(patient);

        assertEquals(searchResults.size(),1);
        
        PersonNames nameResults = searchResults.get(0).getNames();
        assertEquals(2, nameResults.size());
        assertEquals(name1.getFirstName(), nameResults.get(0).getFirstName());
        assertEquals(name1.getLastName(), nameResults.get(0).getLastName());
        assertEquals(name1.getMiddleName(), nameResults.get(0).getMiddleName());
        assertEquals(name1.getTitle(), nameResults.get(0).getTitle());
        assertEquals(name1.getSuffix(), nameResults.get(0).getSuffix());

        assertEquals(name2.getFirstName(), nameResults.get(1).getFirstName());
        assertEquals(name2.getLastName(), nameResults.get(1).getLastName());
        assertEquals(name2.getMiddleName(), nameResults.get(1).getMiddleName());
        assertEquals(name2.getTitle(), nameResults.get(1).getTitle());
        assertEquals(name2.getSuffix(), nameResults.get(1).getSuffix());
   }

    private Patient loadPatient(String lName, String fName, String mName, 
            String sex, String dob, String ssn, String add1, String add2, 
            String city, String state, String zip)
    {
        Patient patient = new Patient();
        PersonName name = new PersonName();
        
        name.setFirstName(fName);
        name.setLastName(lName);
        name.setMiddleName(mName);
        patient.getNames().add(name);
        
        patient.setDateOfBirth(dob);
        patient.setSSN(ssn);
        patient.setGender(sex);
               
        
        //patient.setAddress(addy);
        patient.getAddresses().add(loadAddress(add1, add2, city, state, zip));
        
        return patient;
    }
    private Patient loadPatient(String lName, String fName, String mName,
            String sex, String dob, String ssn, String add1, String add2,
            String city, String state, String zip, String title, String suffix)
    {
        Patient p = loadPatient(lName, fName, mName, sex, dob, ssn, add1, add2,
            city, state, zip);
        p.getNames().get(0).setSuffix(suffix);
        p.getNames().get(0).setTitle(title);


        return p;
    }
    private Address loadAddress(String add1, String add2, String city, String state, String zip)
    {
        Address addy = new Address();

        addy.setStreet1(add1);
        addy.setStreet2(add2);
        addy.setCity(city);
        addy.setState(state);
        addy.setZip(zip);

        return addy;
    }
    private PersonName loadPersonName(String firstName, String middleName, String lastName, String title, String suffix)
    {
        PersonName result = new PersonName();

        result.setFirstName(firstName);
        result.setMiddleName(middleName);
        result.setLastName(lastName);
        result.setSuffix(suffix);
        result.setTitle(title);

        return result;
    }
    private MiniMpi loadConformanceMPI()
    {
        MiniMpi mpi;
        mpi = MiniMpi.GetMpiInstance();
        mpi.Reset();

        Patient p;

        try
        {
            p = loadPatient("Hamilton", "Alan", "James", "M", "07/02/1980", "", "800 Telephone Ct", "", "Honolulu", "HI", "96801", "Mr.", "");
            p.getIdentifiers().add("0000002009", "");
            p.getPhoneNumbers().add(new PhoneNumber("1-808-300-2343"));
            p.getPhoneNumbers().add(new PhoneNumber("1-808-300-3300"));

            mpi.AddUpdate(p);

            p = loadPatient("Davidson", "Amy", "C", "F", "10/17/1983", "", "809 First Ave", "", "Springfield", "MO", "65801");
            p.getIdentifiers().add("0000002010", "");
            p.getPhoneNumbers().add(new PhoneNumber("1-417-989-0987"));
            p.getPhoneNumbers().add(new PhoneNumber("1-417-989-3300"));

            mpi.AddUpdate(p);

            p = loadPatient("Adams", "Theresa", "Susan", "F", "10/17/1983", "", "3131 Over Street", "", "Ft Worth", "TX", "76101", "Ms.","");
            p.getIdentifiers().add("0000002008", "");
            p.getPhoneNumbers().add(new PhoneNumber("1-682-633-0909"));
            p.getPhoneNumbers().add(new PhoneNumber("714-625-7161"));
            p.getAddresses().add(loadAddress("19 Main Street", "", "Fullerton", "CA", "92831"));

            mpi.AddUpdate(p);

            p = loadPatient("Carson", "Robert", "M", "M", "02/10/1960", "", "290 Jackson Lane", "001111111", "Boulder", "CO", "80301", "Mr.", "");
            p.getIdentifiers().add("0000002007", "");
            p.getPhoneNumbers().add(new PhoneNumber("1-303-454-0909"));
            p.getPhoneNumbers().add(new PhoneNumber("1-303-454-0111"));
            

            p = loadPatient("Turner", "Sara", "P", "M", "05/28/1960", "", "912 Coat Dr", "", "Kansas City", "MO", "64116", "Ms.", "");
            p.getIdentifiers().add("0000002009", "");
            mpi.AddUpdate(p);

            p = loadPatient("Smith", "John", "", "M", "04/23/1957", "", "", "", "", "", "");
            p.getIdentifiers().add("0000002009", "");
            mpi.AddUpdate(p);
            
            p = loadPatient("Stewart", "Elizabeth", "Ann", "M", "01/10/1983", "F", "59302 Imprint Drive", "", "Charlotte", "NC", "28201", "Mrs.", "");
            p.getIdentifiers().add("N600025", "");
            p.getPhoneNumbers().add(new PhoneNumber("1-704-424-9000"));
            p.getPhoneNumbers().add(new PhoneNumber("1-704-424-9232"));

            PersonName secondName = loadPersonName("Elizabeth", "Ann", "Moore", "Ms.", "");
            p.getNames().add(secondName);
            mpi.AddUpdate(p);


            p = loadPatient("O'Hara", "Donna", "M", "F", "03/18/1945", "", "76 Mouse Drive", "", "Las Vegas", "NV", "89101", "Ms.", "");
            p.getIdentifiers().add("N600025", "");
            p.getPhoneNumbers().add(new PhoneNumber("1-704-424-9000"));
            p.getPhoneNumbers().add(new PhoneNumber("1-704-424-9232"));

            mpi.AddUpdate(p);

            p = loadPatient("Reagan", "Ellen", "Ann", "F", "01/10/1975", "", "3489 Mountain Lanet", "", "Ashburn", "VA", "20147", "Ms.", "");
            p.getIdentifiers().add("0000002022", "");
            p.getPhoneNumbers().add(new PhoneNumber("1-703-231-9000"));
            p.getPhoneNumbers().add(new PhoneNumber("1-703-231-4001"));

            mpi.AddUpdate(p);

            p = loadPatient("Oliver", "Timothy", "Steven", "M", "06/12/1959", "666660056", "555 Rainbow Ct", "", "Nashville", "TN", "37201", "Mr.", "III");
            p.getIdentifiers().add("0000002022", "");
            p.getPhoneNumbers().add(new PhoneNumber("615-500-2322"));
            p.getPhoneNumbers().add(new PhoneNumber("615-232-1212"));

            mpi.AddUpdate(p);

            p = loadPatient("Parker", "Kenneth", "Thomas", "M", "03/18/1945", "", "40 Saratoga Drive", "", "Houston", "TX", "77053", "Mr.", "III");
            p.getIdentifiers().add("0000002011", "");
            p.getPhoneNumbers().add(new PhoneNumber("281-912-1122"));
            p.getPhoneNumbers().add(new PhoneNumber("281-911-2121"));

            mpi.AddUpdate(p);

            p = loadPatient("De La Cruz", "Mary Beth", "A", "F", "01/18/1975", "", "301 Palace Drive", "", "El Paso", "TX", "79901");
            p.getIdentifiers().add("0000002012", "");
            p.getPhoneNumbers().add(new PhoneNumber("915-090-2222"));
            p.getPhoneNumbers().add(new PhoneNumber("915-333-0112"));

            mpi.AddUpdate(p);


            p = loadPatient("Morgan", "William", "", "M", "02/20/1973", "", "450 Lake Drive", "", "Bakersfield", "CA", "93301", "Mr.", "");
            p.getIdentifiers().add("0000002019", "");
            mpi.AddUpdate(p);

        }
        catch(Exception ex)
        {

        }

        return mpi;

    }
    private MiniMpi loadStandardMPI()
    {
        MiniMpi mpi;
        mpi = MiniMpi.GetMpiInstance();
        mpi.Reset();

        Patient p;
        
        try
        {
                   
            p = loadPatient("BRADFORD", "BILL", "HUGH", "M", "1/20/1975", "000-00-0001", "437 TURNER LANE			", "", "LEESBURG", "VA", "20176");
            p.getIdentifiers().add("DoD-123", "DoD");
            mpi.AddUpdate(p); 
            p=loadPatient("BRADFORD", "KATY			", "POLK		", "F", "7/30/1974", "000-00-0002", "437 TURNER LANE			", "", "LEESBURG", "VA", "20176");
            p.getIdentifiers().add("DoD-124", "DoD");
            mpi.AddUpdate(p); 
            p=loadPatient("BRADFORD", "JILL			", "PAULINE	", "F", "5/24/1999", "000-00-0003", "437 TURNER LANE			", "", "LEESBURG", "VA", "20176");
            p.getIdentifiers().add("DoD-125", "DoD");
            mpi.AddUpdate(p); 
            p=loadPatient("BRADFORD", "TOMMY		", "CARL		", "M", "10/21/2003", "000-00-0004", "437 TURNER LANE			", "", "LEESBURG", "VA", "20176");
            mpi.AddUpdate(p); 
            p.getIdentifiers().add("DoD-126", "DoD");
            p=loadPatient("BRADFORD", "CARL			", "FRANKLIN", "M", "9/4/1941", "000-00-0005", "437 TURNER LANE			", "", "LEESBURG", "VA", "20176");
            mpi.AddUpdate(p); 
            p.getIdentifiers().add("DoD-127", "DoD");
            p=loadPatient("POLK", "ELIZABETH", "ROBERTS	", "F", "11/5/1947", "000-00-0006", "14 E WINDSOR DRIVE	", "", "BURKE		", "VA", "22015");
            p.getIdentifiers().add("CDC-123", "CDC");
            mpi.AddUpdate(p); 
            p=loadPatient("POLK", "RICHARD	", "GEORGE	", "M", "4/16/1946", "000-00-0007", "14 E WINDSOR DRIVE	", "", "BURKE		", "VA", "22015");
            p.getIdentifiers().add("CDC-124", "CDC");
            p.getIdentifiers().add("DoD-128", "DoD");
            mpi.AddUpdate(p); 
            p=loadPatient("POLK", "JASON		", "KENNETH	", "M", "5/16/1971", "000-00-0008", "60 LINCOLN AVE			", "	APT #3			", "FAIRFAX				", "VA", "22032");
            p.getIdentifiers().add("CDC-125", "CDC");
            p.getIdentifiers().add("DoD-129", "DoD");           
            mpi.AddUpdate(p); 
            p=loadPatient("POLK", "WALTER		", "MICHAEL	", "M", "12/5/1978", "000-00-0009", "PO BOX 1446					", "", "GERMANTOWN		", "MD", "20876");
            p.getIdentifiers().add("KAD-124", "KAD");
            p.getIdentifiers().add("CDC-126", "CDC");
            mpi.AddUpdate(p); 
            p=loadPatient("MACBETH ", "FRANK		", "EDWARD	", "M", "6/21/1963", "000-00-0010", "997 STRATFORD STREET", "UNIT 4G", "SILVER SPRING	", "MD", "20906");
            p.getIdentifiers().add("KAD-125", "KAD");
            mpi.AddUpdate(p); 
            p=loadPatient("JONES", "ROMEO		", "TYLER		", "M", "10/10/1992", "000-00-0011", "997 STRATFORD STREET", "UNIT 4G", "SILVER SPRING	", "MD", "20906");
            p.getIdentifiers().add("KAD-126", "KAD");
            mpi.AddUpdate(p); 
            p=loadPatient("SMITH", "JULIET		", "AMANDA	", "F", "4/23/1993", "000-00-0012", "997 STRATFORD STREET", "UNIT 4G", "SILVER SPRING	", "MD", "20906");
            p.getIdentifiers().add("KAD-127", "KAD");
            mpi.AddUpdate(p); 
            p=loadPatient("HARKNESS", "JACK			", "WINSTON	", "M", "9/3/1949", "000-00-0013", "TORCHWOOD ESTATES", "1000 CARDIFF ROAD	", "GREAT FALLS		", "VA", "22066");
            p.getIdentifiers().add("INOVA-123", "INOVA");
            mpi.AddUpdate(p); 
            p=loadPatient("COOPER", "GWEN", "ELIZABETH","F", "8/16/1978", "000-00-0014", "TORCHWOOD ESTATES", "1000 CARDIFF ROAD	", "GREAT FALLS		", "VA", "22066");
            p.getIdentifiers().add("INOVA-124", "INOVA");
            mpi.AddUpdate(p); 
            p=loadPatient("HARPER", "OWEN", "STEPHEN	", "M", "7/1/1968", "000-00-0015", "TORCHWOOD ESTATES", "1000 CARDIFF ROAD	", "GREAT FALLS		", "VA", "22066");
            p.getIdentifiers().add("INOVA-125", "INOVA");
            mpi.AddUpdate(p); 
            p=loadPatient("SATO", "TOSHIK", "", "F", "6/14/1973", "000-00-0016", "TORCHWOOD ESTATES", "1000 CARDIFF ROAD	", "GREAT FALLS		", "VA", "22066");
            p.getIdentifiers().add("INOVA-126", "INOVA");
            p.getIdentifiers().add("CDC-127", "CDC");
            p.getIdentifiers().add("DoD-123", "DoD");
            mpi.AddUpdate(p); 
            p=loadPatient("JONES", "IANTO", "WALDO		", "M", "8/19/1983", "000-00-0017", "TORCHWOOD ESTATES", "1000 CARDIFF ROAD	", "GREAT FALLS		", "VA", "22066");
            mpi.AddUpdate(p); 
            p=loadPatient("MARIANO", "JULIA", "BRIDGET	", "F", "1/13/1998", "000-00-0018", "49604 MAIN STREET", "", "ELLICOTT CITY	", "MD", "21042");
            mpi.AddUpdate(p); 
            p=loadPatient("ELSTON", "MICHAEL", "BUZZ		", "M", "11/21/1928", "000-00-0019", "23 JFK DRIVE", "", "WASHINGTON		", "DC", "20004");
            p.getIdentifiers().add("GH-123", "GH");
            mpi.AddUpdate(p); 
            p=loadPatient("DANGERS", "JACK", "", "M", "12/11/1965", "000-00-0020", "99 PARADISE STREET NW", "", "WASHINGTON		", "DC", "20037");
            p.getIdentifiers().add("GH-124", "GH");
            mpi.AddUpdate(p);         
        }
        catch (Exception ex)
        {
            
        }

      
        
        return mpi;
    }
    private MiniMpi loadStockMpi() {
        MiniMpi mpi;
        mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        Patient patient;

        patient = new Patient();
        patient.getName().setLastName("ADAMS");
        patient.getName().setFirstName("ADAM");
        System.out.println("loading identifier");
        patient.getIdentifiers().add("MRN-123", "GeneralHospital-OID");
        mpi.AddUpdate(patient);

        patient = new Patient();
        patient.getName().setLastName("BUSTER");
        patient.getName().setFirstName("BIGS");
        patient.getIdentifiers().add("MRN-124", "GeneralHospital-OID");
        mpi.AddUpdate(patient);

        patient = new Patient();
        patient.getName().setLastName("CHARLIE");
        patient.getName().setFirstName("CHAPLAN");
        patient.getIdentifiers().add("MRN-134", "GeneralHospital-OID");
        mpi.AddUpdate(patient);

        patient = new Patient();
        patient.getName().setLastName("DONNIE");
        patient.getName().setFirstName("DARKO");
        patient.getIdentifiers().add("MRN-135", "GeneralHospital-OID");
        mpi.AddUpdate(patient);

        patient = new Patient();
        patient.getName().setLastName("EASTER");
        patient.getName().setFirstName("EGG");
        patient.getIdentifiers().add("MRN-200", "GeneralHospital-OID");
        patient.getIdentifiers().add("e123", "Healthcare-r-us-OID");
        mpi.AddUpdate(patient);

        patient = new Patient();
        patient.getName().setLastName("EASTER");
        patient.getName().setFirstName("BUNNY");
        patient.getIdentifiers().add("b7", "Healthcare-r-us-OID");
        mpi.AddUpdate(patient);
        
        patient = new Patient();
        patient.getName().setLastName("CHARLE");
        patient.getName().setFirstName("BROWN");
        patient.setOptedIn(false);
        patient.getIdentifiers().add("MRN-224", "Healthcare-r-us-OID");
        mpi.AddUpdate(patient);

        return mpi;
    }
    
}
