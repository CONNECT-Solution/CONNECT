# import.sql
connect hihportaldb;
delete from UserLogin;
insert into UserLogin (id, salt, sha1, userName) values (0, "ABCD", "TxMu4SPUdek0XU5NovS9U2llt3Q=", "Matt");
-- insert into UserLogin (id, salt, sha1, userName) values (0, "ABCD", "TxMu4SPUdek0XU5NovS9U2llt3A=", "Matt");
insert into UserLogin (id, salt, sha1, userName) values (1, "PYBI", "c90TvJnwlmMYAFgx9U5NCVvWCi0=", "David");
insert into UserLogin (id, salt, sha1, userName) values (2, "RCWH", "rxuLp3lYRgV6iL0Q3UIJZO9s1P0=", "Tiffany");
insert into UserLogin (id, salt, sha1, userName) values (3, "AMEW", "d1hfzuiPL7JgLz5NteWrqD2uYiw=", "Jason");