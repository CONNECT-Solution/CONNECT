# import.sql
# connect hihportaldb;
CREATE TABLE IF NOT EXISTS hihportaldb.UserLogin ( id BIGINT NOT NULL AUTO_INCREMENT, salt varchar(100) NOT NULL, sha1 varchar(100) NOT NULL, userName varchar(100) NOT NULL, PRIMARY KEY (id), UNIQUE(username));
delete from hihportaldb.UserLogin;
insert into hihportaldb.UserLogin (id, salt, sha1, userName) values (1, "ABCD", "TxMu4SPUdek0XU5NovS9U2llt3Q=", "Matt");
-- insert into UserLogin (id, salt, sha1, userName) values (0, "ABCD", "TxMu4SPUdek0XU5NovS9U2llt3A=", "Matt");
insert into hihportaldb.UserLogin (id, salt, sha1, userName) values (2, "PYBI", "c90TvJnwlmMYAFgx9U5NCVvWCi0=", "David");
insert into hihportaldb.UserLogin (id, salt, sha1, userName) values (3, "RCWH", "rxuLp3lYRgV6iL0Q3UIJZO9s1P0=", "Tiffany");
insert into hihportaldb.UserLogin (id, salt, sha1, userName) values (4, "AMEW", "d1hfzuiPL7JgLz5NteWrqD2uYiw=", "Jason");