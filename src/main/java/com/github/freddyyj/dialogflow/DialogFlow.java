package com.github.freddyyj.dialogflow;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;

public class DialogFlow {
	private ArrayList<SessionName> clientList;
	private SessionsClient sessionsClient;
	private Key key;
	private Core core;
	DialogFlow(Core core) {
		this.core=core;
		reloadKey();
		
		clientList=new ArrayList<>();
		
		try {
			sessionsClient=SessionsClient.create();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void reloadKey() {
		key=new Key(core);
	}
	Key getKey() {return key;}
	public void createSession(CommandSender sender) {
		// TODO catch Exception for duplicated session
		clientList.add(SessionName.of(key.getProjectId(), sender.getName()));
	}
	public void closeClient() {
		sessionsClient.close();
	}
}
