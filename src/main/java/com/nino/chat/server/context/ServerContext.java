package com.nino.chat.server.context;


import com.nino.chat.server.model.Message;

/**
 * @author ss
 * @date 2018/9/11 13:57
 */
public interface ServerContext {
    void startUp();
    void publishMessage(Message message);
}
