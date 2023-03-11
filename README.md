# RandomCraft
A plugin for Minecraft Spigot/Paper servers that allows for playing with random item drops and crafting results

### How does it work?
This plugin creates a giant list in memory of all items in Minecraft, which are then mapped to another random item in that list. The mapping is one-to-one. 

This mapping is saved to the config file whenever the server is shut down and is loaded back in on startup. If no mapping is found in the config file, such as during first run, a new random mapping is generated.

When a crafting recipe is made, or a furnace smelts an item, or an entity/block drops an item, the item type is passed through the map and changed to the new type. The number of items in the stack is unchanged when this happens.

## Commands and Permissions
There is only one command, `/randomize`. It takes no arguments and all it does generate a new random item mapping list which replaces the existing one.

The permission is `randomcraft.command` and it defaults to server operators.

## Configuration
Note that the default world names for this plugin are not the default Minecraft/Spigot/Paper world names.
