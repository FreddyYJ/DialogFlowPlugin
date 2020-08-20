package com.github.freddyyj.dialogflow;

import com.github.freddyyj.dialogflow.config.Configuration;
import com.github.freddyyj.dialogflow.event.SessionCreatedEvent;
import com.github.freddyyj.dialogflow.exception.InvalidChatStartException;
import com.github.freddyyj.dialogflow.exception.InvalidChatStopException;
import com.github.freddyyj.dialogflow.exception.InvalidKeyException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.freddyyj.dialogflow.event.MessageResponseEvent;

import java.io.IOException;
import java.util.ArrayList;

/**
 * JavaPlugin for DialogFlowPlugin.
 * @author FreddyYJ_
 */
public final class Core extends JavaPlugin implements Listener {
	private Agent agent;
	private Configuration config;
	private boolean isEnabled=false;

	/**
	 * Override {@link JavaPlugin#onEnable()} method.
	 */
	@Override
	public void onEnable() {
		try {
			agent=Agent.getInstance(this,Key.KEY_PATH);
		} catch (IOException | InvalidKeyException e) {
			getLogger().warning("Plugin loading failed: "+e.getMessage());
			return;
		}
		Bukkit.getPluginManager().registerEvents(this,this);

		try {
			config=new Configuration(this);
		} catch (InvalidConfigurationException e) {
			getLogger().warning("Plugin loading failed: "+e.getMessage());
			return;
		}
		if (config.getAgentName()!=null) agent.setName(config.getAgentName());
		agent.color=config.getAgentColor();

		isEnabled=true;
		getLogger().info("DialogFlowPlugin Enabled!");
		getLogger().info("Agent loaded: "+agent.getName());
		super.onEnable();
	}

	/**
	 * Override {@link JavaPlugin#onDisable()} method.
	 */
	@Override
	public void onDisable() {
		if (isEnabled){
			agent.closeClient();
		}
		getLogger().info("DialogFlowPlugin Disabled!");

		super.onDisable();
	}

	/**
	 * Override {@link JavaPlugin#onCommand(CommandSender, Command, String, String[])} method.
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length==0){
			sender.sendMessage("List of DialogFlowPlugin Commands:");
			if (sender.hasPermission("dialogflow.chat")){
				sender.sendMessage("/df start: Start chatting with Agent.");
				sender.sendMessage("/df stop: Stop chatting with Agent.");
			}
			if (sender.hasPermission("dialogflow.send")){
				sender.sendMessage("/df send (message) [language code]: send (message) with [language code] to Agent once. Default language if no language code specified.");
			}
			if (sender.hasPermission("dialogflow.language"))
				sender.sendMessage("/df language: List of language code that Agent has.");
			if (sender.hasPermission("dialogflow.list"))
				sender.sendMessage("/df list: List of players who chatting with Agent.");
			return true;
		}
		if (args[0].equals("start") && sender instanceof Player && sender.hasPermission("dialogflow.chat")) {
			Player player=(Player) sender;
			try {
				agent.startChatting(player);
				player.sendMessage("Start chatting with Agent "+agent.getName()+"!");
			} catch (InvalidChatStartException e) {
				player.sendMessage("This player is already chatting!");
			}
			return true;
		}
		else if (args[0].equals("stop") && sender instanceof Player && sender.hasPermission("dialogflow.chat")){
			Player player=(Player) sender;
			try {
				agent.stopChatting(player);
				player.sendMessage("Stop chatting with Agent "+agent.getName()+"!");
			} catch (InvalidChatStopException e) {
				player.sendMessage("This player is already leave chatting!");
			}
			return true;
		}
		else if (args[0].equals("send") && sender instanceof Player && sender.hasPermission("dialogflow.send")) {
			Player player=(Player) sender;
			player.sendMessage("<"+player.getDisplayName()+"> "+args[1]);
			if (args.length==2)
			{
				agent.sendMessage(player, args[1],false);
			}
			else {
				agent.sendMessage(player, args[1],args[2],false);
			}
			return true;
		}
		else if (args[0].equals("language") && sender.hasPermission("dialogflow.language")) {
			sender.sendMessage("Language List for "+agent.getName()+":");
			java.util.List<String> list=agent.getLanguageCodes();
			for (int i=0;i<list.size();i++) {
				sender.sendMessage(list.get(i));
			}
			return true;
		}
		else if (args[0].equals("list") && sender.hasPermission("dialogflow.list")){
			sender.sendMessage("List of players who chatting with "+agent.getName()+":");
			for (int i=0;i<agent.getPlayerChatting().size();i++)
				sender.sendMessage(agent.getPlayerChatting().get(i).getName());
			return true;
		}
		return false;
	}

	/**
	 * Default listener when DialogFlow message received.
	 * <p>
	 *     If you want custom listener, create and register same as Spigot event listener.
	 * </p>
	 * @param event event MessageResponseEvent object.
	 */
	@EventHandler
	public void onMessageResponse(MessageResponseEvent event){
		if (!event.isCancelled()){
			Player sender=event.getSender();
			String response=event.getResponse().getQueryResult().getFulfillmentText();

			sender.sendMessage("["+agent.color+agent.getName()+ChatColor.WHITE+"] "+response);
		}
	}

	/**
	 * Default listener for send message when session created
	 * @param event
	 */
	@EventHandler
	public void onSessionCreation(SessionCreatedEvent event){
		if(!event.isCancelled()){
			event.getPlayer().sendMessage("New Session created for Agent: "+agent.getName());
		}
	}

	/**
	 * listener to handle session creation
	 * @param event PlayerJoinEvent object
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		agent.createSession(event.getPlayer());
	}

	/**
	 * listener to handler session remove
	 * @param event PlayerQuitEvent object
	 */
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		agent.removeSession(event.getPlayer());
	}
}
