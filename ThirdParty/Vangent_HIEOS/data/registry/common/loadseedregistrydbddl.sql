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

-- loadseedregistrydb.sql
--
-- -----------------------------------------------------

--
-- Dumping data for table classificationnode
--

-- use omar;

INSERT INTO classificationnode (id,home,lid,objectType,status,versionName,comment_,code,parent,path) VALUES 
 ('urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember',NULL,'urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'HasMember','urn:oasis:names:tc:ebxml-regrep:classificationScheme:AssociationType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:AssociationType/HasMember'),
 ('urn:oasis:names:tc:ebxml-regrep:AssociationType:RelatedTo',NULL,'urn:oasis:names:tc:ebxml-regrep:AssociationType:RelatedTo','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'RelatedTo','urn:oasis:names:tc:ebxml-regrep:classificationScheme:AssociationType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:AssociationType/RelatedTo'),
 ('urn:oasis:names:tc:ebxml-regrep:AssociationType:Replaces',NULL,'urn:oasis:names:tc:ebxml-regrep:AssociationType:Replaces','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Replaces','urn:oasis:names:tc:ebxml-regrep:classificationScheme:AssociationType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:AssociationType/Replaces'),
 ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'RegistryObject','urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject'),
 ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Association',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Association','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Association','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/Association'),
 ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Classification','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/Classification'),
 ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'ClassificationNode','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/ClassificationNode'),
 ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/ClassificationScheme'),
 ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'ExternalIdentifier','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/ExternalIdentifier'),
 ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'ExtrinsicObject','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/ExtrinsicObject'),
 ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:RegistryPackage',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:RegistryPackage','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'RegistryPackage','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/RegistryPackage'),
 ('urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure',NULL,'urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Failure','urn:oasis:names:tc:ebxml-regrep:classificationScheme:ResponseStatusType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ResponseStatusType/Failure'),
 ('urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success',NULL,'urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Success','urn:oasis:names:tc:ebxml-regrep:classificationScheme:ResponseStatusType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ResponseStatusType/Success'),
 ('urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Unavailable',NULL,'urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Unavailable','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Unavailable','urn:oasis:names:tc:ebxml-regrep:classificationScheme:ResponseStatusType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ResponseStatusType/Unavailable'),
 ('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved',NULL,'urn:oasis:names:tc:ebxml-regrep:StatusType:Approved','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Approved','urn:oasis:names:tc:ebxml-regrep:classificationScheme:StatusType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:StatusType/Approved'),
 ('urn:oasis:names:tc:ebxml-regrep:StatusType:Deprecated',NULL,'urn:oasis:names:tc:ebxml-regrep:StatusType:Deprecated','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Deprecated','urn:oasis:names:tc:ebxml-regrep:classificationScheme:StatusType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:StatusType/Deprecated'),
 ('urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted',NULL,'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Submitted','urn:oasis:names:tc:ebxml-regrep:classificationScheme:StatusType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:StatusType/Submitted');

--
-- XDS.B Classification Nodes
--


INSERT INTO classificationnode (id,home,lid,objectType,status,versionName,comment_,code,parent,path) VALUES 
 ('urn:ihe:iti:2007:AssociationType:APND',NULL,'urn:ihe:iti:2007:AssociationType:APND','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'APND','urn:ihe:iti:2007:AssociationType','/urn:ihe:iti:2007:AssociationType/APND'),
 ('urn:ihe:iti:2007:AssociationType:RPLC',NULL,'urn:ihe:iti:2007:AssociationType:RPLC','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'RPLC','urn:ihe:iti:2007:AssociationType','/urn:ihe:iti:2007:AssociationType/RPLC'),
 ('urn:ihe:iti:2007:AssociationType:signs',NULL,'urn:ihe:iti:2007:AssociationType:signs','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'signs','urn:ihe:iti:2007:AssociationType','/urn:ihe:iti:2007:AssociationType/signs'),
 ('urn:ihe:iti:2007:AssociationType:XFRM',NULL,'urn:ihe:iti:2007:AssociationType:XFRM','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'XFRM','urn:ihe:iti:2007:AssociationType','/urn:ihe:iti:2007:AssociationType/XFRM'),
 ('urn:ihe:iti:2007:AssociationType:XFRM_RPLC',NULL,'urn:ihe:iti:2007:AssociationType:XFRM_RPLC','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'XFRM_RPLC','urn:ihe:iti:2007:AssociationType','/urn:ihe:iti:2007:AssociationType/XFRM_RPLC'),
 ('urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1',NULL,'urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'XDSDocumentEntry','urn:uuid:415715f1-fc0b-47c4-90e5-c180b7b82db6','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/XDS/XDSDocumentEntry'),
 ('urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd',NULL,'urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'XDSSubmissionSet','urn:uuid:415715f1-fc0b-47c4-90e5-c180b7b82db6','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/XDS/XDSSubmissionSet'),
 ('urn:uuid:d9d542f3-6cc4-48b6-8870-ea235fbc94c2',NULL,'urn:uuid:d9d542f3-6cc4-48b6-8870-ea235fbc94c2','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'XDSFolder','urn:uuid:415715f1-fc0b-47c4-90e5-c180b7b82db6','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/XDS/XDSFolder');
 
 
 --
 -- Dumping data for table classscheme
 --
 

 INSERT INTO classscheme (id,home,lid,objectType,status,versionName,comment_,isInternal,nodeType) VALUES 
  ('urn:oasis:names:tc:ebxml-regrep:classificationScheme:AssociationType',NULL,'urn:oasis:names:tc:ebxml-regrep:classificationScheme:AssociationType','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'T','urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode'),
  ('urn:oasis:names:tc:ebxml-regrep:classificationScheme:ErrorSeverityType',NULL,'urn:oasis:names:tc:ebxml-regrep:classificationScheme:ErrorSeverityType','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'T','urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode'),
  ('urn:oasis:names:tc:ebxml-regrep:classificationScheme:NodeType',NULL,'urn:oasis:names:tc:ebxml-regrep:classificationScheme:NodeType','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'T','urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode'),
  ('urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType',NULL,'urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'T','urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode'),
  ('urn:oasis:names:tc:ebxml-regrep:classificationScheme:QueryLanguage',NULL,'urn:oasis:names:tc:ebxml-regrep:classificationScheme:QueryLanguage','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'T','urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode'),
  ('urn:oasis:names:tc:ebxml-regrep:classificationScheme:ResponseStatusType',NULL,'urn:oasis:names:tc:ebxml-regrep:classificationScheme:ResponseStatusType','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'T','urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode'),
  ('urn:oasis:names:tc:ebxml-regrep:classificationScheme:StatusType',NULL,'urn:oasis:names:tc:ebxml-regrep:classificationScheme:StatusType','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'T','urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');


 --
 -- XDS.B class schemes
 --
 
 INSERT INTO classscheme (id,home,lid,objectType,status,versionName,comment_,isInternal,nodeType) VALUES 
  ('urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a',NULL,'urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'F','urn:oasis:names:tc:ebxml-regrep:NodeType:EmbeddedPath'),
  ('urn:uuid:2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4',NULL,'urn:uuid:2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'F','urn:oasis:names:tc:ebxml-regrep:NodeType:EmbeddedPath'),
  ('urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d',NULL,'urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'F','urn:oasis:names:tc:ebxml-regrep:NodeType:EmbeddedPath'),
  ('urn:uuid:a7058bb9-b4e4-4307-ba5b-e3f0ab85e12d',NULL,'urn:uuid:a7058bb9-b4e4-4307-ba5b-e3f0ab85e12d','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'F','urn:oasis:names:tc:ebxml-regrep:NodeType:EmbeddedPath'),
  ('urn:uuid:aa543740-bdda-424e-8c96-df4873be8500',NULL,'urn:uuid:aa543740-bdda-424e-8c96-df4873be8500','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'F','urn:oasis:names:tc:ebxml-regrep:NodeType:EmbeddedPath'),
  ('urn:uuid:f0306f51-975f-434e-a61c-c59651d33983',NULL,'urn:uuid:f0306f51-975f-434e-a61c-c59651d33983','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'F','urn:oasis:names:tc:ebxml-regrep:NodeType:EmbeddedPath'),
  ('urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1',NULL,'urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'F','urn:oasis:names:tc:ebxml-regrep:NodeType:EmbeddedPath'),
  ('urn:uuid:1ba97051-7806-41a8-a48b-8fce7af683c5',NULL,'urn:uuid:1ba97051-7806-41a8-a48b-8fce7af683c5','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'F','urn:oasis:names:tc:ebxml-regrep:NodeType:EmbeddedPath'),
  ('urn:uuid:f64ffdf0-4b97-4e06-b79f-a52b38ec2f8a',NULL,'urn:uuid:f64ffdf0-4b97-4e06-b79f-a52b38ec2f8a','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'T','urn:oasis:names:tc:ebxml-regrep:NodeType:EmbeddedPath'),
  ('urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead',NULL,'urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'F','urn:oasis:names:tc:ebxml-regrep:NodeType:EmbeddedPath'),
  ('urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f',NULL,'urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'F','urn:oasis:names:tc:ebxml-regrep:NodeType:EmbeddedPath'), 
  ('urn:uuid:d9d542f3-6cc4-48b6-8870-ea235fbc94c2',NULL,'urn:uuid:d9d542f3-6cc4-48b6-8870-ea235fbc94c2','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'T','urn:oasis:names:tc:ebxml-regrep:NodeType:EmbeddedPath'), 
  ('urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1',NULL,'urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'T','urn:oasis:names:tc:ebxml-regrep:NodeType:EmbeddedPath'),
  ('urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8',NULL,'urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'T','urn:oasis:names:tc:ebxml-regrep:NodeType:EmbeddedPath'),
  ('urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446',NULL,'urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'T','urn:oasis:names:tc:ebxml-regrep:NodeType:EmbeddedPath'),
  ('urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd',NULL,'urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'T','urn:oasis:names:tc:ebxml-regrep:NodeType:EmbeddedPath'), 
  ('urn:ihe:iti:2007:AssociationType',             NULL,'urn:ihe:iti:2007:AssociationType',             'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'T','urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode'),
  ('urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832',NULL,'urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'T','urn:oasis:names:tc:ebxml-regrep:NodeType:EmbeddedPath'),
  ('urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab',NULL,'urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'T','urn:oasis:names:tc:ebxml-regrep:NodeType:EmbeddedPath'), 
  ('urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427',NULL,'urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'T','urn:oasis:names:tc:ebxml-regrep:NodeType:EmbeddedPath'),
  ('urn:uuid:75df8f67-9973-4fbe-a900-df66cefecc5a',NULL,'urn:uuid:75df8f67-9973-4fbe-a900-df66cefecc5a','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'T','urn:oasis:names:tc:ebxml-regrep:NodeType:EmbeddedPath'),
  ('urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d',NULL,'urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'F','urn:oasis:names:tc:ebxml-regrep:NodeType:EmbeddedPath'),
  ('urn:uuid:415715f1-fc0b-47c4-90e5-c180b7b82db6',NULL,'urn:uuid:415715f1-fc0b-47c4-90e5-c180b7b82db6','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'T','urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');

--
-- Dumping data for table user_
--

INSERT INTO user_ (id,home,lid,objectType,status,versionName,comment_,personName_firstName,personName_middleName,personName_lastName) VALUES 
 ('urn:freebxml:registry:predefinedusers:registryguest',NULL,'urn:freebxml:registry:predefinedusers:registryguest','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Person:User','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.0',NULL,'Registry',NULL,'Guest'),
 ('urn:freebxml:registry:predefinedusers:registryoperator',NULL,'urn:freebxml:registry:predefinedUser:RegistryOperator','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Person:User','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.0',NULL,'Registry',NULL,'Operator');

--
-- 'registryobject' table seed data.
--
-- The data here should match the seed data above (without some extra columns and without user_ table).
--
-- This approach was taken to work around a significant performance issue with MySQL and views.
--
INSERT INTO registryobject (id,home,lid,objectType,status,versionName,comment_) VALUES
 ('urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember',NULL,'urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
 ('urn:oasis:names:tc:ebxml-regrep:AssociationType:RelatedTo',NULL,'urn:oasis:names:tc:ebxml-regrep:AssociationType:RelatedTo','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
 ('urn:oasis:names:tc:ebxml-regrep:AssociationType:Replaces',NULL,'urn:oasis:names:tc:ebxml-regrep:AssociationType:Replaces','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
 ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
 ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Association',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Association','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
 ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
 ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
 ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
 ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
 ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
 ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:RegistryPackage',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:RegistryPackage','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
 ('urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure',NULL,'urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
 ('urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success',NULL,'urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
 ('urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Unavailable',NULL,'urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Unavailable','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
 ('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved',NULL,'urn:oasis:names:tc:ebxml-regrep:StatusType:Approved','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
 ('urn:oasis:names:tc:ebxml-regrep:StatusType:Deprecated',NULL,'urn:oasis:names:tc:ebxml-regrep:StatusType:Deprecated','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
 ('urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted',NULL,'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL);

--
-- XDS.B Classification Nodes (registryobject)
--


INSERT INTO registryobject (id,home,lid,objectType,status,versionName,comment_) VALUES
 ('urn:ihe:iti:2007:AssociationType:APND',NULL,'urn:ihe:iti:2007:AssociationType:APND','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
 ('urn:ihe:iti:2007:AssociationType:RPLC',NULL,'urn:ihe:iti:2007:AssociationType:RPLC','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
 ('urn:ihe:iti:2007:AssociationType:signs',NULL,'urn:ihe:iti:2007:AssociationType:signs','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
 ('urn:ihe:iti:2007:AssociationType:XFRM',NULL,'urn:ihe:iti:2007:AssociationType:XFRM','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
 ('urn:ihe:iti:2007:AssociationType:XFRM_RPLC',NULL,'urn:ihe:iti:2007:AssociationType:XFRM_RPLC','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
 ('urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1',NULL,'urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
 ('urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd',NULL,'urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
 ('urn:uuid:d9d542f3-6cc4-48b6-8870-ea235fbc94c2',NULL,'urn:uuid:d9d542f3-6cc4-48b6-8870-ea235fbc94c2','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL);


 --
 -- Dumping data for table classscheme (registryobject)
 --


 INSERT INTO registryobject (id,home,lid,objectType,status,versionName,comment_) VALUES
  ('urn:oasis:names:tc:ebxml-regrep:classificationScheme:AssociationType',NULL,'urn:oasis:names:tc:ebxml-regrep:classificationScheme:AssociationType','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
  ('urn:oasis:names:tc:ebxml-regrep:classificationScheme:ErrorSeverityType',NULL,'urn:oasis:names:tc:ebxml-regrep:classificationScheme:ErrorSeverityType','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
  ('urn:oasis:names:tc:ebxml-regrep:classificationScheme:NodeType',NULL,'urn:oasis:names:tc:ebxml-regrep:classificationScheme:NodeType','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
  ('urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType',NULL,'urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
  ('urn:oasis:names:tc:ebxml-regrep:classificationScheme:QueryLanguage',NULL,'urn:oasis:names:tc:ebxml-regrep:classificationScheme:QueryLanguage','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
  ('urn:oasis:names:tc:ebxml-regrep:classificationScheme:ResponseStatusType',NULL,'urn:oasis:names:tc:ebxml-regrep:classificationScheme:ResponseStatusType','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
  ('urn:oasis:names:tc:ebxml-regrep:classificationScheme:StatusType',NULL,'urn:oasis:names:tc:ebxml-regrep:classificationScheme:StatusType','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL);


 --
 -- XDS.B class schemes (registryobject)
 --

 INSERT INTO registryobject (id,home,lid,objectType,status,versionName,comment_) VALUES
  ('urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a',NULL,'urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
  ('urn:uuid:2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4',NULL,'urn:uuid:2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
  ('urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d',NULL,'urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
  ('urn:uuid:a7058bb9-b4e4-4307-ba5b-e3f0ab85e12d',NULL,'urn:uuid:a7058bb9-b4e4-4307-ba5b-e3f0ab85e12d','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
  ('urn:uuid:aa543740-bdda-424e-8c96-df4873be8500',NULL,'urn:uuid:aa543740-bdda-424e-8c96-df4873be8500','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
  ('urn:uuid:f0306f51-975f-434e-a61c-c59651d33983',NULL,'urn:uuid:f0306f51-975f-434e-a61c-c59651d33983','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
  ('urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1',NULL,'urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
  ('urn:uuid:1ba97051-7806-41a8-a48b-8fce7af683c5',NULL,'urn:uuid:1ba97051-7806-41a8-a48b-8fce7af683c5','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
  ('urn:uuid:f64ffdf0-4b97-4e06-b79f-a52b38ec2f8a',NULL,'urn:uuid:f64ffdf0-4b97-4e06-b79f-a52b38ec2f8a','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
  ('urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead',NULL,'urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
  ('urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f',NULL,'urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
  ('urn:uuid:d9d542f3-6cc4-48b6-8870-ea235fbc94c2',NULL,'urn:uuid:d9d542f3-6cc4-48b6-8870-ea235fbc94c2','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
  ('urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1',NULL,'urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
  ('urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8',NULL,'urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
  ('urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446',NULL,'urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
  ('urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd',NULL,'urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
  ('urn:ihe:iti:2007:AssociationType',             NULL,'urn:ihe:iti:2007:AssociationType',             'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
  ('urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832',NULL,'urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
  ('urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab',NULL,'urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
  ('urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427',NULL,'urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
  ('urn:uuid:75df8f67-9973-4fbe-a900-df66cefecc5a',NULL,'urn:uuid:75df8f67-9973-4fbe-a900-df66cefecc5a','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
  ('urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d',NULL,'urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL),
  ('urn:uuid:415715f1-fc0b-47c4-90e5-c180b7b82db6',NULL,'urn:uuid:415715f1-fc0b-47c4-90e5-c180b7b82db6','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL);