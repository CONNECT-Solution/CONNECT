DROP TABLE IF EXISTS `asyncmsgrepo`;

CREATE TABLE asyncmsgs.asyncmsgrepo (
	MessageId VARCHAR(100) NOT NULL,
	CreationTime DATETIME NOT NULL,
	ServiceName VARCHAR(100),
	MsgData LONGBLOB,
	PRIMARY KEY (MessageId)
);