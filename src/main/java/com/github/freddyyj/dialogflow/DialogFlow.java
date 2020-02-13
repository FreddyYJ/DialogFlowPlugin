package com.github.freddyyj.dialogflow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.cloud.dialogflow.v2.DetectIntentRequest;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.protobuf.InvalidProtocolBufferException;

public class DialogFlow {
	private HashMap<String,SessionName> clientList;
	private SessionsClient sessionsClient;
	private Key key;
	private Core core;
	DialogFlow(Core core) {
		this.core=core;
		reloadKey();
		
		clientList=new HashMap<>();
		
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
	public void createSession(Player player) {
		clientList.put(player.getName(),SessionName.of(key.getProjectId(), player.getName()));
	}
	public void closeClient() {
		sessionsClient.close();
	}
	public DetectIntentResponse sendMessage(Player player,String message) {
		SessionName session=clientList.get(player.getName());
		QueryInput.Builder input=QueryInput.newBuilder();
		TextInput.Builder textBuilder=TextInput.newBuilder();
		textBuilder.setText(message);
		textBuilder.setLanguageCode("ko-KR");
		input.setText(textBuilder);
		QueryInput query=input.build();
		
		return sessionsClient.detectIntent(session, query);
	}
}
