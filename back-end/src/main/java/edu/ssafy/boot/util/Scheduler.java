package edu.ssafy.boot.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import edu.ssafy.boot.service.IContentService;
import edu.ssafy.boot.service.IUserService;

@Component
public class Scheduler {

    @Autowired
    @Qualifier("UserService")
    IUserService ser;
    
    @Autowired
    @Qualifier("ContentService")
    IContentService cSer;

   @Scheduled(cron = "0 0 0 * * ?")
   public void updateUserStatus() {
    //   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    //   Date now = new Date();
    //   String strDate = sdf.format(now);
    //   System.out.println("Java cron job expression:: " + strDate);
    //   System.out.println("Current Thread : " + Thread.currentThread().getName());
      
      ser.updateUserStatus();
   }

   @Scheduled(cron = "0 0 0 * * ?")
   public void deleteReportedContents() {
      cSer.deleteReportedContents();
   }
}