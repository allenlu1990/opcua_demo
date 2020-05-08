package com.scc.runner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RunnerApplication {

  public static void main(String[] args) {
    SpringApplication.run(RunnerApplication.class, args);
  }

}
