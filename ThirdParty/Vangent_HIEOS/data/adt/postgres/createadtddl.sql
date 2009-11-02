--
--  This code is subject to the HIEOS License, Version 1.0
-- 
--  Copyright(c) 2008-2009 Vangent, Inc.  All rights reserved.
-- 
--  Unless required by applicable law or agreed to in writing, software
--  distributed under the License is distributed on an "AS IS" BASIS,
--  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- 
--  See the License for the specific language governing permissions and
--  limitations under the License.
--

-- Table: patient

-- DROP TABLE patient;

CREATE TABLE patient
(
  uuid text NOT NULL,
  id text NOT NULL,
  "timestamp" text,
  birthdatetime text,
  adminsex text,
  accountnumber text,
  bedid text,
  CONSTRAINT patient_pkey PRIMARY KEY (id)
)
WITH (OIDS=TRUE);
ALTER TABLE patient OWNER TO adt;

--
-- Create index on patient 'uuid' col
--
CREATE INDEX patient_uuid_idx ON patient USING btree (uuid);


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
  CONSTRAINT patientaddress_pkey PRIMARY KEY (parent)
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
  CONSTRAINT patientname_pkey PRIMARY KEY (parent)
)
WITH (OIDS=TRUE);
ALTER TABLE patientname OWNER TO adt;


-- Table: patientrace

-- DROP TABLE patientrace;

CREATE TABLE patientrace
(
  parent text NOT NULL,
  race text NOT NULL,
  CONSTRAINT patientrace_pkey PRIMARY KEY (parent)
)
WITH (OIDS=TRUE);
ALTER TABLE patientrace OWNER TO adt;


