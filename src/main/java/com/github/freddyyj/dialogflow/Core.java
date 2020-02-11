package com.github.freddyyj.dialogflow;

import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin{
	private DialogFlow dialogFlow;
	@Override
	public void onEnable() {
		// TODO Create plugin start method
		dialogFlow=new DialogFlow(this);
		
		getLogger().info("DialogFlowPlugin Enabled!: "+dialogFlow.getKey().getProjectId()+", "+dialogFlow.getKey().getPrivateKey());
		super.onEnable();
	}
	@Override
	public void onDisable() {
		// TODO Create plugin stop method
		getLogger().info("DialogFlowPlugin Disabled!");
		
		super.onDisable();
	}
}
