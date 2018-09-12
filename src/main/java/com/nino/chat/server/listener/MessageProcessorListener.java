package com.nino.chat.server.listener;

/**
 * @author ss
 * @date 2018/9/11 14:39
 */
public class MessageProcessorListener {

    public void notifyAdd() {
        System.out.println("notifyAdd");
    }

    public void process() {
        System.out.println("process");
    }
}
