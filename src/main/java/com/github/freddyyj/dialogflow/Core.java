package com.github.freddyyj.dialogflow;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
		if (args[0].equals("df") || args[0].equals("dialogflow")) {
			if (args[1].equals("start")) {
				dialogFlow.createSession(sender);
				return true;
			}
		}
		return super.onCommand(sender, command, label, args);
	}
}
