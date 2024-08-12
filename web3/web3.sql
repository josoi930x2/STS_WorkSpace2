
-- web3 예제의 table

create table person (
    id varchar(30) not null,
    name varchar(50),
    age integer
);

insert into person (id, name, age) values ('aaa', '홍길동', 10);
insert into person (id, name, age) values ('bbb', '김철수', 20);

commit;

show tables;
drop table person;

select * from person;
delete from person where id = 'bbb';