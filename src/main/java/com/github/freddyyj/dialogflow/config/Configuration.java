package com.github.freddyyj.dialogflow.config;

import com.github.freddyyj.dialogflow.Core;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

public class Configuration{
    private String agentName;
    private String agentColor;
    private FileConfiguration config;
    private Core core;
    public Configuration(Core core) throws InvalidConfigurationException {
        this.core=core;
        core.saveDefaultConfig();
        config=core.getConfig();
        ConfigurationSection agentConfig =config.getConfigurationSection("agent");
        if (agentConfig==null){
            throw new InvalidConfigurationException("Invalid config! Did you edit config path, not value?");
        }

        else{
            agentName=agentConfig.getString("name"); // agent.name
            agentColor=agentConfig.getString("color"); // agent.color
            if (agentName==null || agentColor==null){
                throw new InvalidConfigurationException("Invalid config! Did you edit config path, not value?");
            }
            else{
                agentColor=agentColor.toUpperCase();
            }
        }
    }

    public ChatColor getAgentColor() {
        return ChatColor.valueOf(agentColor);
    }

    public String getAgentName() {
        return agentName;
    }
    public void reload() throws InvalidConfigurationException {
        config=core.getConfig();
        ConfigurationSection agentConfig =config.getConfigurationSection("agent");
        if (agentConfig==null){
            throw new InvalidConfigurationException("Invalid config! Did you edit config path, not value?");
        }

        else{
            agentName=agentConfig.getString("name"); // agent.name
            agentColor=agentConfig.getString("color"); // agent.color
            if (agentName==null || agentColor==null){
                throw new InvalidConfigurationException("Invalid config! Did you edit config path, not value?");
            }
            else{
                agentColor=agentColor.toUpperCase();
            }
        }
    }
}
