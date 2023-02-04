package io.github.md5sha256.chunkytools;

import com.github.md5sha256.spigotutils.PaperUtils;
import org.bukkit.Chunk;
import org.bukkit.block.BlockState;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Collection;

public class ChunkInventoryProcessor {
    private final InventoryProcessor processor;

    public ChunkInventoryProcessor(InventoryProcessor processor) {
        this.processor = processor;
    }

    public void processChunk(CommandSender sender, Chunk chunk) {
        BlockState[] tileEntities = PaperUtils.getTileEntities(chunk, false);
        Collection<Inventory> inventories = new ArrayList<>();
        for (BlockState blockState : tileEntities) {
            if (blockState instanceof BlockInventoryHolder inventoryHolder) {
                inventories.add(inventoryHolder.getInventory());
            }
        }
        this.processor.processInventories(sender, inventories);
    }

}
