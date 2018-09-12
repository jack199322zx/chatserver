package com.nino.chat.server.model;

import java.util.List;

/**
 * @author ss
 * @date 2018/9/11 16:15
 */
public class Message {
    private Deliver from;
    private List<? extends Deliver> to;
    private String content;
    private MessageType messageType;

    public Message(Deliver from, List<? extends Deliver> to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
    }

    public Message(List<? extends Deliver> to, String content, MessageType messageType) {
        this.to = to;
        this.content = content;
        this.messageType = messageType;
        if (messageType == MessageType.LOGIN || messageType == MessageType.LOGOUT) {
            from = new System();
        }
    }

    public Deliver getFrom() {
        return from;
    }

    public void setFrom(Deliver from) {
        this.from = from;
    }

    public List<? extends Deliver> getTo() {
        return to;
    }

    public void setTo(List<? extends Deliver> to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "from=" + from +
                ", to=" + to +
                ", content='" + content + '\'' +
                ", messageType=" + messageType +
                '}';
    }

    public enum MessageType {
        LOGIN, LOGOUT
    }
}
