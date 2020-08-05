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
	private ArrayList<Agent> agents;
	@Override
	public void onEnable() {
		try {
			agents=Agent.loadAll(this);
			if (agents==null){
				getLogger().warning("No Agent found! Are you sure add key.json under plugin/DialogFlowPlugin?");
				return;
			}
		} catch (IOException e) {
			getLogger().warning("Plugin loading failed: "+e.getMessage());
			return;
		}
		Bukkit.getPluginManager().registerEvents(this,this);

		getLogger().info("DialogFlowPlugin Enabled!");
		getLogger().info("List of Agents:");
		for (int i=0;i<agents.size();i++)
			getLogger().info("\t"+agents.get(i).getName());
		super.onEnable();
	}
	@Override
	public void onDisable() {
		getLogger().info("DialogFlowPlugin Disabled!");
		for (int i=0;i<agents.size();i++)
			agents.get(i).closeClient();
		
		super.onDisable();
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args[0].equals("start") && sender instanceof Player && sender.hasPermission("dialogflow.chat") &&
				args.length>=2) {
			Player player=(Player) sender;
			Agent agent=getAgent(args[1]);
			if (agent==null){
				player.sendMessage("No Agent found!: "+args[1]);
				return true;
			}
			else {
				try {
					agent.startChatting(player);
				} catch (InvalidChatStartException e) {
					player.sendMessage("This player is already chatting!");
				}
			}
			return true;
		}
		else if (args[0].equals("stop") && sender instanceof Player && sender.hasPermission("dialogflow.chat") &&
				args.length>=2){
			Player player=(Player) sender;
			Agent agent=getAgent(args[1]);
			if (agent==null){
				player.sendMessage("No Agent found!: "+args[1]);
				return true;
			}
			else {
				try {
					agent.stopChatting(player);
				} catch (InvalidChatStopException e) {
					player.sendMessage("This player is already leave chatting!");
				}
			}
			return true;
		}
		else if (args[0].equals("send") && sender instanceof Player && sender.hasPermission("dialogflow.send") &&
				args.length>=4) {
			Player player=(Player) sender;
			Agent agent=getAgent(args[1]);
			if (agent==null){
				player.sendMessage("No Agent found!: "+args[1]);
				return true;
			}

			if (args.length==4)
			{
				agent.sendMessage(player, args[2],false);
			}
			else {
				agent.sendMessage(player, args[2],args[3],false);
			}
			return true;
		}
		else if (args[0].equals("language") && sender.hasPermission("dialogflow.language") && args.length>=2) {
			Agent agent=getAgent(args[1]);
			if (agent==null){
				sender.sendMessage("No Agent found!: "+args[1]);
				return true;
			}

			sender.sendMessage("Language List for "+agent.getName()+":");
			java.util.List<String> list=agent.getLanguageCodes();
			for (int i=0;i<list.size();i++) {
				sender.sendMessage(list.get(i));
			}
			return true;
		}
		else if (args[0].equals("list") && sender.hasPermission("dialogflow.list")){
			sender.sendMessage("List of all Agents:");
			for (int i=0;i<agents.size();i++)
				sender.sendMessage(agents.get(i).getName());
			return true;
		}
		return false;
	}
	public Agent getAgent(String name){
		for (int i=0;i<agents.size();i++){
			if (agents.get(i).getName().equals(name))
				return agents.get(i);
		}
		return null;
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
