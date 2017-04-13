package io.github.axle2005.syntablist.sponge;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.reflect.TypeToken;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class Config {

	SynTabList plugin;

	Path defaultConfig;
	File activeConfig;
	CommentedConfigurationNode rootnode;
	ConfigurationLoader<CommentedConfigurationNode> configManager;

	List<String> entitylist;
	List<String> listEntityDefaults = new ArrayList<String>(Arrays.asList("minecraft:zombie", "minecraft:witch",
			"minecraft:skeleton", "minecraft:creeper", "minecraft:arrow"));
	List<String> listTileDefaults = new ArrayList<String>(Arrays.asList("PlaceHolder"));

	public Config(SynTabList plugin) {
		this.plugin = plugin;
		configManager = HoconConfigurationLoader.builder().setFile(activeConfig).build();

	}

	Config(SynTabList plugin, Path defaultConfig, ConfigurationLoader<CommentedConfigurationNode> configManager) {
		this.plugin = plugin;
		this.defaultConfig = defaultConfig;
		this.configManager = configManager;

		activeConfig = new File(getConfigDir().toFile(), "SynTabList.conf");

		configManager = HoconConfigurationLoader.builder().setFile(activeConfig).build();

		try {

			rootnode = configManager.load();

			if (!activeConfig.exists()) {
				saveConfig(rootnode, configManager);

			}
			convertConfigToNew();

			if (rootnode.getNode("Warning", "Enabled").isVirtual() == true) {
				rootnode.getNode("Warning", "Enabled").setValue(true);
			}
			if (rootnode.getNode("Warning", "Messages","Warning").isVirtual() == true) {
				rootnode.getNode("Warning", "Messages","Warning").setValue("[SynTabList] Clearing Entities in 1 minute");
			}
			if (rootnode.getNode("Warning", "Messages","Cleared").isVirtual() == true) {
				rootnode.getNode("Warning", "Messages","Cleared").setValue("[SynTabList] Entities have been cleared");
			}
			if (rootnode.getNode("Clearing", "Lists", "EntityList").isVirtual() == true) {
				rootnode.getNode("Clearing", "Lists", "EntityList").setValue(listEntityDefaults);
			}
			if (rootnode.getNode("Clearing", "Lists", "TileEntityList").isVirtual() == true) {
				rootnode.getNode("Clearing", "Lists", "TileEntityList").setValue(listTileDefaults);
			}
			if (rootnode.getNode("Clearing", "Interval").isVirtual() == true) {
				rootnode.getNode("Clearing", "Interval").setValue(60);
			}
			if (rootnode.getNode("Clearing", "PassiveMode").isVirtual() == true) {
				rootnode.getNode("Clearing", "PassiveMode").setValue(false);
			}
			if (rootnode.getNode("Clearing", "KillAllMonsters").isVirtual() == true) {
				rootnode.getNode("Clearing", "KillAllMonsters").setValue(false);
			}
			if (rootnode.getNode("Clearing", "KillDrops").isVirtual() == true) {
				rootnode.getNode("Clearing", "KillDrops").setValue(false);
			}
			if (rootnode.getNode("Clearing", "KillAnimalGroups").isVirtual() == true) {
				rootnode.getNode("Clearing", "KillAnimalGroups").setValue(false);
			}
			if (rootnode.getNode("Clearing", "CrashMode").isVirtual() == true) {
				rootnode.getNode("Clearing", "CrashMode").setValue(false);
			}
			if (rootnode.getNode("Clearing", "MobLimiter", "Enabled").isVirtual() == true) {
				rootnode.getNode("Clearing", "MobLimiter", "Enabled").setValue(false);
			}
			if (rootnode.getNode("Clearing", "MobLimiter", "Limit").isVirtual() == true) {
				rootnode.getNode("Clearing", "MobLimiter", "Limit").setValue(500);
			}
			
			
			
			//Convert config to version 1.3.0 (Removal of ListTypes)
			convertTo130();
			
			
			saveConfig(rootnode, configManager);
		}

		catch (IOException e) {
			e.printStackTrace();
		}
		entitylist = getEntitylist();
		// saveConfig(rootnode, configManager);
		save(activeConfig);
	}

	private void convertConfigToNew() {
		if (rootnode.getNode("Warning", "Message").isVirtual() == false) {
			
			rootnode.getNode("Warning", "Messages").setValue(rootnode.getNode("Warning", "Message","Warning").getValue());
			rootnode.getNode("Warning").removeChild("Message");
		}

		if (rootnode.getNode("Interval").isVirtual() == false) {

			rootnode.getNode("Clearing", "Interval").setValue(rootnode.getNode("Interval").getValue());
			rootnode.removeChild("Interval");
		}
		if (rootnode.getNode("PassiveMode").isVirtual() == false) {

			rootnode.getNode("Clearing", "PassiveMode").setValue(rootnode.getNode("PassiveMode").getValue());
			rootnode.removeChild("PassiveMode");
		}
		if (rootnode.getNode("EntityList").isVirtual() == false) {
			rootnode.getNode("Clearing", "EntityList").setValue(rootnode.getNode("EntityList").getValue());
			rootnode.removeChild("EntityList");
		}
		if (rootnode.getNode("Clearing", "EntityList").isVirtual() == false) {
			rootnode.getNode("Clearing", "Lists", "EntityList")
					.setValue(rootnode.getNode("Clearing", "EntityList").getValue());
			rootnode.getNode("Clearing").removeChild("EntityList");
		}
	}
	private void convertTo130()
	{
		if (rootnode.getNode("Clearing","ListType").isVirtual() == false) {
			rootnode.getNode("Clearing").removeChild("ListType");
		}
	}

	public void saveConfig(ConfigurationNode config, ConfigurationLoader<CommentedConfigurationNode> configManager) {
		try {
			configManager.save(config);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void save(File input) {

		configManager = HoconConfigurationLoader.builder().setFile(input).build();

	}

	public Path getConfigDir() {
		return defaultConfig;
	}

	public List<String> getEntitylist() {

		activeConfig = new File(getConfigDir().toFile(), "SynTabList.conf");

		configManager = HoconConfigurationLoader.builder().setFile(activeConfig).build();

		try {

			rootnode = configManager.load();
			if (!activeConfig.exists()) {
				// defaults(activeConfig, rootnode);
				saveConfig(rootnode, configManager);

			}

		}

		catch (IOException e) {
			e.printStackTrace();
		}
		entitylist = new ArrayList<String>();
		try {
			for (String entity : rootnode.getNode("Clearing", "Lists", "EntityList")
					.getList(TypeToken.of(String.class))) {
				entitylist.add(entity.toLowerCase());
				// plugin.getLogger().info(entity);
			}
		} catch (ObjectMappingException e) {
			e.printStackTrace();
		}

		return entitylist;
	}

	public List<String> getTilelist() {

		activeConfig = new File(getConfigDir().toFile(), "SynTabList.conf");

		configManager = HoconConfigurationLoader.builder().setFile(activeConfig).build();

		try {

			rootnode = configManager.load();
			if (!activeConfig.exists()) {
				// defaults(activeConfig, rootnode);
				saveConfig(rootnode, configManager);

			}

		}

		catch (IOException e) {
			e.printStackTrace();
		}
		entitylist = new ArrayList<String>();
		try {
			for (String entity : rootnode.getNode("Clearing", "Lists", "TileEntityList")
					.getList(TypeToken.of(String.class))) {
				entitylist.add(entity.toLowerCase());
				// plugin.getLogger().info(entity);
			}
		} catch (ObjectMappingException e) {
			e.printStackTrace();
		}
		return entitylist;
	}

	public Integer getNodeInt(String node) {
		int x = 0;
		if (node.contains(",")) {
			String[] y = node.split(",");
			if (y.length == 2) {
				x = rootnode.getNode(y[0], y[1]).getInt();
			} else if (y.length == 3) {
				x = rootnode.getNode(y[0], y[1], y[2]).getInt();
			}
		} else
			x = rootnode.getNode(node).getInt();

		return x;

	}

	public Boolean getNodeBoolean(String node) {
		Boolean x = false;
		if (node.contains(",")) {
			String[] y = node.split(",");
			if (y.length == 2) {
				x = rootnode.getNode(y[0], y[1]).getBoolean();
			} else if (y.length == 3) {
				x = rootnode.getNode(y[0], y[1], y[2]).getBoolean();
			}
		} else
			x = rootnode.getNode(node).getBoolean();

		return x;

	}

	public String getNodeString(String node) {
		String x = "";
		if (node.contains(",")) {
			String[] y = node.split(",");
			if (y.length == 2) {
				x = rootnode.getNode(y[0], y[1]).getString();
				;
			} else if (y.length == 3) {
				x = rootnode.getNode(y[0], y[1], y[2]).getString();
			}
		} else
			x = rootnode.getNode(node).getString();

		return x;

	}

	public void setValueString(String node, String child) {
		rootnode.getNode(node).setValue(child);
		save(activeConfig);

	}

	public void setValueList(String node, List<String> list_entities) {
		rootnode.getNode(node).setValue(list_entities);
		save(activeConfig);

	}

}