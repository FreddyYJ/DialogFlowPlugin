package com.github.freddyyj.dialogflow;

import com.google.auth.oauth2.ServiceAccountCredentials;

import java.io.*;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class Key {
	private Core core;
	private ServiceAccountCredentials credentials;
	public static final String KEY_PATH="key.json";
	public Key(Core core,String path) throws IOException {
		this.core=core;

		File keyFolder=new File(core.getDataFolder(),path);
		InputStream input=new FileInputStream(keyFolder);
		credentials=ServiceAccountCredentials.fromStream(input);
	}

	public ServiceAccountCredentials getCredentials() {
		return credentials;
	}
}
