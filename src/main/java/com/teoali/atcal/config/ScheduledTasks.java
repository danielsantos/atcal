package com.teoali.atcal.config;

import com.teoali.atcal.service.PaymentService;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

  @Autowired
  private PaymentService paymentService;

  //private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

//  @Scheduled(fixedRate = 5000)
//  @Scheduled(cron = "0 4,6 * * * ?") -- second, minute, hour, day of month, month, day(s) of week
  @Scheduled(cron = "0 7 * * * ?")
  public void reportCurrentTime() {
    //log.info("The time is now {}", dateFormat.format(new Date()));
    System.out.println("updatePastDuePayments:: BEGIN:: " + dateFormat.format(new Date()));
    System.out.println(paymentService.updatePastDuePayments());
    System.out.println("updatePastDuePayments:: END:: " + dateFormat.format(new Date()));
  }
}
