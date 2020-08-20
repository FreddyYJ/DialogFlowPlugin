package com.github.freddyyj.dialogflow.event;

import com.github.freddyyj.dialogflow.Agent;
import com.google.cloud.dialogflow.v2.SessionName;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * called after new session created
 * @author FreddyYJ_
 */
public class SessionCreatedEvent extends Event implements Cancellable {
    private Player player;
    private SessionName session;
    private Agent agent;
    private boolean isCancelled;
    private static final HandlerList handlerList=new HandlerList();

    /**
     * constructor with target player, created session, target agent
     * @param player target player
     * @param session created session
     * @param agent target agent
     */
    public SessionCreatedEvent(Player player,SessionName session,Agent agent){
        this.player=player;
        this.session=session;
        this.agent=agent;
        isCancelled=false;
    }
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    /**
     * Get this event is cancelled.
     */
    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    /**
     * set this event cancelled
     */
    @Override
    public void setCancelled(boolean cancel) {
        isCancelled=cancel;
    }
    public static HandlerList getHandlerList(){
        return handlerList;
    }


    public Player getPlayer() {
        return player;
    }

    public SessionName getSession() {
        return session;
    }

    public Agent getAgent() {
        return agent;
    }
}
