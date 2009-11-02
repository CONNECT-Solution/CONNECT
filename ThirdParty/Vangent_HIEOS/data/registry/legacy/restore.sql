-- create temporary table pgdump_restore_path(p text);
--
-- NOTE:
--
-- File paths need to be edited. Search for $$PATH$$ and
-- replace it with the path to the directory containing
-- the extracted data files.
--
-- Edit the following to match the path where the
-- tar archive has been extracted.
--
-- insert into pgdump_restore_path values('/xdstmp');

--
-- PostgreSQL database dump
--

SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

-- NOTE (BHT): DO NOT RUN DROP ROUTINES IF STARTING FROM SCRATCH (SCRIPT WILL FAIL TO PROCEED).

DROP INDEX public.user_id;
DROP INDEX public.user_i2;
DROP INDEX public.user_i1;
DROP INDEX public.usagedescription_i2;
DROP INDEX public.usagedescription_i1;
DROP INDEX public.telephonenumber_i1;
DROP INDEX public.specificationlink_id;
DROP INDEX public.specificationlink_i3;
DROP INDEX public.specificationlink_i2;
DROP INDEX public.specificationlink_i1;
DROP INDEX public.slot_i2;
DROP INDEX public.slot_i1;
DROP INDEX public.servicebinding_id;
DROP INDEX public.servicebinding_i1;
DROP INDEX public.service_id;
DROP INDEX public.registrypackage_id;
DROP INDEX public.postaladdress_i4;
DROP INDEX public.postaladdress_i3;
DROP INDEX public.postaladdress_i2;
DROP INDEX public.postaladdress_i1;
DROP INDEX public.organization_id;
DROP INDEX public.organization_i1;
DROP INDEX public.name_i2;
DROP INDEX public.name_i1;
DROP INDEX public.extrinsicobject_id;
DROP INDEX public.extrinsicobject_i1;
DROP INDEX public.externallink_id;
DROP INDEX public.externallink_i1;
DROP INDEX public.externalidentifier_id;
DROP INDEX public.externalidentifier_i1;
DROP INDEX public.emailaddress_i1;
DROP INDEX public.description_i2;
DROP INDEX public.description_i1;
DROP INDEX public.classscheme_id;
DROP INDEX public.classificationnode_id;
DROP INDEX public.classificationnode_i3;
DROP INDEX public.classificationnode_i2;
DROP INDEX public.classificationnode_i1;
DROP INDEX public.classification_id;
DROP INDEX public.classification_i2;
DROP INDEX public.classification_i1;
DROP INDEX public.auditableevent_id;
DROP INDEX public.association_id;
DROP INDEX public.association_i3;
DROP INDEX public.association_i2;
DROP INDEX public.association_i1;
ALTER TABLE ONLY public.user_ DROP CONSTRAINT user__pkey;
ALTER TABLE ONLY public.usagedescription DROP CONSTRAINT usagedescription_pkey;
ALTER TABLE ONLY public.specificationlink DROP CONSTRAINT specificationlink_pkey;
ALTER TABLE ONLY public.slot DROP CONSTRAINT slot_pkey;
ALTER TABLE ONLY public.servicebinding DROP CONSTRAINT servicebinding_pkey;
ALTER TABLE ONLY public.service DROP CONSTRAINT service_pkey;
ALTER TABLE ONLY public.registrypackage DROP CONSTRAINT registrypackage_pkey;
ALTER TABLE ONLY public.organization DROP CONSTRAINT organization_pkey;
ALTER TABLE ONLY public.name_ DROP CONSTRAINT name__pkey;
ALTER TABLE ONLY public.extrinsicobject DROP CONSTRAINT extrinsicobject_pkey;
ALTER TABLE ONLY public.externallink DROP CONSTRAINT externallink_pkey;
ALTER TABLE ONLY public.externalidentifier DROP CONSTRAINT externalidentifier_pkey;
ALTER TABLE ONLY public.description DROP CONSTRAINT description_pkey;
ALTER TABLE ONLY public.classscheme DROP CONSTRAINT classscheme_pkey;
ALTER TABLE ONLY public.classificationnode DROP CONSTRAINT classificationnode_pkey;
ALTER TABLE ONLY public.classification DROP CONSTRAINT classification_pkey;
ALTER TABLE ONLY public.auditableevent DROP CONSTRAINT auditableevent_pkey;
ALTER TABLE ONLY public.association DROP CONSTRAINT association_pkey;
DROP TABLE public.usageparameter;
DROP TABLE public.usagedescription;
DROP TABLE public.telephonenumber;
DROP TABLE public.slot;
DROP VIEW public.registryobject;
DROP TABLE public.user_;
DROP TABLE public.specificationlink;
DROP TABLE public.servicebinding;
DROP TABLE public.service;
DROP VIEW public.registryentry;
DROP TABLE public.registrypackage;
DROP TABLE public.postaladdress;
DROP TABLE public.organization;
DROP TABLE public.name_;
DROP TABLE public.extrinsicobject;
DROP TABLE public.externallink;
DROP TABLE public.externalidentifier;
DROP TABLE public.emailaddress;
DROP TABLE public.description;
DROP TABLE public.classscheme;
DROP TABLE public.classificationnode;
DROP TABLE public.classification;
DROP TABLE public.auditableevent;
DROP TABLE public.association;
DROP SCHEMA public;
--
-- Name: public; Type: SCHEMA; Schema: -; Owner: bill
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO postgres;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: bill
--

COMMENT ON SCHEMA public IS 'Standard public schema';


SET default_tablespace = '';

SET default_with_oids = true;

--
-- Name: association; Type: TABLE; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE TABLE association (
    accesscontrolpolicy character varying(64),
    id character varying(64) NOT NULL,
    objecttype character varying(64),
    associationtype character varying(128) NOT NULL,
    sourceobject character varying(64) NOT NULL,
    targetobject character varying(64) NOT NULL,
    isconfirmedbysourceowner character varying(1),
    isconfirmedbytargetowner character varying(1),
    CONSTRAINT association_objecttype CHECK (((objecttype)::text = ('Association'::character varying)::text))
);


ALTER TABLE public.association OWNER TO xdsoper;

--
-- Name: auditableevent; Type: TABLE; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE TABLE auditableevent (
    accesscontrolpolicy character varying(64),
    id character varying(64) NOT NULL,
    objecttype character varying(64),
    eventtype character varying(128) NOT NULL,
    registryobject character varying(64) NOT NULL,
    timestamp_ character varying(30) NOT NULL,
    user_ character varying(64) NOT NULL,
    CONSTRAINT auditableevent_objecttype CHECK (((objecttype)::text = ('AuditableEvent'::character varying)::text))
);


ALTER TABLE public.auditableevent OWNER TO xdsoper;

--
-- Name: classification; Type: TABLE; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE TABLE classification (
    accesscontrolpolicy character varying(64),
    id character varying(64) NOT NULL,
    objecttype character varying(64),
    classificationnode character varying(64),
    classificationscheme character varying(64),
    classifiedobject character varying(64) NOT NULL,
    noderepresentation character varying(128),
    CONSTRAINT classification_objecttype CHECK (((objecttype)::text = ('Classification'::character varying)::text))
);


ALTER TABLE public.classification OWNER TO xdsoper;

--
-- Name: classificationnode; Type: TABLE; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE TABLE classificationnode (
    accesscontrolpolicy character varying(64),
    id character varying(64) NOT NULL,
    objecttype character varying(64),
    code character varying(64),
    parent character varying(64),
    path character varying(1024),
    CONSTRAINT classificationnode_objecttype CHECK (((objecttype)::text = ('ClassificationNode'::character varying)::text))
);


ALTER TABLE public.classificationnode OWNER TO xdsoper;

--
-- Name: classscheme; Type: TABLE; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE TABLE classscheme (
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
    CONSTRAINT classscheme_objecttype CHECK (((objecttype)::text = ('ClassificationScheme'::character varying)::text))
);


ALTER TABLE public.classscheme OWNER TO xdsoper;

--
-- Name: description; Type: TABLE; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE TABLE description (
    charset character varying(32),
    lang character varying(32) NOT NULL,
    value character varying(256) NOT NULL,
    parent character varying(64) NOT NULL
);


ALTER TABLE public.description OWNER TO xdsoper;

--
-- Name: emailaddress; Type: TABLE; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE TABLE emailaddress (
    address character varying(64) NOT NULL,
    "type" character varying(32),
    parent character varying(64) NOT NULL
);


ALTER TABLE public.emailaddress OWNER TO xdsoper;

--
-- Name: externalidentifier; Type: TABLE; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE TABLE externalidentifier (
    accesscontrolpolicy character varying(64),
    id character varying(64) NOT NULL,
    objecttype character varying(64),
    registryobject character varying(64) NOT NULL,
    identificationscheme character varying(64) NOT NULL,
    value character varying(64) NOT NULL,
    CONSTRAINT externalidentifier_objecttype CHECK (((objecttype)::text = ('ExternalIdentifier'::character varying)::text))
);


ALTER TABLE public.externalidentifier OWNER TO xdsoper;

--
-- Name: externallink; Type: TABLE; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE TABLE externallink (
    accesscontrolpolicy character varying(64),
    id character varying(64) NOT NULL,
    objecttype character varying(64),
    externaluri character varying(256) NOT NULL,
    CONSTRAINT externallink_objecttype CHECK (((objecttype)::text = ('ExternalLink'::character varying)::text))
);


ALTER TABLE public.externallink OWNER TO xdsoper;

--
-- Name: extrinsicobject; Type: TABLE; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE TABLE extrinsicobject (
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
    mimetype character varying(128)
);


ALTER TABLE public.extrinsicobject OWNER TO xdsoper;

--
-- Name: name_; Type: TABLE; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE TABLE name_ (
    charset character varying(32),
    lang character varying(32) NOT NULL,
    value character varying(256) NOT NULL,
    parent character varying(64) NOT NULL
);


ALTER TABLE public.name_ OWNER TO xdsoper;

--
-- Name: organization; Type: TABLE; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE TABLE organization (
    accesscontrolpolicy character varying(64),
    id character varying(64) NOT NULL,
    objecttype character varying(64),
    parent character varying(64),
    primarycontact character varying(64) NOT NULL,
    CONSTRAINT organization_objecttype CHECK (((objecttype)::text = ('Organization'::character varying)::text))
);


ALTER TABLE public.organization OWNER TO xdsoper;

--
-- Name: postaladdress; Type: TABLE; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE TABLE postaladdress (
    city character varying(64),
    country character varying(64),
    postalcode character varying(64),
    state character varying(64),
    street character varying(64),
    streetnumber character varying(32),
    parent character varying(64) NOT NULL
);


ALTER TABLE public.postaladdress OWNER TO xdsoper;

--
-- Name: registrypackage; Type: TABLE; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE TABLE registrypackage (
    accesscontrolpolicy character varying(64),
    id character varying(64) NOT NULL,
    objecttype character varying(64),
    expiration character varying(30),
    majorversion integer NOT NULL,
    minorversion integer NOT NULL,
    stability character varying(128),
    status character varying(128) NOT NULL,
    userversion character varying(64),
    CONSTRAINT registrypackage_objecttype CHECK (((objecttype)::text = ('RegistryPackage'::character varying)::text))
);


ALTER TABLE public.registrypackage OWNER TO xdsoper;

--
-- Name: registryentry; Type: VIEW; Schema: public; Owner: xdsoper
--

CREATE VIEW registryentry AS
    (SELECT classscheme.accesscontrolpolicy, classscheme.id, classscheme.objecttype, classscheme.expiration, classscheme.majorversion, classscheme.minorversion, classscheme.stability, classscheme.status, classscheme.userversion FROM classscheme UNION SELECT extrinsicobject.accesscontrolpolicy, extrinsicobject.id, extrinsicobject.objecttype, extrinsicobject.expiration, extrinsicobject.majorversion, extrinsicobject.minorversion, extrinsicobject.stability, extrinsicobject.status, extrinsicobject.userversion FROM extrinsicobject) UNION SELECT registrypackage.accesscontrolpolicy, registrypackage.id, registrypackage.objecttype, registrypackage.expiration, registrypackage.majorversion, registrypackage.minorversion, registrypackage.stability, registrypackage.status, registrypackage.userversion FROM registrypackage;


ALTER TABLE public.registryentry OWNER TO xdsoper;

--
-- Name: service; Type: TABLE; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE TABLE service (
    accesscontrolpolicy character varying(64),
    id character varying(64) NOT NULL,
    objecttype character varying(64),
    expiration character varying(30),
    majorversion integer NOT NULL,
    minorversion integer NOT NULL,
    stability character varying(128),
    status character varying(128) NOT NULL,
    userversion character varying(64),
    CONSTRAINT service_objecttype CHECK (((objecttype)::text = ('Service'::character varying)::text))
);


ALTER TABLE public.service OWNER TO xdsoper;

--
-- Name: servicebinding; Type: TABLE; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE TABLE servicebinding (
    accesscontrolpolicy character varying(64),
    id character varying(64) NOT NULL,
    objecttype character varying(64),
    service character varying(64) NOT NULL,
    accessuri character varying(256),
    targetbinding character varying(64),
    CONSTRAINT servicebinding_objecttype CHECK (((objecttype)::text = ('ServiceBinding'::character varying)::text))
);


ALTER TABLE public.servicebinding OWNER TO xdsoper;

--
-- Name: specificationlink; Type: TABLE; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE TABLE specificationlink (
    accesscontrolpolicy character varying(64),
    id character varying(64) NOT NULL,
    objecttype character varying(64),
    service character varying(64) NOT NULL,
    servicebinding character varying(64) NOT NULL,
    specificationobject character varying(64) NOT NULL,
    CONSTRAINT specificationlink_objecttype CHECK (((objecttype)::text = ('SpecificationLink'::character varying)::text))
);


ALTER TABLE public.specificationlink OWNER TO xdsoper;

--
-- Name: user_; Type: TABLE; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE TABLE user_ (
    accesscontrolpolicy character varying(64),
    id character varying(64) NOT NULL,
    objecttype character varying(64),
    email character varying(128) NOT NULL,
    organization character varying(64) NOT NULL,
    personname_firstname character varying(64),
    personname_middlename character varying(64),
    personname_lastname character varying(64),
    url character varying(256),
    CONSTRAINT user__objecttype CHECK (((objecttype)::text = ('User'::character varying)::text))
);


ALTER TABLE public.user_ OWNER TO xdsoper;

--
-- Name: registryobject; Type: VIEW; Schema: public; Owner: xdsoper
--

CREATE VIEW registryobject AS
    ((((((((((((SELECT association.accesscontrolpolicy, association.id, association.objecttype FROM association UNION SELECT auditableevent.accesscontrolpolicy, auditableevent.id, auditableevent.objecttype FROM auditableevent) UNION SELECT classification.accesscontrolpolicy, classification.id, classification.objecttype FROM classification) UNION SELECT classificationnode.accesscontrolpolicy, classificationnode.id, classificationnode.objecttype FROM classificationnode) UNION SELECT classscheme.accesscontrolpolicy, classscheme.id, classscheme.objecttype FROM classscheme) UNION SELECT externalidentifier.accesscontrolpolicy, externalidentifier.id, externalidentifier.objecttype FROM externalidentifier) UNION SELECT externallink.accesscontrolpolicy, externallink.id, externallink.objecttype FROM externallink) UNION SELECT extrinsicobject.accesscontrolpolicy, extrinsicobject.id, extrinsicobject.objecttype FROM extrinsicobject) UNION SELECT organization.accesscontrolpolicy, organization.id, organization.objecttype FROM organization) UNION SELECT registrypackage.accesscontrolpolicy, registrypackage.id, registrypackage.objecttype FROM registrypackage) UNION SELECT service.accesscontrolpolicy, service.id, service.objecttype FROM service) UNION SELECT servicebinding.accesscontrolpolicy, servicebinding.id, servicebinding.objecttype FROM servicebinding) UNION SELECT specificationlink.accesscontrolpolicy, specificationlink.id, specificationlink.objecttype FROM specificationlink) UNION SELECT user_.accesscontrolpolicy, user_.id, user_.objecttype FROM user_;


ALTER TABLE public.registryobject OWNER TO xdsoper;

--
-- Name: slot; Type: TABLE; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE TABLE slot (
    sequenceid integer NOT NULL,
    name_ character varying(128) NOT NULL,
    slottype character varying(128),
    value character varying(128),
    parent character varying(64) NOT NULL
);


ALTER TABLE public.slot OWNER TO xdsoper;

--
-- Name: telephonenumber; Type: TABLE; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE TABLE telephonenumber (
    areacode character varying(4),
    countrycode character varying(4),
    extension character varying(8),
    number_ character varying(16),
    phonetype character varying(32),
    url character varying(256),
    parent character varying(64) NOT NULL
);


ALTER TABLE public.telephonenumber OWNER TO xdsoper;

--
-- Name: usagedescription; Type: TABLE; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE TABLE usagedescription (
    charset character varying(32),
    lang character varying(32) NOT NULL,
    value character varying(256) NOT NULL,
    parent character varying(64) NOT NULL
);


ALTER TABLE public.usagedescription OWNER TO xdsoper;

--
-- Name: usageparameter; Type: TABLE; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE TABLE usageparameter (
    value character varying(256) NOT NULL,
    parent character varying(64) NOT NULL
);


ALTER TABLE public.usageparameter OWNER TO xdsoper;

--
-- Data for Name: association; Type: TABLE DATA; Schema: public; Owner: xdsoper
--

--COPY association (accesscontrolpolicy, id, objecttype, associationtype, sourceobject, targetobject, isconfirmedbysourceowner, isconfirmedbytargetowner) --FROM stdin;
--\.
copy association (accesscontrolpolicy, id, objecttype, associationtype, sourceobject, targetobject, isconfirmedbysourceowner, isconfirmedbytargetowner) from 'c:/xdstmp/1654.dat' ;
--
-- Data for Name: auditableevent; Type: TABLE DATA; Schema: public; Owner: xdsoper
--

--COPY auditableevent (accesscontrolpolicy, id, objecttype, eventtype, registryobject, timestamp_, user_) FROM stdin;
--\.
copy auditableevent (accesscontrolpolicy, id, objecttype, eventtype, registryobject, timestamp_, user_)  from 'c:/xdstmp/1655.dat' ;
--
-- Data for Name: classification; Type: TABLE DATA; Schema: public; Owner: xdsoper
--

--COPY classification (accesscontrolpolicy, id, objecttype, classificationnode, classificationscheme, classifiedobject, noderepresentation) FROM stdin;
--\.
copy classification (accesscontrolpolicy, id, objecttype, classificationnode, classificationscheme, classifiedobject, noderepresentation) from 'c:/xdstmp/1656.dat' ;
--
-- Data for Name: classificationnode; Type: TABLE DATA; Schema: public; Owner: xdsoper
--

--COPY classificationnode (accesscontrolpolicy, id, objecttype, code, parent, path) FROM stdin;
--\.
copy classificationnode (accesscontrolpolicy, id, objecttype, code, parent, path)  from 'c:/xdstmp/1657.dat' ;
--
-- Data for Name: classscheme; Type: TABLE DATA; Schema: public; Owner: xdsoper
--

--COPY classscheme (accesscontrolpolicy, id, objecttype, expiration, majorversion, minorversion, stability, status, userversion, isinternal, nodetype) FROM --stdin;
--\.
copy classscheme (accesscontrolpolicy, id, objecttype, expiration, majorversion, minorversion, stability, status, userversion, isinternal, nodetype) from 'c:/xdstmp/1658.dat' ;
--
-- Data for Name: description; Type: TABLE DATA; Schema: public; Owner: xdsoper
--

--COPY description (charset, lang, value, parent) FROM stdin;
--\.
copy description (charset, lang, value, parent)  from 'c:/xdstmp/1663.dat' ;
--
-- Data for Name: emailaddress; Type: TABLE DATA; Schema: public; Owner: xdsoper
--

--COPY emailaddress (address, "type", parent) FROM stdin;
--\.
copy emailaddress (address, "type", parent)  from 'c:/xdstmp/1668.dat' ;
--
-- Data for Name: externalidentifier; Type: TABLE DATA; Schema: public; Owner: xdsoper
--

--COPY externalidentifier (accesscontrolpolicy, id, objecttype, registryobject, identificationscheme, value) FROM stdin;
--\.
copy externalidentifier (accesscontrolpolicy, id, objecttype, registryobject, identificationscheme, value)  from 'c:/xdstmp/1659.dat' ;
--
-- Data for Name: externallink; Type: TABLE DATA; Schema: public; Owner: xdsoper
--

--COPY externallink (accesscontrolpolicy, id, objecttype, externaluri) FROM stdin;
--\.
copy externallink (accesscontrolpolicy, id, objecttype, externaluri)  from 'c:/xdstmp/1660.dat' ;
--
-- Data for Name: extrinsicobject; Type: TABLE DATA; Schema: public; Owner: xdsoper
--

--COPY extrinsicobject (accesscontrolpolicy, id, objecttype, expiration, majorversion, minorversion, stability, status, userversion, isopaque, mimetype) FROM --stdin;
--\.
copy extrinsicobject (accesscontrolpolicy, id, objecttype, expiration, majorversion, minorversion, stability, status, userversion, isopaque, mimetype) from 'c:/xdstmp/1661.dat' ;
--
-- Data for Name: name_; Type: TABLE DATA; Schema: public; Owner: xdsoper
--

--COPY name_ (charset, lang, value, parent) FROM stdin;
--\.
copy name_ (charset, lang, value, parent)  from 'c:/xdstmp/1662.dat' ;
--
-- Data for Name: organization; Type: TABLE DATA; Schema: public; Owner: xdsoper
--

--COPY organization (accesscontrolpolicy, id, objecttype, parent, primarycontact) FROM stdin;
--\.
copy organization (accesscontrolpolicy, id, objecttype, parent, primarycontact)  from 'c:/xdstmp/1665.dat' ;
--
-- Data for Name: postaladdress; Type: TABLE DATA; Schema: public; Owner: xdsoper
--

--COPY postaladdress (city, country, postalcode, state, street, streetnumber, parent) FROM stdin;
--\.
copy postaladdress (city, country, postalcode, state, street, streetnumber, parent)  from 'c:/xdstmp/1667.dat' ;
--
-- Data for Name: registrypackage; Type: TABLE DATA; Schema: public; Owner: xdsoper
--

--COPY registrypackage (accesscontrolpolicy, id, objecttype, expiration, majorversion, minorversion, stability, status, userversion) FROM stdin;
--\.
copy registrypackage (accesscontrolpolicy, id, objecttype, expiration, majorversion, minorversion, stability, status, userversion) from 'c:/xdstmp/1666.dat' ;
--
-- Data for Name: service; Type: TABLE DATA; Schema: public; Owner: xdsoper
--

--COPY service (accesscontrolpolicy, id, objecttype, expiration, majorversion, minorversion, stability, status, userversion) FROM stdin;
--\.
copy service (accesscontrolpolicy, id, objecttype, expiration, majorversion, minorversion, stability, status, userversion)  from 'c:/xdstmp/1669.dat' ;
--
-- Data for Name: servicebinding; Type: TABLE DATA; Schema: public; Owner: xdsoper
--

--COPY servicebinding (accesscontrolpolicy, id, objecttype, service, accessuri, targetbinding) FROM stdin;
--\.
copy servicebinding (accesscontrolpolicy, id, objecttype, service, accessuri, targetbinding)  from 'c:/xdstmp/1670.dat' ;
--
-- Data for Name: slot; Type: TABLE DATA; Schema: public; Owner: xdsoper
--

--COPY slot (sequenceid, name_, slottype, value, parent) FROM stdin;
--\.
copy slot (sequenceid, name_, slottype, value, parent)  from 'c:/xdstmp/1671.dat' ;
--
-- Data for Name: specificationlink; Type: TABLE DATA; Schema: public; Owner: xdsoper
--

--COPY specificationlink (accesscontrolpolicy, id, objecttype, service, servicebinding, specificationobject) FROM stdin;
--\.
copy specificationlink (accesscontrolpolicy, id, objecttype, service, servicebinding, specificationobject)  from 'c:/xdstmp/1672.dat' ;
--
-- Data for Name: telephonenumber; Type: TABLE DATA; Schema: public; Owner: xdsoper
--

--COPY telephonenumber (areacode, countrycode, extension, number_, phonetype, url, parent) FROM stdin;
--\.
copy telephonenumber (areacode, countrycode, extension, number_, phonetype, url, parent)  from 'c:/xdstmp/1674.dat' ;
--
-- Data for Name: usagedescription; Type: TABLE DATA; Schema: public; Owner: xdsoper
--

--COPY usagedescription (charset, lang, value, parent) FROM stdin;
--\.
copy usagedescription (charset, lang, value, parent)  from 'c:/xdstmp/1664.dat' ;
--
-- Data for Name: usageparameter; Type: TABLE DATA; Schema: public; Owner: xdsoper
--

--COPY usageparameter (value, parent) FROM stdin;
--\.
copy usageparameter (value, parent)  from 'c:/xdstmp/1673.dat' ;
--
-- Data for Name: user_; Type: TABLE DATA; Schema: public; Owner: xdsoper
--

--COPY user_ (accesscontrolpolicy, id, objecttype, email, organization, personname_firstname, personname_middlename, personname_lastname, url) FROM stdin;
--\.
copy user_ (accesscontrolpolicy, id, objecttype, email, organization, personname_firstname, personname_middlename, personname_lastname, url) from 'c:/xdstmp/1675.dat' ;
--
-- Name: association_pkey; Type: CONSTRAINT; Schema: public; Owner: xdsoper; Tablespace: 
--

ALTER TABLE ONLY association
    ADD CONSTRAINT association_pkey PRIMARY KEY (id);


--
-- Name: auditableevent_pkey; Type: CONSTRAINT; Schema: public; Owner: xdsoper; Tablespace: 
--

ALTER TABLE ONLY auditableevent
    ADD CONSTRAINT auditableevent_pkey PRIMARY KEY (id);


--
-- Name: classification_pkey; Type: CONSTRAINT; Schema: public; Owner: xdsoper; Tablespace: 
--

ALTER TABLE ONLY classification
    ADD CONSTRAINT classification_pkey PRIMARY KEY (id);


--
-- Name: classificationnode_pkey; Type: CONSTRAINT; Schema: public; Owner: xdsoper; Tablespace: 
--

ALTER TABLE ONLY classificationnode
    ADD CONSTRAINT classificationnode_pkey PRIMARY KEY (id);


--
-- Name: classscheme_pkey; Type: CONSTRAINT; Schema: public; Owner: xdsoper; Tablespace: 
--

ALTER TABLE ONLY classscheme
    ADD CONSTRAINT classscheme_pkey PRIMARY KEY (id);


--
-- Name: description_pkey; Type: CONSTRAINT; Schema: public; Owner: xdsoper; Tablespace: 
--

ALTER TABLE ONLY description
    ADD CONSTRAINT description_pkey PRIMARY KEY (parent, lang, value);


--
-- Name: externalidentifier_pkey; Type: CONSTRAINT; Schema: public; Owner: xdsoper; Tablespace: 
--

ALTER TABLE ONLY externalidentifier
    ADD CONSTRAINT externalidentifier_pkey PRIMARY KEY (id);


--
-- Name: externallink_pkey; Type: CONSTRAINT; Schema: public; Owner: xdsoper; Tablespace: 
--

ALTER TABLE ONLY externallink
    ADD CONSTRAINT externallink_pkey PRIMARY KEY (id);


--
-- Name: extrinsicobject_pkey; Type: CONSTRAINT; Schema: public; Owner: xdsoper; Tablespace: 
--

ALTER TABLE ONLY extrinsicobject
    ADD CONSTRAINT extrinsicobject_pkey PRIMARY KEY (id);


--
-- Name: name__pkey; Type: CONSTRAINT; Schema: public; Owner: xdsoper; Tablespace: 
--

ALTER TABLE ONLY name_
    ADD CONSTRAINT name__pkey PRIMARY KEY (parent, lang, value);


--
-- Name: organization_pkey; Type: CONSTRAINT; Schema: public; Owner: xdsoper; Tablespace: 
--

ALTER TABLE ONLY organization
    ADD CONSTRAINT organization_pkey PRIMARY KEY (id);


--
-- Name: registrypackage_pkey; Type: CONSTRAINT; Schema: public; Owner: xdsoper; Tablespace: 
--

ALTER TABLE ONLY registrypackage
    ADD CONSTRAINT registrypackage_pkey PRIMARY KEY (id);


--
-- Name: service_pkey; Type: CONSTRAINT; Schema: public; Owner: xdsoper; Tablespace: 
--

ALTER TABLE ONLY service
    ADD CONSTRAINT service_pkey PRIMARY KEY (id);


--
-- Name: servicebinding_pkey; Type: CONSTRAINT; Schema: public; Owner: xdsoper; Tablespace: 
--

ALTER TABLE ONLY servicebinding
    ADD CONSTRAINT servicebinding_pkey PRIMARY KEY (id);


--
-- Name: slot_pkey; Type: CONSTRAINT; Schema: public; Owner: xdsoper; Tablespace: 
--

ALTER TABLE ONLY slot
    ADD CONSTRAINT slot_pkey PRIMARY KEY (parent, name_, sequenceid);


--
-- Name: specificationlink_pkey; Type: CONSTRAINT; Schema: public; Owner: xdsoper; Tablespace: 
--

ALTER TABLE ONLY specificationlink
    ADD CONSTRAINT specificationlink_pkey PRIMARY KEY (id);


--
-- Name: usagedescription_pkey; Type: CONSTRAINT; Schema: public; Owner: xdsoper; Tablespace: 
--

ALTER TABLE ONLY usagedescription
    ADD CONSTRAINT usagedescription_pkey PRIMARY KEY (parent, lang, value);


--
-- Name: user__pkey; Type: CONSTRAINT; Schema: public; Owner: xdsoper; Tablespace: 
--

ALTER TABLE ONLY user_
    ADD CONSTRAINT user__pkey PRIMARY KEY (id);


--
-- Name: association_i1; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX association_i1 ON association USING btree (sourceobject);


--
-- Name: association_i2; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX association_i2 ON association USING btree (targetobject);


--
-- Name: association_i3; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX association_i3 ON association USING btree (associationtype);


--
-- Name: association_id; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX association_id ON association USING btree (id);


--
-- Name: auditableevent_id; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX auditableevent_id ON auditableevent USING btree (id);


--
-- Name: classification_i1; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX classification_i1 ON classification USING btree (classifiedobject);


--
-- Name: classification_i2; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX classification_i2 ON classification USING btree (classificationnode);


--
-- Name: classification_id; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX classification_id ON classification USING btree (id);


--
-- Name: classificationnode_i1; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX classificationnode_i1 ON classificationnode USING btree (parent);


--
-- Name: classificationnode_i2; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX classificationnode_i2 ON classificationnode USING btree (code);


--
-- Name: classificationnode_i3; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX classificationnode_i3 ON classificationnode USING btree (path);


--
-- Name: classificationnode_id; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX classificationnode_id ON classificationnode USING btree (id);


--
-- Name: classscheme_id; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX classscheme_id ON classscheme USING btree (id);


--
-- Name: description_i1; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX description_i1 ON description USING btree (value);


--
-- Name: description_i2; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX description_i2 ON description USING btree (lang, value);


--
-- Name: emailaddress_i1; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX emailaddress_i1 ON emailaddress USING btree (parent);


--
-- Name: externalidentifier_i1; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX externalidentifier_i1 ON externalidentifier USING btree (registryobject);


--
-- Name: externalidentifier_id; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX externalidentifier_id ON externalidentifier USING btree (id);


--
-- Name: externallink_i1; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX externallink_i1 ON externallink USING btree (externaluri);


--
-- Name: externallink_id; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX externallink_id ON externallink USING btree (id);


--
-- Name: extrinsicobject_i1; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX extrinsicobject_i1 ON extrinsicobject USING btree (status);


--
-- Name: extrinsicobject_id; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX extrinsicobject_id ON extrinsicobject USING btree (id);


--
-- Name: name_i1; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX name_i1 ON name_ USING btree (value);


--
-- Name: name_i2; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX name_i2 ON name_ USING btree (lang, value);


--
-- Name: organization_i1; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX organization_i1 ON organization USING btree (parent);


--
-- Name: organization_id; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX organization_id ON organization USING btree (id);


--
-- Name: postaladdress_i1; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX postaladdress_i1 ON postaladdress USING btree (parent);


--
-- Name: postaladdress_i2; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX postaladdress_i2 ON postaladdress USING btree (city);


--
-- Name: postaladdress_i3; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX postaladdress_i3 ON postaladdress USING btree (country);


--
-- Name: postaladdress_i4; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX postaladdress_i4 ON postaladdress USING btree (postalcode);


--
-- Name: registrypackage_id; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX registrypackage_id ON registrypackage USING btree (id);


--
-- Name: service_id; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX service_id ON service USING btree (id);


--
-- Name: servicebinding_i1; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX servicebinding_i1 ON servicebinding USING btree (service);


--
-- Name: servicebinding_id; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX servicebinding_id ON servicebinding USING btree (id);


--
-- Name: slot_i1; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX slot_i1 ON slot USING btree (parent);


--
-- Name: slot_i2; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX slot_i2 ON slot USING btree (name_);


--
-- Name: specificationlink_i1; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX specificationlink_i1 ON specificationlink USING btree (service);


--
-- Name: specificationlink_i2; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX specificationlink_i2 ON specificationlink USING btree (servicebinding);


--
-- Name: specificationlink_i3; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX specificationlink_i3 ON specificationlink USING btree (specificationobject);


--
-- Name: specificationlink_id; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX specificationlink_id ON specificationlink USING btree (id);


--
-- Name: telephonenumber_i1; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX telephonenumber_i1 ON telephonenumber USING btree (parent);


--
-- Name: usagedescription_i1; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX usagedescription_i1 ON usagedescription USING btree (value);


--
-- Name: usagedescription_i2; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX usagedescription_i2 ON usagedescription USING btree (lang, value);


--
-- Name: user_i1; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX user_i1 ON user_ USING btree (organization);


--
-- Name: user_i2; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX user_i2 ON user_ USING btree (personname_lastname);


--
-- Name: user_id; Type: INDEX; Schema: public; Owner: xdsoper; Tablespace: 
--

CREATE INDEX user_id ON user_ USING btree (id);


--
-- Name: public; Type: ACL; Schema: -; Owner: bill
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

