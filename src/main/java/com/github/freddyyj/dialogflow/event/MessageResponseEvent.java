package com.github.freddyyj.dialogflow.event;

import com.google.cloud.dialogflow.v2.Agent;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MessageResponseEvent extends Event implements Cancellable {
    private boolean isCancelled=false;
    private static final HandlerList handlerList=new HandlerList();
    private Player sender;
    private DetectIntentResponse response;
    private Agent agent;
    public MessageResponseEvent(Player sender, DetectIntentResponse response,Agent agent){
        this(sender,response,agent,false);
    }
    public MessageResponseEvent(Player sender, DetectIntentResponse response,Agent agent,boolean isAsync){
        super(isAsync);
        this.sender=sender;
        this.response=response;
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
    public DetectIntentResponse getResponse(){return response;}
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
