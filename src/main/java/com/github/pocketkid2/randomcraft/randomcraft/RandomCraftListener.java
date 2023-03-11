package com.github.pocketkid2.randomcraft.randomcraft;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class RandomCraftListener implements Listener, java.net.http.WebSocket.Listener {

	private RandomCraftPlugin plugin;

	public RandomCraftListener(RandomCraftPlugin p) {
		plugin = p;
	}

	@EventHandler
	public void onPrepareCraft(PrepareItemCraftEvent event) {
		if (plugin.getEnabledWorlds().contains(event.getView().getPlayer().getWorld())) {
			ItemStack is = event.getInventory().getResult();
			if (is != null) {
				Material newType = plugin.getItemMapping().get(is.getType());
				if (newType != null) {
					is.setType(newType);
				} else {
					plugin.getLogger().warning("Unable to map type " + is.getType().toString()
							+ " for crafting at location " + event.getInventory().getLocation().toString());
				}
			}
		}
	}

	@EventHandler
	public void onFurnaceSmelt(FurnaceSmeltEvent event) {
		if (plugin.getEnabledWorlds().contains(event.getBlock().getWorld())) {
			ItemStack is = event.getResult();
			if (is != null) {
				Material newType = plugin.getItemMapping().get(is.getType());
				if (newType != null) {
					is.setType(newType);
				} else {
					plugin.getLogger().warning("Unable to map type " + is.getType().toString()
							+ " for furnace at location " + event.getBlock().getLocation().toString());
				}
			}
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if (plugin.getEnabledWorlds().contains(event.getEntity().getWorld())) {
			List<ItemStack> drops = event.getDrops();
			for (ItemStack is : drops) {
				if (is != null) {
					Material newType = plugin.getItemMapping().get(is.getType());
					if (newType != null) {
						is.setType(newType);
					} else {
						plugin.getLogger().warning("Unable to map type " + is.getType().toString()
								+ " for mob drop at location " + event.getEntity().getLocation().toString());
					}
				}
			}
		}
	}

	@EventHandler
	public void onEntityDropItem(EntityDropItemEvent event) {
		if (plugin.getEnabledWorlds().contains(event.getEntity().getWorld())) {
			Item i = event.getItemDrop();
			if (i != null) {
				ItemStack is = i.getItemStack();
				if (is != null) {
					Material newType = plugin.getItemMapping().get(is.getType());
					if (newType != null) {
						is.setType(newType);
						i.setItemStack(is);
					} else {
						plugin.getLogger().warning("Unable to map type " + is.getType().toString()
								+ " for mob drop at location " + event.getEntity().getLocation().toString());
					}
				}
			}
		}
	}

	@EventHandler
	public void onBlockDropItem(BlockDropItemEvent event) {
		if (plugin.getEnabledWorlds().contains(event.getBlock().getWorld())) {
			List<Item> items = event.getItems();
			for (Item i : items) {
				if (i != null) {
					ItemStack is = i.getItemStack();
					if (is != null) {
						Material newType = plugin.getItemMapping().get(is.getType());
						if (newType != null) {
							is.setType(newType);
							i.setItemStack(is);
						} else {
							plugin.getLogger().warning("Unable to map type " + is.getType().toString()
									+ " for block drop at location " + event.getBlock().getLocation().toString());
						}
					}
				}
			}
		}
	}
}
