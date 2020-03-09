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
			String response;
			if (args.length==3)
			{
				response=dialogFlow.sendMessage(player, args[1],args[2]).getQueryResult().getFulfillmentText();

			}
			else {
				response=dialogFlow.sendMessage(player, args[1]).getQueryResult().getFulfillmentText();

			}
			player.sendMessage(response);
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
}
