package com.naver.myhome4.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naver.myhome4.dao.MemberDAO;
import com.naver.myhome4.domain.Member;

// @Service를 붙여야 빈이 생성됨 (클래스를 만들었으면 써야 하니까 빈을 생성해야해)
@Service
public class MemberServiceImpl implements MemberService {
	@Autowired //@Autowired니까 또 MemberDAO에 빈 생성하러 가야해 (어노테이션 붙이는 작업)
	private MemberDAO dao;

	@Override
	public int isId(String id, String password) {
		Member rmember = dao.isId(id);
		int result = -1;  // 아이디가 존재하지 않는 경우 - rmember가 null인 경우
		if(rmember != null) {  // 아이디가 존재하는 경우 
			if(rmember.getPassword().equals(password)) {
				result = 1;  // 아이디와 비밀번호가 일치하는 경우
			} else
				result = 0; // 아이디는 존재하지만 비밀번호가 일치하지 않는 경우
		}
		return result; 
	}

	@Override
	public int insert(Member m) {
		return dao.insert(m);
	}

	@Override
	public int isId(String id) {
		Member rmember = dao.isId(id);
		return (rmember==null) ? -1 : 1;  // -1은 아이디가 존재하지 않는 경우 / 1은 아이디가 존재하는 경우
	}

	@Override
	public Member member_info(String id) {
		return dao.member_info(id);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public int update(Member m) {
	  return dao.update(m);
	}

	@Override
	public List<Member> getSearchList(int index, String search_word, int page, int limit) {
		Map<String, Object> map = new HashMap<String, Object>();
		if( index != -1) {//맨 처음! 회원목록에 아무 검색어도 넣지 않은 경우
			String[] search_field = new String[] {"id", "name", "age", "gender"};
			map.put("search_field", search_field[index]);
			map.put("search_word", "%" + search_word + "%");
		}
		int startrow = (page-1) * limit + 1;
		int endrow = startrow + limit - 1;
		map.put("start", startrow);
		map.put("end", endrow);
	 return dao.getSearchList(map);
	}

	@Override
	public int getSearchListCount(int index, String search_word) {
		Map<String, String> map = new HashMap<String, String>();
		if (index != -1) {
			String[] search_field = new String[] {"id", "name", "age", "gender"};
			map.put("search_field", search_field[index]);
			map.put("search_word", "%" + search_word + "%");
		}
		return dao.getSearchListCount(map);
	}
	
}
