package com.github.freddyyj.dialogflow.event;

import com.google.cloud.dialogflow.v2.Agent;
import com.google.cloud.dialogflow.v2.DetectIntentRequest;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called before message sent to DialogFlow
 */
public class MessageRequestEvent extends Event implements Cancellable {
    private boolean isCancelled=false;
    private static final HandlerList handlerList=new HandlerList();
    private Player sender;
    private SessionName sessionName;
    private QueryInput input;
    private Agent agent;

    /**
     * constructor with default async option
     * @param sender player who send message
     * @param session created session
     * @param input queried input
     * @param agent message destination agent
     */
    public MessageRequestEvent(Player sender,SessionName session, QueryInput input,Agent agent){
        this(sender,session,input,agent,false);
    }

    /**
     * constructor with async option
     * @param sender player who send message
     * @param session created session
     * @param input queried input
     * @param agent message destination agent
     * @param isAsync Send message at async if true.
     */
    public MessageRequestEvent(Player sender, SessionName session, QueryInput input,Agent agent,boolean isAsync){
        super(isAsync);
        this.sender=sender;
        this.sessionName=session;
        this.input=input;
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
    public SessionName getSessionName(){return sessionName;}
    public QueryInput getQueryInput(){return input;}
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
