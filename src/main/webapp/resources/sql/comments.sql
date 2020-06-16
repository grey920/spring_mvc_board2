drop table comments;

create table comments(
num number primary key,
id varchar2(30) references member(id),
content varchar2(200), 
reg_date date,
board_num number references board(board_num) on delete cascade
);
--on delete cascade : 원문글 삭제하면 이 원문글을 참조하는 댓글도 삭제됩니다.--
create sequence com_seq;

select * from comments;

insert into comments values(2, 'admin', 'Hello', sysdate, 5);

insert into comments values(3, 'admin', 'HelloWorld', sysdate, 5);
insert into comments values(4, 'admin', 'HelloWorldwow', sysdate, 5);
insert into comments values(5, 'admin', '못해먹겠다 후하 ', sysdate, 5);

select * from BOARD;

select * from comments;

delete from COMMENTS;



