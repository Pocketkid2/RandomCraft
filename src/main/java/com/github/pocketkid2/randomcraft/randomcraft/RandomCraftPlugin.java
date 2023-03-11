package com.github.pocketkid2.randomcraft.randomcraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class RandomCraftPlugin extends JavaPlugin {

	private Map<Material, Material> map = new HashMap<>();

	private List<World> enabled_worlds = new ArrayList<>();

	private boolean verbose;

	private boolean craftingRecipesEnabled;
	private boolean furnaceSmeltingEnabled;
	private boolean entityDeathDropsEnabled;
	private boolean regularEntityDropsEnabled;
	private boolean blockDropsEnabled;

	public Map<Material, Material> createMapping() {

		// Remove whatever else might have been in the map
		map.clear();

		// Create two lists of every item in the game
		List<Material> list1 = Arrays.asList(Material.values());
		List<Material> list2;

		// Filter out everything that's not a block or item
		list1 = list1.stream().filter(m -> m.isItem() && !m.isAir() && m != Material.LIGHT).collect(Collectors.toList());
		list2 = new ArrayList<>(list1);

		// Shuffle the second list
		Collections.shuffle(list2);

		// An item at a given index in the first (sorted) list maps to the same index in
		// the second (shuffled) list
		for (int i = 0; i < list1.size(); i++) {
			map.put(list1.get(i), list2.get(i));
			if (verbose) {
				getLogger().info("Mapping item " + list1.get(i).toString() + " to item " + list2.get(i).toString());
			}
		}

		return map;
	}

	@Override
	public void onEnable() {

		// If there is no config file, create it
		saveDefaultConfig();

		verbose = getConfig().getBoolean("verbose", false);

		craftingRecipesEnabled = getConfig().getBoolean("crafting-recipes", true);
		furnaceSmeltingEnabled = getConfig().getBoolean("furnace-smelting", true);
		entityDeathDropsEnabled = getConfig().getBoolean("entity-death-drops", true);
		regularEntityDropsEnabled = getConfig().getBoolean("regular-entity-drops", true);
		blockDropsEnabled = getConfig().getBoolean("block-drops", true);

		// Get the mappings
		if (!getConfig().isConfigurationSection("mapping")) {
			getLogger().info("Creating new configuration section");
			getConfig().createSection("mapping", createMapping());
		}
		Map<String, Object> config_string_map = getConfig().getConfigurationSection("mapping").getValues(false);

		// Check its size
		if (map.size() == 0 && config_string_map.size() > 0) {

			getLogger().info("Mapping found in file, loading it in");

			// Convert mappings and apply
			for (Entry<String, Object> entry : config_string_map.entrySet()) {
				map.put(Material.matchMaterial(entry.getKey()),
						entry.getValue() instanceof Material ? (Material) entry.getValue() : Material.matchMaterial((String) entry.getValue()));
			}
		}

		else {

			getLogger().info("No mapping found in file, creating new one");

			// Create a new mapping
			createMapping();
		}

		// Read enabled worlds from config
		@SuppressWarnings("unchecked")
		List<String> world_list = (List<String>) getConfig().getList("worlds", new ArrayList<String>());
		getLogger().info("Random item mapping is enabled in " + world_list.size() + " worlds: " + String.join(", ", world_list));
		enabled_worlds = world_list.stream().map(Bukkit::getWorld).collect(Collectors.toList());

		// Make sure the events are registered
		getServer().getPluginManager().registerEvents(new RandomCraftListener(this), this);

		// Make sure the command is registered
		getCommand("randomize").setExecutor(new RandomCraftCommand(this));

		getLogger().info("Enabled!");
	}

	@Override
	public void onDisable() {

		// Create string map for file
		Map<String, String> config_string_map = new HashMap<>();
		for (Entry<Material, Material> entry : map.entrySet()) {
			config_string_map.put(entry.getKey().toString(), entry.getValue().toString());
		}

		// Save it to the config file
		getConfig().set("mapping", config_string_map);
		saveConfig();

		getLogger().info("Disabled!");
	}

	public Map<Material, Material> getItemMapping() {
		return map;
	}

	public List<World> getEnabledWorlds() {
		return enabled_worlds;
	}

	public boolean areCraftingRecipesEnabled() {
		return craftingRecipesEnabled;
	}

	public boolean isFurnaceSmeltingEnabled() {
		return furnaceSmeltingEnabled;
	}

	public boolean areEntityDeathDropsEnabled() {
		return entityDeathDropsEnabled;
	}

	public boolean areRegularEntityDropsEnabled() {
		return regularEntityDropsEnabled;
	}

	public boolean areBlockDropsEnabled() {
		return blockDropsEnabled;
	}
}
