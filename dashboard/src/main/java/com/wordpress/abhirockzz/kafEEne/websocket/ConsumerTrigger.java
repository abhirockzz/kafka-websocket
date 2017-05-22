package com.wordpress.abhirockzz.kafEEne.websocket;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;

@Singleton
@Startup
public class ConsumerTrigger {
    @Resource
    TimerService ts;
    
    @PostConstruct
    public void schedule(){
        ts.createSingleActionTimer(10000, new TimerConfig());
        System.out.println("Setup one-time timer");
    }
    
    @Inject
    Consumer consumer;
    
    @Timeout
    public void trigger(){
        System.out.println("Timer triggered. invoking consumer....");
        consumer.consume();
    }
}
