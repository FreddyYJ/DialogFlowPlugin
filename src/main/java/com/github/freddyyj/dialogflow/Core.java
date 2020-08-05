package com.github.freddyyj.dialogflow;

import com.github.freddyyj.dialogflow.exception.InvalidChatStartException;
import com.github.freddyyj.dialogflow.exception.InvalidChatStopException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.freddyyj.dialogflow.event.MessageResponseEvent;

import java.io.IOException;
import java.util.ArrayList;

public final class Core extends JavaPlugin implements Listener {
	private Agent agent;
	@Override
	public void onEnable() {
		try {
			agent=Agent.getInstance(this,Key.KEY_PATH);
		} catch (IOException e) {
			getLogger().warning("Plugin loading failed: "+e.getMessage());
			return;
		}
		Bukkit.getPluginManager().registerEvents(this,this);

		getLogger().info("DialogFlowPlugin Enabled!");
		getLogger().info("Agent loaded: "+agent.getName());
		super.onEnable();
	}
	@Override
	public void onDisable() {
		getLogger().info("DialogFlowPlugin Disabled!");
		agent.closeClient();
		
		super.onDisable();
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args[0].equals("start") && sender instanceof Player && sender.hasPermission("dialogflow.chat")) {
			Player player=(Player) sender;
			try {
				agent.startChatting(player);
			} catch (InvalidChatStartException e) {
				player.sendMessage("This player is already chatting!");
			}
			return true;
		}
		else if (args[0].equals("stop") && sender instanceof Player && sender.hasPermission("dialogflow.chat")){
			Player player=(Player) sender;
			try {
				agent.stopChatting(player);
			} catch (InvalidChatStopException e) {
				player.sendMessage("This player is already leave chatting!");
			}
			return true;
		}
		else if (args[0].equals("send") && sender instanceof Player && sender.hasPermission("dialogflow.send")) {
			Player player=(Player) sender;
			if (args.length==4)
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
			sender.sendMessage("List of players who chatting:");
			for (int i=0;i<agent.getPlayerChatting().size();i++)
				sender.sendMessage(agent.getPlayerChatting().get(i).getName());
			return true;
		}
		return false;
	}
	@EventHandler
	public void onMessageResponse(MessageResponseEvent event){
		if (!event.isCancelled()){
			Player sender=event.getSender();
			String response=event.getResponse().getQueryResult().getFulfillmentText();

			sender.sendMessage("Response: "+response);
		}
	}
}
