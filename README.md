# DialogFlowPlugin
DialogFlowPlugin is for accessing DialogFlow agent by Google Cloud Project service key file. Default work is conversation with DialogFlow Agent, but developers can add more actions by add event listener. It supports only one agent.

# How to use
1. Download and install .jar file in <server directory>/plugins/
2. Copy your GCP service key file in <server directory>/plugins/DialogFlowPlugin
3. Change key file name to **key.json**

In normal case, visit curseforge or dowload at release. If you want to clone and build manually, follow it.
1. Run ```git clone <git URI>```
2. Run ```mvn clean compile assembly:single```

# Javadoc
Javadoc files are in docs/. Clone or download repo and move to directory called *docs*.

# Changelog
ver 0.4.0: Beta release
* Now session created at personal
* When player join server, session created, and player leave server, session removed
* Add Events called when session created and removed
* Add Exception called if session is not found

ver 0.3.0 (no release)
* Add configuration Agent name and color.
* Add new InvalidKeyException for handle invalid key.json
* Create javadoc

ver 0.2.0
* Chatting improvements with Agent-similar as normal chatting
* /df or /dialogflow prints command list that has player has permission
* Internal code improved

ver 0.1.0
* Release beta plugin, will have lots of bugs

ver 0.0.1
* Init Project
