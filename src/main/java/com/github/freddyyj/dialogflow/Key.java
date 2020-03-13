package com.github.freddyyj.dialogflow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class Key {
	private Core core;
	private String projectID;
	private String privateKeyID;
	private String privateKey;
	private String clientEmail;
	private static final String KEY_FILE="/key.json";
	public Key(Core core) {
		this.core=core;
		
		FileReader file=null;
		try {
			File keyFolder=new File(core.getDataFolder(),KEY_FILE);
			file = new FileReader(keyFolder);
			
			JsonReader reader=Json.createReader(file);
			JsonObject object=reader.readObject();
			reader.close();
			
			projectID=object.getString("project_id");
			privateKeyID=object.getString("private_key_id");
			privateKey=object.getString("private_key");
			clientEmail=object.getString("client_email");

		} catch (FileNotFoundException e) {
			// TODO Create catch block
			e.printStackTrace();
		}
	}
	String getProjectId() {return projectID;}
	String getPrivateKeyId() {return privateKeyID;}
	String getPrivateKey() {return privateKey;}
	String getClientEmail() {return clientEmail;}
}
