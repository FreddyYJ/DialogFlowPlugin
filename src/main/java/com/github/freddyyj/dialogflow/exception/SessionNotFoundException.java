package com.github.freddyyj.dialogflow.exception;

/**
 * Exception for handle session is not exist
 * @author FreddyYJ_
 */
public class SessionNotFoundException extends RuntimeException{
    String message;
    Throwable cause;
    public SessionNotFoundException(){
        super();
    }
    public SessionNotFoundException(String message){
        super(message);
        this.message=message;
    }
    public SessionNotFoundException(String message,Throwable cause){
        super(message,cause);
        this.message=message;
        this.cause=cause;
    }
    public SessionNotFoundException(Throwable cause){
        super(cause);
        this.cause=cause;
    }

}
