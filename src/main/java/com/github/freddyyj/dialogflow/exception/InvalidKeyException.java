package com.github.freddyyj.dialogflow.exception;

/**
 * Exception if service key account file is invalid
 */
public class InvalidKeyException extends RuntimeException{
    String message;
    Throwable cause;
    public InvalidKeyException(){
        super();
    }
    public InvalidKeyException(String message){
        super(message);
        this.message=message;
    }
    public InvalidKeyException(String message,Throwable cause){
        super(message,cause);
        this.message=message;
        this.cause=cause;
    }
    public InvalidKeyException(Throwable cause){
        super(cause);
        this.cause=cause;
    }

}
