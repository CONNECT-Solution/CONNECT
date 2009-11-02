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

--
-- PostgreSQL database dump
--

--
-- Create schema repo
--

--
-- Definition of table document
--


CREATE TABLE document 
(
  uniqueid varchar(256) NOT NULL,
  hash varchar(256) NOT NULL,
  size BIGINT NOT NULL,
  mimetype varchar(256) NOT NULL,
  repositoryid varchar(256) NOT NULL,
  bytes byteA NOT NULL,
  documentid varchar(256) NOT NULL,
  CONSTRAINT doc_pkey PRIMARY KEY (uniqueid)
)WITH (OIDS=FALSE);


ALTER TABLE document OWNER TO repo;


-- CREATE INDEX "DOC_UNIQUEID_INDEX"
--  ON document
--  USING btree
--  (uniqueid);
