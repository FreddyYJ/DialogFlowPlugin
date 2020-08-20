package com.github.freddyyj.dialogflow.exception;

/**
 * Exception for handle error at stopping chatting with DialogFlow
 */
public class InvalidChatStopException extends RuntimeException {
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
