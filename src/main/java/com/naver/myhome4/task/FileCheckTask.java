package com.naver.myhome4.task;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.naver.myhome4.service.BoardService;
//06-16 작업. 
@Service  // @Scheduled 를 통해 의존 객체를 주입해줬으니 FileCheckTask 에도 객체를 만들어줘야한다.  // 서블릿컨텍스트에서 이 패키지도 스캔해야하니 @Service 애노테이션을 붙여준다. 
public class FileCheckTask {

@Value("${savefoldername}")
private String saveFolder;

@Autowired
private BoardService boardService;


//cron 사용법 
//seconds(초:0~59) minutes(분:0~59) hours(시:0~23) day(일:1~31)
//month(달:1~12) day of week(요일:0~6) year(optional)
@Scheduled(cron="0 09 14 * * *")
public void checkFiles() throws Exception{
   System.out.println("checkFiles");
   List<String> deleteFileList = boardService.getDeleteFileList();
   
   //for(String filename : deleteFileList)
   for(int i = 0; i< deleteFileList.size(); i++) {
      String filename = deleteFileList.get(i);
      File file = new File(saveFolder + filename);
      if(file.exists()) {
         if(file.delete()) {
            System.out.println(file.getPath() + " 삭제되었습니다.");
         }
      }else {
         System.out.println(file.getPath() + " 파일이 존재하지 않습니다.");
      }
   }
}


//스케줄러를 이용해서 주기적으로 매일, 매주, 매월 프로그램 실행을 위한 작업을 실시합니다.
//@Scheduled(fixedDelay = 1000) // 이전에 실행된 Task 종료 시간으로부터 정의된 시간만큼 지난 후 Task 를 실행합니다//밀리세컨드 단위입니다.  // 이 애노테이션을 인식해줘야할 걸 servlet-context.xml 에서 만들어줌
public void test() throws Exception{
   System.out.println("test");
}
}
