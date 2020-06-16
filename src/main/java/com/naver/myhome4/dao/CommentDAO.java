package com.naver.myhome4.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.naver.myhome4.domain.Comment;
@Repository
public class CommentDAO {
@Autowired
private SqlSessionTemplate sqlSession;

public List<Comment> getCommentList(Map<String, Integer> map){
	return sqlSession.selectList("Comments.getList", map); //두 개의 인자만 받는다. (1: 실행할 쿼리문 / 2. 파라미터로 넘겨줄 것들의 정보) ==> 파라미터로 넘길게 많을 떄 map 사용
}

public int commentsInsert(Comment c) {
	return sqlSession.insert("Comments.insert", c);
}


public int commentsDelete(int num) {
	return sqlSession.delete("Comments.delete", num);
}

public int getListCount(int board_num) {
	return sqlSession.selectOne("Comments.count", board_num);
}

public int commentsUpdate(Comment co) {
	return sqlSession.update("Comments.update", co);
}
}
