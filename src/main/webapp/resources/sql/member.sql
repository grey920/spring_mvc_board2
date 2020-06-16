create table member(
id varchar2(15),
password varchar2(10),
name varchar2(15),
age Number,
gender varchar2(5),
email varchar2(30),
primary key(id)
);

select * from member;



select * from member;

update member set password=1 where id='admin'

insert into member values('jsp', 1, 'jsp', 21, '남', 'jsp@naver.com')

insert into member
values('java', 1, 'java', 25, '여', 'java@naver.com');

alter table member modify (password varchar2(60)) ;

select * from member;