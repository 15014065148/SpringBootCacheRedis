package com.luosai;

import org.springframework.stereotype.Component;

/**
 * Created by sai.luo on 2017-12-9.
 */
@Component
public class HelloHandle {
    public void handle(String message){
        System.out.println("helloHandle receive from mq，message is: : "+message);
    }
}
