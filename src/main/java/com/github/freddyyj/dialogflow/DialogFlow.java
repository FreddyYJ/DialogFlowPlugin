package com.github.freddyyj.dialogflow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.github.freddyyj.dialogflow.event.MessageRequestEvent;
import com.github.freddyyj.dialogflow.event.MessageResponseEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.cloud.dialogflow.v2.Agent;
import com.google.cloud.dialogflow.v2.AgentsClient;
import com.google.cloud.dialogflow.v2.DetectIntentRequest;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.ProjectName;
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
	private Agent agent;
	private AgentsClient client;
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
		
		try {
			client=AgentsClient.create();
			ProjectName name=ProjectName.newBuilder().setProject(key.getProjectId()).build();
			agent=client.getAgent(name);
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
		client.shutdown();
	}
	public void sendMessage(Player player,String message) {
		sendMessage(player, message, getDefaultLanguageCode());
	}
	public void sendMessage(Player player,String message,String languageCode) {
		SessionName session=clientList.get(player.getName());
		QueryInput.Builder input=QueryInput.newBuilder();
		TextInput.Builder textBuilder=TextInput.newBuilder();
		textBuilder.setText(message);
		textBuilder.setLanguageCode(languageCode);
		input.setText(textBuilder);
		QueryInput query=input.build();

		DetectIntentRequest.Builder request=DetectIntentRequest.newBuilder();
		request.setQueryInput(query);
		request.setSession(player.getName());

		MessageRequestEvent requestEvent=new MessageRequestEvent(player,request.build());
		Bukkit.getServer().getPluginManager().callEvent(requestEvent);

		DetectIntentResponse response=sessionsClient.detectIntent(session, query);

		MessageResponseEvent responseEvent=new MessageResponseEvent(player,response);
		Bukkit.getServer().getPluginManager().callEvent(responseEvent);
	}
	public String getDefaultLanguageCode() {
		return agent.getDefaultLanguageCode();
	}
	public java.util.List<String> getLanguageCodes(){
		int count=agent.getSupportedLanguageCodesCount();
		ArrayList<String> codeList=new ArrayList<>();
		
		codeList.add(getDefaultLanguageCode());
		for (int i=0;i<count;i++) {
			codeList.add(agent.getSupportedLanguageCodes(i));
		}
		return codeList;
	}
	public int getLanguageCodeCount() {return agent.getSupportedLanguageCodesCount()+1;}
}
