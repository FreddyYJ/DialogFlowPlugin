package com.github.freddyyj.dialogflow;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin{
	private DialogFlow dialogFlow;
	@Override
	public void onEnable() {
		dialogFlow=new DialogFlow(this);
		
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
			dialogFlow.createSession(player);
			player.sendMessage("Player added to session");
			return true;
		}
		else if (args[0].equals("send") && sender instanceof Player) {
			Player player=(Player) sender;
			dialogFlow.createSession(player);
			String response=dialogFlow.sendMessage(player, args[1]).getQueryResult().getFulfillmentText();
			player.sendMessage(response);
			return true;
		}
		return false;
	}
}
