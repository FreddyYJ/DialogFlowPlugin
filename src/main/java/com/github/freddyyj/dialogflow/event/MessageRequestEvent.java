package com.github.freddyyj.dialogflow.event;

import com.google.cloud.dialogflow.v2.DetectIntentRequest;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MessageRequestEvent extends Event {
    private static final HandlerList handlerList=new HandlerList();
    private Player sender;
    private DetectIntentRequest request;
    public MessageRequestEvent(Player sender,DetectIntentRequest request){
        this.sender=sender;
        this.request=request;
    }
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
    public static HandlerList getHandlerList(){
        return handlerList;
    }
    public Player getSender(){return sender;}
    public DetectIntentRequest getRequest(){return request;}
}
