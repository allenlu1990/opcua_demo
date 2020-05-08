package com.scc.runner;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.scc.runner.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SpringTask {
  //    private static final Logger log = LoggerFactory.getLogger(SpringTask.class);
@Autowired
  Task task;
  @Scheduled(cron = "* * * * * *")
  public void task1() {
    task.run();
  }
}
