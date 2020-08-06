package com.github.freddyyj.dialogflow.event;

import com.google.cloud.dialogflow.v2.Agent;
import com.google.cloud.dialogflow.v2.DetectIntentRequest;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MessageRequestEvent extends Event implements Cancellable {
    private boolean isCancelled=false;
    private static final HandlerList handlerList=new HandlerList();
    private Player sender;
    private SessionName sessionName;
    private QueryInput input;
    private Agent agent;
    public MessageRequestEvent(Player sender,SessionName session, QueryInput input,Agent agent){
        this(sender,session,input,agent,false);
    }
    public MessageRequestEvent(Player sender, SessionName session, QueryInput input,Agent agent,boolean isAsync){
        super(isAsync);
        this.sender=sender;
        this.sessionName=session;
        this.input=input;
        this.agent=agent;
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
    public Agent getAgent(){return agent;}

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        isCancelled=cancel;
    }
}
