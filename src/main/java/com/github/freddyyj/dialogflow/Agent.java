package com.github.freddyyj.dialogflow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.freddyyj.dialogflow.event.MessageRequestEvent;
import com.github.freddyyj.dialogflow.event.MessageResponseEvent;
import com.github.freddyyj.dialogflow.exception.InvalidChatStartException;
import com.github.freddyyj.dialogflow.exception.InvalidChatStopException;
import com.google.api.gax.grpc.GrpcCallContext;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.ClientContext;
import com.google.cloud.dialogflow.v2.*;
import com.google.cloud.dialogflow.v2.stub.AgentsStub;
import com.google.cloud.dialogflow.v2.stub.GrpcAgentsStub;
import com.google.cloud.dialogflow.v2.stub.GrpcSessionsStub;
import com.google.cloud.dialogflow.v2.stub.SessionsStub;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.protobuf.InvalidProtocolBufferException;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Agent {
	private ArrayList<Player> chattingPlayerList;
	private SessionsClient sessionsClient;
	private Key key;
	private Core core;
	private com.google.cloud.dialogflow.v2.Agent agent;
	private AgentsClient client;
	protected Agent(Core core,String keyPath) throws IOException {
		this.core=core;
		key=new Key(core,keyPath);

		ClientContext.Builder contextBuilder=ClientContext.newBuilder();
		contextBuilder.setCredentials(key.getCredentials());
		GrpcCallContext callContext=GrpcCallContext.createDefault();
		contextBuilder.setDefaultCallContext(callContext.withCredentials(key.getCredentials()));
		ClientContext context=contextBuilder.build();
		SessionsStub stub= GrpcSessionsStub.create(context);
		sessionsClient=SessionsClient.create(stub);

		AgentsStub agentsStub= GrpcAgentsStub.create(context);
		client=AgentsClient.create(agentsStub);
		ProjectName project=ProjectName.newBuilder().setProject(key.getCredentials().getProjectId()).build();
		agent=client.getAgent(project);
		chattingPlayerList=new ArrayList<>();
		Bukkit.getPluginManager().registerEvents(new ChattingListener(),core);
	}
	public static ArrayList<Agent> loadAll(Core core) throws IOException{
		File folder=core.getDataFolder();
		File[] keyFile=folder.listFiles();
		if (keyFile==null) return null;

		ArrayList<Agent> agents=new ArrayList<>();
		for (int i=0;i< keyFile.length;i++){
			if (keyFile[i].isFile() && isJson(keyFile[i].getName())){
				Agent agent=new Agent(core,keyFile[i].getName());
				agents.add(agent);
			}
		}
		return agents;
	}
	private static boolean isJson(String fileName){
		return fileName.endsWith(".json");
	}
	Key getKey() {return key;}
	public void closeClient() {
		sessionsClient.close();
		client.shutdown();
	}
	public void startChatting(Player player) throws InvalidChatStartException {
		if (chattingPlayerList.contains(player)) throw new InvalidChatStartException("This player already added");
		chattingPlayerList.add(player);
	}
	public boolean isPlayerChatting(Player player){
		return chattingPlayerList.contains(player);
	}
	public void stopChatting(Player player) throws InvalidChatStopException {
		if (!chattingPlayerList.contains(player)) throw new InvalidChatStopException("This player already removed");
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
		sessionBuilder.setProject(key.getCredentials().getProjectId());
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
	public String getName(){
		return agent.getDisplayName();
	}

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