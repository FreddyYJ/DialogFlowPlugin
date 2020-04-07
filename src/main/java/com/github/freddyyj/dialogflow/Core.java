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

public final class Core extends JavaPlugin implements Listener {
	private DialogFlow dialogFlow;
	@Override
	public void onEnable() {
		try {
			dialogFlow=new DialogFlow(this);
		} catch (IOException e) {
			getLogger().warning("Plugin loading failed: "+e.getMessage());
		}
		Bukkit.getPluginManager().registerEvents(this,this);

		getLogger().info("DialogFlowPlugin Enabled!: "+dialogFlow.getKey().getProjectId()+", "+dialogFlow.getKey().getPrivateKey());
		super.onEnable();
	}
	@Override
	public void onDisable() {
		getLogger().info("DialogFlowPlugin Disabled!");
		dialogFlow.closeClient();
		
		super.onDisable();
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args[0].equals("start") && sender instanceof Player && sender.hasPermission("dialogflow.chat")) {
			Player player=(Player) sender;
			try {
				dialogFlow.startChatting(player);
			} catch (InvalidChatStartException e) {
				player.sendMessage("This player is already chatting!");
			}
			return true;
		}
		else if (args[0].equals("stop") && sender instanceof Player && sender.hasPermission("dialogflow.chat")){
			Player player=(Player) sender;
			try {
				dialogFlow.stopChatting(player);
			} catch (InvalidChatStopException e) {
				player.sendMessage("This player has stopped chatting!");
			}
			return true;
		}
		else if (args[0].equals("send") && sender instanceof Player && sender.hasPermission("dialogflow.send")) {
			Player player=(Player) sender;
			if (args.length==3)
			{
				dialogFlow.sendMessage(player, args[1],args[2],false);
			}
			else {
				dialogFlow.sendMessage(player, args[1],false);
			}
			return true;
		}
		else if (args[0].equals("language") && sender.hasPermission("dialogflow.language")) {
			sender.sendMessage("Language List for this Agent:");
			java.util.List<String> list=dialogFlow.getLanguageCodes();
			for (int i=0;i<list.size();i++) {
				sender.sendMessage(list.get(i));
			}
			return true;
		}
		else if (args[0].equals("list") && sender.hasPermission("dialogflow.list")){
			sender.sendMessage("Player list that current chatting:");
			java.util.List<Player> list=dialogFlow.getPlayerChatting();
			for (int i=0;i<list.size();i++)
				sender.sendMessage(list.get(i).getName());
			return true;
		}
		return false;
	}

	@EventHandler
	public void onMessageResponse(MessageResponseEvent event){
		Player sender=event.getSender();
		String response=event.getResponse().getQueryResult().getFulfillmentText();

		sender.sendMessage("Response: "+response);
	}
}
