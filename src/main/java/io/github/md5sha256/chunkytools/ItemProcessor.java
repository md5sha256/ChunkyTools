package io.github.md5sha256.chunkytools;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
@FunctionalInterface
public interface ItemProcessor {

    void processItems(CommandSender sender, Iterable<Item> items);

}
