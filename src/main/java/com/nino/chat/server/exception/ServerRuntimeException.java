package com.nino.chat.server.exception;

/**
 * @author ss
 * @date 2018/9/11 15:05
 */
public class ServerRuntimeException extends RuntimeException {

    public ServerRuntimeException() {
    }

    public ServerRuntimeException(String message) {
        super(message);
    }

    public ServerRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerRuntimeException(Throwable cause) {
        super(cause);
    }

}
