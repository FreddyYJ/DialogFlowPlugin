package com.github.freddyyj.dialogflow;

import com.github.freddyyj.dialogflow.exception.InvalidKeyException;
import com.google.auth.oauth2.ServiceAccountCredentials;

import java.io.*;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class Key {
	private Core core;
	private ServiceAccountCredentials credentials;
	public static final String KEY_PATH="key.json";
	public Key(Core core,String path) throws InvalidKeyException {
		this.core=core;

		File keyFolder=new File(core.getDataFolder(),path);
		InputStream input= null;
		try {
			input = new FileInputStream(keyFolder);
			credentials=ServiceAccountCredentials.fromStream(input);
		} catch (IOException e) {
			throw new InvalidKeyException("Invalid key.json! Do you install key.json under plugins/DialogFlowPlugin?",e);
		}
	}

	public ServiceAccountCredentials getCredentials() {
		return credentials;
	}
}
