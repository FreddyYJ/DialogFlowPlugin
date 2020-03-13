package com.github.freddyyj.dialogflow.event;

import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MessageResponseEvent extends Event {
    private static final HandlerList handlerList=new HandlerList();
    private Player sender;
    private DetectIntentResponse response;
    public MessageResponseEvent(Player sender, DetectIntentResponse response){
        this.sender=sender;
        this.response=response;
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
}
