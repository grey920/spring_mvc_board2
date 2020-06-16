package com.naver.myhome4.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.naver.myhome4.domain.Board;
import com.naver.myhome4.service.BoardService;
import com.naver.myhome4.service.CommentService;

@Controller
public class BoardController {

	@Autowired
	private BoardService boardService;

	@Autowired
	private CommentService commentService;
	
	//추가
		// savefolder.properties
		// 속성=값
		// 의 형식으로 작성하면 됩니다.
		// savefoldername=\\Users\\kyewoon\\spring_kh\\Spring4_MVC_BOARD2\\src\\main\\webapp\\resources\\upload
		// 값을 가져오기 위해서는 속성(property)를 이용합니다.
		@Value("${savefoldername}")
		private String saveFolder;
	
	// 글쓰기
	@GetMapping(value = "/BoardWrite.bo")
	// @RequestMapping(value="/BoardWrite.bo", method=RequestMethod.GET) 이거였는데 버전 업되서 더 편하게 바뀜!!
	public String board_write() {
		return "board/qna_board_write";
	}
	
	/* 게시판 저장 */
	/*
	 * @RequestMapping(value="/board_write_ok.nhn", method=RequestMethod.POST)
	 */
	/*
	 * 스프링 컨테이너는 매개변수 Board객체를 생성하고
	 * Board객체의 setter 메서드들을 호출하여 사용자 입력값을 설정합니다.
	 * 매개변수의 이름과 setter의 property가 일치하면 됩니다.
	 * */
	@PostMapping(value="/BoardAddAction.bo")
	public String board_write_ok(Board board, HttpServletRequest request) throws Exception{
		
		MultipartFile uploadfile = board.getUploadfile();  //<== MultipartFile uploadfile는 변수 선언한건가??
		
		if(!uploadfile.isEmpty()) { //MultipartFile인터페이스 : 업로드한 파일 정보 및 파일 데이터를 표현하기 위한 용도로 사용
			String fileName = uploadfile.getOriginalFilename(); // 원래 파일명 <==String getOriginalFilename() : 업로드한 파일의 이름을 구한다
			board.setBOARD_ORIGINAL(fileName); // 원래 파일명 저장
			
			// 새로운 폴더 이름 : 오늘 년-월-일  
			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR); // 오늘 년도 구합니다.
			int month = c.get(Calendar.MONTH ) + 1; // 오늘 월 구합니다.
			int date = c.get(Calendar.DATE); // 오늘일  구합니다.
			
			// 제거
			//1.  이클립스 관리
//			String saveFolder = request.getSession().getServletContext().getRealPath("resources")
//					+ "/upload/";
			//2. 특정 폴더
//			String saveFolder = 	\\Users\\kyewoon\\spring_kh\\Spring4_MVC_BOARD2\\src\\main\\webapp\\resources\\upload	
			String homedir = saveFolder + year + "-" + month + "-" + date;
			System.out.println(homedir);
			File path1 = new File(homedir);
			if(!(path1.exists())) {
				path1.mkdir();  // 새로운 폴더를 생성
			}
			
			// 난수를 구합니다.  <== 파일명 중복 없애기 위해
			Random r = new Random();
			int random = r.nextInt(100000000);
			
			/*****   확장자  구하기  시작   ****/
			int index = fileName.lastIndexOf(".");
			/*
			 문자열에서 특정 문자열의 위치 값(index)를 반환한다.
			 indexOf가 처음 발견되는 문자열에 대한 index를 반환하는반면, 
			 lastIndexOf는 마지막으로 발견되는 문자열의 index를 반환합니다.
			 (파일명에 점이 여러개 있을 경우 맨 마지막에 발견되는 문자열의 위치를 리턴합니다.)
			  */
			System.out.println("index = " + index);
			
			String fileExtension = fileName.substring(index + 1);
			System.out.println("fileExtension = "+ fileExtension);
			/*****   확장자  구하기  끝   ****/
			
			// 새로운 파일명
			String refileName = "bbs" + year + month + date + random + "." + fileExtension;
			System.out.println("refileName = " + refileName);
			
			// 오라클 디비에 저장될 파일 명
			String fileDBName = "/" + year + "-" + month + "-" + date + "/" + refileName;
			System.out.println("fileDBName = "+ fileDBName);
			
			//⭐️transferTo(File path) : 업로드한 파일을 매개변수의 경로에 저장합니다.
			uploadfile.transferTo(new File(saveFolder + fileDBName));
			
			//바뀐 파일명으로 저장 (최종 파일명)
			board.setBOARD_FILE(fileDBName);
		}
		boardService.insertBoard(board);  // 저장 메소드 호출
		
		return "redirect:/BoardList.bo";
	}

	// URL에서 {}처리된 부분은 컨트롤러의 메서드에서 변수로 처리가 가능합니다.
	// @PathVariable은 {} 의 이름을 처리할 떄 사용합니다.,
	
	//BoardDetailAction.bo?num=9요청시 파라미터 num의 값을 int num에 저장합니다.
	@GetMapping(value="/BoardDetailAction.bo")
	public ModelAndView Detail(int num, ModelAndView mv, HttpServletRequest request) {
		Board board = boardService.getDetail(num);
		if(board == null) {
			System.out.println("상세보기 실패");
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "상세보기 실패입니다.");
		} else {
			System.out.println("상세보기 성공");
			int count = commentService.getListCount(num);
			mv.setViewName("board/qna_board_view");
			mv.addObject("count", count);
			mv.addObject("boarddata", board);
		}
		return mv;
	}
	
	@GetMapping("BoardFileDown.bo")
	public void BoardFileDown(String filename, HttpServletRequest request, String original, HttpServletResponse response) throws Exception{
			String savePath = "resources/upload";
			
			//서브릿의 실행 환경 정보를 답고 있는 객체를 리턴합니다.
			ServletContext context = request.getSession().getServletContext();
			String sDownloadPath = context.getRealPath(savePath);
			
			// String sFilePath = SDownloadPath + "\\" + fileName;
			// "/"를 추가하기 위해 "\\"를 사용합니다.
			String sFilePath = sDownloadPath + "/" + filename;
			System.out.println(sFilePath);
			
			byte b[] = new byte[4096];
			
			// sFilePath에 있는 파일의 MimeType을 구해옵니다.
			String sMimeType = context.getMimeType(sFilePath);
			System.out.println("sMimeType>>>" + sMimeType);

			if (sMimeType == null)
				sMimeType = "application/octet-stream";
			response.setContentType(sMimeType);
			
			// - 이 부분이 한글 파일명이 깨지는 것을 방지해 줍니다.
			String sEncoding = 
					new String(original.getBytes("utf-8"), "ISO-8859-1");
			System.out.println(sEncoding);
			
			
			/*
			 * Content-Disposition : attachment : 브라우저는 해당 Content를
			 * 처리하지 않고, 다운로드하게 됩니다.
			 * */
			response.setHeader("Content-Disposition", "attachment; filename= " + sEncoding);
			try (
					// 웹 브라우저로의 출력 스트림 생성합니다.
					BufferedOutputStream out2 = 
									new BufferedOutputStream(response.getOutputStream());
					//sFilePath로 지정한 파일에 대한 입력 스트림을 생성함니다.
					BufferedInputStream in = 
									new BufferedInputStream(new FileInputStream(sFilePath));)
				{
					int numRead;
					// read(b, 0, b.length) : 바이트 배열 b의 0번부터 b.length
					//크기만큼 읽어옵니다.
					while ((numRead = in.read(b, 0, b.length)) != -1) { //읽을 데이터가 존재하는 경우
						// 바이트 배열 b의 0번부터 numRead크기 만큼 브라우저로 출력
						out2.write(b, 0, numRead);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
	}
	
	@PostMapping("/BoardDeleteAction.bo")
	public ModelAndView BoardDeleteAction(String BOARD_PASS, int num, ModelAndView mv, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		boolean usercheck = boardService.isBoardWriter(num, BOARD_PASS);

		// 비밀번호가 다른 경우
		if (usercheck == false) {
			response.setContentType("text/html; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('비밀번호가 다릅니다.');");
			out.println("history.back();");
			out.println("</script>");
			out.close();
			return null;
		}
		int result = boardService.boardDelete(num);
		//삭제 처리 실패한 경우
				if(result == 0) {
					System.out.println("게시판 삭제 실패");
					mv.setViewName("error/error");
					mv.addObject("url", request.getRequestURL());
					mv.addObject("message", "게시판 삭제 실패입니다.");
				}
				//삭제 처리 성공한 경우 - 글 목록 보기요청을 전송하는 부분입니다.
				System.out.println("게시판 삭제 성공");
				response.setContentType("text/html; charset=utf-8");
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('삭제되었습니다.');");
				out.println("location.href='BoardList.bo';");
				out.println("</script>");
				out.close();
				return null;
	}
	
	@GetMapping("/BoardModifyView.bo")
	public ModelAndView BoardModifyView(int num, ModelAndView mv, HttpServletRequest request) {
		Board boarddata = boardService.getDetail(num);
		//글 내용 불러오기 실패한 경우입니다.
		if(boarddata == null) {
			System.out.println("(수정) 상세보기 실패");
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "(수정)상세보기 실패입니다.");
			return mv;
		}
		System.out.println("(수정)상세보기 성공");
		
		// 수정 폼 페이지로 이동할 때 원문 글 내용을 보여주기 때문에 boarddata 객체를
		// ModelAndView 객체에 저장합니다.\
		mv.addObject("boarddata", boarddata);
		mv.setViewName("board/qna_board_modify");
		return mv;
	}

	@PostMapping("BoardModifyAction.bo")
	public ModelAndView BoardModifyAction(Board board,
				String check, ModelAndView mv, HttpServletRequest request,
				HttpServletResponse response) throws Exception{
		boolean usercheck = boardService.isBoardWriter(board.getBOARD_NUM(),  board.getBOARD_PASS());
		// 비밀번호가 다른 경우
		if (usercheck == false) {
			response.setContentType("text/html; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('비밀번호가 다릅니다.');");
			out.println("history.back();");
			out.println("</script>");
			out.close();
			return null;
		}
		
		MultipartFile uploadfile = board.getUploadfile();
		// 1. 이클립스 관리
//		String saveFolder = request.getSession().getServletContext().getRealPath("resources")+"/upload/";
		//2. 특정 폴더
//		String saveFolder = 	\\Users\\kyewoon\\spring_kh\\Spring4_MVC_BOARD2\\src\\main\\webapp\\resources\\upload\\2020-06-16		
		
		System.out.println("check = " + check);
		if(check != null && !check.equals("")) { // 기존 파일 그대로 사용하는 경우입니다.
			System.out.println("기존 파일 그대로 사용합니다.");
			board.setBOARD_ORIGINAL(check);
			//<input type="hidden" name="BOARD_FILE" value="${boarddata.BOARD_FILE}">
			// 위 문장 때문에 board. setBOARD_FILE() 값은 자동 저장됩니다.
//			String fileDBName = fileDBName(check, saveFolder);
//			board.setBOARD_FILE(fileDBName);
		} else {
			if (uploadfile != null && !uploadfile.isEmpty()) { // 파일 변경한 경우
				String fileName = uploadfile.getOriginalFilename();  // 원래 파일 명
				board.setBOARD_ORIGINAL(fileName);
				
				String fileDBName = fileDBName(fileName, saveFolder);
				
				// transferTo(File path) : 업로드한 파일을 매개변수의 경로에 저장합니다.
				uploadfile.transferTo(new File(saveFolder + fileDBName));
				
				// 바뀐 파일명으로 저장
				board.setBOARD_FILE(fileDBName);
			} else { // uploadfile.isEmpty()인 경우 - 파일 선택하지 않은 경우
				System.out.println(" uploadfile.isEmpty() 선택 파일 없습니다.");
				//<input type="hidden" name="BOARD_FILE" value="${boarddata.BOARD_FILE}">
				// 위 태그에 값이 있다면 "" 로 변경합니다. 
				board.setBOARD_FILE(""); //""로 초기화 합니다.
				board.setBOARD_ORIGINAL(""); //""로 초기화합니다.
			}
		}
		// DAO에서 수정 메서드 호출하여 수정합니다.
		int result = boardService.boardModify(board);
		
		// 수정에 실패한 경우
		if (result == 0) {
			System.out.println("게시판 수정 실패");
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "게시판 수정 실패");
		} else { // 수정 성공의 경우
			System.out.println("게시판 수정 완료");
			String url = "redirect:BoardDetailAction.bo?num=" + board.getBOARD_NUM();
			
			// 수정한 글 내용을 보여주기 위해 글 내용 보기 보기 페이지로 이동하기 위해 경로를 설정합니다.
			mv.setViewName(url);
		}
		return mv;
	}
	
	
	private String fileDBName(String fileName, String saveFolder) {
		// 새로운 폴더 이름 : 오늘 년-월-일  
					Calendar c = Calendar.getInstance();
					int year = c.get(Calendar.YEAR); // 오늘 년도 구합니다.
					int month = c.get(Calendar.MONTH ) + 1; // 오늘 월 구합니다.
					int date = c.get(Calendar.DATE); // 오늘일  구합니다.
					
					String homedir = saveFolder + year + "-" + month + "-" + date;
					System.out.println(homedir);
					File path1 = new File(homedir);
					if(!(path1.exists())) {
						path1.mkdir();  // 새로운 폴더를 생성
					}
					
					// 난수를 구합니다.  <== 파일명 중복 없애기 위해
					Random r = new Random();
					int random = r.nextInt(100000000);
					
					/*****   확장자  구하기  시작   ****/
					int index = fileName.lastIndexOf(".");
					/*
					 문자열에서 특정 문자열의 위치 값(index)를 반환한다.
					 indexOf가 처음 발견되는 문자열에 대한 index를 반환하는반면, 
					 lastIndexOf는 마지막으로 발견되는 문자열의 index를 반환합니다.
					 (파일명에 점이 여러개 있을 경우 맨 마지막에 발견되는 문자열의 위치를 리턴합니다.)
					  */
					System.out.println("index = " + index);
					
					String fileExtension = fileName.substring(index + 1);
					System.out.println("fileExtension = "+ fileExtension);
					/*****   확장자  구하기  끝   ****/
					
					// 새로운 파일명
					String refileName = "bbs" + year + month + date + random + "." + fileExtension;
					System.out.println("refileName = " + refileName);
					
					// 오라클 디비에 저장될 파일 명
					String fileDBName = "/" + year + "-" + month + "-" + date + "/" + refileName;
					System.out.println("fileDBName = "+ fileDBName);
					
		return fileDBName;
	}

	/*
	 * 1. 정상실행 Command 객체에 없는 파라미터를 처리하는 방법
	 * 
	 * @RequestParam(value="age") int age 파라미터 age를 정수형변수 age에 저장하라는 의미입니다. String으로
	 * 넘어오는 파라미터의 값을 변수형에 맞추어 캐스팅합니다.
	 */
	// 글 목록 보기
	@RequestMapping(value = "/BoardList.bo")
	public ModelAndView boardList(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
			ModelAndView mv){

		int limit = 10; // 한 화면에 출력할 레코드 갯수

		int listcount = boardService.getListCount(); // 총 리스트 수(게시물의 갯수)를 받아옴

		// 총페이지 수
		int maxpage = (listcount + limit - 1) / limit;

		// 현재 페이지에 보여줄 시작 페이지 수 (1, 11, 21 등 ...)
		int startpage = ((page - 1) / 10) * 10 + 1;

		// 현재 페이지에 보여줄 마지막 페이지 수 (10, 20, 30 등 ...)
		int endpage = startpage + 10 - 1;

		if (endpage > maxpage)
			endpage = maxpage;

		List<Board> boardlist = boardService.getBoardList(page, limit); // 리스트를 받아옴

		mv.setViewName("board/qna_board_list");
		mv.addObject("page", page);   //request.setAttribute 로 담았던걸 여기서 다 담음
		mv.addObject("maxpage", maxpage);
		mv.addObject("startpage", startpage);
		mv.addObject("endpage", endpage);
		mv.addObject("listcount", listcount);
		mv.addObject("boardlist", boardlist);
		mv.addObject("limit", limit);
		return mv;
	}

	
	@ResponseBody
	@RequestMapping(value="/BoardListAjax.bo")
	public Map<String, Object> boardListAjax(
			@RequestParam(value="page", defaultValue="1", required=false) int page,
			@RequestParam(value="limit", defaultValue="10", required=false) int limit){

		int listcount = boardService.getListCount(); // 총 리스트 수(게시물의 갯수)를 받아옴

		// 총페이지 수
		int maxpage = (listcount + limit - 1) / limit;

		// 현재 페이지에 보여줄 시작 페이지 수 (1, 11, 21 등 ...)
		int startpage = ((page - 1) / 10) * 10 + 1;

		// 현재 페이지에 보여줄 마지막 페이지 수 (10, 20, 30 등 ...)
		int endpage = startpage + 10 - 1;

		if (endpage > maxpage)
			endpage = maxpage;

		List<Board> boardlist = boardService.getBoardList(page, limit); // 리스트를 받아옴

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("maxpage", maxpage);
		map.put("startpage", startpage);
		map.put("endpage", endpage);
		map.put("listcount", listcount);
		map.put("boardlist", boardlist);
		map.put("limit", limit);
		
		return map;
	}
	
	// 답변글 보기
	@GetMapping("BoardReplyView.bo")
	public ModelAndView BoardReplyView(int num, ModelAndView mv,
										HttpServletRequest request) {
		Board board = boardService.getDetail(num);
		if(board == null) {
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "게시판 답변글 가져오기 실패");
		} else {
			mv.addObject("boarddata", board);
			mv.setViewName("board/qna_board_reply");
		}
		return mv;
	}
	
	// 답변글 쓰기 처리
	@PostMapping("BoardReplyAction.bo")
	public ModelAndView BoardReplyAction(Board board, ModelAndView mv,
														HttpServletRequest request) {
		int result = boardService.boardReply(board);
		if(result == 0) {
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "게시판 답변 처리 실패");
		} else {
			mv.addObject("boarddata", board);
			mv.setViewName("redirect:/BoardList.bo");
		}
		return mv;
		}
	}
	
