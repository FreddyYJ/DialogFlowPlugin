package com.github.freddyyj.dialogflow.event;

import com.google.cloud.dialogflow.v2.DetectIntentRequest;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MessageRequestEvent extends Event {
    private static final HandlerList handlerList=new HandlerList();
    private Player sender;
    private SessionName sessionName;
    private QueryInput input;
    public MessageRequestEvent(Player sender,SessionName session, QueryInput input){
        this(sender,session,input,false);
    }
    public MessageRequestEvent(Player sender, SessionName session, QueryInput input, boolean isAsync){
        super(isAsync);
        this.sender=sender;
        this.sessionName=session;
        this.input=input;
    }
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
    public static HandlerList getHandlerList(){
        return handlerList;
    }
    public Player getSender(){return sender;}
    public SessionName getSessionName(){return sessionName;}
    public QueryInput getQueryInput(){return input;}
}
