-- Table: association

-- DROP TABLE association;

CREATE TABLE association
(
  accesscontrolpolicy character varying(64),
  id character varying(64) NOT NULL,
  objecttype character varying(64),
  associationtype character varying(128) NOT NULL,
  sourceobject character varying(64) NOT NULL,
  targetobject character varying(64) NOT NULL,
  isconfirmedbysourceowner character varying(1),
  isconfirmedbytargetowner character varying(1),
  CONSTRAINT association_pkey PRIMARY KEY (id),
  CONSTRAINT association_objecttype CHECK (objecttype::text = 'Association'::character varying::text)
)
WITH (OIDS=TRUE);
ALTER TABLE association OWNER TO ebxmlrr;

-- Index: association_i1

-- DROP INDEX association_i1;

CREATE INDEX association_i1
  ON association
  USING btree
  (sourceobject);

-- Index: association_i2

-- DROP INDEX association_i2;

CREATE INDEX association_i2
  ON association
  USING btree
  (targetobject);

-- Index: association_i3

-- DROP INDEX association_i3;

CREATE INDEX association_i3
  ON association
  USING btree
  (associationtype);

-- Index: association_id

-- DROP INDEX association_id;

CREATE INDEX association_id
  ON association
  USING btree
  (id);


-- Table: auditableevent

-- DROP TABLE auditableevent;

CREATE TABLE auditableevent
(
  accesscontrolpolicy character varying(64),
  id character varying(64) NOT NULL,
  objecttype character varying(64),
  eventtype character varying(128) NOT NULL,
  registryobject character varying(64) NOT NULL,
  timestamp_ character varying(30) NOT NULL,
  user_ character varying(64) NOT NULL,
  CONSTRAINT auditableevent_pkey PRIMARY KEY (id),
  CONSTRAINT auditableevent_objecttype CHECK (objecttype::text = 'AuditableEvent'::character varying::text)
)
WITH (OIDS=TRUE);
ALTER TABLE auditableevent OWNER TO ebxmlrr;

-- Index: auditableevent_id

-- DROP INDEX auditableevent_id;

CREATE INDEX auditableevent_id
  ON auditableevent
  USING btree
  (id);


-- Table: classification

-- DROP TABLE classification;

CREATE TABLE classification
(
  accesscontrolpolicy character varying(64),
  id character varying(64) NOT NULL,
  objecttype character varying(64),
  classificationnode character varying(64),
  classificationscheme character varying(64),
  classifiedobject character varying(64) NOT NULL,
  noderepresentation character varying(128),
  CONSTRAINT classification_pkey PRIMARY KEY (id),
  CONSTRAINT classification_objecttype CHECK (objecttype::text = 'Classification'::character varying::text)
)
WITH (OIDS=TRUE);
ALTER TABLE classification OWNER TO ebxmlrr;

-- Index: classification_i1

-- DROP INDEX classification_i1;

CREATE INDEX classification_i1
  ON classification
  USING btree
  (classifiedobject);

-- Index: classification_i2

-- DROP INDEX classification_i2;

CREATE INDEX classification_i2
  ON classification
  USING btree
  (classificationnode);

-- Index: classification_id

-- DROP INDEX classification_id;

CREATE INDEX classification_id
  ON classification
  USING btree
  (id);

-- Table: classificationnode

-- DROP TABLE classificationnode;

CREATE TABLE classificationnode
(
  accesscontrolpolicy character varying(64),
  id character varying(64) NOT NULL,
  objecttype character varying(64),
  code character varying(64),
  parent character varying(64),
  path character varying(1024),
  CONSTRAINT classificationnode_pkey PRIMARY KEY (id),
  CONSTRAINT classificationnode_objecttype CHECK (objecttype::text = 'ClassificationNode'::character varying::text)
)
WITH (OIDS=TRUE);
ALTER TABLE classificationnode OWNER TO ebxmlrr;

-- Index: classificationnode_i1

-- DROP INDEX classificationnode_i1;

CREATE INDEX classificationnode_i1
  ON classificationnode
  USING btree
  (parent);

-- Index: classificationnode_i2

-- DROP INDEX classificationnode_i2;

CREATE INDEX classificationnode_i2
  ON classificationnode
  USING btree
  (code);

-- Index: classificationnode_i3

-- DROP INDEX classificationnode_i3;

CREATE INDEX classificationnode_i3
  ON classificationnode
  USING btree
  (path);

-- Index: classificationnode_id

-- DROP INDEX classificationnode_id;

CREATE INDEX classificationnode_id
  ON classificationnode
  USING btree
  (id);



-- Table: classscheme

-- DROP TABLE classscheme;

CREATE TABLE classscheme
(
  accesscontrolpolicy character varying(64),
  id character varying(64) NOT NULL,
  objecttype character varying(64),
  expiration character varying(30),
  majorversion integer NOT NULL,
  minorversion integer NOT NULL,
  stability character varying(128),
  status character varying(128) NOT NULL,
  userversion character varying(64),
  isinternal character varying(1) NOT NULL,
  nodetype character varying(32) NOT NULL,
  CONSTRAINT classscheme_pkey PRIMARY KEY (id),
  CONSTRAINT classscheme_objecttype CHECK (objecttype::text = 'ClassificationScheme'::character varying::text)
)
WITH (OIDS=TRUE);
ALTER TABLE classscheme OWNER TO ebxmlrr;

-- Index: classscheme_id

-- DROP INDEX classscheme_id;

CREATE INDEX classscheme_id
  ON classscheme
  USING btree
  (id);


-- Table: description

-- DROP TABLE description;

CREATE TABLE description
(
  charset character varying(32),
  lang character varying(32) NOT NULL,
  "value" character varying(256) NOT NULL,
  parent character varying(64) NOT NULL,
  CONSTRAINT description_pkey PRIMARY KEY (parent, lang, value)
)
WITH (OIDS=TRUE);
ALTER TABLE description OWNER TO ebxmlrr;

-- Index: description_i1

-- DROP INDEX description_i1;

CREATE INDEX description_i1
  ON description
  USING btree
  (value);

-- Index: description_i2

-- DROP INDEX description_i2;

CREATE INDEX description_i2
  ON description
  USING btree
  (lang, value);

-- Table: emailaddress

-- DROP TABLE emailaddress;

CREATE TABLE emailaddress
(
  address character varying(64) NOT NULL,
  "type" character varying(32),
  parent character varying(64) NOT NULL
)
WITH (OIDS=TRUE);
ALTER TABLE emailaddress OWNER TO ebxmlrr;

-- Index: emailaddress_i1

-- DROP INDEX emailaddress_i1;

CREATE INDEX emailaddress_i1
  ON emailaddress
  USING btree
  (parent);

-- Table: externalidentifier

-- DROP TABLE externalidentifier;

CREATE TABLE externalidentifier
(
  accesscontrolpolicy character varying(64),
  id character varying(64) NOT NULL,
  objecttype character varying(64),
  registryobject character varying(64) NOT NULL,
  identificationscheme character varying(64) NOT NULL,
  "value" character varying(64) NOT NULL,
  CONSTRAINT externalidentifier_pkey PRIMARY KEY (id),
  CONSTRAINT externalidentifier_objecttype CHECK (objecttype::text = 'ExternalIdentifier'::character varying::text)
)
WITH (OIDS=TRUE);
ALTER TABLE externalidentifier OWNER TO ebxmlrr;

-- Index: externalidentifier_i1

-- DROP INDEX externalidentifier_i1;

CREATE INDEX externalidentifier_i1
  ON externalidentifier
  USING btree
  (registryobject);

-- Index: externalidentifier_id

-- DROP INDEX externalidentifier_id;

CREATE INDEX externalidentifier_id
  ON externalidentifier
  USING btree
  (id);

-- Table: externallink

-- DROP TABLE externallink;

CREATE TABLE externallink
(
  accesscontrolpolicy character varying(64),
  id character varying(64) NOT NULL,
  objecttype character varying(64),
  externaluri character varying(256) NOT NULL,
  CONSTRAINT externallink_pkey PRIMARY KEY (id),
  CONSTRAINT externallink_objecttype CHECK (objecttype::text = 'ExternalLink'::character varying::text)
)
WITH (OIDS=TRUE);
ALTER TABLE externallink OWNER TO ebxmlrr;

-- Index: externallink_i1

-- DROP INDEX externallink_i1;

CREATE INDEX externallink_i1
  ON externallink
  USING btree
  (externaluri);

-- Index: externallink_id

-- DROP INDEX externallink_id;

CREATE INDEX externallink_id
  ON externallink
  USING btree
  (id);

-- Table: extrinsicobject

-- DROP TABLE extrinsicobject;

CREATE TABLE extrinsicobject
(
  accesscontrolpolicy character varying(64),
  id character varying(64) NOT NULL,
  objecttype character varying(64),
  expiration character varying(30),
  majorversion integer NOT NULL,
  minorversion integer NOT NULL,
  stability character varying(128),
  status character varying(128) NOT NULL,
  userversion character varying(64),
  isopaque character varying(1) NOT NULL,
  mimetype character varying(128),
  CONSTRAINT extrinsicobject_pkey PRIMARY KEY (id)
)
WITH (OIDS=TRUE);
ALTER TABLE extrinsicobject OWNER TO ebxmlrr;

-- Index: extrinsicobject_i1

-- DROP INDEX extrinsicobject_i1;

CREATE INDEX extrinsicobject_i1
  ON extrinsicobject
  USING btree
  (status);

-- Index: extrinsicobject_id

-- DROP INDEX extrinsicobject_id;

CREATE INDEX extrinsicobject_id
  ON extrinsicobject
  USING btree
  (id);

-- Table: name_

-- DROP TABLE name_;

CREATE TABLE name_
(
  charset character varying(32),
  lang character varying(32) NOT NULL,
  "value" character varying(256) NOT NULL,
  parent character varying(64) NOT NULL,
  CONSTRAINT name__pkey PRIMARY KEY (parent, lang, value)
)
WITH (OIDS=TRUE);
ALTER TABLE name_ OWNER TO ebxmlrr;

-- Index: name_i1

-- DROP INDEX name_i1;

CREATE INDEX name_i1
  ON name_
  USING btree
  (value);

-- Index: name_i2

-- DROP INDEX name_i2;

CREATE INDEX name_i2
  ON name_
  USING btree
  (lang, value);

-- Table: organization

-- DROP TABLE organization;

CREATE TABLE organization
(
  accesscontrolpolicy character varying(64),
  id character varying(64) NOT NULL,
  objecttype character varying(64),
  parent character varying(64),
  primarycontact character varying(64) NOT NULL,
  CONSTRAINT organization_pkey PRIMARY KEY (id),
  CONSTRAINT organization_objecttype CHECK (objecttype::text = 'Organization'::character varying::text)
)
WITH (OIDS=TRUE);
ALTER TABLE organization OWNER TO ebxmlrr;

-- Index: organization_i1

-- DROP INDEX organization_i1;

CREATE INDEX organization_i1
  ON organization
  USING btree
  (parent);

-- Index: organization_id

-- DROP INDEX organization_id;

CREATE INDEX organization_id
  ON organization
  USING btree
  (id);

-- Table: postaladdress

-- DROP TABLE postaladdress;

CREATE TABLE postaladdress
(
  city character varying(64),
  country character varying(64),
  postalcode character varying(64),
  state character varying(64),
  street character varying(64),
  streetnumber character varying(32),
  parent character varying(64) NOT NULL
)
WITH (OIDS=TRUE);
ALTER TABLE postaladdress OWNER TO ebxmlrr;

-- Index: postaladdress_i1

-- DROP INDEX postaladdress_i1;

CREATE INDEX postaladdress_i1
  ON postaladdress
  USING btree
  (parent);

-- Index: postaladdress_i2

-- DROP INDEX postaladdress_i2;

CREATE INDEX postaladdress_i2
  ON postaladdress
  USING btree
  (city);

-- Index: postaladdress_i3

-- DROP INDEX postaladdress_i3;

CREATE INDEX postaladdress_i3
  ON postaladdress
  USING btree
  (country);

-- Index: postaladdress_i4

-- DROP INDEX postaladdress_i4;

CREATE INDEX postaladdress_i4
  ON postaladdress
  USING btree
  (postalcode);

-- Table: registrypackage

-- DROP TABLE registrypackage;

CREATE TABLE registrypackage
(
  accesscontrolpolicy character varying(64),
  id character varying(64) NOT NULL,
  objecttype character varying(64),
  expiration character varying(30),
  majorversion integer NOT NULL,
  minorversion integer NOT NULL,
  stability character varying(128),
  status character varying(128) NOT NULL,
  userversion character varying(64),
  CONSTRAINT registrypackage_pkey PRIMARY KEY (id),
  CONSTRAINT registrypackage_objecttype CHECK (objecttype::text = 'RegistryPackage'::character varying::text)
)
WITH (OIDS=TRUE);
ALTER TABLE registrypackage OWNER TO ebxmlrr;

-- Index: registrypackage_id

-- DROP INDEX registrypackage_id;

CREATE INDEX registrypackage_id
  ON registrypackage
  USING btree
  (id);

-- Table: service

-- DROP TABLE service;

CREATE TABLE service
(
  accesscontrolpolicy character varying(64),
  id character varying(64) NOT NULL,
  objecttype character varying(64),
  expiration character varying(30),
  majorversion integer NOT NULL,
  minorversion integer NOT NULL,
  stability character varying(128),
  status character varying(128) NOT NULL,
  userversion character varying(64),
  CONSTRAINT service_pkey PRIMARY KEY (id),
  CONSTRAINT service_objecttype CHECK (objecttype::text = 'Service'::character varying::text)
)
WITH (OIDS=TRUE);
ALTER TABLE service OWNER TO ebxmlrr;

-- Index: service_id

-- DROP INDEX service_id;

CREATE INDEX service_id
  ON service
  USING btree
  (id);

-- Table: servicebinding

-- DROP TABLE servicebinding;

CREATE TABLE servicebinding
(
  accesscontrolpolicy character varying(64),
  id character varying(64) NOT NULL,
  objecttype character varying(64),
  service character varying(64) NOT NULL,
  accessuri character varying(256),
  targetbinding character varying(64),
  CONSTRAINT servicebinding_pkey PRIMARY KEY (id),
  CONSTRAINT servicebinding_objecttype CHECK (objecttype::text = 'ServiceBinding'::character varying::text)
)
WITH (OIDS=TRUE);
ALTER TABLE servicebinding OWNER TO ebxmlrr;

-- Index: servicebinding_i1

-- DROP INDEX servicebinding_i1;

CREATE INDEX servicebinding_i1
  ON servicebinding
  USING btree
  (service);

-- Index: servicebinding_id

-- DROP INDEX servicebinding_id;

CREATE INDEX servicebinding_id
  ON servicebinding
  USING btree
  (id);

-- Table: slot

-- DROP TABLE slot;

CREATE TABLE slot
(
  sequenceid integer NOT NULL,
  name_ character varying(128) NOT NULL,
  slottype character varying(128),
  "value" character varying(128),
  parent character varying(64) NOT NULL,
  CONSTRAINT slot_pkey PRIMARY KEY (parent, name_, sequenceid)
)
WITH (OIDS=TRUE);
ALTER TABLE slot OWNER TO ebxmlrr;

-- Index: slot_i1

-- DROP INDEX slot_i1;

CREATE INDEX slot_i1
  ON slot
  USING btree
  (parent);

-- Index: slot_i2

-- DROP INDEX slot_i2;

CREATE INDEX slot_i2
  ON slot
  USING btree
  (name_);

-- Table: specificationlink

-- DROP TABLE specificationlink;

CREATE TABLE specificationlink
(
  accesscontrolpolicy character varying(64),
  id character varying(64) NOT NULL,
  objecttype character varying(64),
  service character varying(64) NOT NULL,
  servicebinding character varying(64) NOT NULL,
  specificationobject character varying(64) NOT NULL,
  CONSTRAINT specificationlink_pkey PRIMARY KEY (id),
  CONSTRAINT specificationlink_objecttype CHECK (objecttype::text = 'SpecificationLink'::character varying::text)
)
WITH (OIDS=TRUE);
ALTER TABLE specificationlink OWNER TO ebxmlrr;

-- Index: specificationlink_i1

-- DROP INDEX specificationlink_i1;

CREATE INDEX specificationlink_i1
  ON specificationlink
  USING btree
  (service);

-- Index: specificationlink_i2

-- DROP INDEX specificationlink_i2;

CREATE INDEX specificationlink_i2
  ON specificationlink
  USING btree
  (servicebinding);

-- Index: specificationlink_i3

-- DROP INDEX specificationlink_i3;

CREATE INDEX specificationlink_i3
  ON specificationlink
  USING btree
  (specificationobject);

-- Index: specificationlink_id

-- DROP INDEX specificationlink_id;

CREATE INDEX specificationlink_id
  ON specificationlink
  USING btree
  (id);

-- Table: telephonenumber

-- DROP TABLE telephonenumber;

CREATE TABLE telephonenumber
(
  areacode character varying(4),
  countrycode character varying(4),
  extension character varying(8),
  number_ character varying(16),
  phonetype character varying(32),
  url character varying(256),
  parent character varying(64) NOT NULL
)
WITH (OIDS=TRUE);
ALTER TABLE telephonenumber OWNER TO ebxmlrr;

-- Index: telephonenumber_i1

-- DROP INDEX telephonenumber_i1;

CREATE INDEX telephonenumber_i1
  ON telephonenumber
  USING btree
  (parent);

-- Table: usagedescription

-- DROP TABLE usagedescription;

CREATE TABLE usagedescription
(
  charset character varying(32),
  lang character varying(32) NOT NULL,
  "value" character varying(256) NOT NULL,
  parent character varying(64) NOT NULL,
  CONSTRAINT usagedescription_pkey PRIMARY KEY (parent, lang, value)
)
WITH (OIDS=TRUE);
ALTER TABLE usagedescription OWNER TO ebxmlrr;

-- Index: usagedescription_i1

-- DROP INDEX usagedescription_i1;

CREATE INDEX usagedescription_i1
  ON usagedescription
  USING btree
  (value);

-- Index: usagedescription_i2

-- DROP INDEX usagedescription_i2;

CREATE INDEX usagedescription_i2
  ON usagedescription
  USING btree
  (lang, value);

-- Table: usageparameter

-- DROP TABLE usageparameter;

CREATE TABLE usageparameter
(
  "value" character varying(256) NOT NULL,
  parent character varying(64) NOT NULL
)
WITH (OIDS=TRUE);
ALTER TABLE usageparameter OWNER TO ebxmlrr;

-- Table: user_

-- DROP TABLE user_;

CREATE TABLE user_
(
  accesscontrolpolicy character varying(64),
  id character varying(64) NOT NULL,
  objecttype character varying(64),
  email character varying(128) NOT NULL,
  organization character varying(64) NOT NULL,
  personname_firstname character varying(64),
  personname_middlename character varying(64),
  personname_lastname character varying(64),
  url character varying(256),
  CONSTRAINT user__pkey PRIMARY KEY (id),
  CONSTRAINT user__objecttype CHECK (objecttype::text = 'User'::character varying::text)
)
WITH (OIDS=TRUE);
ALTER TABLE user_ OWNER TO ebxmlrr;

-- Index: user_i1

-- DROP INDEX user_i1;

CREATE INDEX user_i1
  ON user_
  USING btree
  (organization);

-- Index: user_i2

-- DROP INDEX user_i2;

CREATE INDEX user_i2
  ON user_
  USING btree
  (personname_lastname);

-- Index: user_id

-- DROP INDEX user_id;

CREATE INDEX user_id
  ON user_
  USING btree
  (id);


-- View: registryentry

-- DROP VIEW registryentry;

CREATE OR REPLACE VIEW registryentry AS 
( SELECT classscheme.accesscontrolpolicy, classscheme.id, classscheme.objecttype, classscheme.expiration, classscheme.majorversion, classscheme.minorversion, classscheme.stability, classscheme.status, classscheme.userversion
   FROM classscheme
UNION 
 SELECT extrinsicobject.accesscontrolpolicy, extrinsicobject.id, extrinsicobject.objecttype, extrinsicobject.expiration, extrinsicobject.majorversion, extrinsicobject.minorversion, extrinsicobject.stability, extrinsicobject.status, extrinsicobject.userversion
   FROM extrinsicobject)
UNION 
 SELECT registrypackage.accesscontrolpolicy, registrypackage.id, registrypackage.objecttype, registrypackage.expiration, registrypackage.majorversion, registrypackage.minorversion, registrypackage.stability, registrypackage.status, registrypackage.userversion
   FROM registrypackage;

ALTER TABLE registryentry OWNER TO ebxmlrr;

-- View: registryobject

-- DROP VIEW registryobject;

CREATE OR REPLACE VIEW registryobject AS 
(((((((((((( SELECT association.accesscontrolpolicy, association.id, association.objecttype
   FROM association
UNION 
 SELECT auditableevent.accesscontrolpolicy, auditableevent.id, auditableevent.objecttype
   FROM auditableevent)
UNION 
 SELECT classification.accesscontrolpolicy, classification.id, classification.objecttype
   FROM classification)
UNION 
 SELECT classificationnode.accesscontrolpolicy, classificationnode.id, classificationnode.objecttype
   FROM classificationnode)
UNION 
 SELECT classscheme.accesscontrolpolicy, classscheme.id, classscheme.objecttype
   FROM classscheme)
UNION 
 SELECT externalidentifier.accesscontrolpolicy, externalidentifier.id, externalidentifier.objecttype
   FROM externalidentifier)
UNION 
 SELECT externallink.accesscontrolpolicy, externallink.id, externallink.objecttype
   FROM externallink)
UNION 
 SELECT extrinsicobject.accesscontrolpolicy, extrinsicobject.id, extrinsicobject.objecttype
   FROM extrinsicobject)
UNION 
 SELECT organization.accesscontrolpolicy, organization.id, organization.objecttype
   FROM organization)
UNION 
 SELECT registrypackage.accesscontrolpolicy, registrypackage.id, registrypackage.objecttype
   FROM registrypackage)
UNION 
 SELECT service.accesscontrolpolicy, service.id, service.objecttype
   FROM service)
UNION 
 SELECT servicebinding.accesscontrolpolicy, servicebinding.id, servicebinding.objecttype
   FROM servicebinding)
UNION 
 SELECT specificationlink.accesscontrolpolicy, specificationlink.id, specificationlink.objecttype
   FROM specificationlink)
UNION 
 SELECT user_.accesscontrolpolicy, user_.id, user_.objecttype
   FROM user_;

ALTER TABLE registryobject OWNER TO ebxmlrr;



