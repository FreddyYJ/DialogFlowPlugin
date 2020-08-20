package com.github.freddyyj.dialogflow;

import com.github.freddyyj.dialogflow.exception.InvalidKeyException;
import com.google.auth.oauth2.ServiceAccountCredentials;

import java.io.*;

/**
 * Class for read service key account file.
 */
class Key {
	private Core core;
	private ServiceAccountCredentials credentials;
	/**
	 * Default key file path.
	 */
	public static final String KEY_PATH="key.json";

	/**
	 * Constructor with DialogFlowPlugin core and key path.
	 * @param core DialogFlowPlugin core
	 * @param path path to service key account file. Use {@link #KEY_PATH}.
	 * @throws InvalidKeyException throws when key file is invalid.
	 */
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

	/**
	 * Get credentials.
	 * @return credentials of agent
	 */
	public ServiceAccountCredentials getCredentials() {
		return credentials;
	}
}
