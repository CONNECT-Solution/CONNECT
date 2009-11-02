-- Table: ip

-- DROP TABLE ip;

CREATE TABLE ip
(
  ip character varying(100) NOT NULL,
  company_name character varying(255) NOT NULL DEFAULT 'Unknown'::character varying,
  email character varying,
  CONSTRAINT ip_pkey PRIMARY KEY (ip)
)
WITH (OIDS=FALSE);
ALTER TABLE ip OWNER TO logs;

-- Index: "IP_INDEX"

-- DROP INDEX "IP_INDEX";

CREATE INDEX "IP_INDEX"
  ON ip
  USING btree
  (ip);


-- Table: main

-- DROP TABLE main;

CREATE TABLE main
(
  messageid character varying(255) NOT NULL,
  is_secure boolean,
  ip character varying(100) NOT NULL,
  timereceived timestamp without time zone NOT NULL DEFAULT '2008-08-30 19:56:01.093'::timestamp without time zone,
  test text NOT NULL,
  pass boolean,
  CONSTRAINT main_pkey PRIMARY KEY (messageid),
  CONSTRAINT main_ip_fkey FOREIGN KEY (ip)
      REFERENCES ip (ip) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS=FALSE);
ALTER TABLE main OWNER TO logs;

-- Index: "MAIN_IP_INDEX"

-- DROP INDEX "MAIN_IP_INDEX";

CREATE INDEX "MAIN_IP_INDEX"
  ON main
  USING btree
  (ip);

-- Index: "MAIN_MID_INDEX"

-- DROP INDEX "MAIN_MID_INDEX";

CREATE INDEX "MAIN_MID_INDEX"
  ON main
  USING btree
  (messageid);



-- Table: error

-- DROP TABLE error;

CREATE TABLE error
(
  messageid character varying(255) NOT NULL,
  "name" character varying(255) NOT NULL,
  "value" text,
  seqid integer NOT NULL DEFAULT 0,
  CONSTRAINT error_messageid_fkey FOREIGN KEY (messageid)
      REFERENCES main (messageid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (OIDS=FALSE);
ALTER TABLE error OWNER TO logs;

-- Index: "ERROR_MID_INDEX"

-- DROP INDEX "ERROR_MID_INDEX";

CREATE INDEX "ERROR_MID_INDEX"
  ON error
  USING btree
  (messageid);



-- Table: http

-- DROP TABLE http;

CREATE TABLE http
(
  messageid character varying(255) NOT NULL,
  "name" character varying(255) NOT NULL,
  "value" text,
  seqid integer NOT NULL DEFAULT 0,
  CONSTRAINT http_messageid_fkey FOREIGN KEY (messageid)
      REFERENCES main (messageid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (OIDS=FALSE);
ALTER TABLE http OWNER TO logs;

-- Index: "HTTP_MID_INDEX"

-- DROP INDEX "HTTP_MID_INDEX";

CREATE INDEX "HTTP_MID_INDEX"
  ON http
  USING btree
  (messageid);




-- Table: other

-- DROP TABLE other;

CREATE TABLE other
(
  messageid character varying(255) NOT NULL,
  "name" character varying(255) NOT NULL,
  "value" text,
  seqid integer NOT NULL DEFAULT 0,
  CONSTRAINT other_messageid_fkey FOREIGN KEY (messageid)
      REFERENCES main (messageid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (OIDS=FALSE);
ALTER TABLE other OWNER TO logs;

-- Index: "OTHER_MID_INDEX"

-- DROP INDEX "OTHER_MID_INDEX";

CREATE INDEX "OTHER_MID_INDEX"
  ON other
  USING btree
  (messageid);



-- Table: soap

-- DROP TABLE soap;

CREATE TABLE soap
(
  messageid character varying(255) NOT NULL,
  "name" character varying(255) NOT NULL,
  "value" text,
  seqid integer NOT NULL DEFAULT 0,
  CONSTRAINT soap_messageid_fkey FOREIGN KEY (messageid)
      REFERENCES main (messageid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (OIDS=FALSE);
ALTER TABLE soap OWNER TO logs;

-- Index: "SOAP_MID_INDEX"

-- DROP INDEX "SOAP_MID_INDEX";

CREATE INDEX "SOAP_MID_INDEX"
  ON soap
  USING btree
  (messageid);
