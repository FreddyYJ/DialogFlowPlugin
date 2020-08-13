package com.github.freddyyj.dialogflow.config;

import com.github.freddyyj.dialogflow.Core;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * class for load configuration
 */
public class Configuration{
    private String agentName;
    private String agentColor;
    private FileConfiguration config;
    private Core core;

    /**
     * default constructor
     * @param core DialogFlowPlugin core
     * @throws InvalidConfigurationException Throws if config.yml is invalid.
     */
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
            if (agentName==null || agentColor==null) {
                throw new InvalidConfigurationException("Invalid config! Did you edit config path, not value?");
            }
            else{
                if (agentName.equals("none"))
                    agentName=null;
                agentColor=agentColor.toUpperCase();
            }
        }
    }

    /**
     * Get agent color.
     */
    public ChatColor getAgentColor() {
        return ChatColor.valueOf(agentColor);
    }

    /**
     * Get agent name.
     */
    public String getAgentName() {
        return agentName;
    }

    /**
     * Reload config.yml.
     * @throws InvalidConfigurationException Throws if config.yml is invalid.
     */
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
