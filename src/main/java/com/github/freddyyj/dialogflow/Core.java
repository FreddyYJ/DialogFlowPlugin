package com.github.freddyyj.dialogflow;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.freddyyj.dialogflow.event.MessageResponseEvent;

public final class Core extends JavaPlugin implements Listener {
	private DialogFlow dialogFlow;
	@Override
	public void onEnable() {
		dialogFlow=new DialogFlow(this);
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
		if (args[0].equals("start") && sender instanceof Player) {
			Player player=(Player) sender;
			player.sendMessage("Player added to session");
			return true;
		}
		else if (args[0].equals("send") && sender instanceof Player) {
			Player player=(Player) sender;
			if (args.length==3)
			{
				dialogFlow.sendMessage(player, args[1],args[2]);
			}
			else {
				dialogFlow.sendMessage(player, args[1]);
			}
			return true;
		}
		else if (args[0].equals("language")) {
			sender.sendMessage("Language List for this Agent:");
			java.util.List<String> list=dialogFlow.getLanguageCodes();
			for (int i=0;i<list.size();i++) {
				sender.sendMessage(list.get(i));
			}
			return true;
		}
		else if (args[0].equals("count")) {
			sender.sendMessage("Count of language of this Agent: "+dialogFlow.getLanguageCodeCount());
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
