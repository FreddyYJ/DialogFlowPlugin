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
import com.github.freddyyj.dialogflow.exception.InvalidKeyException;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.core.GoogleCredentialsProvider;
import com.google.api.gax.grpc.GrpcCallContext;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.ClientContext;
import com.google.cloud.dialogflow.v2.*;
import com.google.cloud.dialogflow.v2.stub.AgentsStub;
import com.google.cloud.dialogflow.v2.stub.GrpcAgentsStub;
import com.google.cloud.dialogflow.v2.stub.GrpcSessionsStub;
import com.google.cloud.dialogflow.v2.stub.SessionsStub;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.protobuf.InvalidProtocolBufferException;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Agent class. It should be Singleton.
 * @author FreddyYJ_
 */
public class Agent {
	private static Agent singleton=null;
	private ArrayList<Player> chattingPlayerList;
	private SessionsClient sessionsClient;
	private Key key;
	private Core core;
	private com.google.cloud.dialogflow.v2.Agent agent;
	private AgentsClient client;
	private String name;
	/**
	 * Agent color for chat response.
	 */
	public ChatColor color;

	/**
	 * Constructor with custom Agent name and color.
	 * @param core DialogFlowPlugin core.
	 * @param keyPath Service key file path for this agent. Default is Key.KEY_PATH.
	 * @param name Custom name for this agent.
	 * @param color Custom color for this agent.
	 * @throws InvalidKeyException Throws if service key file is invalid.
	 * @throws IOException Throws error when creating sessions or agent object.
	 */
	protected Agent(Core core,String keyPath,String name,ChatColor color) throws InvalidKeyException, IOException {
		this.core=core;
		key=new Key(core,keyPath);

		FixedCredentialsProvider credentialsProvider=FixedCredentialsProvider.create(key.getCredentials());
		SessionsSettings sessionsSetting=SessionsSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
		sessionsClient=SessionsClient.create(sessionsSetting);

		AgentsSettings agentsSetting=AgentsSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
		client=AgentsClient.create(agentsSetting);
		ProjectName project=ProjectName.newBuilder().setProject(key.getCredentials().getProjectId()).build();
		agent=client.getAgent(project);
		chattingPlayerList=new ArrayList<>();
		Bukkit.getPluginManager().registerEvents(new ChattingListener(),core);

		this.name=name;
		this.color=color;
	}

	/**
	 * Constructor with custom Agent color.
	 * @param core DialogFlowPlugin core.
	 * @param keyPath Service key file path for this agent. Default is Key.KEY_PATH.
	 * @param color Custom color for this agent.
	 * @throws IOException Throws if service key file is invalid.
	 * @throws InvalidKeyException Throws error when creating sessions or agent object.
	 */
	protected Agent(Core core,String keyPath,ChatColor color) throws IOException, InvalidKeyException {
		this.core=core;
		key=new Key(core,keyPath);

		FixedCredentialsProvider credentialsProvider=FixedCredentialsProvider.create(key.getCredentials());
		SessionsSettings sessionsSetting=SessionsSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
		sessionsClient=SessionsClient.create(sessionsSetting);

		AgentsSettings agentsSetting=AgentsSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
		client=AgentsClient.create(agentsSetting);
		ProjectName project=ProjectName.newBuilder().setProject(key.getCredentials().getProjectId()).build();
		agent=client.getAgent(project);
		chattingPlayerList=new ArrayList<>();
		Bukkit.getPluginManager().registerEvents(new ChattingListener(),core);

		this.name=agent.getDisplayName();
		this.color=color;
	}

	/**
	 * Constructor with default Agent name and color.
	 * <p>
	 *     Default Agent name is from DialogFlow agent name.
	 *     Default Agent color is white.
	 * </p>
	 * @param core DialogFlowPlugin core.
	 * @param keyPath Service key file path for this agent. Default is Key.KEY_PATH.
	 * @throws IOException Throws if service key file is invalid.
	 * @throws InvalidKeyException Throws error when creating sessions or agent object.
	 */
	protected Agent(Core core,String keyPath) throws IOException, InvalidKeyException {
		this.core=core;
		key=new Key(core,keyPath);

		FixedCredentialsProvider credentialsProvider=FixedCredentialsProvider.create(key.getCredentials());
		SessionsSettings sessionsSetting=SessionsSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
		sessionsClient=SessionsClient.create(sessionsSetting);

		AgentsSettings agentsSetting=AgentsSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
		client=AgentsClient.create(agentsSetting);
		ProjectName project=ProjectName.newBuilder().setProject(key.getCredentials().getProjectId()).build();
		agent=client.getAgent(project);
		chattingPlayerList=new ArrayList<>();
		Bukkit.getPluginManager().registerEvents(new ChattingListener(),core);

		this.name=agent.getDisplayName();
		this.color=ChatColor.WHITE;
	}

	/**
	 * Method for get Agent object.
	 * @param core DialogFlowPlugin core.
	 * @param keyPath Service key file path for this agent. Default is Key.KEY_PATH.
	 * @return new or existed Agent object.
	 * @throws IOException Throws if service key file is invalid.
	 * @throws InvalidKeyException Throws error when creating sessions or agent object.
	 */
	public static Agent getInstance(Core core, String keyPath) throws IOException, InvalidKeyException {
		if (singleton==null)
			singleton=new Agent(core,keyPath);
		return singleton;
	}
	private static boolean isJson(String fileName){
		return fileName.endsWith(".json");
	}

	/**
	 * Method for get key object.
	 * @return Key object for this Agent.
	 */
	Key getKey() {return key;}

	/**
	 * Close this client.
	 */
	public void closeClient() {
		sessionsClient.close();
		client.shutdown();
	}

	/**
	 * Start chatting with this agent.
	 * @param player Player who start chatting.
	 * @throws InvalidChatStartException Throws if this player already chatting.
	 */
	public void startChatting(Player player) throws InvalidChatStartException {
		if (chattingPlayerList.contains(player)) throw new InvalidChatStartException("This player already added");
		chattingPlayerList.add(player);
	}

	/**
	 * Check player is chatting with this agent.
	 * @param player Player who want to check.
	 * @return boolean for this player is chatting.
	 */
	public boolean isPlayerChatting(Player player){
		return chattingPlayerList.contains(player);
	}

	/**
	 * Stop chatting with this agent.
	 * @param player Player who stop chatting.
	 * @throws InvalidChatStopException Throws if this player already doesn't chatting.
	 */
	public void stopChatting(Player player) throws InvalidChatStopException {
		if (!chattingPlayerList.contains(player)) throw new InvalidChatStopException("This player already removed");
		chattingPlayerList.remove(player);
	}

	/**
	 * Get List of Player who are chatting with this agent.
	 * @return List of Player who chatting.
	 */
	public List<Player> getPlayerChatting(){return chattingPlayerList;}

	/**
	 * Send message to this agent once.
	 * @param player Player who send message.
	 * @param message message that want to send.
	 * @param isAsync send and receive by async. true can be default.
	 */
	public void sendMessage(Player player,String message,boolean isAsync) {
		sendMessage(player, message, getDefaultLanguageCode(),isAsync);
	}

	/**
	 * Send message to this agent once with special language.
	 * @param player Player who send message.
	 * @param message message that want to send.
	 * @param languageCode special language code for message.
	 * @param isAsync send and receive by async. true can be default.
	 */
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

		MessageRequestEvent requestEvent=new MessageRequestEvent(player,sessionBuilder.build(),query,this.agent,isAsync);
		Bukkit.getServer().getPluginManager().callEvent(requestEvent);

		DetectIntentResponse response=sessionsClient.detectIntent(sessionBuilder.build(),query);

		MessageResponseEvent responseEvent=new MessageResponseEvent(player,response,this.agent,isAsync);
		Bukkit.getServer().getPluginManager().callEvent(responseEvent);
	}

	/**
	 * Get default language code for this agent.
	 * @return language code.
	 */
	public String getDefaultLanguageCode() {
		return agent.getDefaultLanguageCode();
	}

	/**
	 * Get List of all language code that this agent support.
	 * @return List of language code.
	 */
	public java.util.List<String> getLanguageCodes(){
		int count=agent.getSupportedLanguageCodesCount();
		ArrayList<String> codeList=new ArrayList<>();
		
		codeList.add(getDefaultLanguageCode());
		for (int i=0;i<count;i++) {
			codeList.add(agent.getSupportedLanguageCodes(i));
		}
		return codeList;
	}

	/**
	 * Get amount of language that this agent support.
	 * @return amount of language
	 */
	public int getLanguageCodeCount() {return agent.getSupportedLanguageCodesCount()+1;}

	/**
	 * Get name of this agent.
	 * @return agent name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set name of this agent. If name is null, set to DialogFlow Agent name.
	 * @param name agent name.
	 */
	public void setName(String name) {
		if (name==null){
			this.name=agent.getDisplayName();
		}
		else
			this.name = name;
	}

	class ChattingListener implements Listener {
		@EventHandler
		public void onAsyncPlayerChat(AsyncPlayerChatEvent event){
			Player player=event.getPlayer();
			String msg=event.getMessage();
			player.sendMessage("<"+player.getDisplayName()+"> "+msg);
			if (isPlayerChatting(player)) {
				event.setCancelled(true);
				sendMessage(player, msg,true);
			}
		}
	}
}
