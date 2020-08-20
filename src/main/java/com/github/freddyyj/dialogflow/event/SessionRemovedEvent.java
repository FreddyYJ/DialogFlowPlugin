package com.github.freddyyj.dialogflow.event;

import com.github.freddyyj.dialogflow.Agent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * called after session removed
 * @author FreddyYJ_
 */
public class SessionRemovedEvent extends Event implements Cancellable {
    private Player player;
    private Agent agent;
    private boolean isCancelled;
    private static final HandlerList handlerList=new HandlerList();

    /**
     * constructor with target player, target agent
     * @param player target player
     * @param agent target agent
     */
    public SessionRemovedEvent(Player player,Agent agent){
        this.player=player;
        this.agent=agent;
        isCancelled=false;
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

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public Player getPlayer() {
        return player;
    }

    public Agent getAgent() {
        return agent;
    }
}
