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
			

			if (rootnode.getNode("TabList", "Header").isVirtual() == true) {
				rootnode.getNode("TabList", "Header").setValue("&1=======================/n=========&fDeVco&1=========");
			}
			if (rootnode.getNode("TabList", "Footer").isVirtual() == true) {
				rootnode.getNode("TabList", "Footer").setValue("&1=======================");
			}

			if(rootnode.getNode("SynX Channel").isVirtual())
			{
				rootnode.getNode("SynX Channel").setComment("Broadcast only on this channel").setValue("TabList");
			}
			saveConfig(rootnode, configManager);
		}

		catch (IOException e) {
			e.printStackTrace();
		}
		entitylist = getEntitylist();
		// saveConfig(rootnode, configManager);
		save(activeConfig);
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