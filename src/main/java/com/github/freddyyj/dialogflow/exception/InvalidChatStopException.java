package com.github.freddyyj.dialogflow.exception;

public class InvalidChatStopException extends Exception {
    String message;
    Throwable cause;
    public InvalidChatStopException(){
        super();
    }
    public InvalidChatStopException(String message){
        super(message);
        this.message=message;
    }
    public InvalidChatStopException(String message, Throwable cause){
        super(message,cause);
        this.message=message;
        this.cause=cause;
    }
    public InvalidChatStopException(Throwable cause){
        super(cause);
        this.cause=cause;
    }

}
