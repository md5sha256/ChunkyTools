package io.github.md5sha256.chunkytools;

import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Inventory;

@FunctionalInterface
public interface InventoryProcessor {

    void processInventories(CommandSender sender, Iterable<Inventory> inventories);

}
