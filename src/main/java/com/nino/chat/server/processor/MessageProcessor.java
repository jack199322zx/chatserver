package com.nino.chat.server.processor;

import com.nino.chat.server.listener.MessageProcessorListener;
import com.nino.chat.server.model.Deliver;
import com.nino.chat.server.model.Message;
import com.nino.chat.server.model.User;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author ss
 * @date 2018/9/11 14:34
 */
public class MessageProcessor {
    private Queue<Message> messages = new LinkedBlockingDeque<>();
    private Map<String, PrintWriter> printWriters;
    private MessageProcessorListener listener;

    public MessageProcessor(Map<String, PrintWriter> printWriters) {
        this.printWriters = printWriters;
        listener = new MessageProcessorListener();
    }

    public void processMessage() {
        if (messages != null) {
            // currentMessage dosomething
            Message message = processCurrentMessage();
            List<? extends Deliver> to = message.getTo();
            to.forEach(t -> {
                if (t.getDeliverType() == Deliver.DeliverType.USER) {
                    PrintWriter pw = printWriters.get(((User)t).getNickName());
                    pw.println(message.getContent());
                }
            });
        }
    }

    public boolean addMessage(Message message) {
        listener.notifyAdd();
        return messages.offer(message);
    }

    public Message removeMessage(Message message) {
        return messages.poll();
    }

    public Message getCurrentMessage() {
        return messages.peek();
    }

    public Message processCurrentMessage() {
        listener.process();
        return messages.poll();
    }

    public Map<String, PrintWriter> getPrintWriter() {
        return printWriters;
    }

    public void setPrintWriter(Map<String, PrintWriter> printWriters) {
        this.printWriters = printWriters;
    }
}
