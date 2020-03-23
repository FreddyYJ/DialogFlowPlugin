package com.github.freddyyj.dialogflow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class DialogFlow {
	private ArrayList<Player> chattingPlayerList;
	private SessionsClient sessionsClient;
	private Key key;
	private Core core;
	private Agent agent;
	private AgentsClient client;
	DialogFlow(Core core) {
		this.core=core;
		reloadKey();

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
		chattingPlayerList=new ArrayList<>();
		Bukkit.getPluginManager().registerEvents(new ChattingListener(),core);
	}
	public void reloadKey() {
		key=new Key(core);
	}
	Key getKey() {return key;}
	public void closeClient() {
		sessionsClient.close();
		client.shutdown();
	}
	public void startChatting(Player player){
		// TODO Add Exception for already chatting player
		chattingPlayerList.add(player);
	}
	public boolean isPlayerChatting(Player player){
		return chattingPlayerList.contains(player);
	}
	public void stopChatting(Player player){
		// TODO Add Exception for already finish chatting
		chattingPlayerList.remove(player);
	}
	public List<Player> getPlayerChatting(){return chattingPlayerList;}
	public void sendMessage(Player player,String message,boolean isAsync) {
		sendMessage(player, message, getDefaultLanguageCode(),isAsync);
	}
	public void sendMessage(Player player,String message,String languageCode,boolean isAsync) {
		QueryInput.Builder input=QueryInput.newBuilder();
		TextInput.Builder textBuilder=TextInput.newBuilder();
		textBuilder.setText(message);
		textBuilder.setLanguageCode(languageCode);
		input.setText(textBuilder);
		QueryInput query=input.build();

		SessionName.Builder sessionBuilder=SessionName.newBuilder();
		sessionBuilder.setProject(key.getProjectId());
		sessionBuilder.setSession(player.getName());

		MessageRequestEvent requestEvent=new MessageRequestEvent(player,sessionBuilder.build(),query,isAsync);
		Bukkit.getServer().getPluginManager().callEvent(requestEvent);

		DetectIntentResponse response=sessionsClient.detectIntent(sessionBuilder.build(),query);

		MessageResponseEvent responseEvent=new MessageResponseEvent(player,response,isAsync);
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

	class ChattingListener implements Listener {
		@EventHandler
		public void onAsyncPlayerChat(AsyncPlayerChatEvent event){
			Player player=event.getPlayer();
			String msg=event.getMessage();
			if (isPlayerChatting(player)) {
				event.setCancelled(true);
				sendMessage(player, msg,true);
			}
		}
	}
}
