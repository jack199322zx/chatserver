package com.nino.chat.server.model;

/**
 * @author ss
 * @date 2018/9/11 16:26
 */
public abstract class Deliver {

    protected DeliverType deliverType;

    public Deliver(DeliverType type) {
        deliverType = type;
    }

    public DeliverType getDeliverType() {
        return deliverType;
    }

    public void setDeliverType(DeliverType deliverType) {
        this.deliverType = deliverType;
    }

    public enum DeliverType {
        SYSTEM, USER
    }
}
