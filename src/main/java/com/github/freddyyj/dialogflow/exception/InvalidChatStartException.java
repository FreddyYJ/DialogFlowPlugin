package com.github.freddyyj.dialogflow.exception;

/**
 * Exception for handle error at starting chatting with DialogFlow
 */
public class InvalidChatStartException extends Exception{
    String message;
    Throwable cause;
    public InvalidChatStartException(){
        super();
    }
    public InvalidChatStartException(String message){
        super(message);
        this.message=message;
    }
    public InvalidChatStartException(String message,Throwable cause){
        super(message,cause);
        this.message=message;
        this.cause=cause;
    }
    public InvalidChatStartException(Throwable cause){
        super(cause);
        this.cause=cause;
    }
}
