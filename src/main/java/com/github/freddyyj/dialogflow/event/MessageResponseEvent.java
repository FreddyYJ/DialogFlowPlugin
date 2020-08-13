package com.github.freddyyj.dialogflow.event;

import com.google.cloud.dialogflow.v2.Agent;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called after message received from DialogFlow
 */
public class MessageResponseEvent extends Event implements Cancellable {
    private boolean isCancelled=false;
    private static final HandlerList handlerList=new HandlerList();
    private Player sender;
    private DetectIntentResponse response;
    private Agent agent;

    /**
     * constructor with default async option
     * @param sender player who send message
     * @param response response message from DialogFlow
     * @param agent agent that response it
     */
    public MessageResponseEvent(Player sender, DetectIntentResponse response,Agent agent){
        this(sender,response,agent,false);
    }

    /**
     * constructor with async option
     * @param sender player who send message
     * @param response response message from DialogFlow
     * @param agent agent that response it
     * @param isAsync Get message at async if true.
     */
    public MessageResponseEvent(Player sender, DetectIntentResponse response,Agent agent,boolean isAsync){
        super(isAsync);
        this.sender=sender;
        this.response=response;
        this.agent=agent;
    }

    /**
     * Override {@link Event#getHandlers()} method.
     */
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

    /**
     * Override {@link Cancellable#isCancelled()} method.
     */
    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    /**
     * Override {@link Cancellable#setCancelled(boolean)} method.
     */
    @Override
    public void setCancelled(boolean cancel) {
        isCancelled=cancel;
    }
}
