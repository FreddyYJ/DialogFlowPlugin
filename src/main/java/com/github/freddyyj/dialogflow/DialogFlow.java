package com.github.freddyyj.dialogflow;

public class DialogFlow {
	private Key key;
	private Core core;
	public DialogFlow(Core core) {
		this.core=core;
		reloadKey();
	}
	public void reloadKey() {
		key=new Key(core);
	}
	Key getKey() {return key;}
}
