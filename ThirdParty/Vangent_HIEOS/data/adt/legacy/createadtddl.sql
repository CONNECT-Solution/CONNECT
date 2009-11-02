-- Table: patient

-- DROP TABLE patient;

CREATE TABLE patient
(
  uuid text NOT NULL,
  id text,
  "timestamp" text,
  birthdatetime text,
  adminsex text,
  accountnumber text,
  bedid text,
  CONSTRAINT patient_pkey PRIMARY KEY (uuid)
)
WITH (OIDS=TRUE);
ALTER TABLE patient OWNER TO adt;

-- Table: patientaddress

-- DROP TABLE patientaddress;

CREATE TABLE patientaddress
(
  parent text NOT NULL,
  streetaddress text NOT NULL,
  otherdesignation text NOT NULL,
  city text NOT NULL,
  stateorprovince text NOT NULL,
  zipcode text NOT NULL,
  country text NOT NULL,
  countyorparish text NOT NULL,
  CONSTRAINT patientaddress_pkey PRIMARY KEY (parent, streetaddress, otherdesignation, city, stateorprovince, zipcode, country, countyorparish)
)
WITH (OIDS=TRUE);
ALTER TABLE patientaddress OWNER TO adt;

-- Table: patientname

-- DROP TABLE patientname;

CREATE TABLE patientname
(
  parent text NOT NULL,
  familyname text NOT NULL,
  givenname text NOT NULL,
  secondandfurthername text NOT NULL,
  suffix text NOT NULL,
  prefix text NOT NULL,
  degree text NOT NULL,
  CONSTRAINT patientname_pkey PRIMARY KEY (parent, familyname, givenname, secondandfurthername, suffix, prefix, degree)
)
WITH (OIDS=TRUE);
ALTER TABLE patientname OWNER TO adt;


-- Table: patientrace

-- DROP TABLE patientrace;

CREATE TABLE patientrace
(
  parent text NOT NULL,
  race text NOT NULL,
  CONSTRAINT patientrace_pkey PRIMARY KEY (parent, race)
)
WITH (OIDS=TRUE);
ALTER TABLE patientrace OWNER TO adt;


