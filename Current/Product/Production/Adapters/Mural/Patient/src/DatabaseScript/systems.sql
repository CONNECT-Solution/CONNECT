
set @v_ts = (select now());
set @v_user = (select user());

insert into sbyn_systems (systemcode, description, status, id_length, format, input_mask, value_mask, create_date, create_userid) 
    values ('2.16.840.1.113883.3.200', 'VA', 'A', 10, '[0-9]{9}', 'DDDDDDDDD', 'DDDDDDDDD', @v_ts, @v_user);

insert into sbyn_systems (systemcode, description, status, id_length, format, input_mask, value_mask, create_date, create_userid) 
    values ('2.16.840.1.113883.3.198', 'DOD', 'A', 7, '^D[0-9]{6}', 'LDDDDDD', 'LDDDDDD', @v_ts, @v_user);
insert into sbyn_systems (systemcode, description, status, id_length, format, input_mask, value_mask, create_date, create_userid)
    values ('1.1', 'SelfTest System 1', 'A', 10, '[0-9]{9}', 'DDDDDDDDD', 'DDDDDDDDD', @v_ts, @v_user);

insert into sbyn_systems (systemcode, description, status, id_length, format, input_mask, value_mask, create_date, create_userid)
    values ('2.2', 'Selftest Sytem 2', 'A', 7, '^D[0-9]{6}', 'LDDDDDD', 'LDDDDDD', @v_ts, @v_user);


